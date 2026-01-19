package com.probro.khoded.messaging.messageData

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MessageDataTest {

    @Test
    fun shouldCreateValidContactMessageDataWithAllFields() {
        val contactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "john.doe@example.com",
            organization = "Test Corp",
            message = "This is a test message",
            subject = "Test Subject"
        )

        assertEquals("John Doe", contactData.name)
        assertEquals("john.doe@example.com", contactData.email)
        assertEquals("Test Corp", contactData.organization)
        assertEquals("This is a test message", contactData.message)
        assertEquals("Test Subject", contactData.subject)
    }

    @Test
    fun shouldCreateValidConsultationMessageData() {
        val consultationData = MessageData.ConsultationMessageData(
            name = "Jane Doe",
            email = "jane@example.com",
            message = "Hello, I need consultation"
        )

        assertEquals("Jane Doe", consultationData.name)
        assertEquals("jane@example.com", consultationData.email)
        assertEquals("Hello, I need consultation", consultationData.message)
    }

    @Test
    fun shouldHandleEmptyOptionalFieldsInContactMessageData() {
        val contactData = MessageData.ContactMessageData(
            name = "Jane Doe",
            email = "jane@example.com",
            organization = "",
            message = "Hello",
            subject = ""
        )

        assertEquals("Jane Doe", contactData.name)
        assertEquals("jane@example.com", contactData.email)
        assertEquals("", contactData.organization)
        assertEquals("Hello", contactData.message)
        assertEquals("", contactData.subject)
    }

    @Test
    fun shouldHandleLongMessages() {
        val longMessage = "A".repeat(500)
        val contactData = MessageData.ContactMessageData(
            name = "Test User",
            email = "test@example.com",
            organization = "Test Corp",
            message = longMessage,
            subject = "Long message test"
        )

        assertEquals(500, contactData.message.length)
    }

    @Test
    fun formTypeEnumShouldHaveCorrectValues() {
        assertEquals("contact", FormType.CONTACT.value)
        assertEquals("consultation", FormType.CONSULTATION.value)
    }

    @Test
    fun mailParamsEnumShouldHaveCorrectValues() {
        assertEquals("type", MailParams.TYPE.value)
    }
}

class MessageDataConverterTest {

    @Test
    fun contactDataConverterShouldSerializeContactMessageDataToJson() {
        val converter = ContactDataConverter()
        val contactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "john@example.com",
            organization = "Test Corp",
            message = "Test message",
            subject = "Test subject"
        )

        val json = converter.serialize(contactData)
        
        assertTrue(json.contains("\"name\":\"John Doe\""))
        assertTrue(json.contains("\"email\":\"john@example.com\""))
        assertTrue(json.contains("\"organization\":\"Test Corp\""))
        assertTrue(json.contains("\"message\":\"Test message\""))
        assertTrue(json.contains("\"subject\":\"Test subject\""))
    }

    @Test
    fun contactDataConverterShouldDeserializeJsonToContactMessageData() {
        val converter = ContactDataConverter()
        val json = """
            {
                "name": "Jane Smith",
                "email": "jane@example.com",
                "organization": "Jane Corp",
                "message": "Hello world",
                "subject": "Greeting"
            }
        """.trimIndent()

        val contactData = converter.deserialize(json)
        
        assertEquals("Jane Smith", contactData.name)
        assertEquals("jane@example.com", contactData.email)
        assertEquals("Jane Corp", contactData.organization)
        assertEquals("Hello world", contactData.message)
        assertEquals("Greeting", contactData.subject)
    }

    @Test
    fun consultationDataConverterShouldSerializeConsultationMessageDataToJson() {
        val converter = ConsultationDataConverter()
        val consultationData = MessageData.ConsultationMessageData(
            name = "John Doe",
            email = "john@example.com",
            message = "Need consultation"
        )

        val json = converter.serialize(consultationData)
        
        assertTrue(json.contains("\"name\":\"John Doe\""))
        assertTrue(json.contains("\"email\":\"john@example.com\""))
        assertTrue(json.contains("\"message\":\"Need consultation\""))
    }

    @Test
    fun consultationDataConverterShouldDeserializeJsonToConsultationMessageData() {
        val converter = ConsultationDataConverter()
        val json = """
            {
                "name": "Jane Smith",
                "email": "jane@example.com",
                "message": "Hello consultation"
            }
        """.trimIndent()

        val consultationData = converter.deserialize(json)
        
        assertEquals("Jane Smith", consultationData.name)
        assertEquals("jane@example.com", consultationData.email)
        assertEquals("Hello consultation", consultationData.message)
    }
}