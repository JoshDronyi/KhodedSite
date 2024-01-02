package com.probro.khoded.styles

import com.probro.khoded.utils.Constants
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextShadow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.px

val BaseTextStyle by ComponentStyle {
    base {
        Modifier.fontSize(FontSize.Medium)
            .fontFamily("Times New Roman")
            .textAlign(TextAlign.Center)
            .textShadow(TextShadow.Initial)

    }
}

val BaseSectionStyles by ComponentStyle {
    base {
        Modifier.fillMaxWidth()
            .minHeight(Constants.SECTION_HEIGHT.px)
            .borderBottom(width = 2.px, color = Color.black)
    }
}