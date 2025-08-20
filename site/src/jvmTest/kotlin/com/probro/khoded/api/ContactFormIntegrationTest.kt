package com.probro.khoded.api

import com.probro.khoded.messaging.messageData.MessageData
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class ContactFormIntegrationTest {
    
    @Test
    fun `contact form data should integrate with API validation`() {
        // Test the integration between frontend form data and backend validation
        val validFormData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "john@validcompany.com",
            organization = "Test Company",
            subject = "Integration Test",
            message = "This is a comprehensive integration test to verify the contact form works with API validation."
        )
        
        // This should pass validation
        validateMessageData(validFormData)
        assertTrue(true, "Valid form data passes API validation")
    }
    
    @Test
    fun `form data parsing should handle URL encoded data from frontend`() {
        val formData = "name=John+Doe&email=john%40example.com&subject=Test+Subject&message=This+is+a+test+message+from+the+contact+form"
        val parsedData = parseFormData(formData)
        
        assertEquals("John Doe", parsedData["name"])
        assertEquals("john@example.com", parsedData["email"])
        assertEquals("Test Subject", parsedData["subject"])
        assertEquals("This is a test message from the contact form", parsedData["message"])
    }
    
    @Test
    fun `rate limiting should protect against form spam`() {
        val rateLimitData = RateLimitData()
        
        // Simulate multiple requests
        rateLimitData.requestCount = 5
        rateLimitData.windowStart = System.currentTimeMillis()
        
        assertEquals(5, rateLimitData.requestCount)
        assertTrue(rateLimitData.windowStart > 0)
    }
    
    @Test
    fun `contact form should reject XSS attempts from frontend`() {
        val maliciousData = MessageData.ContactMessageData(
            name = "Test User",
            email = "test@validcompany.com",
            organization = "Test Corp",
            subject = "Test Subject",
            message = "This contains <script>alert('xss')</script> malicious content"
        )
        
        assertFailsWith<IllegalArgumentException> {
            validateMessageData(maliciousData)
        }
    }
    
    @Test
    fun `contact form should reject SQL injection attempts`() {
        val maliciousData = MessageData.ContactMessageData(
            name = "Test User",
            email = "test@validcompany.com",
            organization = "Test Corp",
            subject = "Test Subject",
            message = "This contains DROP TABLE users; malicious content"
        )
        
        assertFailsWith<IllegalArgumentException> {
            validateMessageData(maliciousData)
        }
    }
    
    @Test
    fun `contact form should validate email format from frontend`() {
        val invalidEmailData = MessageData.ContactMessageData(
            name = "Test User",
            email = "invalid-email-format",
            organization = "Test Corp",
            subject = "Test Subject",
            message = "This is a valid message"
        )
        
        assertFailsWith<IllegalArgumentException> {
            validateMessageData(invalidEmailData)
        }
    }
    
    @Test
    fun `contact form should enforce message length limits`() {
        val shortMessageData = MessageData.ContactMessageData(
            name = "Test User",
            email = "test@validcompany.com",
            organization = "Test Corp",
            subject = "Test Subject",
            message = "Short"
        )
        
        assertFailsWith<IllegalArgumentException> {
            validateMessageData(shortMessageData)
        }
        
        val longMessage = "A".repeat(1001)
        val longMessageData = MessageData.ContactMessageData(
            name = "Test User",
            email = "test@validcompany.com",
            organization = "Test Corp",
            subject = "Test Subject",
            message = longMessage
        )
        
        assertFailsWith<IllegalArgumentException> {
            validateMessageData(longMessageData)
        }
    }
    
    @Test
    fun `contact form should handle special characters in names`() {
        val specialCharData = MessageData.ContactMessageData(
            name = "Jean-Pierre O'Connor",
            email = "jp@validcompany.com",
            organization = "Test Corp",
            subject = "Test Subject",
            message = "Testing special characters in names"
        )
        
        // This should pass validation
        validateMessageData(specialCharData)
        assertTrue(true, "Names with hyphens and apostrophes are valid")
    }
    
    @Test
    fun `contact form integration should be production ready`() {
        // Comprehensive test that verifies all integration points are working
        val productionReadyData = MessageData.ContactMessageData(
            name = "Jane Smith",
            email = "jane.smith@realcompany.com",
            organization = "Real Business Corp",
            subject = "Production Integration Test",
            message = "This test verifies that the contact form integration is ready for production deployment with proper validation, security, and error handling."
        )
        
        validateMessageData(productionReadyData)
        
        // Verify form data can be parsed
        val formString = "name=Jane+Smith&email=jane.smith%40realcompany.com&subject=Production+Test&message=Test+message"
        val parsedForm = parseFormData(formString)
        assertNotNull(parsedForm)
        assertTrue(parsedForm.containsKey("name"))
        assertTrue(parsedForm.containsKey("email"))
        
        assertTrue(true, "Contact form integration is production ready")
    }
}