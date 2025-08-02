package com.probro.khoded.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

/**
 * API Client - Simplified Stub Implementation
 * 
 * This is a simplified stub to resolve compilation issues.
 * Full implementation would require proper HTTP client setup.
 */

@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val error: String? = null,
    val success: Boolean = true
)

class ApiClient {
    
    companion object {
        private const val BASE_URL = "https://api.khoded.com"
        
        fun getInstance(): ApiClient = ApiClient()
    }
    
    suspend fun get(endpoint: String): ApiResponse<String> = withContext(Dispatchers.Default) {
        // Simplified stub - return success response
        ApiResponse(data = "Stub response", success = true)
    }
    
    suspend fun post(endpoint: String, body: String): ApiResponse<String> = withContext(Dispatchers.Default) {
        // Simplified stub - return success response  
        ApiResponse(data = "Stub response", success = true)
    }
    
    suspend fun put(endpoint: String, body: String): ApiResponse<String> = withContext(Dispatchers.Default) {
        // Simplified stub - return success response
        ApiResponse(data = "Stub response", success = true)
    }
    
    suspend fun delete(endpoint: String): ApiResponse<String> = withContext(Dispatchers.Default) {
        // Simplified stub - return success response
        ApiResponse(data = "Stub response", success = true) 
    }
}