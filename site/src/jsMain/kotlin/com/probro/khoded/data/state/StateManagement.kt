package com.probro.khoded.data.state

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import com.probro.khoded.messaging.MessageResult
import com.probro.khoded.messaging.MessagingStage
import com.probro.khoded.messaging.MessagingState
import com.probro.khoded.messaging.messageData.MessageData
import com.probro.khoded.components.ui.models.ValidationResult
import com.probro.khoded.services.ValidationServiceProvider

/**
 * Comprehensive State Management System
 * 
 * Replaces the stub implementation with a production-ready state management solution
 * that integrates with the existing messaging system and validation services.
 * 
 * Features:
 * - Form state management with validation
 * - Integration with existing MessagingState interface
 * - Coroutine-based async operations
 * - Comprehensive error handling
 * - Memory-efficient state updates
 */

/**
 * Generic form state manager that can be used across different form types
 */
class FormStateManager<T : MessageData>(
    private val initialData: T
) {
    var formData by mutableStateOf(initialData)
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errors by mutableStateOf<Map<String, String>>(emptyMap())
        private set
    
    var submitResult by mutableStateOf<MessageResult?>(null)
        private set
    
    var stage by mutableStateOf<MessagingStage>(MessagingStage.IDLE())
        private set
    
    /**
     * Update form data field
     */
    fun updateField(fieldName: String, value: String, validator: ((String) -> ValidationResult)? = null) {
        // Update the data (implementation depends on T type)
        formData = updateFormData(formData, fieldName, value)
        
        // Validate field if validator provided
        validator?.let { validatorFn ->
            val validation = validatorFn(value)
            errors = if (validation.isValid) {
                errors - fieldName
            } else {
                errors + (fieldName to validation.message)
            }
        }
        
        // Clear submit errors when user starts typing
        if (submitResult is MessageResult.MessagingError) {
            submitResult = null
        }
    }
    
    /**
     * Validate all form fields
     */
    fun validateAll(validationRules: Map<String, (String) -> ValidationResult>): Boolean {
        val newErrors = mutableMapOf<String, String>()
        
        validationRules.forEach { (fieldName, validator) ->
            val fieldValue = getFieldValue(formData, fieldName)
            val validation = validator(fieldValue)
            if (!validation.isValid) {
                newErrors[fieldName] = validation.message
            }
        }
        
        errors = newErrors
        return newErrors.isEmpty()
    }
    
    /**
     * Submit form with async operation
     */
    suspend fun submitForm(
        validationRules: Map<String, (String) -> ValidationResult>,
        submitAction: suspend (T) -> MessageResult
    ) {
        // Start loading state
        setLoading(true)
        setStage(MessagingStage.VALIDATING("Validating form data..."))
        
        try {
            // Validate all fields
            if (!validateAll(validationRules)) {
                setStage(MessagingStage.ERROR("Please correct the errors above"))
                submitResult = MessageResult.MessagingError("Validation failed")
                return
            }
            
            // Submit data
            setStage(MessagingStage.SENDING("Submitting form..."))
            val result = submitAction(formData)
            
            submitResult = result
            
            when (result) {
                is MessageResult.Success -> {
                    setStage(MessagingStage.SENT("Form submitted successfully!"))
                    // Reset form on success
                    resetForm()
                }
                is MessageResult.MessagingError -> {
                    setStage(MessagingStage.ERROR("Submission failed"))
                }
            }
            
        } catch (e: Exception) {
            console.error("Form submission error:", e)
            submitResult = MessageResult.MessagingError("Network error: ${e.message}")
            setStage(MessagingStage.ERROR("Network error occurred"))
        } finally {
            setLoading(false)
        }
    }
    
    /**
     * Reset form to initial state
     */
    fun resetForm() {
        formData = initialData
        errors = emptyMap()
        submitResult = null
        setStage(MessagingStage.IDLE())
    }
    
    /**
     * Clear all errors
     */
    fun clearErrors() {
        errors = emptyMap()
        submitResult = null
    }
    
    /**
     * Set loading state
     */
    private fun setLoading(loading: Boolean) {
        isLoading = loading
    }
    
    /**
     * Set messaging stage
     */
    private fun setStage(newStage: MessagingStage) {
        stage = newStage
    }
    
    /**
     * Update form data based on field name - Type-safe implementation
     * 
     * Note: The unchecked cast is safe here because we verify the type with `is` checks
     * before performing the cast, ensuring type safety at runtime.
     */
    private fun updateFormData(data: T, fieldName: String, value: String): T {
        return when (data) {
            is MessageData.ContactMessageData -> {
                when (fieldName) {
                    "name" -> data.copy(name = value)
                    "email" -> data.copy(email = value)
                    "organization" -> data.copy(organization = value)
                    "subject" -> data.copy(subject = value)
                    "message" -> data.copy(message = value)
                    else -> data
                }.let { 
                    @Suppress("UNCHECKED_CAST") // Safe: verified with `is MessageData.ContactMessageData`
                    it as T 
                }
            }
            is MessageData.ConsultationMessageData -> {
                when (fieldName) {
                    "name" -> data.copy(name = value)
                    "email" -> data.copy(email = value)
                    "message" -> data.copy(message = value)
                    else -> data
                }.let { 
                    @Suppress("UNCHECKED_CAST") // Safe: verified with `is MessageData.ConsultationMessageData`
                    it as T 
                }
            }
            else -> data // Default case for unknown types
        }
    }
    
    /**
     * Get field value from form data
     */
    private fun getFieldValue(data: T, fieldName: String): String {
        return when (data) {
            is MessageData.ContactMessageData -> {
                when (fieldName) {
                    "name" -> data.name
                    "email" -> data.email
                    "organization" -> data.organization
                    "subject" -> data.subject
                    "message" -> data.message
                    else -> ""
                }
            }
            is MessageData.ConsultationMessageData -> {
                when (fieldName) {
                    "name" -> data.name
                    "email" -> data.email
                    "message" -> data.message
                    else -> ""
                }
            }
            else -> ""
        }
    }
}

/**
 * Composable function to remember form state
 */
@Composable
fun <T : MessageData> rememberFormState(initialData: T): FormStateManager<T> {
    return remember { FormStateManager(initialData) }
}

/**
 * Specific composable for contact form state
 */
@Composable
fun rememberContactFormState(): FormStateManager<MessageData.ContactMessageData> {
    return rememberFormState(MessageData.ContactMessageData())
}

/**
 * Specific composable for consultation form state
 */
@Composable
fun rememberConsultationFormState(): FormStateManager<MessageData.ConsultationMessageData> {
    return rememberFormState(MessageData.ConsultationMessageData())
}

/**
 * Backward compatibility - Simple form state for basic forms
 */
class FormState {
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    fun validate(input: String): com.probro.khoded.components.ui.models.ValidationResult {
        return if (input.isNotEmpty()) {
            ValidationResult(true, "")
        } else {
            ValidationResult(false, "Input cannot be empty")
        }
    }
    
    fun setLoading(loading: Boolean) {
        isLoading = loading
    }
    
    fun setError(error: String?) {
        errorMessage = error
    }
    
    fun clearError() {
        errorMessage = null
    }
}

/**
 * Backward compatibility function
 */
@Composable
fun rememberFormState(): FormState {
    return remember { FormState() }
}