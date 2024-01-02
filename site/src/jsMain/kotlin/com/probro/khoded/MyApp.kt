package com.probro.khoded

import androidx.compose.runtime.Composable
import com.probro.khoded.models.Res
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.components.style.toModifier
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

val BlueButtonVariant by ButtonStyle.addVariant {
    base {
        Modifier.background(Res.BrandColors.KhodedBlue.rgb)
            .color(Colors.White)
    }
}

val PinkButtonVariant by ButtonStyle.addVariant {
    base {
        Modifier.background(Res.BrandColors.KhodedPink.rgb)
            .color(Colors.White)
    }
}
