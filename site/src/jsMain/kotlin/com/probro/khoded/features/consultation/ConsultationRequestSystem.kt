package com.probro.khoded.features.consultation

import androidx.compose.runtime.*
import com.probro.khoded.features.consultation.models.*
import com.probro.khoded.features.consultation.state.*
import com.probro.khoded.features.consultation.ui.*
import com.probro.khoded.features.consultation.validation.*

/**
 * Consultation Request System
 * 
 * REFACTORED: This monolithic file has been split into modular components:
 * - Models: ConsultationModels.kt
 * - Validation: ConsultationValidation.kt 
 * - State Management: ConsultationFormState.kt
 * - UI Components: ConsultationFormComponents.kt
 * - Main Form: ConsultationForm.kt
 * 
 * This file now serves as a compatibility wrapper for the new modular architecture.
 * 
 * @since 2.0.0 (Brand Redesign Implementation)
 * @refactored 2.1.0 (Modular Architecture)
 */

/**
 * Main consultation request form component - compatibility wrapper
 * 
 * @deprecated Use ConsultationForm from com.probro.khoded.features.consultation.ConsultationForm
 */
@Composable
fun ConsultationRequestForm(
    onSubmit: suspend (ConsultationRequest) -> Result<String>,
    onCancel: (() -> Unit)? = null
) {
    ConsultationForm(
        onSubmit = onSubmit,
        onCancel = onCancel ?: {},
        enableAutoSave = true
    )
}