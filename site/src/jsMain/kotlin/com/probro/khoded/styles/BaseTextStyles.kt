package com.probro.khoded.styles.base

import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.style.KobwebComposeStyleSheet.attr
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

// Base text component kinds
sealed interface BaseTextKind : ComponentKind
sealed interface HeadingKind : ComponentKind
sealed interface BodyKind : ComponentKind

// Core base text style - foundation for all text components
val BaseTextStyle = CssStyle<BaseTextKind> {
    base {
        Modifier
            .fontSize(FontSize.Small)
            .fontFamily("Times New Roman")
            .padding(0.px)
            .margin(0.px)
            .textAlign(TextAlign.Center)
            .textOverflow(TextOverflow.Ellipsis)
            .overflow(Overflow.Hidden)
            .overflowWrap(OverflowWrap.Anywhere)
            .fillMaxWidth()
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Smaller)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Small)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Medium)
    }
}

// Heading base styles
val HeadingStyle = CssStyle<HeadingKind> {
    base {
        Modifier.fillMaxWidth()
    }
}

val BodyStyle = CssStyle<BodyKind> {
    base {
        Modifier
            .fillMaxSize()
            .backgroundColor(Color.white)
    }
}

// Primary text variants - for main content
val MainTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(20.px) // Base size for mobile
            .textAlign(TextAlign.Start)
            .fontWeight(FontWeight.Bolder)
            .padding(leftRight = 8.px) // Reduced padding for mobile
            .lineHeight(1.4) // Better line spacing for readability
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(18.px)
            .padding(leftRight = 12.px)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(22.px)
            .padding(leftRight = 16.px)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(28.px)
            .padding(leftRight = 20.px)
    }
    Breakpoint.LG {
        Modifier
            .fontSize(32.px)
            .padding(leftRight = 24.px)
    }
    Breakpoint.XL {
        Modifier
            .fontSize(36.px)
            .padding(leftRight = 24.px)
    }
}
// Title variants for different sections
val SectionTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .color(Colors.Black)
            .textAlign(TextAlign.Center)
            .fontWeight(FontWeight.Bold)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(24.px) // Much smaller for mobile
    }
    Breakpoint.SM {
        Modifier.fontSize(32.px)
    }
    Breakpoint.MD {
        Modifier.fontSize(40.px)
    }
    Breakpoint.LG {
        Modifier.fontSize(48.px)
    }
    Breakpoint.XL {
        Modifier.fontSize(56.px)
    }
}
val SubTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(80.percent)
            .textAlign(TextAlign.Start)
            .color(Color.white)
            .margin(topBottom = 30.px)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.XXSmall)
            .translateY(ty = 20.px)
            .fillMaxWidth(60.percent)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.XSmall)
            .margin(topBottom = 10.px)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Medium)
            .margin(topBottom = 20.px)
    }
    Breakpoint.LG {
        Modifier
            .fontSize(FontSize.XLarge)
            .translateX(40.px)
    }
}

// Specialized text variants
val AccentTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .maxWidth(MaxWidth.FitContent)
            .color(Colors.DeepPink)
            .fontStyle(FontStyle.Italic)
            .fontSize(FontSize.Medium)
    }
    Breakpoint.ZERO {
        Modifier.translateY(ty = 70.px)
    }
    Breakpoint.XL {
        Modifier
            .translate(tx = 40.px, ty = 50.px)
            .fontSize(FontSize.XXLarge)
    }
}

val HighlightTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontWeight(FontWeight.Bolder)
            .fontSize(FontSize.Large)
            .padding(leftRight = 5.px)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Small)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Medium)
    }
}

// Button text variant
val ButtonTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Medium)
            .overflowWrap(OverflowWrap.Anywhere)
            .overflow(Overflow.Visible)
            .styleModifier {
                attr("text-wrap", "balance")
            }
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

// Team/Bio related text variants
val TeamBioParagraphVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .padding(0.px)
            .margin(0.px)
            .textAlign(TextAlign.Center)
    }
}

val JobTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontSize(FontSize.Large)
            .fontWeight(FontWeight.Bold)
            .height(Height.FitContent)
            .margin(10.px)
    }
}

val JobDescriptionVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontSize(FontSize.Large)
            .height(Height.FitContent)
    }
}

// Contact page specific text variants
val ContactPromptTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(48.px)
            .textAlign(TextAlign.Start)
            .fontWeight(FontWeight.Bold)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.LG {
        Modifier.fontSize(48.px)
    }
}

val CompanyContactTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .padding(0.px)
            .margin(0.px)
            .fontSize(FontSize.Larger)
            .fontWeight(FontWeight.Bolder)
            .textAlign(TextAlign.End)
    }
}