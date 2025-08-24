package com.probro.khoded.email

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MailClientTest {

    @Test
    fun `MailSubjects enum should have correct values`() {
        assertEquals("Client Request Form", MailSubjects.CLIENT_REQUEST_FORM.value)
        assertEquals("Contact Us", MailSubjects.CLIENT_CONTACT.value)
    }

    @Test
    fun `EmailProvider enum should exist`() {
        // Test that the EmailProvider enum exists and has expected values
        val provider = EmailProvider.GMAIL_API
        assertNotNull(provider)
    }

    @Test
    fun `should create MailClient with logger`() {
        // Create a mock logger for testing
        val mockLogger = object : com.varabyte.kobweb.api.log.Logger {
            override fun trace(message: String) = Unit
            override fun debug(message: String) = Unit
            override fun info(message: String) = Unit
            override fun warn(message: String) = Unit
            override fun error(message: String) = Unit
        }
        
        val mailClient = MailClient(mockLogger)
        assertNotNull(mailClient)
    }
}