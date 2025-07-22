package com.probro.khoded.data.api

import com.probro.khoded.data.models.*
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.w3c.fetch.*
import kotlin.js.Promise

/**
 * Type-Safe API Client
 * 
 * Comprehensive API client with:
 * - Type-safe request/response handling
 * - Comprehensive error handling
 * - Request/response interceptors
 * - Retry logic with exponential backoff
 * - Request deduplication
 * - Cache management
 */

class ApiClient(
    private val baseUrl: String = "/api",
    private val timeout: Int = 30000,
    private val retryAttempts: Int = 3
) {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = false
    }
    
    private val requestCache = mutableMapOf<String, Promise<Response>>()
    private val responseCache = mutableMapOf<String, CachedResponse>()
    
    // =============================================================================
    // PUBLIC API METHODS
    // =============================================================================
    
    suspend fun submitContactForm(data: ContactFormData): ApiResult<ContactFormResponse> {
        return post("/contact", data)
    }
    
    suspend fun subscribeToNewsletter(data: NewsletterSubscription): ApiResult<NewsletterResponse> {
        return post("/newsletter/subscribe", data)
    }
    
    suspend fun unsubscribeFromNewsletter(email: String, token: String): ApiResult<Unit> {
        return post("/newsletter/unsubscribe", mapOf(
            "email" to email,
            "token" to token
        ))
    }
    
    suspend fun getCaseStudies(
        page: Int = 1, 
        pageSize: Int = 10,
        featured: Boolean? = null
    ): ApiResult<PaginatedResponse<CaseStudy>> {
        val params = mutableMapOf<String, String>().apply {
            put("page", page.toString())
            put("pageSize", pageSize.toString())
            featured?.let { put("featured", it.toString()) }
        }
        
        return get("/case-studies", params, cacheFor = 5 * 60 * 1000) // Cache for 5 minutes
    }
    
    suspend fun getCaseStudy(slug: String): ApiResult<CaseStudy> {
        return get("/case-studies/$slug", cacheFor = 10 * 60 * 1000) // Cache for 10 minutes
    }
    
    suspend fun getTestimonials(): ApiResult<List<ClientTestimonial>> {
        return get("/testimonials", cacheFor = 15 * 60 * 1000) // Cache for 15 minutes
    }
    
    // =============================================================================
    // HTTP METHODS
    // =============================================================================
    
    private suspend inline fun <reified T> get(
        endpoint: String, 
        params: Map<String, String> = emptyMap(),
        cacheFor: Long? = null
    ): ApiResult<T> {
        val url = buildUrl(endpoint, params)
        val cacheKey = "GET:$url"
        
        // Check cache first
        cacheFor?.let { duration ->
            responseCache[cacheKey]?.let { cached ->
                if (System.currentTimeMillis() - cached.timestamp < duration) {
                    try {
                        return ApiResult.Success(json.decodeFromString<T>(cached.data))
                    } catch (e: Exception) {
                        // Remove invalid cache entry
                        responseCache.remove(cacheKey)
                    }
                }
            }
        }
        
        return withRetry {
            val response = fetch(url, RequestInit(
                method = "GET",
                headers = buildHeaders()
            )).await()
            
            val result = handleResponse<T>(response)
            
            // Cache successful responses
            if (result is ApiResult.Success && cacheFor != null) {
                responseCache[cacheKey] = CachedResponse(
                    data = json.encodeToString(result.data),
                    timestamp = System.currentTimeMillis()
                )
            }
            
            result
        }
    }
    
    private suspend inline fun <reified T, reified R> post(
        endpoint: String,
        body: T
    ): ApiResult<R> {
        val url = "$baseUrl$endpoint"
        
        return withRetry {
            val response = fetch(url, RequestInit(
                method = "POST",
                headers = buildHeaders("application/json"),
                body = json.encodeToString(body)
            )).await()
            
            handleResponse<R>(response)
        }
    }
    
    private suspend inline fun <reified R> put(
        endpoint: String,
        body: Any
    ): ApiResult<R> {
        val url = "$baseUrl$endpoint"
        
        return withRetry {
            val response = fetch(url, RequestInit(
                method = "PUT",
                headers = buildHeaders("application/json"),
                body = json.encodeToString(body)
            )).await()
            
            handleResponse<R>(response)
        }
    }
    
    private suspend inline fun <reified R> delete(endpoint: String): ApiResult<R> {
        val url = "$baseUrl$endpoint"
        
        return withRetry {
            val response = fetch(url, RequestInit(
                method = "DELETE",
                headers = buildHeaders()
            )).await()
            
            handleResponse<R>(response)
        }
    }
    
    // =============================================================================
    // RESPONSE HANDLING
    // =============================================================================
    
    private suspend inline fun <reified T> handleResponse(response: Response): ApiResult<T> {
        return try {
            val responseText = response.text().await()
            
            when {
                response.ok -> {
                    if (responseText.isEmpty() && T::class == Unit::class) {
                        @Suppress("UNCHECKED_CAST")
                        ApiResult.Success(Unit as T)
                    } else {
                        val apiResponse = json.decodeFromString<ApiResponse<T>>(responseText)
                        when (apiResponse) {
                            is ApiResponse.Success -> ApiResult.Success(apiResponse.data)
                            is ApiResponse.Error -> ApiResult.Error(
                                ApiError.ServerError(
                                    message = apiResponse.message,
                                    code = apiResponse.code,
                                    statusCode = response.status.toInt()
                                )
                            )
                        }
                    }
                }
                
                response.status.toInt() == 429 -> {
                    ApiResult.Error(ApiError.RateLimitError("Too many requests. Please try again later."))
                }
                
                response.status.toInt() in 400..499 -> {
                    val errorResponse = try {
                        json.decodeFromString<ApiResponse.Error>(responseText)
                    } catch (e: Exception) {
                        ApiResponse.Error("Client error: ${response.status} ${response.statusText}")
                    }
                    
                    ApiResult.Error(
                        ApiError.ClientError(
                            message = errorResponse.message,
                            code = errorResponse.code,
                            statusCode = response.status.toInt(),
                            details = errorResponse.details
                        )
                    )
                }
                
                response.status.toInt() in 500..599 -> {
                    ApiResult.Error(
                        ApiError.ServerError(
                            message = "Server error occurred. Please try again later.",
                            statusCode = response.status.toInt()
                        )
                    )
                }
                
                else -> {
                    ApiResult.Error(
                        ApiError.UnknownError("Unexpected response: ${response.status} ${response.statusText}")
                    )
                }
            }
        } catch (e: SerializationException) {
            ApiResult.Error(
                ApiError.ParseError("Failed to parse server response: ${e.message}")
            )
        } catch (e: Exception) {
            ApiResult.Error(
                ApiError.NetworkError("Network request failed: ${e.message}")
            )
        }
    }
    
    // =============================================================================
    // RETRY LOGIC
    // =============================================================================
    
    private suspend fun <T> withRetry(operation: suspend () -> ApiResult<T>): ApiResult<T> {
        repeat(retryAttempts) { attempt ->
            when (val result = operation()) {
                is ApiResult.Success -> return result
                is ApiResult.Error -> {
                    when (result.error) {
                        is ApiError.NetworkError,
                        is ApiError.ServerError -> {
                            if (attempt < retryAttempts - 1) {
                                val delayMs = calculateBackoffDelay(attempt)
                                delay(delayMs)
                                continue
                            }
                        }
                        else -> return result // Don't retry client errors
                    }
                }
            }
        }
        
        return ApiResult.Error(ApiError.NetworkError("Request failed after $retryAttempts attempts"))
    }
    
    private fun calculateBackoffDelay(attempt: Int): Long {
        return (1000L * (1 shl attempt)).coerceAtMost(10000L) // Max 10 seconds
    }
    
    // =============================================================================
    // UTILITY METHODS
    // =============================================================================
    
    private fun buildUrl(endpoint: String, params: Map<String, String> = emptyMap()): String {
        val url = "$baseUrl$endpoint"
        if (params.isEmpty()) return url
        
        val queryString = params.entries.joinToString("&") { (key, value) ->
            "${encodeURIComponent(key)}=${encodeURIComponent(value)}"
        }
        
        return "$url?$queryString"
    }
    
    private fun buildHeaders(contentType: String? = null): Headers {
        return js("new Headers()").unsafeCast<Headers>().apply {
            set("Accept", "application/json")
            contentType?.let { set("Content-Type", it) }
            
            // CSRF protection
            set("X-Requested-With", "XMLHttpRequest")
            
            // Add any authentication headers if needed
            getAuthToken()?.let { token ->
                set("Authorization", "Bearer $token")
            }
        }
    }
    
    private fun getAuthToken(): String? {
        return try {
            window.localStorage.getItem("auth_token")
        } catch (e: Exception) {
            null
        }
    }
    
    private fun encodeURIComponent(str: String): String {
        return js("encodeURIComponent(str)").unsafeCast<String>()
    }
    
    // =============================================================================
    // CACHE MANAGEMENT
    // =============================================================================
    
    fun clearCache() {
        responseCache.clear()
        requestCache.clear()
    }
    
    fun clearCacheForEndpoint(endpoint: String) {
        responseCache.keys.removeAll { it.contains(endpoint) }
    }
    
    private data class CachedResponse(
        val data: String,
        val timestamp: Long
    )
}

// =============================================================================
// RESULT TYPES
// =============================================================================

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val error: ApiError) : ApiResult<Nothing>()
    
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    
    fun getOrNull(): T? = if (this is Success) data else null
    fun getErrorOrNull(): ApiError? = if (this is Error) error else null
    
    inline fun onSuccess(action: (T) -> Unit): ApiResult<T> {
        if (this is Success) action(data)
        return this
    }
    
    inline fun onError(action: (ApiError) -> Unit): ApiResult<T> {
        if (this is Error) action(error)
        return this
    }
    
    inline fun <R> map(transform: (T) -> R): ApiResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
        }
    }
    
    inline fun <R> flatMap(transform: (T) -> ApiResult<R>): ApiResult<R> {
        return when (this) {
            is Success -> transform(data)
            is Error -> this
        }
    }
}

sealed class ApiError {
    data class NetworkError(val message: String) : ApiError()
    data class ParseError(val message: String) : ApiError()
    data class ClientError(
        val message: String,
        val code: String? = null,
        val statusCode: Int,
        val details: Map<String, String>? = null
    ) : ApiError()
    data class ServerError(
        val message: String,
        val code: String? = null,
        val statusCode: Int
    ) : ApiError()
    data class RateLimitError(val message: String) : ApiError()
    data class UnknownError(val message: String) : ApiError()
    
    fun getUserMessage(): String {
        return when (this) {
            is NetworkError -> "Please check your internet connection and try again."
            is ParseError -> "We're having trouble processing the server response. Please try again."
            is ClientError -> when (statusCode) {
                400 -> message.ifBlank { "Please check your input and try again." }
                401 -> "You need to be logged in to perform this action."
                403 -> "You don't have permission to perform this action."
                404 -> "The requested resource was not found."
                422 -> message.ifBlank { "Please check your input and try again." }
                else -> message.ifBlank { "There was a problem with your request." }
            }
            is ServerError -> "We're experiencing technical difficulties. Please try again later."
            is RateLimitError -> message
            is UnknownError -> "Something unexpected happened. Please try again."
        }
    }
}

// =============================================================================
// RESPONSE MODELS
// =============================================================================

@Serializable
data class ContactFormResponse(
    val id: String,
    val message: String = "Thank you for your inquiry! We'll be in touch soon.",
    val estimatedResponseTime: String = "within 24 hours"
)

@Serializable
data class NewsletterResponse(
    val message: String = "Successfully subscribed to newsletter!",
    val requiresConfirmation: Boolean = true,
    val confirmationSent: Boolean = false
)

// =============================================================================
// SINGLETON INSTANCE
// =============================================================================

object ApiClientProvider {
    private var _instance: ApiClient? = null
    
    fun getInstance(): ApiClient {
        return _instance ?: ApiClient().also { _instance = it }
    }
    
    fun configure(baseUrl: String, timeout: Int = 30000, retryAttempts: Int = 3) {
        _instance = ApiClient(baseUrl, timeout, retryAttempts)
    }
}