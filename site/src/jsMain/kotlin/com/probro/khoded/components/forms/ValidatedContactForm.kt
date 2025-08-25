package com.probro.khoded.components.forms

import androidx.compose.runtime.*
import com.probro.khoded.components.ui.models.ValidationResult
import com.probro.khoded.data.api.ApiClient
import com.probro.khoded.services.ValidationServiceProvider
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import kotlin.js.Date

/**
 * Validated Contact Form Component
 *
 * Features:
 * - Real-time input validation
 * - Secure API submission
 * - Rate limiting protection
 * - Accessibility compliance
 * - User-friendly error messages
 */

data class ContactFormData(
    val name: String = "",
    val email: String = "",
    val subject: String = "",
    val message: String = ""
)

data class ContactFormState(
    val data: ContactFormData = ContactFormData(),
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val errors: Map<String, String> = emptyMap(),
    val submitError: String? = null,
    val submitSuccess: String? = null
)

@Composable
fun ValidatedContactForm(
    modifier: Modifier = Modifier,
    onSubmitSuccess: ((String) -> Unit)? = null
) {
    var formState by remember { mutableStateOf(ContactFormState()) }
    val validator = remember { ValidationServiceProvider.instance }
    val apiClient = remember { ApiClient.getInstance() }
    val scope = rememberCoroutineScope()

    // Validation functions
    fun validateField(field: String, value: String): ValidationResult {
        return when (field) {
            "name" -> validator.validateName(value, "Name")
            "email" -> validator.validateEmail(value)
            "subject" -> validator.validateRequiredText(value, "Subject", minLength = 3, maxLength = 100)
            "message" -> validator.validateMessage(value, minLength = 10, maxLength = 1000)
            else -> ValidationResult(true, "")
        }
    }

    // Update form data and validate
    fun updateField(field: String, value: String) {
        val newData = when (field) {
            "name" -> formState.data.copy(name = value)
            "email" -> formState.data.copy(email = value)
            "subject" -> formState.data.copy(subject = value)
            "message" -> formState.data.copy(message = value)
            else -> formState.data
        }

        // Real-time validation
        val validation = validateField(field, value)
        val newErrors = if (validation.isValid) {
            formState.errors - field
        } else {
            formState.errors + (field to validation.message)
        }

        formState = formState.copy(
            data = newData,
            errors = newErrors,
            submitError = null // Clear submit error when user starts typing
        )
    }

    // Submit form
    fun submitForm() {
        // Validate all fields
        val validations = mapOf(
            "name" to validateField("name", formState.data.name),
            "email" to validateField("email", formState.data.email),
            "subject" to validateField("subject", formState.data.subject),
            "message" to validateField("message", formState.data.message)
        )

        val errors = validations.filterValues { !it.isValid }
            .mapValues { it.value.message }

        if (errors.isNotEmpty()) {
            formState = formState.copy(errors = errors)
            return
        }

        // Submit form
        formState = formState.copy(isSubmitting = true, submitError = null)

        scope.launch {
            try {
                val formData = mapOf(
                    "name" to formState.data.name,
                    "email" to formState.data.email,
                    "subject" to formState.data.subject,
                    "message" to formState.data.message,
                    "timestamp" to Date.now().toLong().toString()
                )

                val response = apiClient.postForm("/api/sendemail", formData)

                if (response.success) {
                    formState = formState.copy(
                        isSubmitting = false,
                        isSubmitted = true,
                        submitSuccess = "Thank you! Your message has been sent successfully. We'll get back to you within 24 hours.",
                        data = ContactFormData(), // Reset form
                        errors = emptyMap()
                    )
                    onSubmitSuccess?.invoke(response.data ?: "Success")
                } else {
                    formState = formState.copy(
                        isSubmitting = false,
                        submitError = response.error ?: "Failed to send message. Please try again."
                    )
                }
            } catch (e: Exception) {
                // SECURITY: Debug logging removed for production
                // Log error internally without exposing details to client
                formState = formState.copy(
                    isSubmitting = false,
                    submitError = "Network error. Please check your connection and try again."
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .backgroundColor(Color.white)
            .borderRadius(12.px)
            .padding(40.px)
            .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 16.px, color = rgba(0, 0, 0, 0.15)),
        verticalArrangement = Arrangement.spacedBy(20.px)
    ) {
        // Success message
        if (formState.isSubmitted && formState.submitSuccess != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .backgroundColor(rgba(34, 197, 94, 0.1))
                    .border(1.px, LineStyle.Solid, rgb(34, 197, 94))
                    .borderRadius(8.px)
                    .padding(16.px),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.px)
            ) {
                SpanText(
                    "✅ Message Sent Successfully!",
                    modifier = Modifier
                        .fontSize(16.px)
                        .fontWeight(600)
                        .color(rgb(34, 197, 94))
                        .textAlign(TextAlign.Center)
                )
                SpanText(
                    formState.submitSuccess!!,
                    modifier = Modifier
                        .fontSize(14.px)
                        .color(rgb(21, 128, 61))
                        .textAlign(TextAlign.Center)
                )
            }
        } else {
            // Form fields
            ValidatedFormField(
                label = "Name",
                value = formState.data.name,
                error = formState.errors["name"],
                placeholder = "Enter your full name",
                onValueChange = { updateField("name", it) }
            )

            ValidatedFormField(
                label = "Email",
                value = formState.data.email,
                error = formState.errors["email"],
                placeholder = "Enter your email address",
                inputType = "email",
                onValueChange = { updateField("email", it) }
            )

            ValidatedFormField(
                label = "Subject",
                value = formState.data.subject,
                error = formState.errors["subject"],
                placeholder = "What's this about?",
                onValueChange = { updateField("subject", it) }
            )

            ValidatedFormField(
                label = "Message",
                value = formState.data.message,
                error = formState.errors["message"],
                placeholder = "Tell us about your project and requirements...",
                isTextArea = true,
                onValueChange = { updateField("message", it) }
            )

            // Submit error
            if (formState.submitError != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .backgroundColor(rgba(239, 68, 68, 0.1))
                        .border(1.px, LineStyle.Solid, rgb(239, 68, 68))
                        .borderRadius(8.px)
                        .padding(16.px)
                ) {
                    SpanText(
                        formState.submitError!!,
                        modifier = Modifier
                            .fontSize(14.px)
                            .color(rgb(239, 68, 68))
                    )
                }
            }

            // Submit button
            Button(
                attrs = Modifier
                    .fillMaxWidth()
                    .padding(16.px, 24.px)
                    .backgroundColor(if (formState.isSubmitting) rgb(156, 163, 175) else rgb(6, 182, 212))
                    .borderRadius(8.px)
                    .cursor(if (formState.isSubmitting) Cursor.NotAllowed else Cursor.Pointer)
                    .toAttrs {
                        if (!formState.isSubmitting) {
                            onClick { submitForm() }
                        }
                        attr("disabled", formState.isSubmitting.toString())
                        attr("aria-label", if (formState.isSubmitting) "Sending message..." else "Send message")
                    }
            ) {
                SpanText(
                    if (formState.isSubmitting) "Sending..." else "Send Message",
                    modifier = Modifier
                        .fontSize(16.px)
                        .fontWeight(600)
                        .color(Color.white)
                        .textAlign(TextAlign.Center)
                )
            }
        }
    }
}

@Composable
private fun ValidatedFormField(
    label: String,
    value: String,
    error: String? = null,
    placeholder: String = "",
    inputType: String = "text",
    isTextArea: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val hasError = error != null
    val borderColor = if (hasError) rgb(239, 68, 68) else rgb(209, 213, 219)
    val focusBorderColor = if (hasError) rgb(239, 68, 68) else rgb(6, 182, 212)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.px)
    ) {
        SpanText(
            label,
            modifier = Modifier
                .fontSize(14.px)
                .fontWeight(500)
                .color(rgb(15, 23, 42))
        )

        if (isTextArea) {
            TextArea(
                value = value,
                attrs = Modifier
                    .fillMaxWidth()
                    .height(120.px)
                    .backgroundColor(Color.white)
                    .border(1.px, LineStyle.Solid, borderColor)
                    .borderRadius(8.px)
                    .padding(12.px)
                    .fontSize(14.px)
                    .color(rgb(15, 23, 42))
                    .toAttrs {
                        attr("placeholder", placeholder)
                        attr("aria-label", label)
                        attr("aria-invalid", hasError.toString())
                        if (hasError) {
                            attr("aria-describedby", "${label.lowercase()}-error")
                        }
                        onFocusIn {
                            (it.target as? HTMLElement)?.style?.borderColor = focusBorderColor.toString()
                        }
                        onFocusOut {
                            (it.target as? HTMLElement)?.style?.borderColor = borderColor.toString()
                        }
                        onInput { event ->
                            onValueChange((event.target as? HTMLTextAreaElement)?.value ?: "")
                        }
                    }
            )
        } else {
            Input(
                type = InputType.Text,
                attrs = Modifier
                    .fillMaxWidth()
                    .height(48.px)
                    .backgroundColor(Color.white)
                    .border(1.px, LineStyle.Solid, borderColor)
                    .borderRadius(8.px)
                    .padding(leftRight = 12.px)
                    .fontSize(14.px)
                    .color(rgb(15, 23, 42))
                    .onFocusIn {
                        (it.target as? HTMLElement)?.style?.borderColor = focusBorderColor.toString()
                    }
                    .onFocusOut {
                        (it.target as? HTMLElement)?.style?.borderColor = borderColor.toString()
                    }
                    .onKeyUp {
                        onValueChange((it.target as? HTMLInputElement)?.value ?: "")
                    }
                    .toAttrs {
                        attr("type", inputType)
                        attr("placeholder", placeholder)
                        attr("aria-label", label)
                        if (hasError) {
                            attr("aria-describedby", "${label.lowercase()}-error")
                        }
                        attr("aria-invalid", hasError.toString())
                    }
            )
        }

        // Error message
        if (hasError) {
            SpanText(
                error!!,
                modifier = Modifier
                    .fontSize(12.px)
                    .color(rgb(239, 68, 68))
                    .attrsModifier {
                        id("${label.lowercase()}-error")
                        attr("role", "alert")
                        attr("aria-live", "polite")
                    }
            )
        }
    }
}