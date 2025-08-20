package com.probro.khoded.services

import com.probro.khoded.components.ui.models.ValidationResult
import com.probro.khoded.components.ui.models.ValidationSeverity

/**
 * ValidationService - Implements Single Responsibility Principle (SRP)
 * 
 * This service has ONE responsibility: handle all validation logic for form inputs.
 * Separates validation concerns from UI components, making testing easier and
 * validation rules reusable across different components.
 * 
 * Benefits of SRP implementation:
 * - Centralized validation logic
 * - Easy unit testing of validation rules
 * - Consistent validation behavior across components
 * - Easy to extend with new validation types
 * 
 * @since 2.0.0 (SOLID principles refactor)
 */
class ValidationService {
    
    /**
     * Validates email format using comprehensive regex pattern.
     */
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult(false, "Email is required")
            !email.matches(EMAIL_PATTERN) -> ValidationResult(false, "Please enter a valid email address")
            email.length > MAX_EMAIL_LENGTH -> ValidationResult(false, "Email address is too long")
            else -> ValidationResult(true, "")
        }
    }
    
    /**
     * Validates phone number format (supports international formats).
     */
    fun validatePhone(phone: String): ValidationResult {
        val cleanPhone = phone.replace(Regex("[\\s\\-\\(\\)]"), "")
        return when {
            phone.isEmpty() -> ValidationResult(false, "Phone number is required")
            !cleanPhone.matches(PHONE_PATTERN) -> ValidationResult(false, "Please enter a valid phone number")
            cleanPhone.length < MIN_PHONE_LENGTH -> ValidationResult(false, "Phone number is too short")
            cleanPhone.length > MAX_PHONE_LENGTH -> ValidationResult(false, "Phone number is too long")
            else -> ValidationResult(true, "")
        }
    }
    
    /**
     * Validates required text fields with configurable length constraints.
     */
    fun validateRequiredText(
        text: String, 
        fieldName: String = "This field",
        minLength: Int = 1,
        maxLength: Int = 500
    ): ValidationResult {
        return when {
            text.trim().isEmpty() -> ValidationResult(false, "$fieldName is required")
            text.trim().length < minLength -> ValidationResult(false, "$fieldName must be at least $minLength characters")
            text.length > maxLength -> ValidationResult(false, "$fieldName cannot exceed $maxLength characters")
            else -> ValidationResult(true, "")
        }
    }
    
    /**
     * Validates name fields (person names, company names, etc.).
     */
    fun validateName(name: String, fieldName: String = "Name"): ValidationResult {
        val trimmedName = name.trim()
        return when {
            trimmedName.isEmpty() -> ValidationResult(false, "$fieldName is required")
            trimmedName.length < MIN_NAME_LENGTH -> ValidationResult(false, "$fieldName must be at least $MIN_NAME_LENGTH characters")
            trimmedName.length > MAX_NAME_LENGTH -> ValidationResult(false, "$fieldName cannot exceed $MAX_NAME_LENGTH characters")
            !trimmedName.matches(NAME_PATTERN) -> ValidationResult(false, "$fieldName contains invalid characters")
            else -> ValidationResult(true, "")
        }
    }
    
    /**
     * Validates URL format.
     */
    fun validateUrl(url: String): ValidationResult {
        return when {
            url.isEmpty() -> ValidationResult(false, "URL is required")
            !url.matches(URL_PATTERN) -> ValidationResult(false, "Please enter a valid URL (must start with http:// or https://)")
            url.length > MAX_URL_LENGTH -> ValidationResult(false, "URL is too long")
            else -> ValidationResult(true, "")
        }
    }
    
    /**  
     * Validates message/textarea content with character counting.
     */
    fun validateMessage(
        message: String, 
        minLength: Int = MIN_MESSAGE_LENGTH,
        maxLength: Int = MAX_MESSAGE_LENGTH
    ): ValidationResult {
        val trimmedMessage = message.trim()
        return when {
            trimmedMessage.isEmpty() -> ValidationResult(false, "Message is required")
            trimmedMessage.length < minLength -> ValidationResult(false, "Message must be at least $minLength characters")
            trimmedMessage.length > maxLength -> ValidationResult(false, "Message cannot exceed $maxLength characters")
            else -> {
                val severity = when {
                    trimmedMessage.length > maxLength * 0.9 -> ValidationSeverity.Warning
                    else -> ValidationSeverity.Success
                }
                ValidationResult(true, "", severity)
            }
        }
    }
    
    /**
     * Validates custom pattern with user-defined rules.
     */
    fun validatePattern(
        value: String,
        pattern: Regex,
        errorMessage: String = "Invalid format"
    ): ValidationResult {
        return when {
            value.isEmpty() -> ValidationResult(false, "This field is required")
            !value.matches(pattern) -> ValidationResult(false, errorMessage)
            else -> ValidationResult(true, "")
        }
    }
    
    /**
     * Batch validation for multiple fields (useful for form validation).
     */
    fun validateFields(validations: List<ValidationResult>): ValidationResult {
        val failures = validations.filter { !it.isValid }
        return when {
            failures.isEmpty() -> ValidationResult(true, "All fields valid")
            failures.size == 1 -> failures.first()
            else -> ValidationResult(false, "Please correct ${failures.size} errors in the form")
        }
    }
    
    companion object {
        // Email validation constants
        private val EMAIL_PATTERN = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        private const val MAX_EMAIL_LENGTH = 254
        
        // Phone validation constants  
        private val PHONE_PATTERN = Regex("^[+]?[1-9]?[0-9]{7,15}$")
        private const val MIN_PHONE_LENGTH = 7
        private const val MAX_PHONE_LENGTH = 15
        
        // Name validation constants
        private val NAME_PATTERN = Regex("^[A-Za-z\\s'-]{2,50}$")
        private const val MIN_NAME_LENGTH = 2
        private const val MAX_NAME_LENGTH = 50
        
        // URL validation constants
        private val URL_PATTERN = Regex("^https?://[A-Za-z0-9.-]+\\.[A-Za-z]{2,}.*$")
        private const val MAX_URL_LENGTH = 2048
        
        // Message validation constants
        private const val MIN_MESSAGE_LENGTH = 10
        private const val MAX_MESSAGE_LENGTH = 1000
    }
}

/**
 * Singleton instance for global access to validation service.
 * Following dependency injection principles.
 */
object ValidationServiceProvider {
    val instance: ValidationService by lazy { ValidationService() }
}