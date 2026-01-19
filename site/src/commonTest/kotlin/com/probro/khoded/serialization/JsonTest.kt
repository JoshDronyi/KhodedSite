package com.probro.khoded.serialization

import com.probro.khoded.messaging.messageData.MessageData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlin.test.*

class JsonTest {
    
    @Test
    fun shouldSerializeContactMessageDataToJson() {
        val contactData = MessageData.ContactMessageData(
            name = "John Doe",
            email = "john@example.com",
            organization = "Test Corp",
            message = "Hello world",
            subject = "Test Subject"
        )
        
        val jsonString = json.encodeToString(contactData)

        // Check for key-value pairs (prettyPrint adds space after colon)
        assertTrue(jsonString.contains("\"name\"") && jsonString.contains("John Doe"))
        assertTrue(jsonString.contains("\"email\"") && jsonString.contains("john@example.com"))
        assertTrue(jsonString.contains("\"organization\"") && jsonString.contains("Test Corp"))
        assertTrue(jsonString.contains("\"message\"") && jsonString.contains("Hello world"))
        assertTrue(jsonString.contains("\"subject\"") && jsonString.contains("Test Subject"))
    }
    
    @Test
    fun shouldDeserializeJsonToContactMessageData() {
        val jsonString = """
            {
                "name": "Jane Smith",
                "email": "jane@example.com",
                "organization": "Jane Corp",
                "message": "Test message",
                "subject": "Test subject"
            }
        """.trimIndent()
        
        val contactData = json.decodeFromString<MessageData.ContactMessageData>(jsonString)
        
        assertEquals("Jane Smith", contactData.name)
        assertEquals("jane@example.com", contactData.email)
        assertEquals("Jane Corp", contactData.organization)
        assertEquals("Test message", contactData.message)
        assertEquals("Test subject", contactData.subject)
    }
    
    @Test
    fun shouldSerializeConsultationMessageDataToJson() {
        val consultationData = MessageData.ConsultationMessageData(
            name = "Bob Johnson",
            email = "bob@example.com",
            message = "I need consultation"
        )
        
        val jsonString = json.encodeToString(consultationData)

        // Check for key-value pairs (prettyPrint adds space after colon)
        assertTrue(jsonString.contains("\"name\"") && jsonString.contains("Bob Johnson"))
        assertTrue(jsonString.contains("\"email\"") && jsonString.contains("bob@example.com"))
        assertTrue(jsonString.contains("\"message\"") && jsonString.contains("I need consultation"))
    }
    
    @Test
    fun shouldDeserializeJsonToConsultationMessageData() {
        val jsonString = """
            {
                "name": "Alice Brown",
                "email": "alice@example.com",
                "message": "Consultation request"
            }
        """.trimIndent()
        
        val consultationData = json.decodeFromString<MessageData.ConsultationMessageData>(jsonString)
        
        assertEquals("Alice Brown", consultationData.name)
        assertEquals("alice@example.com", consultationData.email)
        assertEquals("Consultation request", consultationData.message)
    }
    
    @Test
    fun shouldHandleEmptyOptionalFieldsInContactMessageData() {
        val jsonString = """
            {
                "name": "Test User",
                "email": "test@example.com",
                "organization": "",
                "message": "Message",
                "subject": ""
            }
        """.trimIndent()
        
        val contactData = json.decodeFromString<MessageData.ContactMessageData>(jsonString)
        
        assertEquals("Test User", contactData.name)
        assertEquals("test@example.com", contactData.email)
        assertEquals("", contactData.organization)
        assertEquals("Message", contactData.message)
        assertEquals("", contactData.subject)
    }
    
    @Test
    fun shouldHandleSpecialCharactersInJson() {
        val contactData = MessageData.ContactMessageData(
            name = "José María",
            email = "jose@example.com",
            organization = "Café & Co.",
            message = "Hello! 你好 🌟",
            subject = "Special chars: @#$%"
        )
        
        val jsonString = json.encodeToString(contactData)
        val deserializedData = json.decodeFromString<MessageData.ContactMessageData>(jsonString)
        
        assertEquals(contactData.name, deserializedData.name)
        assertEquals(contactData.email, deserializedData.email)
        assertEquals(contactData.organization, deserializedData.organization)
        assertEquals(contactData.message, deserializedData.message)
        assertEquals(contactData.subject, deserializedData.subject)
    }
}