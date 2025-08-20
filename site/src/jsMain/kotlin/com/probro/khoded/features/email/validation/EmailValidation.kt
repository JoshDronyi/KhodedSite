package com.probro.khoded.features.email.validation

import com.probro.khoded.features.email.models.*
import com.probro.khoded.components.ui.models.ValidationResult
import com.probro.khoded.components.ui.models.KhodedValidation

/**
 * Email Validation Logic
 * 
 * Comprehensive validation for all email-related inputs.
 * Separated from UI for reusability and testing.
 */

object EmailValidation {
    
    private val emailRegex = Regex(
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    )
    
    fun validateEmailAddress(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "Email address is required")
            email.length > 254 -> ValidationResult(false, "Email address is too long")
            !emailRegex.matches(email) -> ValidationResult(false, "Please enter a valid email address")
            else -> ValidationResult(true)
        }
    }
    
    fun validateEmailSubject(subject: String): ValidationResult {
        return when {
            subject.isBlank() -> ValidationResult(false, "Subject is required")
            subject.length < 3 -> ValidationResult(false, "Subject must be at least 3 characters")
            subject.length > 200 -> ValidationResult(false, "Subject must be less than 200 characters")
            else -> ValidationResult(true)
        }
    }
    
    fun validateEmailBody(body: String): ValidationResult {
        return when {
            body.isBlank() -> ValidationResult(false, "Message body is required")
            body.length < 10 -> ValidationResult(false, "Message must be at least 10 characters")
            body.length > 10000 -> ValidationResult(false, "Message must be less than 10,000 characters")
            else -> ValidationResult(true)
        }
    }
    
    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "Name is required")
            name.length < 2 -> ValidationResult(false, "Name must be at least 2 characters")
            name.length > 100 -> ValidationResult(false, "Name must be less than 100 characters")
            !name.all { it.isLetter() || it.isWhitespace() || it == '\'' || it == '-' } -> 
                ValidationResult(false, "Name can only contain letters, spaces, hyphens, and apostrophes")
            else -> ValidationResult(true)
        }
    }
    
    fun validateCompanyName(companyName: String): ValidationResult {
        return when {
            companyName.isBlank() -> ValidationResult(false, "Company name is required")
            companyName.length < 2 -> ValidationResult(false, "Company name must be at least 2 characters")
            companyName.length > 200 -> ValidationResult(false, "Company name must be less than 200 characters")
            else -> ValidationResult(true)
        }
    }
    
    fun validatePhoneNumber(phone: String): ValidationResult {
        val phoneDigits = phone.replace(Regex("[^0-9+]"), "")
        return when {
            phone.isBlank() -> ValidationResult(false, "Phone number is required")
            phoneDigits.length < 7 -> ValidationResult(false, "Phone number is too short")
            phoneDigits.length > 15 -> ValidationResult(false, "Phone number is too long")
            else -> ValidationResult(true)
        }
    }
    
    fun validateProjectBudget(budget: String): ValidationResult {
        return when {
            budget.isBlank() -> ValidationResult(false, "Budget range is required")
            else -> ValidationResult(true)
        }
    }
    
    fun validateProjectDescription(description: String): ValidationResult {
        return when {
            description.isBlank() -> ValidationResult(false, "Project description is required")
            description.length < 20 -> ValidationResult(false, "Please provide more details (minimum 20 characters)")
            description.length > 5000 -> ValidationResult(false, "Description must be less than 5,000 characters")
            else -> ValidationResult(true)
        }
    }
    
    fun validateTimeframe(timeframe: String): ValidationResult {
        return when {
            timeframe.isBlank() -> ValidationResult(false, "Project timeframe is required")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * Pre-configured KhodedValidation objects for common use cases
     */
    val emailValidator = KhodedValidation(
        customValidator = ::validateEmailAddress
    )
    
    val nameValidator = KhodedValidation(
        customValidator = ::validateName
    )
    
    val subjectValidator = KhodedValidation(
        customValidator = ::validateEmailSubject
    )
    
    val bodyValidator = KhodedValidation(
        customValidator = ::validateEmailBody
    )
    
    val phoneValidator = KhodedValidation(
        customValidator = ::validatePhoneNumber
    )
    
    val companyValidator = KhodedValidation(
        customValidator = ::validateCompanyName
    )
    
    val projectDescriptionValidator = KhodedValidation(
        customValidator = ::validateProjectDescription
    )
    
    val budgetValidator = KhodedValidation(
        customValidator = ::validateProjectBudget
    )
    
    val timeframeValidator = KhodedValidation(
        customValidator = ::validateTimeframe
    )
}