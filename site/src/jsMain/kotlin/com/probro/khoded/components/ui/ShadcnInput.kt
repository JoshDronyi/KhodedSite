package com.probro.khoded.components.ui

/**
 * Shadcn-ui inspired Input component for Kobweb.
 *
 * Modern, accessible input component following Shadcn-ui design principles,
 * replacing Bootstrap's BSInput with enhanced UX and accessibility.
 *
 * Features:
 * - WCAG 2.2 AA compliance
 * - Modern Shadcn-ui styling
 * - Enhanced focus states
 * - Built-in validation
 * - Screen reader optimized
 * - Keyboard navigation support
 *
 * @since 2.0.0 (Bootstrap replacement)
 */

import androidx.compose.runtime.*
import com.probro.khoded.components.ui.models.ComponentSize
import com.probro.khoded.components.ui.models.KhodedValidation
import com.probro.khoded.hooks.useFocus
import com.probro.khoded.tokens.DesignTokens
import com.probro.khoded.theme.providers.useFormTheme
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

/**
 * Shadcn-ui inspired input component.
 * Direct replacement for Bootstrap's BSInput.
 */
@Composable
fun KhodedInput(
    value: String,
    placeholder: String = "",
    required: Boolean = false,
    validation: KhodedValidation = KhodedValidation(),
    size: ComponentSize = ComponentSize.Medium,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val validationResult = remember(value) { validation.validate(value) }
    val inputId = remember { "input-${kotlin.random.Random.nextInt()}" }
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Input(
            type = InputType.Text,
            attrs = Modifier
                .fillMaxWidth()
                .height(
                    when (size) {
                        ComponentSize.Small -> 32.px
                        ComponentSize.Medium -> 40.px
                        ComponentSize.Large -> 48.px
                    }
                )
                .padding(leftRight = 12.px, topBottom = 8.px)
                .border {
                    width(1.px)
                    style(LineStyle.Solid)
                    color(
                        when {
                            !validationResult.isValid -> Color("#ef4444")
                            isFocused -> Color("#3b82f6")
                            else -> Color("#d1d5db")
                        }
                    )
                }
                .borderRadius(6.px)
                .backgroundColor(Color.white)
                .fontSize(14.px)
                .color(Color("#111827"))
                .styleModifier {
                    property("outline", "none")
                    property("transition", "border-color 200ms, box-shadow 200ms")
                }
                .attrsModifier {
                    if (isFocused) {
                        style {
                            property("box-shadow", "0 0 0 2px rgba(59, 130, 246, 0.1)")
                        }
                    }
                }
                .then(modifier)
                .toAttrs {
                    id(inputId)
                    attr("placeholder", placeholder)
                    attr("aria-required", required.toString())
                    attr("aria-invalid", (!validationResult.isValid).toString())
                    if (!validationResult.isValid) {
                        attr("aria-describedby", "$inputId-error")
                    }
                    onFocusIn { isFocused = true }
                    onFocusOut { isFocused = false }
                    onInput { event ->
                        onValueChange(event.value ?: "")
                    }
                    value(value)
                }
        )
        
        // Error message with ARIA support
        if (!validationResult.isValid && validationResult.message.isNotEmpty()) {
            P(
                attrs = Modifier
                    .fontSize(12.px)
                    .color(Color("#ef4444"))
                    .margin(top = 4.px)
                    .toAttrs {
                        id("$inputId-error")
                        attr("role", "alert")
                        attr("aria-live", "polite")
                    }
            ) {
                Text(validationResult.message)
            }
        }
    }
}

/**
 * Shadcn-ui inspired textarea component.
 * Direct replacement for Bootstrap's BSTextArea.
 */
@Composable
fun KhodedTextArea(
    value: String,
    label: String = "",
    placeholder: String = "",
    required: Boolean = false,
    size: ComponentSize = ComponentSize.Medium,
    rows: Int = 4,
    maxLength: Int = 500,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val characterCount = value.length
    val isNearLimit = characterCount > maxLength * 0.8
    val inputId = remember { "textarea-${kotlin.random.Random.nextInt()}" }
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Label
        if (label.isNotEmpty()) {
            Label(
                attrs = Modifier
                    .fontSize(14.px)
                    .fontWeight(FontWeight.Medium)
                    .color(Color("#374151"))
                    .margin(bottom = 6.px)
                    .toAttrs {
                        attr("for", inputId)
                    }
            ) {
                Text(label)
                if (required) {
                    Text(" *")
                }
            }
        }
        
        org.jetbrains.compose.web.dom.TextArea(
            attrs = Modifier
                .fillMaxWidth()
                .minHeight(
                    when (size) {
                        ComponentSize.Small -> 80.px
                        ComponentSize.Medium -> 96.px
                        ComponentSize.Large -> 120.px
                    }
                )
                .padding(12.px)
                .border {
                    width(1.px)
                    style(LineStyle.Solid)
                    color(
                        when {
                            characterCount >= maxLength -> Color("#ef4444")
                            isFocused -> Color("#3b82f6")
                            else -> Color("#d1d5db")
                        }
                    )
                }
                .borderRadius(6.px)
                .backgroundColor(Color.white)
                .fontSize(14.px)
                .color(Color("#111827"))
                .styleModifier {
                    property("outline", "none")
                    property("resize", "vertical")
                    property("transition", "border-color 200ms, box-shadow 200ms")
                }
                .attrsModifier {
                    if (isFocused) {
                        style {
                            property("box-shadow", "0 0 0 2px rgba(59, 130, 246, 0.1)")
                        }
                    }
                }
                .then(modifier)
                .toAttrs {
                    id(inputId)
                    attr("placeholder", placeholder)
                    attr("rows", rows.toString())
                    attr("maxlength", maxLength.toString())
                    attr("aria-required", required.toString())
                    attr("aria-describedby", "$inputId-counter")
                    onFocusIn { isFocused = true }
                    onFocusOut { isFocused = false }
                    onInput { event ->
                        if ((event.value?.length ?: 0) <= maxLength) {
                            onValueChange(event.value ?: "")
                        }
                    }
                    value(value)
                }
        )
        
        // Character counter
        if (isNearLimit) {
            P(
                attrs = Modifier
                    .fontSize(12.px)
                    .color(
                        if (characterCount >= maxLength) Color("#ef4444") 
                        else Color("#6b7280")
                    )
                    .margin(top = 4.px)
                    .textAlign(TextAlign.Right)
                    .toAttrs {
                        id("$inputId-counter")
                        attr("aria-live", "polite")
                    }
            ) {
                Text("$characterCount / $maxLength")
            }
        }
    }
}