package com.probro.khoded.components.accessibility

import androidx.compose.runtime.*
import com.probro.khoded.styles.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.*
import com.varabyte.kobweb.silk.components.navigation.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*

/**
 * Accessibility-First Components for Khoded Website
 * 
 * WCAG 2.2 AA Compliant components with:
 * - Proper focus management
 * - Screen reader support
 * - Keyboard navigation
 * - High contrast support
 * - Touch-friendly interaction targets
 */

// =============================================================================
// ACCESSIBLE BUTTON COMPONENTS
// =============================================================================

enum class KhodedButtonVariant { Primary, Secondary, Ghost, Danger }
enum class KhodedButtonSize { Small, Medium, Large }

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
    val (backgroundColor, textColor, hoverColor) = when (variant) {
        KhodedButtonVariant.Primary -> Triple(
            KhodedColors.Purple500, 
            KhodedColors.TextInverse, 
            KhodedColors.Purple600
        )
        KhodedButtonVariant.Secondary -> Triple(
            KhodedColors.Teal500, 
            KhodedColors.TextInverse, 
            KhodedColors.Teal600
        )
        KhodedButtonVariant.Ghost -> Triple(
            Color.transparent, 
            KhodedColors.Purple500, 
            KhodedColors.Purple50
        )
        KhodedButtonVariant.Danger -> Triple(
            KhodedColors.Error, 
            KhodedColors.TextInverse, 
            Color.rgb(185, 28, 28)
        )
    }
    
    val padding = when (size) {
        KhodedButtonSize.Small -> Pair(KhodedSpacing.sm, KhodedSpacing.md)
        KhodedButtonSize.Medium -> Pair(KhodedSpacing.md, KhodedSpacing.lg)
        KhodedButtonSize.Large -> Pair(KhodedSpacing.lg, KhodedSpacing.xl)
    }
    
    val fontSize = when (size) {
        KhodedButtonSize.Small -> KhodedTypography.sm
        KhodedButtonSize.Medium -> KhodedTypography.base
        KhodedButtonSize.Large -> KhodedTypography.lg
    }
    
    Button(
        attrs = modifier
            .minHeight(KhodedSpacing.touchTargetMin) // WCAG touch target
            .minWidth(KhodedSpacing.touchTargetMin)
            .padding(vertical = padding.first, horizontal = padding.second)
            .backgroundColor(if (disabled) KhodedColors.Gray300 else backgroundColor)
            .color(if (disabled) KhodedColors.TextMuted else textColor)
            .borderRadius(KhodedRadius.md)
            .border(
                if (variant == KhodedButtonVariant.Ghost) 2.px else 0.px,
                LineStyle.Solid,
                if (disabled) KhodedColors.Gray300 else backgroundColor
            )
            .fontSize(fontSize)
            .fontWeight(KhodedTypography.medium)
            .fontFamily(KhodedTypography.fontFamilyDefault)
            .cursor(if (disabled) Cursor.NotAllowed else Cursor.Pointer)
            .opacity(if (disabled) 0.6 else 1.0)
            .transition(CSSTransition("all", KhodedAnimations.fast))
            .hover {
                if (!disabled) {
                    backgroundColor(hoverColor)
                    transform { translateY((-1).px) }
                }
            }
            .focus {
                outline("2px solid ${KhodedColors.Focus}")
                outlineOffset(2.px)
            }
            .active {
                if (!disabled) {
                    transform { translateY(0.px) }
                }
            }
            .toAttrs {
                onClick { if (!disabled) onClick() }
                disabled(disabled)
                type(type)
                ariaLabel?.let { attr("aria-label", it) }
                ariaDescribedBy?.let { attr("aria-describedby", it) }
                if (disabled) {
                    attr("aria-disabled", "true")
                }
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
    type: InputType = InputType.Text,
    required: Boolean = false,
    disabled: Boolean = false,
    errorMessage: String? = null,
    helpText: String? = null,
    autoComplete: String? = null
) {
    val inputId = remember { "input-${kotlin.random.Random.nextInt()}" }
    val errorId = remember { "error-$inputId" }
    val helpId = remember { "help-$inputId" }
    
    Column(
        modifier = Modifier.gap(KhodedSpacing.xs)
    ) {
        // Label
        Label(
            forId = inputId,
            attrs = Modifier
                .fontSize(KhodedTypography.sm)
                .fontWeight(KhodedTypography.medium)
                .color(KhodedColors.TextPrimary)
                .marginBottom(KhodedSpacing.xs)
                .toAttrs()
        ) {
            Text(label)
            if (required) {
                Span(
                    attrs = Modifier
                        .color(KhodedColors.Error)
                        .marginLeft(KhodedSpacing.xs)
                        .toAttrs {
                            attr("aria-label", "required field")
                        }
                ) {
                    Text("*")
                }
            }
        }
        
        // Input
        Input(
            type = type,
            value = value,
            attrs = modifier
                .id(inputId)
                .fillMaxWidth()
                .minHeight(KhodedSpacing.touchTargetMin)
                .padding(KhodedSpacing.md)
                .fontSize(16.px) // Prevents iOS zoom
                .fontFamily(KhodedTypography.fontFamilyDefault)
                .backgroundColor(if (disabled) KhodedColors.Gray100 else KhodedColors.Background)
                .color(if (disabled) KhodedColors.TextMuted else KhodedColors.TextPrimary)
                .border(
                    1.px, 
                    LineStyle.Solid, 
                    when {
                        errorMessage != null -> KhodedColors.Error
                        else -> KhodedColors.Gray300
                    }
                )
                .borderRadius(KhodedRadius.md)
                .transition(CSSTransition("all", KhodedAnimations.fast))
                .focus {
                    borderColor(if (errorMessage != null) KhodedColors.Error else KhodedColors.Purple500)
                    boxShadow(
                        if (errorMessage != null) "0 0 0 3px rgba(220, 38, 38, 0.1)"
                        else KhodedShadows.focus
                    )
                    outline("none")
                }
                .hover {
                    if (!disabled) {
                        borderColor(if (errorMessage != null) KhodedColors.Error else KhodedColors.Gray400)
                    }
                }
                .toAttrs {
                    placeholder(placeholder)
                    required(required)
                    disabled(disabled)
                    autoComplete?.let { autoComplete(it) }
                    
                    // Accessibility attributes
                    attr("aria-invalid", (errorMessage != null).toString())
                    if (errorMessage != null) {
                        attr("aria-describedby", errorId)
                    } else if (helpText != null) {
                        attr("aria-describedby", helpId)
                    }
                    
                    onInput { event ->
                        onValueChange(event.value)
                    }
                }
        )
        
        // Help text
        helpText?.let { text ->
            P(
                attrs = Modifier
                    .id(helpId)
                    .fontSize(KhodedTypography.sm)
                    .color(KhodedColors.TextSecondary)
                    .margin(0.px)
                    .toAttrs()
            ) {
                Text(text)
            }
        }
        
        // Error message
        errorMessage?.let { message ->
            P(
                attrs = Modifier
                    .id(errorId)
                    .fontSize(KhodedTypography.sm)
                    .color(KhodedColors.Error)
                    .margin(0.px)
                    .toAttrs {
                        attr("role", "alert")
                        attr("aria-live", "polite")
                    }
            ) {
                Text("⚠ $message")
            }
        }
    }
}

@Composable
fun KhodedAccessibleTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    required: Boolean = false,
    disabled: Boolean = false,
    rows: Int = 4,
    maxLength: Int? = null,
    errorMessage: String? = null,
    helpText: String? = null
) {
    val textAreaId = remember { "textarea-${kotlin.random.Random.nextInt()}" }
    val errorId = remember { "error-$textAreaId" }
    val helpId = remember { "help-$textAreaId" }
    
    Column(
        modifier = Modifier.gap(KhodedSpacing.xs)
    ) {
        // Label
        Label(
            forId = textAreaId,
            attrs = Modifier
                .fontSize(KhodedTypography.sm)
                .fontWeight(KhodedTypography.medium)
                .color(KhodedColors.TextPrimary)
                .marginBottom(KhodedSpacing.xs)
                .toAttrs()
        ) {
            Text(label)
            if (required) {
                Span(
                    attrs = Modifier
                        .color(KhodedColors.Error)
                        .marginLeft(KhodedSpacing.xs)
                        .toAttrs {
                            attr("aria-label", "required field")
                        }
                ) {
                    Text("*")
                }
            }
        }
        
        // Textarea
        TextArea(
            value = value,
            attrs = modifier
                .id(textAreaId)
                .fillMaxWidth()
                .minHeight((KhodedSpacing.touchTargetMin.value * rows).px)
                .padding(KhodedSpacing.md)
                .fontSize(16.px) // Prevents iOS zoom
                .fontFamily(KhodedTypography.fontFamilyDefault)
                .backgroundColor(if (disabled) KhodedColors.Gray100 else KhodedColors.Background)
                .color(if (disabled) KhodedColors.TextMuted else KhodedColors.TextPrimary)
                .border(
                    1.px, 
                    LineStyle.Solid, 
                    when {
                        errorMessage != null -> KhodedColors.Error
                        else -> KhodedColors.Gray300
                    }
                )
                .borderRadius(KhodedRadius.md)
                .resize(Resize.Vertical)
                .transition(CSSTransition("all", KhodedAnimations.fast))
                .focus {
                    borderColor(if (errorMessage != null) KhodedColors.Error else KhodedColors.Purple500)
                    boxShadow(
                        if (errorMessage != null) "0 0 0 3px rgba(220, 38, 38, 0.1)"
                        else KhodedShadows.focus
                    )
                    outline("none")
                }
                .hover {
                    if (!disabled) {
                        borderColor(if (errorMessage != null) KhodedColors.Error else KhodedColors.Gray400)
                    }
                }
                .toAttrs {
                    placeholder(placeholder)
                    required(required)
                    disabled(disabled)
                    rows(rows)
                    maxLength?.let { maxLength(it) }
                    
                    // Accessibility attributes
                    attr("aria-invalid", (errorMessage != null).toString())
                    if (errorMessage != null) {
                        attr("aria-describedby", errorId)
                    } else if (helpText != null) {
                        attr("aria-describedby", helpId)
                    }
                    
                    onInput { event ->
                        onValueChange(event.value)
                    }
                }
        )
        
        // Character counter
        maxLength?.let { max ->
            Div(
                attrs = Modifier
                    .textAlign(TextAlign.End)
                    .fontSize(KhodedTypography.xs)
                    .color(
                        when {
                            value.length > max -> KhodedColors.Error
                            value.length > max * 0.9 -> KhodedColors.Warning
                            else -> KhodedColors.TextSecondary
                        }
                    )
                    .toAttrs()
            ) {
                Text("${value.length}/$max")
            }
        }
        
        // Help text
        helpText?.let { text ->
            P(
                attrs = Modifier
                    .id(helpId)
                    .fontSize(KhodedTypography.sm)
                    .color(KhodedColors.TextSecondary)
                    .margin(0.px)
                    .toAttrs()
            ) {
                Text(text)
            }
        }
        
        // Error message
        errorMessage?.let { message ->
            P(
                attrs = Modifier
                    .id(errorId)
                    .fontSize(KhodedTypography.sm)
                    .color(KhodedColors.Error)
                    .margin(0.px)
                    .toAttrs {
                        attr("role", "alert")
                        attr("aria-live", "polite")
                    }
            ) {
                Text("⚠ $message")
            }
        }
    }
}

// =============================================================================
// ACCESSIBLE NAVIGATION COMPONENTS
// =============================================================================

@Composable
fun SkipToContent(targetId: String = "main-content") {
    A(
        href = "#$targetId",
        attrs = Modifier
            .position(Position.Absolute)
            .top((-40).px)
            .left(KhodedSpacing.md)
            .backgroundColor(KhodedColors.Purple600)
            .color(KhodedColors.TextInverse)
            .padding(KhodedSpacing.sm, KhodedSpacing.md)
            .borderRadius(KhodedRadius.sm)
            .fontSize(KhodedTypography.sm)
            .fontWeight(KhodedTypography.medium)
            .textDecorationLine(TextDecorationLine.None)
            .zIndex(10000)
            .focus {
                top(KhodedSpacing.md)
            }
            .transition(CSSTransition("top", KhodedAnimations.fast))
            .toAttrs {
                attr("aria-label", "Skip to main content")
            }
    ) {
        Text("Skip to main content")
    }
}

// =============================================================================
// ACCESSIBLE ALERT COMPONENTS
// =============================================================================

enum class AlertType { Success, Warning, Error, Info }

@Composable
fun KhodedAlert(
    message: String,
    type: AlertType = AlertType.Info,
    modifier: Modifier = Modifier,
    dismissible: Boolean = false,
    onDismiss: (() -> Unit)? = null,
    title: String? = null
) {
    val (backgroundColor, borderColor, iconColor, icon) = when (type) {
        AlertType.Success -> arrayOf(
            Color.rgb(240, 253, 244), 
            KhodedColors.Success, 
            KhodedColors.Success, 
            "✓"
        )
        AlertType.Warning -> arrayOf(
            Color.rgb(255, 251, 235), 
            KhodedColors.Warning, 
            KhodedColors.Warning, 
            "⚠"
        )
        AlertType.Error -> arrayOf(
            Color.rgb(254, 242, 242), 
            KhodedColors.Error, 
            KhodedColors.Error, 
            "✕"
        )
        AlertType.Info -> arrayOf(
            Color.rgb(239, 246, 255), 
            KhodedColors.Info, 
            KhodedColors.Info, 
            "ℹ"
        )
    }
    
    Div(
        attrs = modifier
            .fillMaxWidth()
            .padding(KhodedSpacing.lg)
            .backgroundColor(backgroundColor as Color)
            .border(1.px, LineStyle.Solid, borderColor as Color)
            .borderRadius(KhodedRadius.md)
            .toAttrs {
                attr("role", if (type == AlertType.Error) "alert" else "status")
                attr("aria-live", if (type == AlertType.Error) "assertive" else "polite")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .alignItems(AlignItems.Start)
                .gap(KhodedSpacing.md)
        ) {
            // Icon
            Span(
                attrs = Modifier
                    .fontSize(KhodedTypography.lg)
                    .color(iconColor as Color)
                    .toAttrs()
            ) {
                Text(icon as String)
            }
            
            // Content
            Column(
                modifier = Modifier.flexGrow(1).gap(KhodedSpacing.xs)
            ) {
                title?.let { titleText ->
                    H4(
                        attrs = Modifier
                            .fontSize(KhodedTypography.base)
                            .fontWeight(KhodedTypography.semiBold)
                            .color(borderColor as Color)
                            .margin(0.px)
                            .toAttrs()
                    ) {
                        Text(titleText)
                    }
                }
                
                P(
                    attrs = Modifier
                        .fontSize(KhodedTypography.sm)
                        .color(KhodedColors.TextPrimary)
                        .margin(0.px)
                        .lineHeight(KhodedTypography.relaxed)
                        .toAttrs()
                ) {
                    Text(message)
                }
            }
            
            // Dismiss button
            if (dismissible && onDismiss != null) {
                Button(
                    attrs = Modifier
                        .size(24.px)
                        .backgroundColor(Color.transparent)
                        .border(0.px)
                        .borderRadius(KhodedRadius.sm)
                        .color(borderColor as Color)
                        .cursor(Cursor.Pointer)
                        .hover {
                            backgroundColor(Color.black.copy(alpha = 26)) // 10% opacity
                        }
                        .focus {
                            outline("2px solid ${KhodedColors.Focus}")
                            outlineOffset(2.px)
                        }
                        .toAttrs {
                            attr("aria-label", "Dismiss alert")
                            onClick { onDismiss() }
                        }
                ) {
                    Text("×")
                }
            }
        }
    }
}

// =============================================================================
// LOADING AND STATUS COMPONENTS
// =============================================================================

@Composable
fun KhodedLoadingSpinner(
    size: CSSSizeValue<CSSUnit.px> = 24.px,
    color: Color = KhodedColors.Purple500,
    modifier: Modifier = Modifier
) {
    Div(
        attrs = modifier
            .size(size)
            .border(2.px, LineStyle.Solid, KhodedColors.Gray200)
            .borderTop(2.px, LineStyle.Solid, color)
            .borderRadius(50.percent)
            .animation(
                Animation(
                    name = "spin",
                    duration = 1.s,
                    iterationCount = AnimationIterationCount.Infinite,
                    timingFunction = AnimationTimingFunction.Linear
                )
            )
            .toAttrs {
                attr("role", "status")
                attr("aria-label", "Loading")
            }
    )
}