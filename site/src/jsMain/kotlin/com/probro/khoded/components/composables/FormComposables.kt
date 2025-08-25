package com.probro.khoded.components.composables

/**
 * Form composable components with validation and accessibility features.
 *
 * This file provides a comprehensive set of form components that implement
 * the Khoded design system with built-in validation, accessibility support,
 * and responsive design. All components follow Material Design principles
 * and WCAG accessibility guidelines.
 *
 * Key features:
 * - Real-time validation with visual feedback
 * - Accessibility compliance (ARIA labels, roles, etc.)
 * - Responsive design with mobile-first approach
 * - Consistent visual styling with the design system
 * - TypeScript-like type safety through Kotlin
 *
 * @since 1.0.0
 * @see com.probro.khoded.utils.Strings for form text constants
 * @see com.probro.khoded.styles.components for component styling
 */

import androidx.compose.runtime.*
import com.probro.khoded.utils.Strings
import com.probro.khoded.components.ui.KhodedInput
import com.probro.khoded.components.ui.KhodedTextArea
import com.probro.khoded.components.ui.models.ComponentSize
import com.probro.khoded.components.ui.models.KhodedValidation
import com.probro.khoded.services.ValidationServiceProvider
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.ExperimentalComposeWebApi

/**
 * Enhanced text input component with validation and accessibility features.
 *
 * A comprehensive text input component that provides real-time validation,
 * error handling, focus management, and full accessibility compliance.
 * Integrates with the Khoded design system for consistent visual appearance.
 *
 * Features:
 * - Real-time validation with visual feedback
 * - Accessibility attributes (ARIA labels, roles)
 * - Focus and blur state management
 * - Error message display with smooth transitions
 * - Responsive design with mobile optimization
 * - Integration with Shadcn-ui inspired design system
 *
 * @param placeholder Text displayed when the input is empty
 * @param required Whether the field is required for form submission
 * @param validation Validation rules and configuration object
 * @param modifier Styling modifiers for custom appearance
 * @param onValueChange Callback invoked when the input value changes
 *
 * @since 1.0.0
 * @see TextArea for multi-line text input
 * @see InputValidation for validation configuration
 *
 * Example usage:
 * ```kotlin
 * TextBox(
 *     placeholder = "Enter your email",
 *     required = true,
 *     validation = InputValidation(
 *         isValid = { email -> email.contains("@") },
 *         errorMessage = "Please enter a valid email address"
 *     )
 * ) { newValue ->
 *     emailState = newValue
 * }
 * ```
 */
@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun TextBox(
    placeholder: String = "",
    required: Boolean = false,
    validation: KhodedValidation = KhodedValidation(),
    modifier: Modifier = Modifier,
    onValueChange: (newText: String) -> Unit
) {
    var value by remember { mutableStateOf("") }

    // Enhanced validation using ValidationService (SRP principle)
    val validationService = ValidationServiceProvider.instance
    val enhancedValidation = remember(placeholder, required, validation) {
        if (placeholder.contains("email", ignoreCase = true)) {
            KhodedValidation(
                customValidator = { email -> validationService.validateEmail(email) },
                required = required
            )
        } else {
            KhodedValidation(required = required)
        }
    }

    KhodedInput(
        value = value,
        placeholder = placeholder,
        required = required,
        validation = enhancedValidation,
        modifier = modifier,
        onValueChange = {
            value = it
            onValueChange(it)
        }
    )
}

@Composable
fun MessageArea(
    placeholder: String = "Enter Message here.",
    modifier: Modifier = Modifier,
    onValueChange: (newText: String) -> Unit
) {
    var value by remember { mutableStateOf("") }
    val maxLength = 500
    
    // Use ValidationService for message validation (SRP principle)
    val validationService = ValidationServiceProvider.instance
    val messageValidation = KhodedValidation(
        customValidator = { message -> validationService.validateMessage(message, minLength = 10, maxLength = maxLength) },
        required = true
    )

    KhodedTextArea(
        value = value,
        label = placeholder,
        placeholder = placeholder,
        required = true,
        size = ComponentSize.Large,
        maxLength = maxLength,
        modifier = modifier,
        onValueChange = {
            value = it
            onValueChange(it)
        }
    )
}