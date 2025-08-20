package com.probro.khoded.models

/**
 * Represents the state and behavior of a button component in the UI.
 *
 * This data class encapsulates the essential properties needed to render and handle
 * button interactions throughout the application. It follows the principle of
 * composition over inheritance for UI state management.
 *
 * @property buttonText The display text shown on the button. Should be concise and action-oriented.
 * @property onButtonClick The callback function executed when the button is clicked.
 *                        This allows for flexible handling of button interactions
 *                        without coupling the button component to specific business logic.
 *
 * @since 1.0.0
 * @see com.probro.khoded.components.composables.FormComposables for usage examples
 *
 * Example usage:
 * ```kotlin
 * val submitButton = ButtonState(
 *     buttonText = "Submit Form",
 *     onButtonClick = { handleFormSubmission() }
 * )
 * ```
 */
data class ButtonState(
    val buttonText: String,
    val onButtonClick: () -> Unit
)