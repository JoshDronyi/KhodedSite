package com.probro.khoded.styles.componentStyles

import com.probro.khoded.models.KhodedColors
import com.probro.khoded.styles.components.BaseTextInputStyle
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.focus
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.*

// NEW mobile-first container - doesn't conflict with existing
sealed interface ContainerKind : ComponentKind

val MobileFirstContainerStyle = CssStyle<ContainerKind> {
    base {
        Modifier
            .fillMaxWidth()
            .padding(16.px) // Mobile-first padding
            .height(Height.Inherit)
            .maxWidth(100.vw) // Prevent overflow
    }

    Breakpoint.SM {
        Modifier.padding(24.px)
    }

    Breakpoint.MD {
        Modifier
            .padding(32.px)
//            .maxWidth(1200.px)
            .margin(leftRight = autoLength)
    }

    Breakpoint.LG {
        Modifier.padding(40.px)
    }
}

val NavigationContainerVariant = MobileFirstContainerStyle.addVariant {
    base {
        Modifier
            .margin(0.px)
            .padding(leftRight = 1.cssRem, topBottom = 0.75.cssRem)
            .background(Color.white)
            .boxShadow(
                offsetX = 0.px,
                offsetY = 2.px,
                blurRadius = 4.px,
                color = rgba(0, 0, 0, 0.1)
            )
    }
    Breakpoint.MD {
        Modifier
            .padding(32.px)
            .maxWidth(100.vh)
//            .margin(leftRight = autoLength)
    }

}

// Enhanced button variant (new, doesn't replace existing)
val EnhancedBlueButtonVariant = ButtonStyle.addVariant {
    base {
        Modifier
            .background(KhodedColors.BLUE.rgb)
            .color(Colors.White)
            .padding(leftRight = 24.px, topBottom = 16.px)
            .borderRadius(12.px)
            .fontSize(16.px)
            .fontWeight(FontWeight.SemiBold)
            .minHeight(48.px) // WCAG compliance
            .minWidth(48.px)
            .cursor(Cursor.Pointer)
            .border(width = 0.px, style = LineStyle.None)
            .textAlign(TextAlign.Center)
            .transition(
                Transition.of("all", 0.2.s, TransitionTimingFunction.EaseInOut)
            )
            .outline(width = 0.px, style = LineStyle.None)
    }

    Breakpoint.SM {
        Modifier
            .padding(leftRight = 32.px, topBottom = 18.px)
            .fontSize(17.px)
    }

    Breakpoint.MD {
        Modifier
            .padding(leftRight = 40.px, topBottom = 20.px)
            .fontSize(18.px)
    }

    hover {
        Modifier
            .background(KhodedColors.HOVER_BLUE.rgb)
            .transform { scale(1.02) }
    }

    focus {
        Modifier
            .outline(width = 2.px, style = LineStyle.Solid, color = Color("#007bff"))
            .outlineOffset(2.px)
    }
}

// Mobile-optimized form inputs (new variant)
val MobileOptimizedInputVariant = BaseTextInputStyle.addVariant {
    base {
        Modifier
            .fontSize(16.px) // Prevents iOS zoom
            .minHeight(48.px) // Touch target
            .padding(16.px)
            .border {
                width(1.px)
                style(LineStyle.Solid)
                color(Color("#e1e5e9"))
            }
    }

    focus {
        Modifier
            .border {
                width(2.px)
                style(LineStyle.Solid)
                color(KhodedColors.BLUE.rgb)
            }
            .boxShadow(
                0.px, 0.px, 0.px, 3.px,
                color = KhodedColors.BLUE_HIGHLIGHT.rgb,
                inset = false
            )
    }
}