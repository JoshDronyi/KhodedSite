package com.probro.khoded.data.api

import kotlinx.coroutines.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.fetch.*
import kotlin.js.json

/**
 * API Client - Production Implementation
 *
 * Provides secure HTTP client functionality for frontend API calls
 * Includes proper error handling, rate limiting, and request validation
 */

@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val error: String? = null,
    val success: Boolean = true,
    val statusCode: Int = 200
)

@Serializable
data class ApiError(
    val message: String,
    val code: String? = null,
    val details: Map<String, String>? = null
)

class ApiClient {

    companion object {
        private const val BASE_URL = "" // Use relative URLs for same-origin requests
        private const val REQUEST_TIMEOUT = 30000 // 30 seconds

        fun getInstance(): ApiClient = ApiClient()
        val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
        
        // Public helper for JSON serialization - to be used with postJson
        inline fun <reified T> toJson(data: T): String = json.encodeToString(data)

    }

    /**
     * Rate limiting check (client-side)
     */
    private fun checkRateLimit(endpoint: String): Boolean {
        val now = kotlinx.browser.document.defaultView?.performance?.now() ?: 0.0
        val key = "api_rate_limit_$endpoint"
        val lastRequest = kotlinx.browser.localStorage.getItem(key)?.toDoubleOrNull() ?: 0.0

        // Allow 1 request per 2 seconds per endpoint (basic client-side rate limiting)
        if (now - lastRequest < 2000) {
            return false
        }

        kotlinx.browser.localStorage.setItem(key, now.toString())
        return true
    }

    /**
     * Create fetch headers with security considerations
     */
    private fun createHeaders(contentType: String = "application/json"): dynamic {
        return json(
            "Content-Type" to contentType,
            "X-Requested-With" to "XMLHttpRequest", // CSRF protection
            "Accept" to "application/json"
        )
    }

    /**
     * Handle fetch response and convert to ApiResponse
     */
    private suspend fun handleResponse(response: Response): ApiResponse<String> {
        val responseText = response.text().await()

        return if (response.ok) {
            ApiResponse(
                data = responseText,
                success = true,
                statusCode = response.status.toInt()
            )
        } else {
            val errorMessage = when (response.status.toInt()) {
                400 -> "Bad Request: Please check your input"
                401 -> "Unauthorized: Please check your credentials"
                403 -> "Forbidden: Access denied"
                404 -> "Not Found: The requested resource was not found"
                429 -> "Too Many Requests: Please wait before trying again"
                500 -> "Server Error: Please try again later"
                else -> "Request failed with status ${response.status}"
            }

            ApiResponse(
                data = null,
                error = errorMessage,
                success = false,
                statusCode = response.status.toInt()
            )
        }
    }

    suspend fun get(endpoint: String): ApiResponse<String> {
        return try {
            if (!checkRateLimit(endpoint)) {
                return ApiResponse(
                    error = "Rate limit exceeded. Please wait before making another request.",
                    success = false,
                    statusCode = 429
                )
            }

            val url = if (endpoint.startsWith("http")) endpoint else "$BASE_URL$endpoint"

            val response = kotlinx.browser.window.fetch(
                url,
                RequestInit(
                    method = "GET",
                    headers = createHeaders()
                )
            ).await()

            handleResponse(response)

        } catch (e: Exception) {
            console.error("GET request failed:", e)
            ApiResponse(
                error = "Network error: ${e.message ?: "Unknown error"}",
                success = false,
                statusCode = 0
            )
        }
    }

    suspend fun post(endpoint: String, body: String): ApiResponse<String> {
        return try {
            if (!checkRateLimit(endpoint)) {
                return ApiResponse(
                    error = "Rate limit exceeded. Please wait before making another request.",
                    success = false,
                    statusCode = 429
                )
            }

            val url = if (endpoint.startsWith("http")) endpoint else "$BASE_URL$endpoint"

            val response = kotlinx.browser.window.fetch(
                url,
                RequestInit(
                    method = "POST",
                    headers = createHeaders(),
                    body = body
                )
            ).await()

            handleResponse(response)

        } catch (e: Exception) {
            console.error("POST request failed:", e)
            ApiResponse(
                error = "Network error: ${e.message ?: "Unknown error"}",
                success = false,
                statusCode = 0
            )
        }
    }

    suspend fun put(endpoint: String, body: String): ApiResponse<String> {
        return try {
            if (!checkRateLimit(endpoint)) {
                return ApiResponse(
                    error = "Rate limit exceeded. Please wait before making another request.",
                    success = false,
                    statusCode = 429
                )
            }

            val url = if (endpoint.startsWith("http")) endpoint else "$BASE_URL$endpoint"

            val response = kotlinx.browser.window.fetch(
                url,
                RequestInit(
                    method = "PUT",
                    headers = createHeaders(),
                    body = body
                )
            ).await()

            handleResponse(response)

        } catch (e: Exception) {
            console.error("PUT request failed:", e)
            ApiResponse(
                error = "Network error: ${e.message ?: "Unknown error"}",
                success = false,
                statusCode = 0
            )
        }
    }

    suspend fun delete(endpoint: String): ApiResponse<String> {
        return try {
            if (!checkRateLimit(endpoint)) {
                return ApiResponse(
                    error = "Rate limit exceeded. Please wait before making another request.",
                    success = false,
                    statusCode = 429
                )
            }

            val url = if (endpoint.startsWith("http")) endpoint else "$BASE_URL$endpoint"

            val response = kotlinx.browser.window.fetch(
                url,
                RequestInit(
                    method = "DELETE",
                    headers = createHeaders()
                )
            ).await()

            handleResponse(response)

        } catch (e: Exception) {
            console.error("DELETE request failed:", e)
            ApiResponse(
                error = "Network error: ${e.message ?: "Unknown error"}",
                success = false,
                statusCode = 0
            )
        }
    }

    /**
     * Convenience method for sending JSON data
     */
    suspend fun postJson(endpoint: String, jsonData: String): ApiResponse<String> {
        return post(endpoint, jsonData)
    }

    /**
     * Convenience method for sending form data (useful for contact forms)
     */
    suspend fun postForm(endpoint: String, formData: Map<String, String>): ApiResponse<String> {
        return try {
            if (!checkRateLimit(endpoint)) {
                return ApiResponse(
                    error = "Rate limit exceeded. Please wait before making another request.",
                    success = false,
                    statusCode = 429
                )
            }

            val url = if (endpoint.startsWith("http")) endpoint else "$BASE_URL$endpoint"
            val formBody = formData.entries.joinToString("&") { (key, value) ->
                "${encodeURIComponent(key)}=${encodeURIComponent(value)}"
            }

            val response = kotlinx.browser.window.fetch(
                url,
                RequestInit(
                    method = "POST",
                    headers = createHeaders("application/x-www-form-urlencoded"),
                    body = formBody
                )
            ).await()

            handleResponse(response)

        } catch (e: Exception) {
            console.error("Form POST request failed:", e)
            ApiResponse(
                error = "Network error: ${e.message ?: "Unknown error"}",
                success = false,
                statusCode = 0
            )
        }
    }
}

// Helper function for URL encoding
private fun encodeURIComponent(str: String): String {
    return js("encodeURIComponent")(str) as String
}