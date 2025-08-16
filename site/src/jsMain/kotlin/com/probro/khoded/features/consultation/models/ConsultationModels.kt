package com.probro.khoded.features.consultation.models

/**
 * Consultation request data models
 * Extracted from monolithic ConsultationRequestSystem for better maintainability
 */

/**
 * Consultation request data model
 */
data class ConsultationRequest(
    val personalInfo: PersonalInfo = PersonalInfo(),
    val projectInfo: ProjectInfo = ProjectInfo(),
    val businessInfo: BusinessInfo = BusinessInfo(),
    val additionalInfo: AdditionalInfo = AdditionalInfo()
)

data class PersonalInfo(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val company: String = "",
    val title: String = ""
)

data class ProjectInfo(
    val projectType: String = "",
    val budget: String = "",
    val timeline: String = "",
    val description: String = "",
    val goals: List<String> = emptyList(),
    val platforms: List<String> = emptyList()
)

data class BusinessInfo(
    val industry: String = "",
    val companySize: String = "",
    val currentWebsite: String = "",
    val targetAudience: String = "",
    val competitors: List<String> = emptyList()
)

data class AdditionalInfo(
    val hasExistingBrand: Boolean = false,
    val needsHosting: Boolean = false,
    val needsMaintenance: Boolean = false,
    val additionalServices: List<String> = emptyList(),
    val comments: String = ""
)

enum class ConsultationStep {
    PERSONAL_INFO,
    PROJECT_INFO, 
    BUSINESS_INFO,
    ADDITIONAL_INFO,
    REVIEW,
    SUBMIT
}

data class ConsultationFormState(
    val currentStep: ConsultationStep = ConsultationStep.PERSONAL_INFO,
    val request: ConsultationRequest = ConsultationRequest(),
    val isSubmitting: Boolean = false,
    val isComplete: Boolean = false,
    val errors: Map<String, String> = emptyMap(),
    val progress: Float = 0f
)