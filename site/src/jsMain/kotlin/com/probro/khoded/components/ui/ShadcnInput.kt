package com.probro.khoded.components.ui

import androidx.compose.runtime.*
import com.probro.khoded.components.ui.models.ComponentSize
import com.probro.khoded.components.ui.models.KhodedValidation
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.attributes.InputType

@Composable
fun KhodedInput(
    value: String,
    placeholder: String = "",
    required: Boolean = false,
    validation: KhodedValidation = KhodedValidation(),
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val validationResult = validation.validate(value)
    val showError = !validationResult.isValid && value.isNotEmpty()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.px)
    ) {
        Input(
            type = InputType.Text,
            attrs = Modifier
                .fillMaxWidth()
                .padding(12.px, 16.px)
                .borderRadius(8.px)
                .border(
                    width = 2.px,
                    style = LineStyle.Solid,
                    color = when {
                        showError -> rgb(220, 38, 38)
                        isFocused -> rgb(6, 182, 212)
                        else -> rgb(229, 231, 235)
                    }
                )
                .fontSize(16.px)
                .backgroundColor(Color.white)
                .onFocusIn { isFocused = true }
                .onFocusOut { isFocused = false }
                .toAttrs {
                    attr("placeholder", placeholder)
                    if (required) {
                        attr("required", "")
                    }
                    value(value)
                    onInput { event ->
                        onValueChange(event.value ?: "")
                    }
                    style {
                        property("transition", "border-color 0.2s ease")
                        property("outline", "none")
                    }
                }
        )
        
        if (showError) {
            SpanText(
                text = validationResult.message,
                modifier = Modifier
                    .fontSize(14.px)
                    .color(rgb(220, 38, 38))
            )
        }
    }
}

@Composable
fun KhodedTextArea(
    value: String,
    label: String = "",
    placeholder: String = "",
    required: Boolean = false,
    size: ComponentSize = ComponentSize.Medium,
    maxLength: Int? = null,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val characterCount = value.length
    val isAtLimit = maxLength?.let { characterCount >= it } ?: false

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.px)
    ) {
        if (label.isNotEmpty()) {
            SpanText(
                text = label,
                modifier = Modifier
                    .fontSize(14.px)
                    .fontWeight(500)
                    .color(rgb(55, 65, 81))
            )
        }
        
        TextArea(
            attrs = Modifier
                .fillMaxWidth()
                .minHeight(when(size) {
                    ComponentSize.Small -> 80.px
                    ComponentSize.Medium -> 120.px
                    ComponentSize.Large -> 160.px
                })
                .padding(12.px, 16.px)
                .borderRadius(8.px)
                .border(
                    width = 2.px,
                    style = LineStyle.Solid,
                    color = when {
                        isAtLimit -> rgb(245, 101, 101)
                        isFocused -> rgb(6, 182, 212)
                        else -> rgb(229, 231, 235)
                    }
                )
                .fontSize(16.px)
                .backgroundColor(Color.white)
                .fontFamily("inherit")
                .onFocusIn { isFocused = true }
                .onFocusOut { isFocused = false }
                .toAttrs {
                    attr("placeholder", placeholder)
                    if (required) {
                        attr("required", "")
                    }
                    maxLength?.let { attr("maxlength", it.toString()) }
                    value(value)
                    onInput { event ->
                        val newValue = event.value ?: ""
                        if (maxLength == null || newValue.length <= maxLength) {
                            onValueChange(newValue)
                        }
                    }
                    style {
                        property("transition", "border-color 0.2s ease")
                        property("outline", "none")
                        property("resize", "vertical")
                    }
                }
        )
        
        maxLength?.let { max ->
            SpanText(
                text = "$characterCount/$max",
                modifier = Modifier
                    .fontSize(12.px)
                    .color(if (isAtLimit) rgb(220, 38, 38) else rgb(107, 114, 128))
            )
        }
    }
}