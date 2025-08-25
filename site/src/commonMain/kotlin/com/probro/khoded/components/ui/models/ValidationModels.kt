package com.probro.khoded.components.ui.models

/**
 * Enhanced validation system inspired by Shadcn-ui design principles.
 * 
 * Provides comprehensive validation with accessibility support,
 * replacing Bootstrap's InputValidation with improved UX.
 *
 * Features:
 * - WCAG 2.2 compliant error messaging
 * - Real-time validation feedback
 * - Custom validation rules
 * - Accessibility-first design
 *
 * @since 2.0.0 (Bootstrap replacement)
 */

/**
 * Component size enumeration following Shadcn-ui size system.
 */
enum class ComponentSize {
    Small,    // 32px height
    Medium,   // 40px height  
    Large     // 48px height
}

/**
 * Enhanced validation configuration with accessibility support.
 */
data class KhodedValidation(
    val isValid: (String) -> Boolean = { true },
    val errorMessage: String = "",
    val required: Boolean = false,
    val minLength: Int = 0,
    val maxLength: Int = Int.MAX_VALUE,
    val pattern: Regex? = null,
    val customValidator: ((String) -> ValidationResult)? = null
) {
    /**
     * Validate input value and return comprehensive result.
     */
    fun validate(value: String): ValidationResult {
        return when {
            required && value.isEmpty() -> 
                ValidationResult(false, "This field is required")
            
            value.isNotEmpty() && value.length < minLength -> 
                ValidationResult(false, "Minimum length is $minLength characters")
            
            value.length > maxLength -> 
                ValidationResult(false, "Maximum length is $maxLength characters")
            
            pattern != null && !value.matches(pattern) -> 
                ValidationResult(false, errorMessage.ifEmpty { "Invalid format" })
            
            customValidator != null -> customValidator.invoke(value)
            
            !isValid(value) -> 
                ValidationResult(false, errorMessage.ifEmpty { "Invalid input" })
            
            else -> ValidationResult(true, "")
        }
    }
}

/**
 * Validation result with accessibility metadata.
 */
data class ValidationResult(
    val isValid: Boolean,
    val message: String = "",
    val severity: ValidationSeverity = if (isValid) ValidationSeverity.Success else ValidationSeverity.Error
)

/**
 * Validation severity levels for different UI states.
 */
enum class ValidationSeverity {
    Success,  // Green - valid input
    Warning,  // Amber - valid but needs attention  
    Error     // Red - invalid input
}

/**
 * Predefined validation patterns for common use cases.
 */
object ValidationPatterns {
    val EMAIL = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    val PHONE = Regex("^[+]?[1-9]?[0-9]{7,15}$")
    val URL = Regex("^https?://[A-Za-z0-9.-]+\\.[A-Za-z]{2,}.*$")
    val ALPHANUMERIC = Regex("^[A-Za-z0-9]+$")
    val NAME = Regex("^[A-Za-z\\s'-]{2,50}$")
}