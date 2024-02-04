package com.probro.khoded.styles

import com.probro.khoded.models.BaseTheme
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

val ServiceTabStyle by ComponentStyle {
    base {
        Modifier.margin(24.px).padding(8.px)
            .background(color = BaseTheme.secondaryColor.rgb)
            .border(width = 4.px, style = LineStyle.Groove, color = BaseTheme.primaryColor.rgb)
    }
}
