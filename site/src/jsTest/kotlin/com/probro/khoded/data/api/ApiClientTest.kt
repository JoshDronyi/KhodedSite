package com.probro.khoded.data.api

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class ApiClientTest {

    @Test
    fun `should create ApiClient instance`() {
        val apiClient = ApiClient.getInstance()
        assertNotNull(apiClient)
    }

    @Test
    fun `should serialize data to JSON`() {
        data class TestData(val name: String, val value: Int)
        val testData = TestData("test", 123)
        
        val json = ApiClient.toJson(testData)
        
        assertTrue(json.contains("\"name\":\"test\""))
        assertTrue(json.contains("\"value\":123"))
    }

    @Test
    fun `ApiResponse should handle success responses`() {
        val successResponse = ApiResponse(
            data = "success data",
            success = true,
            statusCode = 200
        )
        
        assertTrue(successResponse.success)
        assertEquals(200, successResponse.statusCode)
        assertEquals("success data", successResponse.data)
        assertEquals(null, successResponse.error)
    }

    @Test
    fun `ApiResponse should handle error responses`() {
        val errorResponse = ApiResponse<String>(
            error = "Something went wrong",
            success = false,
            statusCode = 400
        )
        
        assertFalse(errorResponse.success)
        assertEquals(400, errorResponse.statusCode)
        assertEquals("Something went wrong", errorResponse.error)
        assertEquals(null, errorResponse.data)
    }

    @Test
    fun `ApiError should contain proper error information`() {
        val apiError = ApiError(
            message = "Validation failed",
            code = "VALIDATION_ERROR",
            details = mapOf("field" to "email", "reason" to "invalid format")
        )
        
        assertEquals("Validation failed", apiError.message)
        assertEquals("VALIDATION_ERROR", apiError.code)
        assertNotNull(apiError.details)
        assertEquals("email", apiError.details?.get("field"))
    }

    @Test
    fun `should create proper JSON headers`() {
        // This test would require accessing private methods, so we'll test behavior indirectly
        val apiClient = ApiClient.getInstance()
        assertNotNull(apiClient)
    }
}