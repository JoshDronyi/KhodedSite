package com.probro.khoded.styles.popups

import com.probro.khoded.models.KhodedColors
import com.probro.khoded.models.Res.TextStyle.FONT_FAMILY
import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.styles.components.BaseContainerStyle
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.extendedBy
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

// Popup component kinds
sealed interface PopUpKind : ComponentKind

// Base popup styles
val BasePopUpTextStyle = CssStyle<PopUpKind> {
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

val BasePopUpScreenVariant = BaseContainerStyle.addVariant {
    base {
        Modifier
            .maxWidth(MaxWidth.MaxContent)
            .borderRadius(20.px)
            .padding(10.px)
    }
}

// Popup image styles
val BasePopUpImageVariant = BaseImageStyle.addVariant {
    base {
        Modifier
    }
}

val BioImageVariant = BasePopUpImageVariant.extendedBy {
    base {
        Modifier
    }
    Breakpoint.ZERO {
        Modifier.maxHeight(20.vh)
    }
    Breakpoint.SM {
        Modifier.maxHeight(30.vh)
    }
    Breakpoint.MD {
        Modifier.maxHeight(40.vh)
    }
}

// Founder popup styles
val FounderPopUpVariant = BasePopUpScreenVariant.extendedBy {
    base {
        Modifier
            .backgroundColor(KhodedColors.PURPLE.rgb)
            .color(KhodedColors.POWDER_BLUE.rgb)
            .border {
                width(3.px)
                style(LineStyle.Solid)
                color(KhodedColors.POWDER_BLUE.rgb)
            }
    }
    Breakpoint.ZERO {
        Modifier.fillMaxWidth(90.percent)
    }
    Breakpoint.SM {
        Modifier.fillMaxWidth(80.percent)
    }
    Breakpoint.MD {
        Modifier.fillMaxWidth(75.percent)
    }
    Breakpoint.LG {
        Modifier.fillMaxWidth(50.percent)
    }
}

val FounderPopUpTextVariant = BasePopUpTextStyle.addVariant {
    base {
        Modifier
            .color(KhodedColors.POWDER_BLUE.rgb)
            .overflow(Overflow.Auto)
            .overflowWrap(OverflowWrap.BreakWord)
            .scrollBehavior(ScrollBehavior.Smooth)
            .scrollMargin(20.px)
            .styleModifier {
                property("scrollbar-color", "${Colors.BlueViolet.toRgb()} ${Colors.Transparent.toRgb()}")
                property("scrollbar-width", "thin")
            }
    }
}

val FounderTitleVariant = BasePopUpTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontStyle(FontStyle.Oblique)
            .fontWeight(FontWeight.Bolder)
            .textDecorationLine(TextDecorationLine.Underline)
    }
}

val FounderDescVariant = BasePopUpTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontStyle(FontStyle.Normal)
            .fontWeight(FontWeight.Normal)
    }
}

val FounderShortDescVariant = BasePopUpTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontStyle(FontStyle.Italic)
            .fontWeight(FontWeight.ExtraLight)
            .margin(topBottom = 20.px)
            .padding(leftRight = 10.px)
            .backgroundColor(Colors.BlueViolet.copy(alpha = 30))
            .borderRadius(15.px)
    }
}

// Messaging popup styles
val MessagingPopUpVariant = BasePopUpScreenVariant.extendedBy {
    base {
        Modifier
            .maxHeight(50.vh)
            .background(KhodedColors.LIGHT_BLUE.rgb)
            .padding(10.px)
            .border {
                width(2.px)
                color(KhodedColors.PURPLE.rgb)
                style(LineStyle.Groove)
            }
    }
    Breakpoint.ZERO {
        Modifier.fillMaxWidth(90.percent)
    }
    Breakpoint.SM {
        Modifier.fillMaxWidth(80.percent)
    }
    Breakpoint.MD {
        Modifier.fillMaxWidth(50.percent)
    }
}

val MessagingPopUpTextVariant = BasePopUpTextStyle.addVariant {
    base {
        Modifier.color(KhodedColors.PURPLE.rgb)
    }
}

// Story popup styles
val StoryPopUpVariant = BasePopUpScreenVariant.extendedBy {
    base {
        Modifier
    }
}

val StoryPopUpTextVariant = BasePopUpTextStyle.addVariant {
    base {
        Modifier
    }
}

// Popup button styles
val PopUpCTAVariant = ButtonStyle.addVariant {
    base {
        Modifier.background(Colors.Black.copy(alpha = 40))
    }
    hover {
        Modifier.background(Colors.Black.copy(alpha = 80))
    }
    Breakpoint.ZERO {
        Modifier.padding(leftRight = 10.px, topBottom = 5.px)
    }
}