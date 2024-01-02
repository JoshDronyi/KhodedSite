package com.probro.khoded.models

data class ButtonState(
    val buttonText: String,
    val onButtonClick: () -> Unit
)