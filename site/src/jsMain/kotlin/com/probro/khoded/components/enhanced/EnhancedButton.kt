package com.probro.khoded.components.enhanced

import androidx.compose.runtime.*
import com.probro.khoded.components.base.BaseComponent
import com.probro.khoded.components.base.InteractiveComponent
import com.probro.khoded.components.base.ComponentUtils.toAccessibilityAttrs
import com.probro.khoded.design.KhodedDesignSystem
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text

/**
 * EnhancedButton - Concrete implementation extending InteractiveComponent
 * 
 * Demonstrates Open/Closed Principle (OCP):
 * - EXTENDS InteractiveComponent without MODIFYING it
 * - Adds button-specific functionality while reusing base behavior
 * - Can be further extended for specialized button types
 * 
 * This shows how OCP enables:
 * - Code reuse through inheritance
 * - Extension without modification
 * - Consistent behavior across component families
 * 
 * @since 2.0.0 (SOLID principles refactor)
 */
class EnhancedButton : InteractiveComponent() {
    
    enum class ButtonVariant {
        Primary, Secondary, Outline, Ghost, Danger
    }
    
    enum class ButtonSize {
        Small, Medium, Large
    }
    
    data class ButtonProps(
        val text: String,
        val variant: ButtonVariant = ButtonVariant.Primary,
        val size: ButtonSize = ButtonSize.Medium,
        val fullWidth: Boolean = false,
        val loading: Boolean = false,
        val icon: String? = null,
        val iconPosition: IconPosition = IconPosition.Left
    )
    
    enum class IconPosition { Left, Right }
    
    @Composable
    override fun Content(
        modifier: Modifier,
        props: BaseProps,
        additionalContent: @Composable () -> Unit
    ) {
        // This method should not be called directly for buttons
        error("Use RenderButton method for EnhancedButton components")
    }
    
    @Composable
    fun RenderButton(
        buttonProps: ButtonProps,
        interactionProps: InteractionProps,
        modifier: Modifier = Modifier,
        additionalContent: @Composable () -> Unit = {}
    ) {
        val buttonModifier = modifier
            .then(getButtonSizeModifier(buttonProps.size))
            .then(getButtonVariantModifier(buttonProps.variant))
            .then(if (buttonProps.fullWidth) Modifier.fillMaxWidth() else Modifier)
            .then(if (buttonProps.loading) Modifier.opacity(0.7) else Modifier)
        
        Button(
            attrs = buttonModifier
                .toAttrs {
                    // Apply accessibility attributes from base props
                    interactionProps.baseProps.toAccessibilityAttrs().forEach { (key, value) ->
                        attr(key, value)
                    }
                    
                    // Button-specific attributes
                    if (interactionProps.baseProps.disabled || buttonProps.loading) {
                        attr("disabled", "")
                    }
                    
                    // Interaction handlers
                    interactionProps.onClick?.let { onClick { it() } }
                    interactionProps.onFocus?.let { onFocusIn { it() } }
                    interactionProps.onBlur?.let { onFocusOut { it() } }
                }
        ) {
            if (buttonProps.loading) {
                LoadingSpinner()
            } else {
                if (buttonProps.icon != null && buttonProps.iconPosition == IconPosition.Left) {
                    ButtonIcon(buttonProps.icon)
                }
                
                Text(buttonProps.text)
                
                if (buttonProps.icon != null && buttonProps.iconPosition == IconPosition.Right) {
                    ButtonIcon(buttonProps.icon)
                }
            }
            
            // Additional content from composition
            additionalContent()
        }
    }
    
    @Composable
    private fun LoadingSpinner() {
        org.jetbrains.compose.web.dom.Div(
            attrs = Modifier
                .size(16.px)
                .border(2.px, LineStyle.Solid, Color.currentColor)
                .borderRadius(50.percent)
                .margin(right = 8.px)
                .toAttrs {
                    style {
                        property("border-top-color", "transparent")
                        property("animation", "spin 1s linear infinite")
                    }
                }
        )
    }
    
    @Composable  
    private fun ButtonIcon(icon: String) {
        org.jetbrains.compose.web.dom.Span(
            attrs = Modifier
                .fontSize(16.px)
                .margin(right = 8.px)
                .toAttrs()
        ) {
            Text(icon)
        }
    }
    
    private fun getButtonSizeModifier(size: ButtonSize): Modifier {
        return when (size) {
            ButtonSize.Small -> Modifier.padding(6.px, 12.px).fontSize(14.px)
            ButtonSize.Medium -> Modifier.padding(8.px, 16.px).fontSize(16.px)
            ButtonSize.Large -> Modifier.padding(12.px, 24.px).fontSize(18.px)
        }
    }
    
    private fun getButtonVariantModifier(variant: ButtonVariant): Modifier {
        return when (variant) {
            ButtonVariant.Primary -> Modifier
                .backgroundColor(KhodedDesignSystem.colors.primary)
                .color(KhodedDesignSystem.colors.textInverse)
                .border(0.px)
            
            ButtonVariant.Secondary -> Modifier
                .backgroundColor(KhodedDesignSystem.colors.backgroundSecondary)
                .color(KhodedDesignSystem.colors.textPrimary)
                .border(0.px)
            
            ButtonVariant.Outline -> Modifier
                .backgroundColor(Color.transparent)
                .color(KhodedDesignSystem.colors.primary)
                .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.primary)
            
            ButtonVariant.Ghost -> Modifier
                .backgroundColor(Color.transparent)
                .color(KhodedDesignSystem.colors.primary)
                .border(0.px)
            
            ButtonVariant.Danger -> Modifier
                .backgroundColor(KhodedDesignSystem.colors.error)
                .color(KhodedDesignSystem.colors.textInverse)
                .border(0.px)
        }.then(
            Modifier
                .borderRadius(6.px)
                .cursor(Cursor.Pointer)
                .styleModifier { 
                    property("transition", KhodedDesignSystem.animations.transition)
                }
        )
    }
}

/**
 * Convenient composable function for using EnhancedButton
 * Demonstrates how OCP makes the API clean and extensible
 */
@Composable
fun KhodedEnhancedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: EnhancedButton.ButtonVariant = EnhancedButton.ButtonVariant.Primary,
    size: EnhancedButton.ButtonSize = EnhancedButton.ButtonSize.Medium,
    disabled: Boolean = false,
    loading: Boolean = false,
    icon: String? = null,
    ariaLabel: String? = null
) {
    val button = remember { EnhancedButton() }
    
    button.RenderButton(
        buttonProps = EnhancedButton.ButtonProps(
            text = text,
            variant = variant,
            size = size,
            loading = loading,
            icon = icon
        ),
        interactionProps = InteractiveComponent.InteractionProps(
            baseProps = BaseComponent.BaseProps(
                disabled = disabled,
                ariaLabel = ariaLabel ?: text
            ),
            onClick = onClick
        ),
        modifier = modifier
    )
}