package com.probro.khoded.styles.componentStyles

import com.probro.khoded.styles.KhodedColors
import com.probro.khoded.styles.KhodedSpacing
import com.probro.khoded.styles.KhodedRadius
import com.probro.khoded.styles.KhodedShadows
import com.probro.khoded.styles.KhodedAnimations
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
            .padding(KhodedSpacing.lg) // Mobile-first padding
            .height(Height.Inherit)
            .maxWidth(100.vw) // Prevent overflow
    }

    Breakpoint.SM {
        Modifier.padding(KhodedSpacing.xl2)
    }

    Breakpoint.MD {
        Modifier
            .padding(KhodedSpacing.xl3)
            .maxWidth(1200.px)
            .margin(leftRight = org.jetbrains.compose.web.css.auto)
    }

    Breakpoint.LG {
        Modifier.padding(KhodedSpacing.xl4)
    }
}

val NavigationContainerVariant = MobileFirstContainerStyle.addVariant {
    base {
        Modifier
            .margin(0.px)
            .padding(leftRight = KhodedSpacing.lg, topBottom = KhodedSpacing.md)
            .background(KhodedColors.Background)
            .boxShadow(KhodedShadows.sm)
    }
    Breakpoint.MD {
        Modifier
            .padding(KhodedSpacing.xl3)
            .fillMaxWidth()
            .margin(leftRight = org.jetbrains.compose.web.css.auto)
    }

}

// Enhanced button variant (new, doesn't replace existing)
val EnhancedPrimaryButtonVariant = ButtonStyle.addVariant {
    base {
        Modifier
            .background(KhodedColors.Purple500.toString())
            .color(KhodedColors.TextInverse)
            .padding(leftRight = KhodedSpacing.xl2, topBottom = KhodedSpacing.lg)
            .borderRadius(KhodedRadius.md)
            .fontSize(KhodedSpacing.lg)
            .fontWeight(FontWeight.SemiBold)
            .minHeight(KhodedSpacing.touchTargetMin) // WCAG compliance
            .minWidth(KhodedSpacing.touchTargetMin)
            .cursor(Cursor.Pointer)
            .border(width = 0.px, style = LineStyle.None)
            .textAlign(TextAlign.Center)
            .transition(CSSTransition("all", KhodedAnimations.normal))
            .outline(width = 0.px, style = LineStyle.None)
    }

    Breakpoint.SM {
        Modifier
            .padding(leftRight = KhodedSpacing.xl3, topBottom = KhodedSpacing.xl)
            .fontSize(17.px)
    }

    Breakpoint.MD {
        Modifier
            .padding(leftRight = KhodedSpacing.xl4, topBottom = KhodedSpacing.xl)
            .fontSize(18.px)
    }

    hover {
        Modifier
            .background(KhodedColors.Purple600.toString())
            .transform { scale(1.02) }
    }

    focus {
        Modifier
            .outline(width = 2.px, style = LineStyle.Solid, color = KhodedColors.Focus)
            .outlineOffset(2.px)
    }
}

// Mobile-optimized form inputs (new variant)
val MobileOptimizedInputVariant = BaseTextInputStyle.addVariant {
    base {
        Modifier
            .fontSize(KhodedSpacing.lg) // Prevents iOS zoom
            .minHeight(KhodedSpacing.touchTargetMin) // Touch target
            .padding(KhodedSpacing.lg)
            .border(1.px, LineStyle.Solid, KhodedColors.Gray300)
            .borderRadius(KhodedRadius.md)
            .color(KhodedColors.TextPrimary)
    }

    focus {
        Modifier
            .border(2.px, LineStyle.Solid, KhodedColors.Purple500)
            .boxShadow(KhodedShadows.focus)
    }
}

// Text backing card for content over images
val TextBackingCardVariant = MobileFirstContainerStyle.addVariant {
    base {
        Modifier
            .background("rgba(255, 255, 255, 0.85)") // Semi-transparent white
            .borderRadius(KhodedRadius.lg)
            .padding(KhodedSpacing.xl)
            .margin(KhodedSpacing.lg)
            .boxShadow(KhodedShadows.lg)
    }
    
    Breakpoint.SM {
        Modifier
            .padding(KhodedSpacing.xl2)
            .margin(KhodedSpacing.xl)
            .borderRadius(KhodedRadius.xl)
    }
    
    Breakpoint.MD {
        Modifier
            .padding(KhodedSpacing.xl3)
            .margin(KhodedSpacing.xl2)
            .borderRadius(KhodedRadius.xl2)
            .background("rgba(255, 255, 255, 0.75)") // Slightly more transparent on larger screens
    }
    
    Breakpoint.LG {
        Modifier
            .padding(KhodedSpacing.xl4)
            .margin(KhodedSpacing.xl3)
            .background("rgba(255, 255, 255, 0.7)") // Even more transparent on desktop
    }
}