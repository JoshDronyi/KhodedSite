package com.probro.khoded.components.interactions

import androidx.compose.runtime.*
import com.probro.khoded.styles.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*
import com.probro.khoded.components.accessibility.KhodedButtonVariant
import com.probro.khoded.components.accessibility.KhodedButtonSize

/**
 * Advanced Interaction Patterns - Simplified version
 * 
 * Provides interactive components with basic animations and feedback.
 * This is a simplified implementation to ensure compilation compatibility.
 */

// =============================================================================
// INTERACTION STATE MANAGEMENT
// =============================================================================

enum class InteractionState {
    Idle, Hover, Active, Pressed
}

// =============================================================================
// ENHANCED INTERACTIVE BUTTON
// =============================================================================

@Composable
fun EnhancedInteractiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: KhodedButtonVariant = KhodedButtonVariant.Primary,
    size: KhodedButtonSize = KhodedButtonSize.Medium,
    disabled: Boolean = false,
    showRipple: Boolean = true,
    hapticFeedback: Boolean = false
) {
    var interactionState by remember { mutableStateOf(InteractionState.Idle) }
    var isPressed by remember { mutableStateOf(false) }

    val backgroundColor = when (variant) {
        KhodedButtonVariant.Primary -> if (isPressed) Color("#5b21b6") else Color("#6366f1")
        KhodedButtonVariant.Secondary -> if (isPressed) Color("#0891b2") else Color("#06b6d4")
        KhodedButtonVariant.Ghost -> Color.transparent
        KhodedButtonVariant.Danger -> if (isPressed) Color("#b91c1c") else Color("#dc2626")
    }

    val textColor = if (variant == KhodedButtonVariant.Ghost) Color("#6366f1") else Color.white
    
    val padding = when (size) {
        KhodedButtonSize.Small -> "8px 16px"
        KhodedButtonSize.Medium -> "12px 24px"
        KhodedButtonSize.Large -> "16px 32px"
    }
    
    Button(
        attrs = modifier
            .backgroundColor(backgroundColor)
            .color(textColor)
            .attrsModifier {
                style {
                    property("padding", padding)
                    property("border", if (variant == KhodedButtonVariant.Ghost) "2px solid #6366f1" else "none")
                    property("border-radius", "8px")
                    property("cursor", if (disabled) "not-allowed" else "pointer")
                    property("opacity", if (disabled) "0.6" else "1.0")
                    property("transition", "all 0.2s ease")
                    property("transform", if (isPressed && !disabled) "scale(0.98)" else "scale(1.0)")
                }
            }
            .toAttrs {
                onMouseDown { if (!disabled) isPressed = true }
                onMouseUp { isPressed = false }
                onMouseLeave { 
                    isPressed = false
                    interactionState = InteractionState.Idle
                }
                onMouseEnter { 
                    if (!disabled) interactionState = InteractionState.Hover 
                }
                onClick { if (!disabled) onClick() }
                if (disabled) disabled()
            }
    ) {
        Text(text)
    }
}

// =============================================================================
// MICRO-INTERACTION UTILITIES
// =============================================================================

@Composable
fun WithHoverEffect(
    modifier: Modifier = Modifier,
    hoverScale: Double = 1.05,
    content: @Composable () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    
    Div(
        attrs = modifier
            .attrsModifier {
                style {
                    property("transition", "transform 0.2s ease")
                    property("transform", if (isHovered) "scale($hoverScale)" else "scale(1.0)")
                }
            }
            .toAttrs {
                onMouseEnter { isHovered = true }
                onMouseLeave { isHovered = false }
            }
    ) {
        content()
    }
}

@Composable
fun WithPressEffect(
    modifier: Modifier = Modifier,
    pressScale: Double = 0.95,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Div(
        attrs = modifier
            .attrsModifier {
                style {
                    property("transition", "transform 0.1s ease")
                    property("transform", if (isPressed) "scale($pressScale)" else "scale(1.0)")
                }
            }
            .toAttrs {
                onMouseDown { isPressed = true }
                onMouseUp { isPressed = false }
                onMouseLeave { isPressed = false }
            }
    ) {
        content()
    }
}

// =============================================================================
// GESTURE RECOGNITION
// =============================================================================

@Composable
fun WithGestureRecognition(
    modifier: Modifier = Modifier,
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    onTap: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var startX by remember { mutableStateOf(0.0) }
    var startY by remember { mutableStateOf(0.0) }
    
    Div(
        attrs = modifier
            .toAttrs {
                onClick { onTap() }
            }
    ) {
        content()
    }
}

// =============================================================================
// LOADING AND STATE INDICATORS
// =============================================================================

@Composable
fun LoadingSpinner(
    modifier: Modifier = Modifier,
    size: Int = 24,
    color: String = "#6366f1"
) {
    Div(
        attrs = modifier
            .attrsModifier {
                style {
                    property("width", "${size}px")
                    property("height", "${size}px")
                    property("border", "2px solid #f3f4f6")
                    property("border-top", "2px solid $color")
                    property("border-radius", "50%")
                    property("animation", "spin 1s linear infinite")
                }
            }
            .toAttrs {
                attr("role", "status")
                attr("aria-label", "Loading")
            }
    )
}