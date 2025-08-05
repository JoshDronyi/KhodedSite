package com.probro.khoded.styles

import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.CssStyle
import org.jetbrains.compose.web.css.*

// Simplified text styles for Kobweb 0.22.0 compatibility
val HeadingStyle = CssStyle {
    base {
        Modifier
            .fontWeight(700)
            .lineHeight(1.2)
            .color(rgb(31, 41, 55))
    }
}

val BodyTextStyle = CssStyle {
    base {
        Modifier
            .fontSize(16.px)
            .lineHeight(1.6)
            .color(rgb(75, 85, 99))
    }
}

val ButtonTextStyle = CssStyle {
    base {
        Modifier
            .fontSize(16.px)
            .fontWeight(600)
            .textAlign(TextAlign.Center)
    }
}