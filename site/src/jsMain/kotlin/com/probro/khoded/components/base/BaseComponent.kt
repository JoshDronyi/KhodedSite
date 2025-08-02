package com.probro.khoded.components.base

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import org.jetbrains.compose.web.css.*

/**
 * BaseComponent - Implements Open/Closed Principle (OCP)
 * 
 * This abstract base provides common functionality that is CLOSED for modification
 * but OPEN for extension. New component types can extend this base without changing
 * existing code, following the OCP principle.
 * 
 * Benefits of OCP implementation:
 * - Common functionality is centralized and reusable
 * - New component variants can be added without modifying existing code
 * - Consistent behavior across all UI components
 * - Easy to maintain and test base functionality
 * 
 * @since 2.0.0 (SOLID principles refactor)
 */
abstract class BaseComponent {
    
    /**
     * Common properties available to all components.
     */
    data class BaseProps(
        val id: String? = null,
        val className: String? = null,
        val testId: String? = null,
        val ariaLabel: String? = null,
        val ariaDescribedBy: String? = null,
        val disabled: Boolean = false,
        val hidden: Boolean = false,
        val tabIndex: Int? = null
    )
    
    /**
     * Abstract method that must be implemented by all concrete components.
     * This defines the component-specific rendering logic.
     */
    @Composable
    abstract fun Content(
        modifier: Modifier,
        props: BaseProps,
        additionalContent: @Composable () -> Unit = {}
    )
    
    /**
     * Common rendering wrapper that applies base functionality.
     * This method is CLOSED for modification but used by all extensions.
     */
    @Composable
    fun Render(
        modifier: Modifier = Modifier,
        props: BaseProps = BaseProps(),
        content: @Composable () -> Unit = {}
    ) {
        // Apply common accessibility and base styling
        val enhancedModifier = modifier
            .then(if (props.hidden) Modifier.styleModifier { display(DisplayStyle.None) } else Modifier)
            .then(if (props.disabled) Modifier.opacity(0.6) else Modifier)
        
        // Render the specific component implementation
        Content(
            modifier = enhancedModifier,
            props = props,
            additionalContent = content
        )
    }
}

/**
 * FormComponent - Extends BaseComponent for form-specific functionality
 * Demonstrates OCP: adding new functionality without modifying BaseComponent
 */
abstract class FormComponent : BaseComponent() {
    
    data class FormProps(
        val baseProps: BaseProps = BaseProps(),
        val required: Boolean = false,
        val name: String? = null,
        val value: String = "",
        val placeholder: String = "",
        val autoComplete: String? = null,
        val errorMessage: String? = null,
        val helpText: String? = null
    )
    
    /**
     * Enhanced rendering for form components with validation display.
     */
    @Composable
    fun RenderForm(
        modifier: Modifier = Modifier,
        formProps: FormProps,
        onValueChange: (String) -> Unit = {},
        content: @Composable () -> Unit = {}
    ) {
        // Use base render with enhanced form-specific props
        val enhancedProps = formProps.baseProps.copy(
            ariaLabel = formProps.baseProps.ariaLabel ?: formProps.placeholder,
            ariaDescribedBy = when {
                formProps.errorMessage != null -> "${formProps.baseProps.id}-error"
                formProps.helpText != null -> "${formProps.baseProps.id}-help"
                else -> formProps.baseProps.ariaDescribedBy
            }
        )
        
        Render(
            modifier = modifier,
            props = enhancedProps,
            content = content
        )
    }
    
    @Composable
    abstract override fun Content(
        modifier: Modifier,
        props: BaseProps,
        additionalContent: @Composable () -> Unit
    )
}

/**
 * InteractiveComponent - Extends BaseComponent for interactive elements
 * Demonstrates OCP: adding interaction handling without modifying base
 */
abstract class InteractiveComponent : BaseComponent() {
    
    data class InteractionProps(
        val baseProps: BaseProps = BaseProps(),
        val onClick: (() -> Unit)? = null,
        val onHover: (() -> Unit)? = null,
        val onFocus: (() -> Unit)? = null,
        val onBlur: (() -> Unit)? = null,
        val cursor: String = "pointer"
    )
    
    @Composable
    fun RenderInteractive(
        modifier: Modifier = Modifier,
        interactionProps: InteractionProps,
        content: @Composable () -> Unit = {}
    ) {
        // Apply interaction-specific enhancements
        val enhancedModifier = modifier
            .then(if (interactionProps.onClick != null) Modifier.cursor(Cursor.Pointer) else Modifier)
        
        Render(
            modifier = enhancedModifier,
            props = interactionProps.baseProps,
            content = content
        )
    }
    
    @Composable
    abstract override fun Content(
        modifier: Modifier,
        props: BaseProps,
        additionalContent: @Composable () -> Unit
    )
}

/**
 * Utility extensions for BaseComponent implementations
 */
object ComponentUtils {
    
    /**
     * Creates accessibility attributes from BaseProps.
     */
    fun BaseComponent.BaseProps.toAccessibilityAttrs(): Map<String, String> {
        return buildMap {
            id?.let { put("id", it) }
            className?.let { put("class", it) }
            testId?.let { put("data-testid", it) }
            ariaLabel?.let { put("aria-label", it) }
            ariaDescribedBy?.let { put("aria-describedby", it) }
            tabIndex?.let { put("tabindex", it.toString()) }
            if (disabled) put("aria-disabled", "true")
            if (hidden) put("aria-hidden", "true")
        }
    }
    
    /**
     * Generates unique component ID if not provided.
     */
    fun generateComponentId(prefix: String = "component"): String {
        return "$prefix-${kotlin.random.Random.nextInt(10000)}"
    }
}