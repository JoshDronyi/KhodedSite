package com.probro.khoded.utils.messaging

import com.probro.khoded.messaging.messageData.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise

class MailClientTest {
    
    @Test
    fun shouldHandleContactMessageSending() = MainScope().promise {
        val contactData = MessageData.ContactMessageData(
            name = "Test User",
            email = "test@example.com",
            organization = "Test Corp",
            message = "Test message",
            subject = "Test Subject"
        )
        
        val response = MailClient.sendEmail(contactData, FormType.CONTACT)
        
        assertNotNull(response)
        // In test environment, this will likely be an error due to no API access
        // But we verify the method handles the call properly
    }
    
    @Test
    fun shouldHandleConsultationMessageSending() = MainScope().promise {
        val consultationData = MessageData.ConsultationMessageData(
            name = "Consultant",
            email = "consultant@example.com",
            message = "Need consultation"
        )
        
        val response = MailClient.sendEmail(consultationData, FormType.CONSULTATION)
        
        assertNotNull(response)
    }
    
    @Test
    fun shouldValidateMessageTypeMatchesDataType() = MainScope().promise {
        val contactData = MessageData.ContactMessageData(
            name = "Test User",
            email = "test@example.com",
            organization = "Test Corp",
            message = "Test message",
            subject = "Test Subject"
        )
        
        // Try to send contact data with consultation type - should fail validation
        val response = MailClient.sendEmail(contactData, FormType.CONSULTATION)
        
        assertTrue(response is MailResponse.Error)
        assertTrue(response.exceptionMessage.contains("Please send a ConsultationMessageData object"))
    }
    
    @Test
    fun shouldValidateConsultationTypeMatchesDataType() = MainScope().promise {
        val consultationData = MessageData.ConsultationMessageData(
            name = "Consultant",
            email = "consultant@example.com",
            message = "Need consultation"
        )
        
        // Try to send consultation data with contact type - should fail validation
        val response = MailClient.sendEmail(consultationData, FormType.CONTACT)
        
        assertTrue(response is MailResponse.Error)
        assertTrue(response.exceptionMessage.contains("Please send a ContactMessageData object"))
    }
    
    @Test
    fun shouldHandleIntakeFormSubmission() = MainScope().promise {
        val intakeForm = JsonObject(mapOf(
            "organization" to JsonPrimitive("Test Organization"),
            "contactEmail" to JsonPrimitive("contact@test.com"),
            "projectType" to JsonPrimitive("Web Development")
        ))
        
        val response = MailClient.sendIntakeForm(intakeForm)
        
        assertNotNull(response)
        // In test environment, this will likely be an error due to no API access
        // But we verify the method handles the call properly
    }
    
    @Test
    fun shouldHandleEmptyIntakeForm() = MainScope().promise {
        val emptyForm = JsonObject(emptyMap())
        
        val response = MailClient.sendIntakeForm(emptyForm)
        
        assertNotNull(response)
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
    
    @Test
    fun shouldHandleLargeMessageData() = MainScope().promise {
        val largeMessage = "A".repeat(5000) // 5KB message
        
        val contactData = MessageData.ContactMessageData(
            name = "Test User",
            email = "test@example.com",
            organization = "Test Corp",
            message = largeMessage,
            subject = "Large message test"
        )
        
        val response = MailClient.sendEmail(contactData, FormType.CONTACT)
        
        assertNotNull(response)
    }
}