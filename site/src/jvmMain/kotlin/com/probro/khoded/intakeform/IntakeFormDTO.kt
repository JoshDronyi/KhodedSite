package com.probro.khoded

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IntakeFormDTO(
    @SerialName("FormType")
    var formType: String? = null,
    @SerialName("Organization")
    var organization: String? = null,
    @SerialName("CONTACT_INFO")
    var contactFormAnswers: List<FormAnswerDTO>? = null,
    @SerialName("PROJECT_OVERVIEW")
    var projectOverviewAnswers: List<FormAnswerDTO>? = null,
    @SerialName("DESIGN_BRANDING")
    var designBrandingAnswers: List<FormAnswerDTO>? = null,
    @SerialName("STRUCTURE_FUNCTIONALITY")
    var structureFunctionalityAnswers: List<FormAnswerDTO>? = null,
    @SerialName("CONTENT_IMAGERY")
    var contentImageryAnswers: List<FormAnswerDTO>? = null,
    @SerialName("TIMELINE_BUDGET")
    var timelineBudgetAnswers: List<FormAnswerDTO>? = null,
    @SerialName("MAINTENANCE_UPDATES")
    var maintenanceUpdatesAnswers: List<FormAnswerDTO>? = null,
    @SerialName("ADDITIONAL_INFO")
    var additionalInfo: List<FormAnswerDTO>? = null
)

@Serializable
data class FormAnswerDTO(
    val questionText: String,
    val answerValue: String
)
