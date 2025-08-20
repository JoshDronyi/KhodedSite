package com.probro.khoded.components.forms

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.*
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.coroutines.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.InputType
import com.probro.khoded.design.KhodedDesignSystem
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.dom.Select
import org.jetbrains.compose.web.dom.Option
import org.jetbrains.compose.web.dom.Text

/**
 * Consultation Form Components
 * 
 * Specialized form components for the multi-step consultation process.
 * These components preserve all functionality while providing consistent UI.
 */

/**
 * Form Navigation Component for multi-step forms
 */
@Composable
fun FormNavigation(
    canGoBack: Boolean,
    canProceed: Boolean,
    isLastStep: Boolean,
    isSubmitting: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onSkip: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth().margin(top = KhodedDesignSystem.spacing.xl2)
    ) {
        // Cancel and skip buttons row
        if (onCancel != null || onSkip != null) {
            Row(
                modifier = Modifier.fillMaxWidth().margin(bottom = KhodedDesignSystem.spacing.md),
                horizontalArrangement = Arrangement.spacedBy(KhodedDesignSystem.spacing.md)
            ) {
                onCancel?.let { cancel ->
                    Button(
                        onClick = { cancel() },
                        enabled = !isSubmitting,
                        modifier = Modifier
                            .backgroundColor(Color.transparent)
                            .color(KhodedDesignSystem.colors.error)
                            .padding(KhodedDesignSystem.spacing.md)
                            .borderRadius(6.px)
                            .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.error)
                    ) {
                        SpanText("Cancel")
                    }
                }
                
                onSkip?.let { skip ->
                    Button(
                        onClick = { skip() },
                        enabled = !isSubmitting,
                        modifier = Modifier
                            .backgroundColor(Color.transparent)
                            .color(KhodedDesignSystem.colors.textTertiary)
                            .padding(KhodedDesignSystem.spacing.md)
                            .borderRadius(6.px)
                            .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.borderPrimary)
                    ) {
                        SpanText("Skip")
                    }
                }
            }
        }
        
        // Main navigation row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            if (canGoBack) {
                Button(
                    onClick = { onBack() },
                    enabled = !isSubmitting,
                    modifier = Modifier
                        .backgroundColor(Color.transparent)
                        .color(KhodedDesignSystem.colors.textSecondary)
                        .padding(KhodedDesignSystem.spacing.lg, KhodedDesignSystem.spacing.md)
                        .borderRadius(6.px)
                        .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.borderPrimary)
                        .minHeight(KhodedDesignSystem.spacing.touchTarget)
                ) {
                    SpanText("← Back")
                }
            } else {
                // Empty space to maintain alignment
                Div {}
            }
            
            // Next/Submit button
            Button(
                onClick = { onNext() },
                enabled = canProceed && !isSubmitting,
                modifier = Modifier
                    .backgroundColor(
                        if (canProceed && !isSubmitting) 
                            KhodedDesignSystem.colors.primary 
                        else 
                            KhodedDesignSystem.colors.backgroundTertiary
                    )
                    .color(
                        if (canProceed && !isSubmitting) 
                            KhodedDesignSystem.colors.textInverse 
                        else 
                            KhodedDesignSystem.colors.textTertiary
                    )
                    .padding(KhodedDesignSystem.spacing.xl2, KhodedDesignSystem.spacing.md)
                    .borderRadius(KhodedDesignSystem.borderRadius.medium)
                    .minHeight(KhodedDesignSystem.spacing.touchTarget)
                    .minWidth(120.px)
            ) {
                SpanText(
                    when {
                        isSubmitting -> "Submitting..."
                        isLastStep -> "Submit"
                        else -> "Next →"
                    }
                )
            }
        }
    }
}

/**
 * Form Button Component with consistent styling
 */
@Composable
fun FormButton(
    text: String,
    onClick: () -> Unit,
    type: FormButtonType = FormButtonType.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { if (!isLoading && enabled) onClick() },
        enabled = enabled && !isLoading,
        modifier = modifier
            .backgroundColor(
                when (type) {
                    FormButtonType.PRIMARY -> {
                        if (enabled && !isLoading) KhodedDesignSystem.colors.primary 
                        else KhodedDesignSystem.colors.backgroundTertiary
                    }
                    FormButtonType.SECONDARY -> Color.transparent
                    FormButtonType.DANGER -> {
                        if (enabled && !isLoading) KhodedDesignSystem.colors.error 
                        else KhodedDesignSystem.colors.backgroundTertiary
                    }
                }
            )
            .color(
                when (type) {
                    FormButtonType.PRIMARY -> {
                        if (enabled && !isLoading) KhodedDesignSystem.colors.textInverse 
                        else KhodedDesignSystem.colors.textTertiary
                    }
                    FormButtonType.SECONDARY -> KhodedDesignSystem.colors.textSecondary
                    FormButtonType.DANGER -> {
                        if (enabled && !isLoading) Color.white 
                        else KhodedDesignSystem.colors.textTertiary
                    }
                }
            )
            .padding(KhodedDesignSystem.spacing.lg, KhodedDesignSystem.spacing.md)
            .borderRadius(KhodedDesignSystem.borderRadius.medium)
            .minHeight(KhodedDesignSystem.spacing.touchTarget)
            .then(
                if (type == FormButtonType.SECONDARY) {
                    Modifier.border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.borderPrimary)
                } else Modifier
            )
    ) {
        SpanText(if (isLoading) "Loading..." else text)
    }
}

enum class FormButtonType {
    PRIMARY,
    SECONDARY, 
    DANGER
}

/**
 * Form Checkbox Component
 */
@Composable
fun FormCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    label: String? = null
) {
    val displayText = text ?: label ?: ""
    Row(
        modifier = modifier.fillMaxWidth().gap(KhodedDesignSystem.spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Input(
            type = InputType.Checkbox,
            attrs = Modifier
                .size(18.px)
                .toAttrs {
                    checked(checked)
                    onChange { event ->
                        val target = event.target as? org.w3c.dom.HTMLInputElement
                        onCheckedChange(target?.checked ?: false)
                    }
                }
        )
        
        SpanText(
            text = displayText,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.bodyMedium)
                .color(KhodedDesignSystem.colors.textPrimary)
                .cursor(Cursor.Pointer)
                .onClick { onCheckedChange(!checked) }
        )
    }
}

/**
 * Form Label Component
 */
@Composable
fun FormLabel(
    text: String,
    required: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(KhodedDesignSystem.spacing.space1)
    ) {
        SpanText(
            text = text,
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
}

/**
 * Form Input Component
 */
@Composable
fun FormInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    error: String? = null,
    required: Boolean = false,
    modifier: Modifier = Modifier
) {
    val hasError = error != null
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(KhodedDesignSystem.spacing.space1)
    ) {
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
                    if (hasError) KhodedDesignSystem.colors.error else KhodedDesignSystem.colors.borderPrimary
                )
                .borderRadius(6.px)
                .fontSize(KhodedDesignSystem.typography.bodyMedium)
                .toAttrs {
                    attr("placeholder", placeholder)
                    attr("required", required.toString())
                    if (hasError) {
                        attr("aria-invalid", "true")
                    }
                    value(value)
                    onInput { event ->
                        onValueChange(event.value ?: "")
                    }
                }
        )
        
        if (hasError) {
            SpanText(
                text = error!!,
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.labelSmall)
                    .color(KhodedDesignSystem.colors.error)
            )
        }
    }
}

/**
 * Form Textarea Component
 */
@Composable
fun FormTextarea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    error: String? = null,
    required: Boolean = false,
    modifier: Modifier = Modifier,
    minHeight: CSSLengthValue = 120.px
) {
    val hasError = error != null
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(KhodedDesignSystem.spacing.space1)
    ) {
        TextArea(
            attrs = Modifier
                .fillMaxWidth()
                .minHeight(minHeight)
                .padding(KhodedDesignSystem.spacing.md)
                .backgroundColor(KhodedDesignSystem.colors.backgroundPrimary)
                .border(
                    1.px,
                    LineStyle.Solid,
                    if (hasError) KhodedDesignSystem.colors.error else KhodedDesignSystem.colors.borderPrimary
                )
                .borderRadius(6.px)
                .fontSize(KhodedDesignSystem.typography.bodyMedium)
                .toAttrs {
                    attr("placeholder", placeholder)
                    attr("required", required.toString())
                    if (hasError) {
                        attr("aria-invalid", "true")
                    }
                    value(value)
                    onInput { event ->
                        onValueChange(event.value ?: "")
                    }
                }
        )
        
        if (hasError) {
            SpanText(
                text = error!!,
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.labelSmall)
                    .color(KhodedDesignSystem.colors.error)
            )
        }
    }
}

/**
 * Form Select Component
 */
@Composable
fun FormSelect(
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    placeholder: String = "Select an option",
    error: String? = null,
    required: Boolean = false,
    modifier: Modifier = Modifier
) {
    val hasError = error != null
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(KhodedDesignSystem.spacing.space1)
    ) {
        Select(
            attrs = Modifier
                .fillMaxWidth()
                .height(KhodedDesignSystem.spacing.touchTargetMin)
                .padding(KhodedDesignSystem.spacing.md)
                .backgroundColor(KhodedDesignSystem.colors.backgroundPrimary)
                .border(
                    1.px,
                    LineStyle.Solid,
                    if (hasError) KhodedDesignSystem.colors.error else KhodedDesignSystem.colors.borderPrimary
                )
                .borderRadius(6.px)
                .fontSize(KhodedDesignSystem.typography.bodyMedium)
                .toAttrs {
                    attr("required", required.toString())
                    if (hasError) {
                        attr("aria-invalid", "true")
                    }
                    onChange { event ->
                        onValueChange(event.value ?: "")
                    }
                }
        ) {
            if (placeholder.isNotEmpty() && value.isEmpty()) {
                Option(
                    value = "",
                    attrs = {
                        attr("disabled", "true")
                        selected()
                    }
                ) {
                    Text(placeholder)
                }
            }
            
            options.forEach { option ->
                Option(
                    value = option,
                    attrs = {
                        if (value == option) selected()
                    }
                ) {
                    Text(option)
                }
            }
        }
        
        if (hasError) {
            SpanText(
                text = error!!,
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.labelSmall)
                    .color(KhodedDesignSystem.colors.error)
            )
        }
    }
}