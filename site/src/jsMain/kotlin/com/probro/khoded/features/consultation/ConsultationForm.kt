package com.probro.khoded.features.consultation

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.*
import com.varabyte.kobweb.silk.components.layout.*
import com.varabyte.kobweb.silk.components.text.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

import com.probro.khoded.features.consultation.models.*
import com.probro.khoded.features.consultation.state.*
import com.probro.khoded.features.consultation.ui.*
import com.probro.khoded.features.consultation.validation.*
import com.probro.khoded.components.forms.*
import com.probro.khoded.design.KhodedDesignSystem

/**
 * Main Consultation Form Component
 * 
 * Refactored from monolithic ConsultationRequestSystem to use modular architecture:
 * - Models: ConsultationModels.kt
 * - Validation: ConsultationValidation.kt  
 * - State Management: ConsultationFormState.kt
 * - UI Components: ConsultationFormComponents.kt
 */
@Composable
fun ConsultationForm(
    onSubmit: suspend (ConsultationRequest) -> Result<String>,
    onCancel: () -> Unit = {},
    enableAutoSave: Boolean = true,
    onAutoSave: (suspend (ConsultationRequest) -> Unit)? = null
) {
    val stateManager = remember { ConsultationFormStateManager() }
    var formState by rememberConsultationFormState()
    
    // Auto-save functionality
    if (enableAutoSave && onAutoSave != null) {
        AutoSaveEffect(
            formState = formState,
            onSave = onAutoSave,
            onSaveStateChanged = { isSaving ->
                formState = stateManager.setAutoSaving(formState, isSaving)
            }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(800.px)
            .margin(0.px)
            .padding(KhodedDesignSystem.spacing.lg)
            .gap(KhodedDesignSystem.spacing.xl)
    ) {
        // Form Header
        ConsultationFormHeader(
            currentStep = formState.currentStep + 1,
            totalSteps = formState.steps.size,
            stepTitle = formState.currentStepData.title,
            stepDescription = formState.currentStepData.description,
            progressPercentage = formState.progressPercentage
        )
        
        // Step Content
        when (formState.currentStepData.step) {
            ConsultationStep.PERSONAL_INFO -> {
                PersonalInfoStep(
                    personalInfo = formState.consultationRequest.personalInfo,
                    validationErrors = formState.validationErrors,
                    onPersonalInfoChange = { personalInfo ->
                        formState = stateManager.updatePersonalInfo(formState, personalInfo)
                    },
                    onValidationError = { field, error ->
                        formState = stateManager.updateValidationError(formState, field, error)
                    }
                )
            }
            
            ConsultationStep.PROJECT_INFO -> {
                ProjectInfoStep(
                    projectInfo = formState.consultationRequest.projectInfo,
                    validationErrors = formState.validationErrors,
                    onProjectInfoChange = { projectInfo ->
                        formState = stateManager.updateProjectInfo(formState, projectInfo)
                    },
                    onValidationError = { field, error ->
                        formState = stateManager.updateValidationError(formState, field, error)
                    }
                )
            }
            
            ConsultationStep.BUSINESS_INFO -> {
                BusinessInfoStep(
                    businessInfo = formState.consultationRequest.businessInfo,
                    validationErrors = formState.validationErrors,
                    onBusinessInfoChange = { businessInfo ->
                        formState = stateManager.updateBusinessInfo(formState, businessInfo)
                    },
                    onValidationError = { field, error ->
                        formState = stateManager.updateValidationError(formState, field, error)
                    }
                )
            }
            
            ConsultationStep.ADDITIONAL_INFO -> {
                AdditionalInfoStep(
                    additionalInfo = formState.consultationRequest.additionalInfo,
                    onAdditionalInfoChange = { additionalInfo ->
                        formState = stateManager.updateAdditionalInfo(formState, additionalInfo)
                    }
                )
            }
            
            ConsultationStep.REVIEW -> {
                ReviewStep(
                    consultationRequest = formState.consultationRequest
                )
            }
            
            ConsultationStep.SUBMIT -> {
                // This step is handled by the submission logic
            }
        }
        
        // Form Navigation
        FormNavigation(
            canGoBack = formState.currentStep > 0,
            canProceed = formState.canProceed || formState.currentStepData.isOptional,
            isLastStep = formState.isLastStep,
            isSubmitting = formState.isSubmitting,
            onBack = {
                formState = stateManager.goToPreviousStep(formState)
            },
            onNext = {
                if (formState.isLastStep) {
                    formState = stateManager.startSubmission(formState)
                } else {
                    formState = stateManager.goToNextStep(formState)
                }
            },
            onSkip = if (formState.currentStepData.isOptional) {
                {
                    if (formState.isLastStep) {
                        formState = stateManager.startSubmission(formState)
                    } else {
                        formState = stateManager.goToNextStep(formState)
                    }
                }
            } else null,
            onCancel = onCancel
        )
        
        // Auto-save indicator
        if (formState.isAutoSaving) {
            AutoSaveIndicator()
        }
        
        // Submission status
        when (val status = formState.submissionStatus) {
            is SubmissionStatus.Success -> {
                InfoCard(
                    title = "Success!",
                    description = status.message,
                    type = InfoCardType.INFO
                )
            }
            
            is SubmissionStatus.Error -> {
                InfoCard(
                    title = "Submission Failed",
                    description = status.message,
                    type = InfoCardType.WARNING
                )
                
                if (status.canRetry) {
                    FormButton(
                        text = "Try Again",
                        type = FormButtonType.PRIMARY,
                        onClick = {
                            formState = stateManager.startSubmission(formState)
                        }
                    )
                }
            }
            
            else -> { /* No status to show */ }
        }
    }
    
    // Handle form submission
    LaunchedEffect(formState.submissionStatus) {
        if (formState.submissionStatus is SubmissionStatus.Loading) {
            try {
                val result = onSubmit(formState.consultationRequest)
                result.fold(
                    onSuccess = { message ->
                        formState = stateManager.completeSubmission(formState, message)
                    },
                    onFailure = { error ->
                        formState = stateManager.handleSubmissionError(
                            formState,
                            error.message ?: "Failed to submit consultation request"
                        )
                    }
                )
            } catch (e: Exception) {
                formState = stateManager.handleSubmissionError(
                    formState,
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }
}

/**
 * Additional Information Step Component
 */
@Composable
private fun AdditionalInfoStep(
    additionalInfo: AdditionalInfo,
    onAdditionalInfoChange: (AdditionalInfo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(KhodedDesignSystem.spacing.lg)
    ) {
        InfoCard(
            title = "Optional Services",
            description = "These services are optional but can help make your project even more successful.",
            type = InfoCardType.INFO
        )
        
        // Service checkboxes
        Column(
            modifier = Modifier.gap(KhodedDesignSystem.spacing.md)
        ) {
            FormCheckbox(
                checked = additionalInfo.hasExistingBrand,
                label = "I have existing brand guidelines",
                onCheckedChange = { checked ->
                    onAdditionalInfoChange(additionalInfo.copy(hasExistingBrand = checked))
                }
            )
            
            FormCheckbox(
                checked = additionalInfo.needsHosting,
                label = "I need hosting services",
                onCheckedChange = { checked ->
                    onAdditionalInfoChange(additionalInfo.copy(needsHosting = checked))
                }
            )
            
            FormCheckbox(
                checked = additionalInfo.needsMaintenance,
                label = "I need ongoing maintenance",
                onCheckedChange = { checked ->
                    onAdditionalInfoChange(additionalInfo.copy(needsMaintenance = checked))
                }
            )
        }
        
        // Additional comments
        Column {
            FormLabel(text = "Additional Comments")
            FormTextarea(
                value = additionalInfo.comments,
                placeholder = "Any additional information or special requirements...",
                minHeight = 100.px,
                onValueChange = { value ->
                    onAdditionalInfoChange(additionalInfo.copy(comments = value))
                }
            )
        }
    }
}

/**
 * Review Step Component
 */
@Composable
private fun ReviewStep(
    consultationRequest: ConsultationRequest
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(KhodedDesignSystem.spacing.lg)
    ) {
        InfoCard(
            title = "Review Your Information",
            description = "Please review your information before submitting your consultation request.",
            type = InfoCardType.INFO
        )
        
        // Review sections
        ReviewSection("Personal Information") {
            ReviewItem("Name", "${consultationRequest.personalInfo.firstName} ${consultationRequest.personalInfo.lastName}")
            ReviewItem("Email", consultationRequest.personalInfo.email)
            if (consultationRequest.personalInfo.phone.isNotBlank()) {
                ReviewItem("Phone", consultationRequest.personalInfo.phone)
            }
            if (consultationRequest.personalInfo.company.isNotBlank()) {
                ReviewItem("Company", consultationRequest.personalInfo.company)
            }
        }
        
        ReviewSection("Project Information") {
            ReviewItem("Type", consultationRequest.projectInfo.projectType)
            ReviewItem("Budget", consultationRequest.projectInfo.budget)
            ReviewItem("Timeline", consultationRequest.projectInfo.timeline)
            ReviewItem("Description", consultationRequest.projectInfo.description)
        }
        
        ReviewSection("Business Information") {
            ReviewItem("Industry", consultationRequest.businessInfo.industry)
            ReviewItem("Company Size", consultationRequest.businessInfo.companySize)
            ReviewItem("Target Audience", consultationRequest.businessInfo.targetAudience)
        }
    }
}

@Composable
private fun ReviewSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(KhodedDesignSystem.spacing.md)
            .backgroundColor(KhodedDesignSystem.colors.backgroundSecondary)
            .borderRadius(KhodedDesignSystem.borderRadius.medium)
            .gap(KhodedDesignSystem.spacing.sm)
    ) {
        SpanText(
            text = title,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.headingSmall)
                .fontWeight(KhodedDesignSystem.typography.fontWeightSemiBold)
                .color(KhodedDesignSystem.colors.textPrimary)
        )
        
        content()
    }
}

@Composable
private fun ReviewItem(label: String, value: String) {
    if (value.isNotBlank()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(KhodedDesignSystem.spacing.sm)
        ) {
            SpanText(
                text = "$label:",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.bodyMedium)
                    .fontWeight(KhodedDesignSystem.typography.fontWeightMedium)
                    .color(KhodedDesignSystem.colors.textSecondary)
            )
            
            SpanText(
                text = value,
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.bodyMedium)
                    .color(KhodedDesignSystem.colors.textPrimary)
                    .flexGrow(1)
            )
        }
    }
}