package com.probro.khoded.features.email

import androidx.compose.runtime.*
import com.probro.khoded.features.consultation.ConsultationRequest
import com.probro.khoded.design.KhodedDesignSystem
import com.probro.khoded.components.ui.models.KhodedValidation
import com.probro.khoded.components.ui.models.ValidationResult
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.components.icons.fa.FaStar
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.InputType

/**
 * Email Communication System
 * 
 * Comprehensive email communication system for handling consultation requests,
 * general inquiries, and customer communications with automated templates
 * and delivery tracking.
 * 
 * Features:
 * - Multiple email templates for different scenarios
 * - Automated consultation confirmation emails
 * - Customer follow-up email workflows
 * - Email validation and error handling
 * - Mobile-responsive email composition interface
 * - Integration with backend email services
 * 
 * @since 2.0.0 (Brand Redesign Implementation)
 */

/**
 * Email data models
 */
data class EmailRequest(
    val from: EmailAddress,
    val to: EmailAddress,
    val subject: String,
    val body: String,
    val template: EmailTemplate = EmailTemplate.GENERAL,
    val priority: EmailPriority = EmailPriority.NORMAL,
    val attachments: List<EmailAttachment> = emptyList()
)

data class EmailAddress(
    val email: String,
    val name: String = ""
) {
    override fun toString(): String = if (name.isNotEmpty()) "$name <$email>" else email
}

data class EmailAttachment(
    val filename: String,
    val contentType: String,
    val data: ByteArray
)

enum class EmailTemplate(val templateName: String, val subject: String) {
    CONSULTATION_CONFIRMATION(
        "consultation_confirmation",
        "Thank you for your consultation request - Khoded"
    ),
    CONSULTATION_FOLLOW_UP(
        "consultation_follow_up", 
        "Following up on your consultation request - Khoded"
    ),
    GENERAL(
        "general_inquiry", 
        "Thank you for contacting Khoded"
    ),
    PROJECT_UPDATE(
        "project_update",
        "Project Update - Khoded"
    ),
    WELCOME(
        "welcome",
        "Welcome to Khoded - Let's Build Something Amazing"
    )
}

enum class EmailPriority { LOW, NORMAL, HIGH, URGENT }

enum class EmailStatus { DRAFT, QUEUED, SENDING, SENT, FAILED, BOUNCED }

data class EmailResponse(
    val success: Boolean,
    val messageId: String? = null,
    val error: String? = null,
    val status: EmailStatus = EmailStatus.DRAFT
)

/**
 * Email service interface for dependency injection
 */
interface EmailService {
    suspend fun sendEmail(request: EmailRequest): Result<EmailResponse>
    suspend fun sendConsultationConfirmation(consultation: ConsultationRequest): Result<EmailResponse>
    suspend fun sendGeneralInquiry(inquiry: GeneralInquiryRequest): Result<EmailResponse>
    fun generateEmailTemplate(template: EmailTemplate, data: Map<String, Any>): String
}

/**
 * General inquiry request model
 */
data class GeneralInquiryRequest(
    val name: String,
    val email: String,
    val subject: String,
    val message: String,
    val source: String = "website",
    val timestamp: Long = kotlinx.browser.window.performance.now().toLong()
)

/**
 * Email composition states
 */
data class EmailCompositionState(
    val to: String = "",
    val subject: String = "",
    val message: String = "",
    val template: EmailTemplate = EmailTemplate.GENERAL,
    val isValid: Boolean = false,
    val isSending: Boolean = false,
    val validationErrors: Map<String, String> = emptyMap(),
    val sendStatus: EmailSendStatus = EmailSendStatus.None
)

sealed class EmailSendStatus {
    object None : EmailSendStatus()
    object Sending : EmailSendStatus()
    data class Success(val messageId: String) : EmailSendStatus()
    data class Error(val message: String, val canRetry: Boolean = true) : EmailSendStatus()
}

/**
 * Email validation rules
 */
object EmailValidation {
    val email = KhodedValidation(
        customValidator = { value ->
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
            if (emailRegex.matches(value.trim())) {
                ValidationResult(true)
            } else {
                ValidationResult(false, "Please enter a valid email address")
            }
        }
    )
    
    val subject = KhodedValidation(
        customValidator = { value ->
            when {
                value.trim().isEmpty() -> ValidationResult(false, "Subject is required")
                value.trim().length < 5 -> ValidationResult(false, "Subject must be at least 5 characters")
                value.trim().length > 200 -> ValidationResult(false, "Subject cannot exceed 200 characters")
                else -> ValidationResult(true)
            }
        }
    )
    
    val message = KhodedValidation(
        customValidator = { value ->
            when {
                value.trim().isEmpty() -> ValidationResult(false, "Message is required")
                value.trim().length < 10 -> ValidationResult(false, "Message must be at least 10 characters")
                value.trim().length > 5000 -> ValidationResult(false, "Message cannot exceed 5000 characters")
                else -> ValidationResult(true)
            }
        }
    )
    
    val name = KhodedValidation(
        customValidator = { value ->
            when {
                value.trim().isEmpty() -> ValidationResult(false, "Name is required")
                value.trim().length < 2 -> ValidationResult(false, "Name must be at least 2 characters")
                !value.trim().all { it.isLetter() || it.isWhitespace() || it == '\'' || it == '-' } -> 
                    ValidationResult(false, "Name can only contain letters, spaces, hyphens, and apostrophes")
                else -> ValidationResult(true)
            }
        }
    )
}

/**
 * Main email composition interface
 */
@Composable
fun EmailCompositionInterface(
    emailService: EmailService,
    initialTemplate: EmailTemplate = EmailTemplate.GENERAL,
    onEmailSent: (EmailResponse) -> Unit = {},
    onCancel: (() -> Unit)? = null
) {
    var compositionState by remember { mutableStateOf(EmailCompositionState(template = initialTemplate)) }
    
    // Real-time validation
    LaunchedEffect(compositionState.to, compositionState.subject, compositionState.message) {
        delay(300) // Debounce validation
        
        val errors = mutableMapOf<String, String>()
        
        // Validate email
        val emailResult = EmailValidation.email.validate(compositionState.to)
        if (!emailResult.isValid) {
            errors["to"] = emailResult.message
        }
        
        // Validate subject
        val subjectResult = EmailValidation.subject.validate(compositionState.subject)
        if (!subjectResult.isValid) {
            errors["subject"] = subjectResult.message
        }
        
        // Validate message
        val messageResult = EmailValidation.message.validate(compositionState.message)
        if (!messageResult.isValid) {
            errors["message"] = messageResult.message
        }
        
        compositionState = compositionState.copy(
            validationErrors = errors,
            isValid = errors.isEmpty()
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(800.px)
            .backgroundColor(KhodedDesignSystem.colors.surface)
            .borderRadius(8.px)
            .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 16.px, color = rgba(0, 0, 0, 0.15))
            .padding(KhodedDesignSystem.spacing.xl2)
            .gap(KhodedDesignSystem.spacing.space4)
    ) {
        // Header
        EmailCompositionHeader(
            template = compositionState.template,
            onTemplateChange = { template ->
                compositionState = compositionState.copy(template = template)
            }
        )
        
        // Form fields
        EmailFormFields(
            state = compositionState,
            onStateChange = { newState ->
                compositionState = newState
            }
        )
        
        // Actions
        EmailCompositionActions(
            canSend = compositionState.isValid && !compositionState.isSending,
            isSending = compositionState.isSending,
            onSend = {
                compositionState = compositionState.copy(
                    isSending = true,
                    sendStatus = EmailSendStatus.Sending
                )
                
                val emailRequest = EmailRequest(
                    from = EmailAddress("noreply@khoded.com", "Khoded Team"),
                    to = EmailAddress(compositionState.to),
                    subject = compositionState.subject,
                    body = compositionState.message,
                    template = compositionState.template
                )
                
                // Handle sending in LaunchedEffect
            },
            onCancel = onCancel
        )
        
        // Status display
        EmailStatusDisplay(status = compositionState.sendStatus)
    }
    
    // Handle email sending
    LaunchedEffect(compositionState.sendStatus) {
        if (compositionState.sendStatus is EmailSendStatus.Sending) {
            try {
                val emailRequest = EmailRequest(
                    from = EmailAddress("noreply@khoded.com", "Khoded Team"),
                    to = EmailAddress(compositionState.to),
                    subject = compositionState.subject,
                    body = compositionState.message,
                    template = compositionState.template
                )
                
                val result = emailService.sendEmail(emailRequest)
                result.fold(
                    onSuccess = { response ->
                        compositionState = compositionState.copy(
                            isSending = false,
                            sendStatus = EmailSendStatus.Success(response.messageId ?: "")
                        )
                        onEmailSent(response)
                    },
                    onFailure = { error ->
                        compositionState = compositionState.copy(
                            isSending = false,
                            sendStatus = EmailSendStatus.Error(
                                error.message ?: "Failed to send email",
                                canRetry = true
                            )
                        )
                    }
                )
            } catch (e: Exception) {
                compositionState = compositionState.copy(
                    isSending = false,
                    sendStatus = EmailSendStatus.Error(
                        e.message ?: "An unexpected error occurred",
                        canRetry = true
                    )
                )
            }
        }
    }
}

/**
 * Email composition header with template selector
 */
@Composable
private fun EmailCompositionHeader(
    template: EmailTemplate,
    onTemplateChange: (EmailTemplate) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().gap(KhodedDesignSystem.spacing.space3)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SpanText(
                text = "Compose Email",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.headingLarge)
                    .fontWeight(KhodedDesignSystem.typography.fontWeightSemiBold)
                    .color(KhodedDesignSystem.colors.textPrimary)
            )
            
            FaStar(
                modifier = Modifier
                    .fontSize(24.px)
                    .color(KhodedDesignSystem.colors.primary)
            )
        }
        
        // Template selector
        Column(
            modifier = Modifier.gap(KhodedDesignSystem.spacing.space2)
        ) {
            SpanText(
                text = "Email Template",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.labelLarge)
                    .fontWeight(KhodedDesignSystem.typography.fontWeightMedium)
                    .color(KhodedDesignSystem.colors.textPrimary)
            )
            
            Select(
                attrs = Modifier
                    .fillMaxWidth()
                    .height(KhodedDesignSystem.spacing.touchTarget)
                    .padding(KhodedDesignSystem.spacing.md)
                    .backgroundColor(KhodedDesignSystem.colors.backgroundPrimary)
                    .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.borderPrimary)
                    .borderRadius(6.px)
                    .toAttrs {
                        onChange { event ->
                            val selectedValue = event.value
                            EmailTemplate.values().find { it.name == selectedValue }?.let { emailTemplate ->
                                onTemplateChange(emailTemplate)
                            }
                        }
                    }
            ) {
                EmailTemplate.values().forEach { emailTemplate ->
                    Option(
                        value = emailTemplate.name,
                        attrs = if (emailTemplate == template) {
                            Modifier.toAttrs { attr("selected", "selected") }
                        } else {
                            Modifier.toAttrs()
                        }
                    ) {
                        Text(emailTemplate.templateName.replace("_", " ").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() })
                    }
                }
            }
        }
    }
}

/**
 * Form fields for email composition
 */
@Composable
private fun EmailFormFields(
    state: EmailCompositionState,
    onStateChange: (EmailCompositionState) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().gap(KhodedDesignSystem.spacing.space4)
    ) {
        // To field
        EmailFormField(
            label = "To",
            value = state.to,
            placeholder = "recipient@example.com",
            error = state.validationErrors["to"],
            required = true,
            onValueChange = { value ->
                onStateChange(state.copy(to = value))
            }
        )
        
        // Subject field
        EmailFormField(
            label = "Subject",
            value = state.subject,
            placeholder = "Enter email subject",
            error = state.validationErrors["subject"],
            required = true,
            maxLength = 200,
            onValueChange = { value ->
                onStateChange(state.copy(subject = value))
            }
        )
        
        // Message field
        EmailFormTextArea(
            label = "Message",
            value = state.message,
            placeholder = "Enter your message...",
            error = state.validationErrors["message"],
            required = true,
            maxLength = 5000,
            onValueChange = { value ->
                onStateChange(state.copy(message = value))
            }
        )
    }
}

/**
 * Individual form field component
 */
@Composable
private fun EmailFormField(
    label: String,
    value: String,
    placeholder: String,
    error: String?,
    required: Boolean = false,
    maxLength: Int? = null,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.gap(KhodedDesignSystem.spacing.space1)
    ) {
        // Label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(KhodedDesignSystem.spacing.space1)
        ) {
            SpanText(
                text = label,
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.labelLarge)
                    .fontWeight(KhodedDesignSystem.typography.fontWeightMedium)
                    .color(KhodedDesignSystem.colors.textPrimary)
            )
            if (required) {
                SpanText(
                    text = "*",
                    modifier = Modifier
                        .fontSize(KhodedDesignSystem.typography.labelLarge)
                        .color(KhodedDesignSystem.colors.error)
                )
            }
        }
        
        // Input
        Input(
            type = InputType.Text,
            attrs = Modifier
                .fillMaxWidth()
                .height(KhodedDesignSystem.spacing.touchTargetMin)
                .padding(KhodedDesignSystem.spacing.md)
                .backgroundColor(KhodedDesignSystem.colors.backgroundPrimary)
                .border(
                    1.px,
                    LineStyle.Solid,
                    if (error != null) KhodedDesignSystem.colors.error else KhodedDesignSystem.colors.borderPrimary
                )
                .borderRadius(6.px)
                .fontSize(KhodedDesignSystem.typography.bodyMedium)
                .toAttrs {
                    attr("placeholder", placeholder)
                    maxLength?.let { attr("maxlength", it.toString()) }
                    value(value)
                    onInput { event ->
                        onValueChange(event.value ?: "")
                    }
                }
        )
        
        // Error message and character count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            error?.let { errorMessage ->
                SpanText(
                    text = errorMessage,
                    modifier = Modifier
                        .fontSize(KhodedDesignSystem.typography.labelSmall)
                        .color(KhodedDesignSystem.colors.error)
                )
            }
            
            maxLength?.let { max ->
                SpanText(
                    text = "${value.length}/$max",
                    modifier = Modifier
                        .fontSize(KhodedDesignSystem.typography.labelSmall)
                        .color(KhodedDesignSystem.colors.textTertiary)
                )
            }
        }
    }
}

/**
 * Text area component for message field
 */
@Composable
private fun EmailFormTextArea(
    label: String,
    value: String,
    placeholder: String,
    error: String?,
    required: Boolean = false,
    maxLength: Int = 5000,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.gap(KhodedDesignSystem.spacing.space1)
    ) {
        // Label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(KhodedDesignSystem.spacing.space1)
        ) {
            SpanText(
                text = label,
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.labelLarge)
                    .fontWeight(KhodedDesignSystem.typography.fontWeightMedium)
                    .color(KhodedDesignSystem.colors.textPrimary)
            )
            if (required) {
                SpanText(
                    text = "*",
                    modifier = Modifier
                        .fontSize(KhodedDesignSystem.typography.labelLarge)
                        .color(KhodedDesignSystem.colors.error)
                )
            }
        }
        
        // TextArea
        org.jetbrains.compose.web.dom.TextArea(
            attrs = Modifier
                .fillMaxWidth()
                .minHeight(200.px)
                .padding(KhodedDesignSystem.spacing.md)
                .backgroundColor(KhodedDesignSystem.colors.backgroundPrimary)
                .border(
                    1.px,
                    LineStyle.Solid,
                    if (error != null) KhodedDesignSystem.colors.error else KhodedDesignSystem.colors.borderPrimary
                )
                .borderRadius(6.px)
                .fontSize(KhodedDesignSystem.typography.bodyMedium)
                .toAttrs {
                    attr("placeholder", placeholder)
                    attr("maxlength", maxLength.toString())
                    value(value)
                    onInput { event ->
                        val newValue = event.value ?: ""
                        if (newValue.length <= maxLength) {
                            onValueChange(newValue)
                        }
                    }
                }
        )
        
        // Error message and character count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            error?.let { errorMessage ->
                SpanText(
                    text = errorMessage,
                    modifier = Modifier
                        .fontSize(KhodedDesignSystem.typography.labelSmall)
                        .color(KhodedDesignSystem.colors.error)
                )
            }
            
            SpanText(
                text = "${value.length}/$maxLength",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.labelSmall)
                    .color(KhodedDesignSystem.colors.textTertiary)
            )
        }
    }
}

/**
 * Action buttons for email composition
 */
@Composable
private fun EmailCompositionActions(
    canSend: Boolean,
    isSending: Boolean,
    onSend: () -> Unit,
    onCancel: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = KhodedDesignSystem.spacing.lg),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        onCancel?.let {
            Button(
                onClick = { it() },
                modifier = Modifier
                    .backgroundColor(Color.transparent)
                    .color(KhodedDesignSystem.colors.textSecondary)
                    .padding(KhodedDesignSystem.spacing.lg, KhodedDesignSystem.spacing.md)
                    .borderRadius(6.px)
                    .minHeight(KhodedDesignSystem.spacing.touchTarget)
            ) {
                SpanText("Cancel")
            }
        }
        
        Button(
            onClick = { onSend() },
            enabled = canSend && !isSending,
            modifier = Modifier
                .backgroundColor(
                    if (canSend && !isSending) 
                        KhodedDesignSystem.colors.primary 
                    else 
                        KhodedDesignSystem.colors.backgroundTertiary
                )
                .color(
                    if (canSend && !isSending) 
                        KhodedDesignSystem.colors.textInverse 
                    else 
                        KhodedDesignSystem.colors.textTertiary
                )
                .padding(KhodedDesignSystem.spacing.xl2, KhodedDesignSystem.spacing.md)
                .borderRadius(KhodedDesignSystem.borderRadius.medium)
                .minHeight(KhodedDesignSystem.spacing.touchTarget)
                .minWidth(150.px)
        ) {
            if (isSending) {
                SpanText("Sending...")
            } else {
                SpanText("Send Email")
            }
        }
    }
}

/**
 * Email status display component
 */
@Composable
private fun EmailStatusDisplay(status: EmailSendStatus) {
    when (status) {
        is EmailSendStatus.Success -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .backgroundColor(KhodedDesignSystem.colors.successBackground)
                    .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.success)
                    .borderRadius(6.px)
                    .padding(KhodedDesignSystem.spacing.lg)
                    .gap(KhodedDesignSystem.spacing.space3),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FaStar(
                    modifier = Modifier
                        .fontSize(20.px)
                        .color(KhodedDesignSystem.colors.success)
                )
                Column {
                    SpanText(
                        text = "Email sent successfully!",
                        modifier = Modifier
                            .fontSize(KhodedDesignSystem.typography.bodyMedium)
                            .fontWeight(KhodedDesignSystem.typography.fontWeightMedium)
                            .color(KhodedDesignSystem.colors.success)
                    )
                    if (status.messageId.isNotEmpty()) {
                        SpanText(
                            text = "Message ID: ${status.messageId}",
                            modifier = Modifier
                                .fontSize(KhodedDesignSystem.typography.labelSmall)
                                .color(KhodedDesignSystem.colors.textSecondary)
                        )
                    }
                }
            }
        }
        
        is EmailSendStatus.Error -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .backgroundColor(KhodedDesignSystem.colors.errorBackground)
                    .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.error)
                    .borderRadius(6.px)
                    .padding(KhodedDesignSystem.spacing.lg)
                    .gap(KhodedDesignSystem.spacing.space3),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FaStar(
                    modifier = Modifier
                        .fontSize(20.px)
                        .color(KhodedDesignSystem.colors.error)
                )
                Column {
                    SpanText(
                        text = "Failed to send email",
                        modifier = Modifier
                            .fontSize(KhodedDesignSystem.typography.bodyMedium)
                            .fontWeight(KhodedDesignSystem.typography.fontWeightMedium)
                            .color(KhodedDesignSystem.colors.error)
                    )
                    SpanText(
                        text = status.message,
                        modifier = Modifier
                            .fontSize(KhodedDesignSystem.typography.bodySmall)
                            .color(KhodedDesignSystem.colors.textSecondary)
                    )
                }
            }
        }
        
        EmailSendStatus.Sending -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .backgroundColor(KhodedDesignSystem.colors.infoBackground)
                    .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.info)
                    .borderRadius(6.px)
                    .padding(KhodedDesignSystem.spacing.lg)
                    .gap(KhodedDesignSystem.spacing.space3),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FaStar(
                    modifier = Modifier
                        .fontSize(20.px)
                        .color(KhodedDesignSystem.colors.info)
                )
                SpanText(
                    text = "Sending email...",
                    modifier = Modifier
                        .fontSize(KhodedDesignSystem.typography.bodyMedium)
                        .color(KhodedDesignSystem.colors.info)
                )
            }
        }
        
        EmailSendStatus.None -> {
            // No status to display
        }
    }
}

/**
 * Quick contact form for general inquiries
 */
@Composable
fun QuickContactForm(
    emailService: EmailService,
    onSubmitted: (GeneralInquiryRequest) -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var validationErrors by remember { mutableStateOf(mapOf<String, String>()) }
    
    // Real-time validation
    LaunchedEffect(name, email, subject, message) {
        delay(300)
        
        val errors = mutableMapOf<String, String>()
        
        val nameResult = EmailValidation.name.validate(name)
        if (!nameResult.isValid) errors["name"] = nameResult.message
        
        val emailResult = EmailValidation.email.validate(email)
        if (!emailResult.isValid) errors["email"] = emailResult.message
        
        val subjectResult = EmailValidation.subject.validate(subject)
        if (!subjectResult.isValid) errors["subject"] = subjectResult.message
        
        val messageResult = EmailValidation.message.validate(message)
        if (!messageResult.isValid) errors["message"] = messageResult.message
        
        validationErrors = errors
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(600.px)
            .backgroundColor(KhodedDesignSystem.colors.surface)
            .borderRadius(8.px)
            .padding(KhodedDesignSystem.spacing.xl2)
            .gap(KhodedDesignSystem.spacing.space4)
            .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 12.px, color = rgba(0, 0, 0, 0.1))
    ) {
        // Header
        SpanText(
            text = "Get in Touch",
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.headingLarge)
                .fontWeight(KhodedDesignSystem.typography.fontWeightSemiBold)
                .color(KhodedDesignSystem.colors.textPrimary)
        )
        
        SpanText(
            text = "Have a question? We'd love to hear from you.",
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.bodyMedium)
                .color(KhodedDesignSystem.colors.textSecondary)
        )
        
        // Form fields
        EmailFormField(
            label = "Name",
            value = name,
            placeholder = "Your full name",
            error = validationErrors["name"],
            required = true,
            onValueChange = { name = it }
        )
        
        EmailFormField(
            label = "Email",
            value = email,
            placeholder = "your.email@example.com",
            error = validationErrors["email"],
            required = true,
            onValueChange = { email = it }
        )
        
        EmailFormField(
            label = "Subject",
            value = subject,
            placeholder = "What's this about?",
            error = validationErrors["subject"],
            required = true,
            maxLength = 200,
            onValueChange = { subject = it }
        )
        
        EmailFormTextArea(
            label = "Message",
            value = message,
            placeholder = "Tell us more about your inquiry...",
            error = validationErrors["message"],
            required = true,
            maxLength = 1000,
            onValueChange = { message = it }
        )
        
        // Submit button
        Button(
            onClick = {
                if (validationErrors.isEmpty()) {
                    isSubmitting = true
                    val inquiry = GeneralInquiryRequest(
                        name = name,
                        email = email,
                        subject = subject,
                        message = message
                    )
                    onSubmitted(inquiry)
                }
            },
            enabled = validationErrors.isEmpty() && !isSubmitting,
            modifier = Modifier
                .backgroundColor(
                    if (validationErrors.isEmpty() && !isSubmitting) 
                        KhodedDesignSystem.colors.primary 
                    else 
                        KhodedDesignSystem.colors.backgroundTertiary
                )
                .color(
                    if (validationErrors.isEmpty() && !isSubmitting) 
                        KhodedDesignSystem.colors.textInverse 
                    else 
                        KhodedDesignSystem.colors.textTertiary
                )
                .padding(KhodedDesignSystem.spacing.xl, KhodedDesignSystem.spacing.md)
                .borderRadius(KhodedDesignSystem.borderRadius.medium)
                .minHeight(KhodedDesignSystem.spacing.touchTarget)
                .minWidth(150.px)
                // TODO: Add align-self when API is available
        ) {
            if (isSubmitting) {
                SpanText("Sending...")
            } else {
                SpanText("Send Message")
            }
        }
    }
}