package com.probro.khoded.features.consultation.ui

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.*
import com.varabyte.kobweb.silk.components.layout.*
import com.varabyte.kobweb.silk.components.text.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import com.probro.khoded.features.consultation.models.*
import com.probro.khoded.features.consultation.validation.*
import com.probro.khoded.components.forms.FormSelect
import com.probro.khoded.components.forms.FormLabel
import com.probro.khoded.components.forms.FormInput
import com.probro.khoded.components.forms.FormTextarea
import com.probro.khoded.design.KhodedDesignSystem

/**
 * Form header with progress indicator
 */
@Composable
fun ConsultationFormHeader(
    currentStep: Int,
    totalSteps: Int,
    stepTitle: String,
    stepDescription: String,
    progressPercentage: Float
) {
    Column(
        modifier = Modifier.fillMaxWidth().gap(KhodedDesignSystem.spacing.md)
    ) {
        // Progress indicator
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .height(4.px)
                .backgroundColor(KhodedDesignSystem.colors.backgroundTertiary)
                .borderRadius(4.px)
                .toAttrs()
        ) {
            Div(
                attrs = Modifier
                    .width((progressPercentage).percent)
                    .height(4.px)
                    .backgroundColor(KhodedDesignSystem.colors.primary)
                    .borderRadius(4.px)
                    .toAttrs()
            )
        }
        
        // Step info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                SpanText(
                    text = stepTitle,
                    modifier = Modifier
                        .fontSize(KhodedDesignSystem.typography.headingMedium)
                        .fontWeight(KhodedDesignSystem.typography.fontWeightSemiBold)
                        .color(KhodedDesignSystem.colors.textPrimary)
                )
                SpanText(
                    text = stepDescription,
                    modifier = Modifier
                        .fontSize(KhodedDesignSystem.typography.bodyMedium)
                        .color(KhodedDesignSystem.colors.textSecondary)
                )
            }
            
            SpanText(
                text = "$currentStep of $totalSteps",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.labelMedium)
                    .color(KhodedDesignSystem.colors.textTertiary)
            )
        }
    }
}

/**
 * Personal Information Step Component
 */
@Composable
fun PersonalInfoStep(
    personalInfo: PersonalInfo,
    validationErrors: Map<String, String>,
    onPersonalInfoChange: (PersonalInfo) -> Unit,
    onValidationError: (String, String?) -> Unit
) {
    var validationCount by remember { mutableStateOf(0) }
    
    LaunchedEffect(personalInfo) {
        delay(500) // Debounce validation
        validationCount++
        
        // Validate fields using the separated validation logic
        val errors = ConsultationValidation.validatePersonalInfo(personalInfo)
        errors.forEach { (field, message) ->
            onValidationError(field, message)
        }
        
        // Clear errors for valid fields
        val validFields = listOf("firstName", "lastName", "email", "phone") - errors.keys
        validFields.forEach { field ->
            onValidationError(field, null)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(KhodedDesignSystem.spacing.lg)
    ) {
        // Name fields row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(KhodedDesignSystem.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(KhodedDesignSystem.spacing.md)
        ) {
            // First Name
            Column(modifier = Modifier.flexGrow(1)) {
                FormLabel(text = "First Name", required = true)
                FormInput(
                    value = personalInfo.firstName,
                    placeholder = "Enter your first name",
                    error = validationErrors["firstName"],
                    onValueChange = { value ->
                        onPersonalInfoChange(personalInfo.copy(firstName = value))
                    }
                )
            }
            
            // Last Name
            Column(modifier = Modifier.flexGrow(1)) {
                FormLabel(text = "Last Name", required = true)
                FormInput(
                    value = personalInfo.lastName,
                    placeholder = "Enter your last name",
                    error = validationErrors["lastName"],
                    onValueChange = { value ->
                        onPersonalInfoChange(personalInfo.copy(lastName = value))
                    }
                )
            }
        }
        
        // Email
        Column {
            FormLabel(text = "Email Address", required = true)
            FormInput(
                value = personalInfo.email,
                placeholder = "Enter your email address",
                error = validationErrors["email"],
                onValueChange = { value ->
                    onPersonalInfoChange(personalInfo.copy(email = value))
                }
            )
        }
        
        // Phone
        Column {
            FormLabel(text = "Phone Number")
            FormInput(
                value = personalInfo.phone,
                placeholder = "Enter your phone number (optional)",
                error = validationErrors["phone"],
                onValueChange = { value ->
                    onPersonalInfoChange(personalInfo.copy(phone = value))
                }
            )
        }
        
        // Company and Title row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(KhodedDesignSystem.spacing.md)
        ) {
            Column(modifier = Modifier.flexGrow(1)) {
                FormLabel(text = "Company")
                FormInput(
                    value = personalInfo.company,
                    placeholder = "Company name (optional)",
                    onValueChange = { value ->
                        onPersonalInfoChange(personalInfo.copy(company = value))
                    }
                )
            }
            
            Column(modifier = Modifier.flexGrow(1)) {
                FormLabel(text = "Job Title")
                FormInput(
                    value = personalInfo.title,
                    placeholder = "Your job title (optional)",
                    onValueChange = { value ->
                        onPersonalInfoChange(personalInfo.copy(title = value))
                    }
                )
            }
        }
    }
}

/**
 * Project Information Step Component
 */
@Composable
fun ProjectInfoStep(
    projectInfo: ProjectInfo,
    validationErrors: Map<String, String>,
    onProjectInfoChange: (ProjectInfo) -> Unit,
    onValidationError: (String, String?) -> Unit
) {
    LaunchedEffect(projectInfo) {
        delay(500) // Debounce validation
        
        val errors = ConsultationValidation.validateProjectInfo(projectInfo)
        errors.forEach { (field, message) ->
            onValidationError(field, message)
        }
        
        val validFields = listOf("projectType", "budget", "timeline", "description") - errors.keys
        validFields.forEach { field ->
            onValidationError(field, null)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(KhodedDesignSystem.spacing.lg)
    ) {
        // Project Type
        Column {
            FormLabel(text = "Project Type", required = true)
            FormSelect(
                value = projectInfo.projectType,
                placeholder = "Select project type",
                error = validationErrors["projectType"],
                options = listOf(
                    "Web Development",
                    "Mobile App Development", 
                    "E-commerce Platform",
                    "Website Redesign",
                    "Ongoing Maintenance",
                    "Other"
                ),
                onValueChange = { value ->
                    onProjectInfoChange(projectInfo.copy(projectType = value))
                }
            )
        }
        
        // Budget and Timeline row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(KhodedDesignSystem.spacing.md)
        ) {
            Column(modifier = Modifier.flexGrow(1)) {
                FormLabel(text = "Budget Range", required = true)
                FormSelect(
                    value = projectInfo.budget,
                    placeholder = "Select budget range",
                    error = validationErrors["budget"],
                    options = listOf(
                        "Under $5,000",
                        "$5,000 - $10,000",
                        "$10,000 - $25,000",
                        "$25,000 - $50,000",
                        "$50,000+"
                    ),
                    onValueChange = { value ->
                        onProjectInfoChange(projectInfo.copy(budget = value))
                    }
                )
            }
            
            Column(modifier = Modifier.flexGrow(1)) {
                FormLabel(text = "Timeline", required = true)
                FormSelect(
                    value = projectInfo.timeline,
                    placeholder = "Select timeline",
                    error = validationErrors["timeline"],
                    options = listOf(
                        "ASAP",
                        "1-3 months",
                        "3-6 months", 
                        "6-12 months",
                        "Flexible"
                    ),
                    onValueChange = { value ->
                        onProjectInfoChange(projectInfo.copy(timeline = value))
                    }
                )
            }
        }
        
        // Project Description
        Column {
            FormLabel(text = "Project Description", required = true)
            FormTextarea(
                value = projectInfo.description,
                placeholder = "Please describe your project in detail...",
                error = validationErrors["description"],
                minHeight = 120.px,
                onValueChange = { value ->
                    onProjectInfoChange(projectInfo.copy(description = value))
                }
            )
        }
    }
}

/**
 * Business Information Step Component
 */
@Composable
fun BusinessInfoStep(
    businessInfo: BusinessInfo,
    validationErrors: Map<String, String>,
    onBusinessInfoChange: (BusinessInfo) -> Unit,
    onValidationError: (String, String?) -> Unit
) {
    LaunchedEffect(businessInfo) {
        delay(500)
        
        val errors = ConsultationValidation.validateBusinessInfo(businessInfo)
        errors.forEach { (field, message) ->
            onValidationError(field, message)
        }
        
        val validFields = listOf("industry", "companySize", "targetAudience") - errors.keys
        validFields.forEach { field ->
            onValidationError(field, null)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(KhodedDesignSystem.spacing.lg)
    ) {
        // Industry and Company Size row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(KhodedDesignSystem.spacing.md)
        ) {
            Column(modifier = Modifier.flexGrow(1)) {
                FormLabel(text = "Industry", required = true)
                FormSelect(
                    value = businessInfo.industry,
                    placeholder = "Select industry",
                    error = validationErrors["industry"],
                    options = listOf(
                        "Technology",
                        "Healthcare",
                        "Finance",
                        "Retail",
                        "Education",
                        "Manufacturing",
                        "Other"
                    ),
                    onValueChange = { value ->
                        onBusinessInfoChange(businessInfo.copy(industry = value))
                    }
                )
            }
            
            Column(modifier = Modifier.flexGrow(1)) {
                FormLabel(text = "Company Size", required = true)
                FormSelect(
                    value = businessInfo.companySize,
                    placeholder = "Select company size",
                    error = validationErrors["companySize"],
                    options = listOf(
                        "1-10 employees",
                        "11-50 employees",
                        "51-200 employees",
                        "201-500 employees",
                        "500+ employees"
                    ),
                    onValueChange = { value ->
                        onBusinessInfoChange(businessInfo.copy(companySize = value))
                    }
                )
            }
        }
        
        // Current Website
        Column {
            FormLabel(text = "Current Website")
            FormInput(
                value = businessInfo.currentWebsite,
                placeholder = "https://your-current-website.com (optional)",
                onValueChange = { value ->
                    onBusinessInfoChange(businessInfo.copy(currentWebsite = value))
                }
            )
        }
        
        // Target Audience
        Column {
            FormLabel(text = "Target Audience", required = true)
            FormTextarea(
                value = businessInfo.targetAudience,
                placeholder = "Describe your target audience and customers...",
                error = validationErrors["targetAudience"],
                minHeight = 100.px,
                onValueChange = { value ->
                    onBusinessInfoChange(businessInfo.copy(targetAudience = value))
                }
            )
        }
    }
}

/**
 * Info Card Component for displaying contextual information
 */
enum class InfoCardType { INFO, PRIVACY, WARNING }

@Composable
fun InfoCard(
    title: String,
    description: String,
    type: InfoCardType
) {
    val (backgroundColor, borderColor, textColor) = when (type) {
        InfoCardType.INFO -> Triple(
            KhodedDesignSystem.colors.infoBackground,
            KhodedDesignSystem.colors.info,
            KhodedDesignSystem.colors.textPrimary
        )
        InfoCardType.PRIVACY -> Triple(
            KhodedDesignSystem.colors.successBackground,
            KhodedDesignSystem.colors.success,
            KhodedDesignSystem.colors.textPrimary
        )
        InfoCardType.WARNING -> Triple(
            KhodedDesignSystem.colors.warningBackground,
            KhodedDesignSystem.colors.warning,
            KhodedDesignSystem.colors.textPrimary
        )
    }
    
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .padding(KhodedDesignSystem.spacing.md)
            .backgroundColor(backgroundColor)
            .border(1.px, LineStyle.Solid, borderColor)
            .borderRadius(KhodedDesignSystem.borderRadius.medium)
            .toAttrs()
    ) {
        Column(
            modifier = Modifier.gap(KhodedDesignSystem.spacing.sm)
        ) {
            SpanText(
                text = title,
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.labelMedium)
                    .fontWeight(KhodedDesignSystem.typography.fontWeightSemiBold)
                    .color(textColor)
            )
            
            SpanText(
                text = description,
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.bodySmall)
                    .color(textColor)
            )
        }
    }
}

/**
 * Auto-save indicator component
 */
@Composable
fun AutoSaveIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(KhodedDesignSystem.spacing.sm)
            .gap(KhodedDesignSystem.spacing.xs)
    ) {
        Div(
            attrs = Modifier
                .size(8.px)
                .backgroundColor(KhodedDesignSystem.colors.success)
                .borderRadius(50.percent)
                .toAttrs()
        )
        
        SpanText(
            text = "Saving...",
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.labelSmall)
                .color(KhodedDesignSystem.colors.textTertiary)
        )
    }
}