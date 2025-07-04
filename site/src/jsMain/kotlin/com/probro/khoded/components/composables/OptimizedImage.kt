package com.probro.khoded.components

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun OptimizedImage(
    src: String,
    description: String,
    modifier: Modifier = Modifier,
    showPlaceholder: Boolean = true
) {
    var isLoaded by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }

    if (hasError && showPlaceholder) {
        // Fallback using real Compose Web DOM
        Div(
            attrs = modifier
                .width(100.percent)
                .height(200.px)
                .backgroundColor(Color("#f8f9fa"))
                .borderRadius(8.px)
                .display(DisplayStyle.Flex)
                .alignItems(AlignItems.Center)
                .justifyContent(JustifyContent.Center)
                .border(1.px, LineStyle.Solid, Color("#dee2e6"))
                .toAttrs()
        ) {
            Text("Image not available")
        }
    } else {
        // Use the actual Kobweb Image component
        Image(
            src = src,
            description = description,
            modifier = modifier
                .objectFit(ObjectFit.Cover)
                .opacity(if (isLoaded) 1.0 else 0.8)
                .transition(
                    Transition.of(property = "opacity", duration = 300.ms)
                )
                .attrs(Pair("loading","lazy"))

        )
    }

    // Simple loaded state tracking using LaunchedEffect
    LaunchedEffect(src) {
        isLoaded = false
        kotlinx.coroutines.delay(100) // Small delay to show loading state
        isLoaded = true
    }
}