package com.probro.khoded.styles.componentStyles

import com.probro.khoded.design.KhodedDesignSystem
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
// Fix for auto import

// NEW mobile-first container - doesn't conflict with existing
sealed interface ContainerKind : ComponentKind

val MobileFirstContainerStyle = CssStyle<ContainerKind> {
    base {
        Modifier
            .fillMaxWidth()
            .padding(KhodedDesignSystem.spacing.lg) // Mobile-first padding
            .height(Height.Inherit)
            .maxWidth(100.vw) // Prevent overflow
    }

    Breakpoint.SM {
        Modifier.padding(KhodedDesignSystem.spacing.xl2)
    }

    Breakpoint.MD {
        Modifier
            .padding(KhodedDesignSystem.spacing.xl3)
            .maxWidth(1200.px)
            // TODO: Add margin auto when CSS API stabilizes
    }

    Breakpoint.LG {
        Modifier.padding(KhodedDesignSystem.spacing.xl4)
    }
}

val NavigationContainerVariant = MobileFirstContainerStyle.addVariant {
    base {
        Modifier
            .margin(0.px)
            .padding(leftRight = KhodedDesignSystem.spacing.lg, topBottom = KhodedDesignSystem.spacing.md)
            .backgroundColor(KhodedDesignSystem.colors.backgroundPrimary)
            // TODO: Add box shadow when CSS API stabilizes
    }
    Breakpoint.MD {
        Modifier
            .padding(KhodedDesignSystem.spacing.xl3)
            .fillMaxWidth()
            // TODO: Add margin auto when CSS API stabilizes
    }

}

// Enhanced button variant (new, doesn't replace existing)
val EnhancedPrimaryButtonVariant = ButtonStyle.addVariant {
    base {
        Modifier
            .backgroundColor(KhodedDesignSystem.colors.primary)
            .color(KhodedDesignSystem.colors.textInverse)
            .padding(leftRight = KhodedDesignSystem.spacing.xl2, topBottom = KhodedDesignSystem.spacing.lg)
            .borderRadius(KhodedDesignSystem.borderRadius.md)
            .fontSize(KhodedDesignSystem.spacing.lg)
            .fontWeight(FontWeight.SemiBold)
            .minHeight(KhodedDesignSystem.spacing.touchTargetMin) // WCAG compliance
            .minWidth(KhodedDesignSystem.spacing.touchTargetMin)
            .cursor(Cursor.Pointer)
            .border(width = 0.px, style = LineStyle.None)
            .textAlign(TextAlign.Center)
            // TODO: Add transition when CSS API stabilizes
            .outline(width = 0.px, style = LineStyle.None)
    }

    Breakpoint.SM {
        Modifier
            .padding(leftRight = KhodedDesignSystem.spacing.xl3, topBottom = KhodedDesignSystem.spacing.xl)
            .fontSize(17.px)
    }

    Breakpoint.MD {
        Modifier
            .padding(leftRight = KhodedDesignSystem.spacing.xl4, topBottom = KhodedDesignSystem.spacing.xl)
            .fontSize(18.px)
    }

    hover {
        Modifier
            .backgroundColor(KhodedDesignSystem.colors.primaryDark)
            // TODO: Add transform scale when CSS API stabilizes
    }

    focus {
        Modifier
            .outline(width = 2.px, style = LineStyle.Solid, color = KhodedDesignSystem.colors.interactive)
            .outlineOffset(2.px)
    }
}

// TODO: Mobile-optimized form inputs - BaseTextInputStyle not yet available
// val MobileOptimizedInputVariant = BaseTextInputStyle.addVariant {
//     base {
//         Modifier
//             .fontSize(KhodedDesignSystem.spacing.lg) // Prevents iOS zoom
//             .minHeight(KhodedDesignSystem.spacing.touchTargetMin) // Touch target
//             .padding(KhodedDesignSystem.spacing.lg)
//             .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.borderSecondary)
//             .borderRadius(KhodedDesignSystem.borderRadius.md)
//             .color(KhodedDesignSystem.colors.textPrimary)
//     }
//
//     focus {
//         Modifier
//             .border(2.px, LineStyle.Solid, KhodedDesignSystem.colors.primary)
//             // TODO: Add box shadow when CSS API stabilizes
//     }
// }

// Text backing card for content over images
val TextBackingCardVariant = MobileFirstContainerStyle.addVariant {
    base {
        Modifier
            .backgroundColor(rgba(255, 255, 255, 0.85)) // Semi-transparent white
            .borderRadius(KhodedDesignSystem.borderRadius.lg)
            .padding(KhodedDesignSystem.spacing.xl)
            .margin(KhodedDesignSystem.spacing.lg)
            // TODO: Add box shadow when CSS API stabilizes
    }
    
    Breakpoint.SM {
        Modifier
            .padding(KhodedDesignSystem.spacing.xl2)
            .margin(KhodedDesignSystem.spacing.xl)
            .borderRadius(KhodedDesignSystem.borderRadius.xl)
    }
    
    Breakpoint.MD {
        Modifier
            .padding(KhodedDesignSystem.spacing.xl3)
            .margin(KhodedDesignSystem.spacing.xl2)
            .borderRadius(KhodedDesignSystem.borderRadius.xl2)
            .backgroundColor(rgba(255, 255, 255, 0.75)) // Slightly more transparent on larger screens
    }
    
    Breakpoint.LG {
        Modifier
            .padding(KhodedDesignSystem.spacing.xl4)
            .margin(KhodedDesignSystem.spacing.xl3)
            .backgroundColor(rgba(255, 255, 255, 0.7)) // Even more transparent on desktop
    }
}