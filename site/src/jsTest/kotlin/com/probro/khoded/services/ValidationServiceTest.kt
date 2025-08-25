package com.probro.khoded.services

import com.probro.khoded.components.ui.models.ValidationResult
import com.probro.khoded.services.ValidationService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidationServiceTest {
    
    private val validationService = ValidationService()

    @Test
    fun shouldValidateCorrectEmailAddresses() {
        val validEmails = listOf(
            "user@example.com",
            "test.email@domain.co.uk",
            "firstname.lastname@company.org",
            "user+tag@example.com",
            "user123@test-domain.com",
            "valid_email@sub.domain.com"
        )

        validEmails.forEach { email ->
            val result = validationService.validateEmail(email)
            assertTrue(result.isValid, "Email $email should be valid")
            assertEquals("", result.message)
        }
    }

    @Test
    fun shouldRejectInvalidEmailAddresses() {
        val invalidEmails = listOf(
            "plainaddress",
            "user@",
            "@domain.com",
            "user..name@domain.com",
            "user@domain",
            "user@.domain.com",
            "user@domain.com.",
            "",
            "   ",
            "user name@domain.com",
            "user@domain com"
        )

        invalidEmails.forEach { email ->
            val result = validationService.validateEmail(email)
            assertFalse(result.isValid, "Email $email should be invalid")
            assertTrue(result.message.isNotEmpty())
        }
    }

    @Test
    fun shouldValidateVariousPhoneNumberFormats() {
        val validPhones = listOf(
            "1234567890",
            "(123) 456-7890",
            "123-456-7890",
            "123.456.7890",
            "+1 123 456 7890",
            "+44 20 7946 0958",
            "123 456 7890"
        )

        validPhones.forEach { phone ->
            val result = validationService.validatePhone(phone)
            assertTrue(result.isValid, "Phone $phone should be valid")
            assertEquals("", result.message)
        }
    }

    @Test
    fun shouldRejectInvalidPhoneNumbers() {
        val invalidPhones = listOf(
            "",
            "123",
            "12345678901234567890",
            "abc-def-ghij",
            "123-45-6789"
        )

        invalidPhones.forEach { phone ->
            val result = validationService.validatePhone(phone)
            assertFalse(result.isValid, "Phone $phone should be invalid")
            assertTrue(result.message.isNotEmpty())
        }
    }

    @Test
    fun shouldValidateProperNames() {
        val validNames = listOf(
            "John Doe",
            "Mary Jane Smith",
            "José María García",
            "O'Connor",
            "Jean-Pierre",
            "Al-Rahman"
        )

        validNames.forEach { name ->
            val result = validationService.validateName(name)
            assertTrue(result.isValid, "Name $name should be valid")
            assertEquals("", result.message)
        }
    }

    @Test
    fun shouldRejectInvalidNames() {
        val invalidNames = listOf(
            "",
            "   ",
            "A",
            "A".repeat(101),
            "John123",
            "John@Doe"
        )

        invalidNames.forEach { name ->
            val result = validationService.validateName(name)
            assertFalse(result.isValid, "Name $name should be invalid")
            assertTrue(result.message.isNotEmpty())
        }
    }

    @Test
    fun shouldValidateProperMessages() {
        val validMessages = listOf(
            "Hello, I would like to inquire about your services.",
            "This is a test message with special characters: áéíóú ñ ç",
            "Message with numbers: 123 and symbols: !@#$%",
            "Multi-line\nmessage\nwith\nbreaks",
            "A".repeat(100)
        )

        validMessages.forEach { message ->
            val result = validationService.validateMessage(message)
            assertTrue(result.isValid, "Message should be valid")
            assertEquals("", result.message)
        }
    }

    @Test
    fun shouldRejectInvalidMessages() {
        val invalidMessages = listOf(
            "",
            "   ",
            "Hi" // Too short
        )

        invalidMessages.forEach { message ->
            val result = validationService.validateMessage(message)
            assertFalse(result.isValid, "Message should be invalid")
            assertTrue(result.message.isNotEmpty())
        }
    }

    @Test
    fun shouldValidateUrls() {
        val validUrls = listOf(
            "https://example.com",
            "http://example.com",
            "https://subdomain.example.com/path",
            "https://example.co.uk/path?param=value"
        )

        validUrls.forEach { url ->
            val result = validationService.validateUrl(url)
            assertTrue(result.isValid, "URL $url should be valid")
            assertEquals("", result.message)
        }
    }

    @Test
    fun shouldRejectInvalidUrls() {
        val invalidUrls = listOf(
            "",
            "not-a-url",
            "ftp://example.com", // Wrong protocol
            "example.com" // Missing protocol
        )

        invalidUrls.forEach { url ->
            val result = validationService.validateUrl(url)
            assertFalse(result.isValid, "URL $url should be invalid")
            assertTrue(result.message.isNotEmpty())
        }
    }

    @Test
    fun shouldValidateRequiredTextFields() {
        val result1 = validationService.validateRequiredText("Valid text")
        assertTrue(result1.isValid)

        val result2 = validationService.validateRequiredText("")
        assertFalse(result2.isValid)

        val result3 = validationService.validateRequiredText("   ")
        assertFalse(result3.isValid)
    }

    @Test
    fun shouldValidateWithCustomPatterns() {
        val alphanumericPattern = Regex("^[A-Za-z0-9]+$")
        
        val result1 = validationService.validatePattern("ABC123", alphanumericPattern)
        assertTrue(result1.isValid)

        val result2 = validationService.validatePattern("ABC-123", alphanumericPattern)
        assertFalse(result2.isValid)
    }

    @Test
    fun shouldBatchValidateMultipleFields() {
        val validResults = listOf(
            ValidationResult(true, ""),
            ValidationResult(true, ""),
            ValidationResult(true, "")
        )
        
        val batchResult1 = validationService.validateFields(validResults)
        assertTrue(batchResult1.isValid)

        val mixedResults = listOf(
            ValidationResult(true, ""),
            ValidationResult(false, "Error 1"),
            ValidationResult(false, "Error 2")
        )

        val batchResult2 = validationService.validateFields(mixedResults)
        assertFalse(batchResult2.isValid)
    }
}