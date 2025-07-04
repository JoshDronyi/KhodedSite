package com.probro.khoded.components.composables

import androidx.compose.runtime.*
import com.probro.khoded.styles.components.TextAreaVariant
import com.probro.khoded.styles.components.TextBoxVariant
import com.probro.khoded.utils.Strings
import com.stevdza.san.kotlinbs.forms.BSInput
import com.stevdza.san.kotlinbs.forms.BSTextArea
import com.stevdza.san.kotlinbs.models.InputSize
import com.stevdza.san.kotlinbs.models.InputValidation
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.attr
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.InputStyle
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun TextBox(
    placeholder: String = "",
    required: Boolean = false,
    validation: InputValidation = InputValidation(),
    modifier: Modifier = Modifier,
    onValueChange: (newText: String) -> Unit
) {
    var value by remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    // Real-time validation
    LaunchedEffect(value) {
        if (value.isNotEmpty()) {
            when {
                placeholder.contains("email", ignoreCase = true) -> {
                    hasError = !value.matches(Regex(Strings.EMAIL_REGEX))
                    errorMessage = if (hasError) "Please enter a valid email address" else ""
                }

                required && value.isEmpty() -> {
                    hasError = true
                    errorMessage = "This field is required"
                }

                else -> {
                    hasError = false
                    errorMessage = ""
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BSInput(
            value = value,
            placeholder = placeholder,
            required = required,
            validation = validation,
            onValueChange = {
                value = it
                onValueChange(it)
            },
            modifier = InputStyle.toModifier(TextBoxVariant)
                .then(modifier)
                .border {
                    color(
                        when {
                            hasError -> Color("#dc3545")
                            isFocused -> Color("#007bff")
                            else -> Color("#ced4da")
                        }
                    )
                    width(if (isFocused || hasError) 2.px else 1.px)
                }
                .onFocusIn { isFocused = true }
                .onFocusOut { isFocused = false }
        )

        // Error message with smooth animation
        if (hasError && errorMessage.isNotEmpty()) {
            P(
                attrs = Modifier
                    .fontSize(12.px)
                    .color(Color("#dc3545"))
                    .margin(top = 4.px)
                    .padding(leftRight = 8.px)
                    .opacity(if (hasError) 1 else 0)
                    .transition(
                        Transition.of("opacity", 200.ms)
                    )
                    .toAttrs()
            ) {
                Text(errorMessage)
            }
        }
    }
}

@Composable
fun MessageArea(
    placeholder: String = "Enter Message here.",
    modifier: Modifier = Modifier,
    onValueChange: (newText: String) -> Unit
) {
    var value by remember { mutableStateOf("") }
    var characterCount by remember { mutableStateOf(0) }
    var isFocused by remember { mutableStateOf(false) }
    val maxLength = 500

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BSTextArea(
            value = value,
            label = placeholder,
            onValueChange = {
                if (it.length <= maxLength) {
                    value = it
                    characterCount = it.length
                    onValueChange(it)
                }
            },
            size = InputSize.Large,
            required = true,
            modifier = InputStyle.toModifier(TextAreaVariant)
                .then(modifier)
                .border {
                    color(if (isFocused) Color("#007bff") else Color("#ced4da"))
                    width(if (isFocused) 2.px else 1.px)
                }
                .onFocusIn { isFocused = true }
                .onFocusOut { isFocused = false }
        )

        // Character counter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.px),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (value.length > maxLength * 0.8) {
                P(
                    attrs = Modifier
                        .fontSize(12.px)
                        .color(if (characterCount >= maxLength) Color("#dc3545") else Color("#6c757d"))
                        .toAttrs()
                ) {
                    Text("$characterCount / $maxLength")
                }
            }
        }
    }
}

// Helper extension functions for focus events
fun Modifier.onFocusIn(callback: () -> Unit): Modifier = this.attr("onfocusin", "")
fun Modifier.onFocusOut(callback: () -> Unit): Modifier = this.attr("onfocusout", "")