package com.probro.khoded.styles.componentStyles

import com.probro.khoded.models.KhodedColors
import com.probro.khoded.styles.textStyles.BaseContainerStyle
import com.probro.khoded.styles.textStyles.ImageStyle
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.extendedBy
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

val PopUpScreenVariant = BaseContainerStyle.addVariant {
    base {
        Modifier
            .maxWidth(MaxWidth.MaxContent)
            .borderRadius(20.px)
            .padding(10.px)
    }
}
val PopUpImageVariant = ImageStyle.addVariant {
    base {
        Modifier
    }
}
val BioImageVariant = PopUpImageVariant.extendedBy {
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

val FounderPopUpVariant = PopUpScreenVariant.extendedBy {
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
        Modifier
            .fillMaxWidth(90.percent)
    }
    Breakpoint.SM {
        Modifier.fillMaxWidth(80.percent)
    }
    Breakpoint.MD {
        Modifier
            .fillMaxWidth(75.percent)
    }
    Breakpoint.LG {
        Modifier
            .fillMaxWidth(50.percent)
    }

}

val FounderPopUpTextVariant = PopUpTextStyle.addVariant {
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

val MessagingPopUpTextVariant = PopUpTextStyle.addVariant {
    base {
        Modifier
            .color(KhodedColors.PURPLE.rgb)
    }
}
val MessagingPopUpVariant = PopUpScreenVariant.extendedBy {
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
val StoryPopUpTextVariant = PopUpTextStyle.addVariant {
    base {
        Modifier
    }
}
val StoryPopUpVariant = PopUpScreenVariant.extendedBy {
    base {
        Modifier
    }
}
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
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
}
val FounderTitleVariant = PopUpTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontStyle(FontStyle.Oblique)
            .fontWeight(FontWeight.Bolder)
            .textDecorationLine(TextDecorationLine.Underline)
    }
}
val FounderDescVariant = PopUpTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontStyle(FontStyle.Normal)
            .fontWeight(FontWeight.Normal)
    }
}
val FounderShortDescVariant = PopUpTextStyle.addVariant {
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

