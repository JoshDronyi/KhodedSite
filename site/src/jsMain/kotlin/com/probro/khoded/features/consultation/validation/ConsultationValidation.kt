package com.probro.khoded.features.consultation.validation

import com.probro.khoded.features.consultation.models.*
import com.probro.khoded.components.ui.models.ValidationResult

/**
 * Consultation form validation service
 * Extracted from monolithic file for better maintainability and testability
 */
object ConsultationValidation {
    
    fun validatePersonalInfo(info: PersonalInfo): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        
        if (info.firstName.isBlank()) {
            errors["firstName"] = "First name is required"
        } else if (info.firstName.length < 2) {
            errors["firstName"] = "First name must be at least 2 characters"
        }
        
        if (info.lastName.isBlank()) {
            errors["lastName"] = "Last name is required"
        } else if (info.lastName.length < 2) {
            errors["lastName"] = "Last name must be at least 2 characters"
        }
        
        if (info.email.isBlank()) {
            errors["email"] = "Email address is required"
        } else if (!isValidEmail(info.email)) {
            errors["email"] = "Please enter a valid email address"
        }
        
        if (info.phone.isNotBlank() && !isValidPhone(info.phone)) {
            errors["phone"] = "Please enter a valid phone number"
        }
        
        return errors
    }
    
    fun validateProjectInfo(info: ProjectInfo): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        
        if (info.projectType.isBlank()) {
            errors["projectType"] = "Please select a project type"
        }
        
        if (info.budget.isBlank()) {
            errors["budget"] = "Please select a budget range"
        }
        
        if (info.timeline.isBlank()) {
            errors["timeline"] = "Please select a project timeline"
        }
        
        if (info.description.isBlank()) {
            errors["description"] = "Project description is required"
        } else if (info.description.length < 20) {
            errors["description"] = "Please provide at least 20 characters describing your project"
        }
        
        return errors
    }
    
    fun validateBusinessInfo(info: BusinessInfo): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        
        if (info.industry.isBlank()) {
            errors["industry"] = "Please select your industry"
        }
        
        if (info.companySize.isBlank()) {
            errors["companySize"] = "Please select your company size"
        }
        
        if (info.targetAudience.isBlank()) {
            errors["targetAudience"] = "Please describe your target audience"
        } else if (info.targetAudience.length < 10) {
            errors["targetAudience"] = "Please provide at least 10 characters describing your target audience"
        }
        
        return errors
    }
    
    fun validateAdditionalInfo(info: AdditionalInfo): Map<String, String> {
        // Additional info is mostly optional
        return emptyMap()
    }
    
    fun validateComplete(request: ConsultationRequest): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        
        errors.putAll(validatePersonalInfo(request.personalInfo))
        errors.putAll(validateProjectInfo(request.projectInfo))
        errors.putAll(validateBusinessInfo(request.businessInfo))
        errors.putAll(validateAdditionalInfo(request.additionalInfo))
        
        return errors
    }
    
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && 
               email.contains(".") && 
               email.length > 5 &&
               !email.startsWith("@") &&
               !email.endsWith("@") &&
               email.count { it == '@' } == 1
    }
    
    private fun isValidPhone(phone: String): Boolean {
        val cleanPhone = phone.replace(Regex("[^0-9+]"), "")
        return cleanPhone.length in 10..15
    }
}