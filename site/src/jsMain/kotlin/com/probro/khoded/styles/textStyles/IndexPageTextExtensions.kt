package com.probro.khoded.styles.textStyles

import com.probro.khoded.models.KhodedColors
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.InputStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val LandingImageVariant = ImageStyle.addVariant {
    base {
        Modifier.fillMaxWidth(80.percent)
            .padding(top = 60.px)
    }
    Breakpoint.ZERO
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
//    Breakpoint.XL {
//        Modifier
//            .translateX(tx = 50.px)
//    }
}
val LandingTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(50.percent)
            .fillMaxHeight()
            .margin(bottom = 40.px)
    }
}

val HomeLandingBackgroundVariant = BackgroundStyle.addVariant {
    base {
        Modifier
            .padding(bottom = 40.px)
            .backgroundImage(
                linearGradient(
                    from = KhodedColors.PURPLE.rgb,
                    to = Colors.RebeccaPurple,
                    dir = LinearGradient.Direction.ToBottom,
                    interpolation = ColorInterpolationMethod.ProphotoRgb
                )
            )
    }
}
val ServicesBackgroundVariant = BackgroundStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(80.percent)
            .padding(topBottom = 40.px)
            .color(Color.white)
    }
}
val DesignBackgroundVariant = BaseSectionStyles.addVariant {
    base {
        Modifier
            .color(Colors.Black)
            .margin(bottom = 200.px)
            .padding(top = 50.px, bottom = 200.px)
            .height(Height.MaxContent)
    }
}
val ConsultationBackgroundVariant = BackgroundStyle.addVariant {
    base {
        Modifier
            .backgroundImage(
                linearGradient(
                    from = Colors.SkyBlue,
                    to = KhodedColors.PURPLE.rgb,
                    dir = LinearGradient.Direction.ToBottom,
                    interpolation = ColorInterpolationMethod.ProphotoRgb
                )
            )
    }
}

val ServicesTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .color(Colors.Black)
            .textAlign(TextAlign.Center)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(48.px)
    }
    Breakpoint.LG {
        Modifier.fontSize(72.px)
    }
    Breakpoint.XL
}
val DesignTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .color(Color.black)
    }

    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.SM {
        Modifier.fontSize(48.px)
    }
    Breakpoint.MD
    Breakpoint.LG {
        Modifier.fontSize(60.px)
    }
    Breakpoint.XL
}
val ConsultationTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
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
    Breakpoint.XL {
        Modifier.fontSize(60.px)
    }
}
val DesignSubTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .color(Color.black)
            .margin(topBottom = 40.px)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.XSmall)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.Medium)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XLarge)
    }
}

val HomeSubTitleVariant = BaseTextStyle.addVariant {
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
        Modifier.fontSize(FontSize.Medium)
            .margin(topBottom = 20.px)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XLarge)
            .translateX(40.px)
    }
}

val firstLineVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .textAlign(TextAlign.Center)
            .fontSize(FontSize.Small)
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = 70.px)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL {
        Modifier
            .translate(tx = 40.px, ty = 50.px)
            .fontSize(60.px)
    }
}
val PinkTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .maxWidth(MaxWidth.FitContent)
            .color(Colors.DeepPink)
            .fontStyle(FontStyle.Italic)
            .fontSize(FontSize.Medium)
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = 70.px)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL {
        Modifier
            .translate(tx = 40.px, ty = 50.px)
            .fontSize(FontSize.XXLarge)
    }
}
val SecondLineVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .width(Width.MaxContent)
            .textAlign(TextAlign.Start)
            .margin(left = 10.px)
            .fontSize(FontSize.Medium)
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = 70.px)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL {
        Modifier
            .translate(tx = 40.px, ty = 50.px)
            .fontSize(FontSize.XXLarge)
    }
}

val FreeTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontWeight(FontWeight.Bolder)
            .fontSize(FontSize.Large)
            .padding(leftRight = 5.px)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.Small)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
}
val ConsultationSectionVariant = BaseSectionStyles.addVariant {
    base {
        Modifier.fillMaxWidth(80.percent)
    }
}
val ConsultationImageVariant = ImageStyle.addVariant {
    base {
        Modifier
            .fillMaxSize()
            .translateY(ty = (-50).px)
    }
    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM {
        Modifier
    }
    Breakpoint.MD {
        Modifier
    }
    Breakpoint.LG {
        Modifier
    }
}
val ConsultationRequestVariant = BaseFormStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(60.percent)
            .fillMaxHeight()
            .padding(topBottom = 10.px)
    }
    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM
    Breakpoint.MD {
        Modifier.fillMaxWidth(70.percent)
    }
    Breakpoint.LG {
        Modifier
            .fillMaxWidth(60.percent)
    }
    Breakpoint.XL
}
val ConsultationTextBox = InputStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(80.percent)
    }
}

