package com.probro.khoded.components.accessibility

import androidx.compose.runtime.*
import com.probro.khoded.styles.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*

/**
 * Accessible Components - Simplified version for compilation compatibility
 * 
 * This is a simplified version of the accessibility components to ensure
 * the project builds successfully. Full accessibility features can be
 * implemented incrementally.
 */

// =============================================================================
// COMPONENT VARIANTS AND ENUMS
// =============================================================================

enum class KhodedButtonVariant {
    Primary, Secondary, Ghost, Danger
}

enum class KhodedButtonSize {
    Small, Medium, Large
}

// =============================================================================
// ACCESSIBLE BUTTON COMPONENT
// =============================================================================

@Composable
fun KhodedAccessibleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: KhodedButtonVariant = KhodedButtonVariant.Primary,
    size: KhodedButtonSize = KhodedButtonSize.Medium,
    disabled: Boolean = false,
    ariaLabel: String? = null,
    ariaDescribedBy: String? = null,
    type: ButtonType = ButtonType.Button
) {
    Button(
        attrs = modifier
            .padding(12.px, 24.px)
            .backgroundColor(
                when (variant) {
                    KhodedButtonVariant.Primary -> Color("#6366f1")
                    KhodedButtonVariant.Secondary -> Color("#06b6d4") 
                    KhodedButtonVariant.Ghost -> Color.transparent
                    KhodedButtonVariant.Danger -> Color("#dc2626")
                }
            )
            .color(
                if (variant == KhodedButtonVariant.Ghost) Color("#6366f1") else Color.white
            )
            .border(
                if (variant == KhodedButtonVariant.Ghost) 2.px else 0.px,
                LineStyle.Solid,
                Color("#6366f1")
            )
            .borderRadius(8.px)
            .attrsModifier {
                style {
                    property("cursor", if (disabled) "not-allowed" else "pointer")
                }
            }
            .opacity(if (disabled) 0.6 else 1.0)
            .toAttrs {
                onClick { if (!disabled) onClick() }
                if (disabled) disabled()
                type(type)
                ariaLabel?.let { attr("aria-label", it) }
                ariaDescribedBy?.let { attr("aria-describedby", it) }
            }
    ) {
        Text(text)
    }
}

// =============================================================================
// ACCESSIBLE FORM COMPONENTS
// =============================================================================

@Composable
fun KhodedAccessibleInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    type: InputType<String> = InputType.Text,
    required: Boolean = false,
    disabled: Boolean = false,
    errorMessage: String? = null,
    helpText: String? = null,
    autoComplete: String? = null
) {
    val inputId = remember { "input-${kotlin.random.Random.nextInt()}" }
    val errorId = remember { "error-$inputId" }
    val helpId = remember { "help-$inputId" }
    
    Div(
        attrs = modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .toAttrs()
    ) {
        // Label
        Label(
            forId = inputId,
            attrs = Modifier
                .fontSize(14.px)
                .attrsModifier { style { property("font-weight", "500") } }
                .color(Color("#374151"))
                .attrsModifier { style { property("margin-bottom", "4px") } }
                .toAttrs()
        ) {
            Text(label)
            if (required) {
                Span(
                    attrs = Modifier
                        .color(Color("#dc2626"))
                        .toAttrs()
                ) {
                    Text(" *")
                }
            }
        }
        
        // Input
        Input(
            type = type,
            attrs = modifier
                .id(inputId)
                .padding(8.px, 12.px)
                .border(1.px, LineStyle.Solid, Color("#d1d5db"))
                .borderRadius(4.px)
                .fontSize(14.px)
                .width(100.percent)
                .toAttrs {
                    value(value)
                    onInput { onValueChange(it.value) }
                    placeholder(placeholder)
                    if (disabled) disabled()
                    if (required) required()
                    autoComplete?.let { attr("autocomplete", it) }
                    
                    val describedBy = buildList {
                        helpText?.let { add(helpId) }
                        errorMessage?.let { add(errorId) }
                    }.joinToString(" ")
                    
                    if (describedBy.isNotEmpty()) {
                        attr("aria-describedby", describedBy)
                    }
                    
                    if (errorMessage != null) {
                        attr("aria-invalid", "true")
                    }
                }
        )
        
        // Help text
        helpText?.let { text ->
            Div(
                attrs = Modifier
                    .id(helpId)
                    .fontSize(12.px)
                    .color(Color("#6b7280"))
                    .attrsModifier { style { property("margin-top", "4px") } }
                    .toAttrs()
            ) {
                Text(text)
            }
        }
        
        // Error message
        errorMessage?.let { error ->
            Div(
                attrs = Modifier
                    .id(errorId)
                    .fontSize(12.px)
                    .color(Color("#dc2626"))
                    .attrsModifier { style { property("margin-top", "4px") } }
                    .toAttrs {
                        attr("role", "alert")
                        attr("aria-live", "polite")
                    }
            ) {
                Text(error)
            }
        }
    }
}

// =============================================================================
// SCREEN READER ONLY TEXT
// =============================================================================

@Composable
fun ScreenReaderOnly(
    text: String,
    modifier: Modifier = Modifier
) {
    Div(
        attrs = modifier
            .position(Position.Absolute)
            .left((-10000).px)
            .width(1.px)
            .height(1.px)
            .attrsModifier { style { property("overflow", "hidden") } }
            .toAttrs {
                attr("class", "sr-only")
            }
    ) {
        Text(text)
    }
}