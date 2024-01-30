package com.probro.khoded

import androidx.compose.runtime.Composable
import com.probro.khoded.models.Res
import com.probro.khoded.styles.BaseTextStyle
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@App
@Composable
fun MyApp(content: @Composable () -> Unit) {
    SilkApp {
        Surface(SmoothColorStyle.toModifier().minHeight(100.vh)) {
            content()
        }
    }
}

val BaseButtonTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier.fontSize(FontSize.Medium)
    }
}

val BlueButtonVariant by ButtonStyle.addVariant {
    base {
        Modifier.background(Res.BrandColors.KhodedBlue.rgb)
            .color(Colors.White)
    }
    Breakpoint.ZERO {
        Modifier.padding(leftRight = 10.px, topBottom = 5.px)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
}

val PinkButtonVariant by ButtonStyle.addVariant {
    base {
        Modifier.background(Res.BrandColors.KhodedPink.rgb)
            .color(Colors.White)
    }

    Breakpoint.ZERO {
        Modifier.padding(leftRight = 10.px, topBottom = 5.px)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
}

val ReadMoreVariant by ButtonStyle.addVariant {
    base {
        Modifier
            .backgroundColor(Colors.Transparent)
    }
}
