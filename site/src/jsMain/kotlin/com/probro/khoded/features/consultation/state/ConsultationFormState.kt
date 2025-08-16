package com.probro.khoded.features.consultation.state

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import com.probro.khoded.features.consultation.models.*

/**
 * Consultation form state management
 * Extracted from monolithic ConsultationRequestSystem for better maintainability
 */

/**
 * Form submission status
 */
sealed class SubmissionStatus {
    object Idle : SubmissionStatus()
    object Loading : SubmissionStatus()
    data class Success(val message: String) : SubmissionStatus()
    data class Error(val message: String, val canRetry: Boolean = true) : SubmissionStatus()
}

/**
 * Form step configuration
 */
data class FormStepConfig(
    val step: ConsultationStep,
    val title: String,
    val description: String,
    val isOptional: Boolean = false
)

/**
 * Complete consultation form state
 */
data class ExtendedConsultationFormState(
    val currentStep: Int = 0,
    val consultationRequest: ConsultationRequest = ConsultationRequest(),
    val validationErrors: Map<String, String> = emptyMap(),
    val submissionStatus: SubmissionStatus = SubmissionStatus.Idle,
    val isAutoSaving: Boolean = false,
    val isSubmitting: Boolean = false,
    val steps: List<FormStepConfig> = defaultSteps
) {
    val currentStepData: FormStepConfig
        get() = steps.getOrNull(currentStep) ?: steps.first()
    
    val progressPercentage: Float
        get() = if (steps.isEmpty()) 0f else ((currentStep + 1).toFloat() / steps.size.toFloat()) * 100f
    
    val isLastStep: Boolean
        get() = currentStep >= steps.size - 1
    
    val canProceed: Boolean
        get() = when (currentStepData.step) {
            ConsultationStep.PERSONAL_INFO -> {
                consultationRequest.personalInfo.firstName.isNotBlank() &&
                consultationRequest.personalInfo.lastName.isNotBlank() &&
                consultationRequest.personalInfo.email.isNotBlank() &&
                !validationErrors.keys.any { it in listOf("firstName", "lastName", "email", "phone") }
            }
            ConsultationStep.PROJECT_INFO -> {
                consultationRequest.projectInfo.projectType.isNotBlank() &&
                consultationRequest.projectInfo.budget.isNotBlank() &&
                consultationRequest.projectInfo.timeline.isNotBlank() &&
                consultationRequest.projectInfo.description.isNotBlank() &&
                !validationErrors.keys.any { it in listOf("projectType", "budget", "timeline", "description") }
            }
            ConsultationStep.BUSINESS_INFO -> {
                consultationRequest.businessInfo.industry.isNotBlank() &&
                consultationRequest.businessInfo.companySize.isNotBlank() &&
                consultationRequest.businessInfo.targetAudience.isNotBlank() &&
                !validationErrors.keys.any { it in listOf("industry", "companySize", "targetAudience") }
            }
            ConsultationStep.ADDITIONAL_INFO -> true // Optional step
            ConsultationStep.REVIEW -> true
            ConsultationStep.SUBMIT -> false // Handled separately
        }

    companion object {
        val defaultSteps = listOf(
            FormStepConfig(
                step = ConsultationStep.PERSONAL_INFO,
                title = "Personal Information",
                description = "Tell us about yourself"
            ),
            FormStepConfig(
                step = ConsultationStep.PROJECT_INFO,
                title = "Project Details",
                description = "Describe your project requirements"
            ),
            FormStepConfig(
                step = ConsultationStep.BUSINESS_INFO,
                title = "Business Information",
                description = "Help us understand your business"
            ),
            FormStepConfig(
                step = ConsultationStep.ADDITIONAL_INFO,
                title = "Additional Services",
                description = "Optional additional requirements",
                isOptional = true
            ),
            FormStepConfig(
                step = ConsultationStep.REVIEW,
                title = "Review & Submit",
                description = "Review your information before submitting"
            )
        )
    }
}

/**
 * Consultation form state manager
 */
class ConsultationFormStateManager {
    
    /**
     * Update personal information
     */
    fun updatePersonalInfo(
        currentState: ExtendedConsultationFormState,
        personalInfo: PersonalInfo
    ): ExtendedConsultationFormState {
        return currentState.copy(
            consultationRequest = currentState.consultationRequest.copy(
                personalInfo = personalInfo
            )
        )
    }
    
    /**
     * Update project information
     */
    fun updateProjectInfo(
        currentState: ExtendedConsultationFormState,
        projectInfo: ProjectInfo
    ): ExtendedConsultationFormState {
        return currentState.copy(
            consultationRequest = currentState.consultationRequest.copy(
                projectInfo = projectInfo
            )
        )
    }
    
    /**
     * Update business information
     */
    fun updateBusinessInfo(
        currentState: ExtendedConsultationFormState,
        businessInfo: BusinessInfo
    ): ExtendedConsultationFormState {
        return currentState.copy(
            consultationRequest = currentState.consultationRequest.copy(
                businessInfo = businessInfo
            )
        )
    }
    
    /**
     * Update additional information
     */
    fun updateAdditionalInfo(
        currentState: ExtendedConsultationFormState,
        additionalInfo: AdditionalInfo
    ): ExtendedConsultationFormState {
        return currentState.copy(
            consultationRequest = currentState.consultationRequest.copy(
                additionalInfo = additionalInfo
            )
        )
    }
    
    /**
     * Update validation error
     */
    fun updateValidationError(
        currentState: ExtendedConsultationFormState,
        field: String,
        error: String?
    ): ExtendedConsultationFormState {
        val updatedErrors = if (error != null) {
            currentState.validationErrors + (field to error)
        } else {
            currentState.validationErrors - field
        }
        
        return currentState.copy(validationErrors = updatedErrors)
    }
    
    /**
     * Navigate to next step
     */
    fun goToNextStep(
        currentState: ExtendedConsultationFormState
    ): ExtendedConsultationFormState {
        if (currentState.isLastStep || !currentState.canProceed) {
            return currentState
        }
        
        return currentState.copy(currentStep = currentState.currentStep + 1)
    }
    
    /**
     * Navigate to previous step
     */
    fun goToPreviousStep(
        currentState: ExtendedConsultationFormState
    ): ExtendedConsultationFormState {
        if (currentState.currentStep <= 0) {
            return currentState
        }
        
        return currentState.copy(currentStep = currentState.currentStep - 1)
    }
    
    /**
     * Start submission process
     */
    fun startSubmission(
        currentState: ExtendedConsultationFormState
    ): ExtendedConsultationFormState {
        return currentState.copy(
            isSubmitting = true,
            submissionStatus = SubmissionStatus.Loading
        )
    }
    
    /**
     * Complete submission successfully
     */
    fun completeSubmission(
        currentState: ExtendedConsultationFormState,
        message: String
    ): ExtendedConsultationFormState {
        return currentState.copy(
            isSubmitting = false,
            submissionStatus = SubmissionStatus.Success(message)
        )
    }
    
    /**
     * Handle submission error
     */
    fun handleSubmissionError(
        currentState: ExtendedConsultationFormState,
        error: String,
        canRetry: Boolean = true
    ): ExtendedConsultationFormState {
        return currentState.copy(
            isSubmitting = false,
            submissionStatus = SubmissionStatus.Error(error, canRetry)
        )
    }
    
    /**
     * Set auto-saving state
     */
    fun setAutoSaving(
        currentState: ExtendedConsultationFormState,
        isSaving: Boolean
    ): ExtendedConsultationFormState {
        return currentState.copy(isAutoSaving = isSaving)
    }
    
    /**
     * Reset form state
     */
    fun reset(): ExtendedConsultationFormState {
        return ExtendedConsultationFormState()
    }
}

/**
 * Consultation form state composable hook
 */
@Composable
fun rememberConsultationFormState(
    initialState: ExtendedConsultationFormState = ExtendedConsultationFormState()
): MutableState<ExtendedConsultationFormState> {
    return remember { mutableStateOf(initialState) }
}

/**
 * Auto-save functionality
 */
@Composable
fun AutoSaveEffect(
    formState: ExtendedConsultationFormState,
    onSave: suspend (ConsultationRequest) -> Unit,
    onSaveStateChanged: (Boolean) -> Unit,
    saveDelay: Long = 2000L // 2 seconds delay
) {
    LaunchedEffect(formState.consultationRequest) {
        onSaveStateChanged(true)
        
        delay(saveDelay)
        
        try {
            onSave(formState.consultationRequest)
        } catch (e: Exception) {
            console.error("Auto-save failed", e)
        } finally {
            onSaveStateChanged(false)
        }
    }
}