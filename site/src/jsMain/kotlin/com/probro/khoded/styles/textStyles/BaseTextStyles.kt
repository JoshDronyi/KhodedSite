package com.probro.khoded.styles.textStyles

import com.probro.khoded.models.KhodedColors
import com.probro.khoded.utils.Constants
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.style.KobwebComposeStyleSheet.attr
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.forms.InputStyle
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.px

sealed interface BaseTextKind : ComponentKind
sealed interface SectionKind : ComponentKind
sealed interface ImageKind : ComponentKind
sealed interface BackgroundKind : ComponentKind
sealed interface ColumnKind : ComponentKind
sealed interface RowKind : ComponentKind
sealed interface ContainerKind : ComponentKind
sealed interface TabKind : ComponentKind
sealed interface FooterKind : ComponentKind
sealed interface TextInputKind : ComponentKind
sealed interface FormKind : ComponentKind
sealed interface CTAKind : ComponentKind


val BaseTextStyle = CssStyle<BaseTextKind> {
    base {
        Modifier.fontSize(FontSize.Medium)
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
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XLarge)
    }
    Breakpoint.XL {
        Modifier.fontSize(FontSize.XXLarge)
    }
}

val MainTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontSize(48.px)
            .textAlign(TextAlign.Start)
            .fontWeight(FontWeight.Bolder)
            .padding(leftRight = 15.px)
            .textAlign(TextAlign.Start)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.XLarge)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.XL {
        Modifier.fontSize(36.px)
    }
}

val ImageStyle = CssStyle<ImageKind> {
    base {
        Modifier
            .fillMaxWidth()
            .borderRadius(20.px)
            .objectFit(ObjectFit.Fill)
    }
}
val BaseSectionStyles = CssStyle<SectionKind> {
    base {
        Modifier.fillMaxWidth()
            .minHeight(Constants.SECTION_HEIGHT.px)
            .borderBottom(width = 2.px, color = Color.black)
    }
}
val BackgroundStyle = CssStyle<BackgroundKind> {
    base {
        Modifier
            .fillMaxWidth()
            .height(Height.FitContent)
    }
}
val ColumnStyle = CssStyle<ColumnKind> {
    base {
        Modifier
            .fillMaxWidth()
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = (-5).px)
    }
    Breakpoint.SM {
        Modifier
            .translateY(ty = (-40).px)
    }
    Breakpoint.MD {
        Modifier
            .translateY(ty = (-30).px)
    }
    Breakpoint.LG {
        Modifier
            .translateY(ty = (-10).px)
    }
    Breakpoint.XL {
        Modifier
            .translateY(ty = 25.px)
    }
}
val BaseTabStyle = CssStyle<TabKind> {
    base {
        Modifier
            .borderRadius(20.px)
            .background(Color.white)
            .color(Color.black)
            .textOverflow(TextOverflow.Ellipsis)
            .overflowWrap(OverflowWrap.Anywhere)
            .minHeight(Height.FitContent)
            .padding(10.px)
            .margin(10.px)
    }
}
val BaseContainerStyle = CssStyle<ContainerKind> {
    base {
        Modifier
            .fillMaxWidth()
            .padding(20.px)
            .height(Height.Inherit)
    }
}

val FooterRow = CssStyle<FooterKind> {
    base {
        Modifier
    }
}
val BaseTextInputStyle = CssStyle<TextInputKind> {
    base {
        Modifier.color(Colors.DarkGray)
            .margin(bottom = 10.px)
    }
}
val BaseCTAStyle = CssStyle<CTAKind> {
    base {
        Modifier
            .width(Width.FitContent)
            .padding(leftRight = 10.px, topBottom = 5.px)
            .margin(topBottom = 10.px)
    }
}

val BaseFormStyle = CssStyle<FormKind> {
    base {
        Modifier
            .fillMaxWidth()
    }
    Breakpoint.ZERO
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
}
val BaseRowStyle = CssStyle<RowKind> {
    base {
        Modifier
            .fillMaxWidth()
            .margin(topBottom = 5.px)
    }
    Breakpoint.ZERO {
        Modifier
            .margin(topBottom = 5.px)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
}
val TextAreaVariant = InputStyle.addVariant {
    base {
        Modifier
            .color(Colors.Purple)
            .fontSize(FontSize.XLarge)
    }
    Breakpoint.ZERO {
        Modifier
            .padding(5.px)
            .fontSize(FontSize.XXSmall)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.XSmall)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Medium)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.XL {
        Modifier.fontSize(FontSize.Larger)
    }
}
val TextBoxVariant = InputStyle.addVariant {
    base {
        Modifier
            .color(Colors.Purple)
            .fontSize(FontSize.XLarge)
            .textAlign(TextAlign.Center)
    }

    Breakpoint.ZERO {
        Modifier
            .padding(5.px)
            .fontSize(FontSize.XXSmall)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.XSmall)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Medium)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.XL {
        Modifier.fontSize(FontSize.Larger)
    }
}

val BlueButtonVariant = ButtonStyle.addVariant {
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

val PinkButtonVariant = ButtonStyle.addVariant {
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

val ReadMoreButtonVariant = ButtonStyle.addVariant {
    base {
        Modifier
            .backgroundColor(Colors.Black.copy(alpha = 30))
    }

    hover {
        Modifier
            .background(Colors.Black.copy(alpha = 60))
    }
}

val BaseButtonTextVariant = BaseTextStyle.addVariant {
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

