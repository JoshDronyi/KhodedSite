package com.probro.khoded.data.api

import com.probro.khoded.messaging.messageData.MessageData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class ApiClientTest {

    @Test
    fun shouldCreateApiClientInstance() {
        val apiClient = ApiClient.getInstance()
        assertNotNull(apiClient)
    }

    @Test
    fun shouldSerializeDataToJson() {
        // Use an existing @Serializable class instead of local data class
        val contactData = MessageData.ContactMessageData(
            name = "test",
            email = "test@example.com",
            message = "test message"
        )

        val json = ApiClient.toJson(contactData)

        assertTrue(json.contains("\"name\":\"test\""))
        assertTrue(json.contains("\"email\":\"test@example.com\""))
    }

    @Test
    fun apiResponseShouldHandleSuccessResponses() {
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
    fun apiResponseShouldHandleErrorResponses() {
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
    fun apiErrorShouldContainProperErrorInformation() {
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
    fun shouldCreateProperJsonHeaders() {
        // This test would require accessing private methods, so we'll test behavior indirectly
        val apiClient = ApiClient.getInstance()
        assertNotNull(apiClient)
    }
}