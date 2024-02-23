package com.probro.khoded

import androidx.compose.runtime.Composable
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.styles.BaseTextStyle
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.style.KobwebComposeStyleSheet.attr
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.components.style.hover
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
        Modifier
            .fontSize(FontSize.Medium)
            .overflowWrap(OverflowWrap.Anywhere)
            .overflow(Overflow.Visible)
            .styleModifier {
                attr("text-wrap", "balance")
            }
//            .wordBreak(WordBreak.BreakAll)
            .height(Height.FitContent)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.XSmall)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Smaller)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.XL {
        Modifier.fontSize(FontSize.Larger)
    }
}

val BlueButtonVariant by ButtonStyle.addVariant {
    base {
        Modifier.background(KhodedColors.BLUE.rgb)
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
        Modifier.background(KhodedColors.PURPLE.rgb)
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
val PopUpCTAVariant by ButtonStyle.addVariant {
    base {
        Modifier.background(Colors.Black.copy(alpha = 40))
    }

    hover {
        Modifier.background(Colors.Black.copy(alpha = 80))
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
            .backgroundColor(Colors.Black.copy(alpha = 30))
    }

    hover {
        Modifier
            .background(Colors.Black.copy(alpha = 60))
    }
}
