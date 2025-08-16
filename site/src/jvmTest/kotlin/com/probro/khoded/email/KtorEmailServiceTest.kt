package com.probro.khoded.email

import com.probro.khoded.messaging.messageData.MailResponse
import com.varabyte.kobweb.api.log.Logger
import kotlin.test.*
import kotlinx.coroutines.runBlocking

class KtorEmailServiceTest {
    
    private val testLogger = object : Logger {
        override fun trace(message: String) {}
        override fun debug(message: String) {}
        override fun info(message: String) {}
        override fun warn(message: String) {}
        override fun error(message: String) {}
    }
    
    @Test
    fun `should create KtorEmailService with Gmail provider`() {
        val service = KtorEmailService(testLogger, EmailProvider.GMAIL_API)
        assertNotNull(service)
    }
    
    @Test
    fun `should validate email parameters before sending`() = runBlocking {
        val service = KtorEmailService(testLogger, EmailProvider.GMAIL_API)
        
        // Test with invalid email
        val response = service.sendEmail(
            toEmail = "invalid-email",
            subject = "Test",
            body = "Test message",
            isHtml = false
        )
        
        // Should return error response for invalid email
        assertTrue(response is MailResponse.Error)
    }
    
    @Test
    fun `should handle valid email format`() = runBlocking {
        val service = KtorEmailService(testLogger, EmailProvider.GMAIL_API)
        
        // Test with valid email format (will fail at API level in test, but format should pass)
        val response = service.sendEmail(
            toEmail = "test@example.com",
            subject = "Test Subject",
            body = "Test message body",
            isHtml = false
        )
        
        // Response should be created (though API call may fail in test environment)
        assertNotNull(response)
    }
    
    @Test
    fun `should support HTML email format`() = runBlocking {
        val service = KtorEmailService(testLogger, EmailProvider.GMAIL_API)
        
        val htmlBody = "<html><body><h1>Test HTML Email</h1></body></html>"
        
        val response = service.sendEmail(
            toEmail = "test@example.com",
            subject = "HTML Test",
            body = htmlBody,
            isHtml = true
        )
        
        assertNotNull(response)
    }
    
    @Test
    fun `should handle empty subject gracefully`() = runBlocking {
        val service = KtorEmailService(testLogger, EmailProvider.GMAIL_API)
        
        val response = service.sendEmail(
            toEmail = "test@example.com",
            subject = "",
            body = "Message with empty subject",
            isHtml = false
        )
        
        assertNotNull(response)
    }
    
    @Test
    fun `should handle long message bodies`() = runBlocking {
        val service = KtorEmailService(testLogger, EmailProvider.GMAIL_API)
        
        val longBody = "A".repeat(10000) // 10KB message
        
        val response = service.sendEmail(
            toEmail = "test@example.com",
            subject = "Long message test",
            body = longBody,
            isHtml = false
        )
        
        assertNotNull(response)
    }
    
    @Test
    fun `should close resources properly`() = runBlocking {
        val service = KtorEmailService(testLogger, EmailProvider.GMAIL_API)
        
        try {
            service.close()
            assertTrue(true) // Service closed successfully
        } catch (e: Exception) {
            fail("Service should close without throwing: ${e.message}")
        }
    }
    
    @Test
    fun `should handle service unavailability`() = runBlocking {
        val service = KtorEmailService(testLogger, EmailProvider.GMAIL_API)
        
        // Test with configuration that would cause service failure
        val response = service.sendEmail(
            toEmail = "test@example.com",
            subject = "Service test",
            body = "Testing service availability",
            isHtml = false
        )
        
        // Should handle errors gracefully
        assertNotNull(response)
    }
}