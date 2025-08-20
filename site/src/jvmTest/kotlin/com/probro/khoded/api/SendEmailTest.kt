package com.probro.khoded.api

import com.probro.khoded.messaging.messageData.MessageData
import com.probro.khoded.messaging.messageData.FormType
import com.probro.khoded.messaging.messageData.MailParams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SendEmailTest {

    @Test
    fun `should validate contact message data successfully`() {
        val validContactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "john@example.com",
            organization = "Test Corp",
            subject = "Test subject",
            message = "This is a valid test message with sufficient length."
        )

        // This should not throw an exception
        validateMessageData(validContactData)
    }

    @Test
    fun `should validate consultation message data successfully`() {
        val validConsultationData = MessageData.ConsultationMessageData(
            name = "Jane Doe",
            email = "jane@example.com",
            message = "This is a valid consultation message."
        )

        // This should not throw an exception
        validateMessageData(validConsultationData)
    }

    @Test
    fun `should reject empty name`() {
        val invalidContactData = MessageData.ContactMessageData(
            name = "",
            email = "john@example.com",
            organization = "Test Corp",
            subject = "Test subject",
            message = "This is a test message."
        )

        assertFailsWith<IllegalArgumentException> {
            validateMessageData(invalidContactData)
        }
    }

    @Test
    fun `should reject invalid email format`() {
        val invalidContactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "invalid-email",
            organization = "Test Corp",
            subject = "Test subject",
            message = "This is a test message."
        )

        assertFailsWith<IllegalArgumentException> {
            validateMessageData(invalidContactData)
        }
    }

    @Test
    fun `should reject message that is too short`() {
        val invalidContactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "john@example.com",
            organization = "Test Corp",
            subject = "Test subject",
            message = "Short"
        )

        assertFailsWith<IllegalArgumentException> {
            validateMessageData(invalidContactData)
        }
    }

    @Test
    fun `should reject message with XSS content`() {
        val maliciousContactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "john@example.com",
            organization = "Test Corp",
            subject = "Test subject",
            message = "This message contains <script>alert('xss')</script> malicious content."
        )

        assertFailsWith<IllegalArgumentException> {
            validateMessageData(maliciousContactData)
        }
    }

    @Test
    fun `should reject message with SQL injection patterns`() {
        val maliciousContactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "john@example.com",
            organization = "Test Corp",
            subject = "Test subject",
            message = "This message contains DROP TABLE users; malicious content."
        )

        assertFailsWith<IllegalArgumentException> {
            validateMessageData(maliciousContactData)
        }
    }

    @Test
    fun `should reject name with malicious content`() {
        val maliciousContactData = MessageData.ContactMessageData(
            name = "<script>alert('xss')</script>",
            email = "john@example.com",
            organization = "Test Corp",
            subject = "Test subject",
            message = "This is a valid test message."
        )

        assertFailsWith<IllegalArgumentException> {
            validateMessageData(maliciousContactData)
        }
    }

    @Test
    fun `should reject name that is too long`() {
        val invalidContactData = MessageData.ContactMessageData(
            name = "A".repeat(100),
            email = "john@example.com",
            organization = "Test Corp",
            subject = "Test subject",
            message = "This is a valid test message."
        )

        assertFailsWith<IllegalArgumentException> {
            validateMessageData(invalidContactData)
        }
    }

    @Test
    fun `should reject email that is too long`() {
        val longEmail = "a".repeat(250) + "@example.com"
        val invalidContactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = longEmail,
            organization = "Test Corp",
            subject = "Test subject",
            message = "This is a valid test message."
        )

        assertFailsWith<IllegalArgumentException> {
            validateMessageData(invalidContactData)
        }
    }

    @Test
    fun `should handle form data parsing correctly`() {
        val formData = "name=John+Doe&email=john%40example.com&message=Test+message"
        val parsedData = parseFormData(formData)
        
        assertEquals("John Doe", parsedData["name"])
        assertEquals("john@example.com", parsedData["email"])
        assertEquals("Test message", parsedData["message"])
    }

    @Test
    fun `should validate test domains are rejected`() {
        val testEmails = listOf(
            "user@test.com",
            "user@example.com",
            "user@fake.com",
            "user@invalid.com"
        )

        testEmails.forEach { testEmail ->
            val invalidContactData = MessageData.ContactMessageData(
                name = "John Doe",
                email = testEmail,
                organization = "Test Corp",
                subject = "Test subject",
                message = "This is a valid test message."
            )

            assertFailsWith<IllegalArgumentException> {
                validateMessageData(invalidContactData)
            }
        }
    }

    @Test
    fun `FormType enum should have correct values`() {
        assertEquals("contact", FormType.CONTACT.value)
        assertEquals("consultation", FormType.CONSULTATION.value)
    }

    @Test
    fun `MailParams enum should have correct values`() {
        assertEquals("type", MailParams.TYPE.value)
    }

    @Test
    fun `RateLimitData should initialize properly`() {
        val rateLimitData = RateLimitData()
        assertEquals(0, rateLimitData.requestCount)
        assertTrue(rateLimitData.windowStart > 0)
    }

    @Test
    fun `should accept valid organization names`() {
        val validContactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "john@validcompany.com", // Use non-test domain
            organization = "Valid Company Inc.",
            subject = "Test subject",
            message = "This is a valid test message with sufficient length."
        )

        // This should not throw an exception
        validateMessageData(validContactData)
    }

    @Test
    fun `should reject spam-like messages`() {
        val spamMessage = """
            Click here now! Limited time offer! Buy now! Act now!
            Free money guaranteed income work from home amazing opportunity!
        """.trimIndent()
        
        val spamContactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "john@realcompany.org", // Use non-test domain
            organization = "Test Corp",
            subject = "Test subject",
            message = spamMessage
        )

        assertFailsWith<IllegalArgumentException> {
            validateMessageData(spamContactData)
        }
    }
}