package com.probro.khoded.styles.componentStyles

import com.probro.khoded.models.Res.TextStyle.FONT_FAMILY
import com.probro.khoded.styles.textStyles.BackgroundStyle
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

sealed interface DivKind : ComponentKind
sealed interface NavItemKind : ComponentKind
sealed interface PopUpKind : ComponentKind


val BackingCardStyle = CssStyle<DivKind> {
    base {
        Modifier
            .fillMaxWidth(90.percent)
            .borderRadius(20.px)
            .color(Colors.Black)
    }
}

val NavItemStyle = CssStyle<NavItemKind> {
    base {
        Modifier
            .padding(0.px)
            .margin(0.px)
            .textAlign(TextAlign.Center)
    }
}

val PopUpTextStyle = CssStyle<PopUpKind> {
    base {
        Modifier
            .padding(0.px)
            .margin(0.px)
            .fontFamily(FONT_FAMILY)
            .fillMaxWidth()
            .textAlign(TextAlign.Center)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Small)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XLarge)
    }
}

val HeaderBackground = BackgroundStyle.addVariant {
    base {
        Modifier.backgroundColor(Colors.Transparent)
    }
}