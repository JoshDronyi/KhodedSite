package com.probro.khoded.styles.components

import com.probro.khoded.models.BaseTheme
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.utils.Constants
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.forms.InputStyle
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.extendedBy
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.*

// Component kinds
sealed interface SectionKind : ComponentKind
sealed interface BackgroundKind : ComponentKind
sealed interface ColumnKind : ComponentKind
sealed interface RowKind : ComponentKind
sealed interface ContainerKind : ComponentKind
sealed interface TabKind : ComponentKind
sealed interface FooterKind : ComponentKind
sealed interface TextInputKind : ComponentKind
sealed interface FormKind : ComponentKind
sealed interface CTAKind : ComponentKind
sealed interface DivKind : ComponentKind
sealed interface NavItemKind : ComponentKind

// Base layout styles - Mobile First Approach
val BaseSectionStyle = CssStyle<SectionKind> {
    base {
        Modifier
            .fillMaxWidth()
            .minHeight(60.vh) // More flexible than fixed pixel height
            .borderBottom(width = 1.px, color = Color.black)
            .margin(topBottom = 8.px)
            .padding(leftRight = 4.vw) // Viewport-based padding
    }

    Breakpoint.SM {
        Modifier
            .minHeight(50.vh)
            .borderBottom(width = 2.px, color = Color.black)
            .margin(topBottom = 12.px)
            .padding(leftRight = 6.vw)
    }

    Breakpoint.MD {
        Modifier
            .minHeight(Constants.SECTION_HEIGHT.px)
            .margin(topBottom = 16.px)
            .padding(leftRight = 8.vw)
    }

    Breakpoint.LG {
        Modifier.padding(leftRight = 10.vw)
    }
}

val BaseContainerStyle = CssStyle<ContainerKind> {
    base {
        Modifier
            .fillMaxWidth()
            .padding(12.px)
            .height(Height.Inherit)
    }

    Breakpoint.SM {
        Modifier.padding(16.px)
    }

    Breakpoint.MD {
        Modifier.padding(24.px)
    }

    Breakpoint.LG {
        Modifier.padding(32.px)
    }

    Breakpoint.XL {
        Modifier.padding(40.px)
    }
}

val BaseRowStyle = CssStyle<RowKind> {
    base {
        Modifier
            .fillMaxWidth()
            .margin(topBottom = 8.px)
            .gap(8.px)
    }

    Breakpoint.SM {
        Modifier
            .margin(topBottom = 12.px)
            .gap(12.px)
    }

    Breakpoint.MD {
        Modifier
            .margin(topBottom = 16.px)
            .gap(16.px)
    }

    Breakpoint.LG {
        Modifier
            .margin(topBottom = 20.px)
            .gap(20.px)
    }
}

val BaseColumnStyle = CssStyle<ColumnKind> {
    base {
        Modifier
            .fillMaxWidth()
            .gap(8.px)
    }

    Breakpoint.SM {
        Modifier.gap(12.px)
    }

    Breakpoint.MD {
        Modifier.gap(16.px)
    }

    Breakpoint.LG {
        Modifier.gap(20.px)
    }
}

// Background styles
val BaseBackgroundStyle = CssStyle<BackgroundKind> {
    base {
        Modifier
            .fillMaxWidth()
            .height(Height.FitContent)
            .backgroundSize(BackgroundSize.Cover)
            .backgroundRepeat(BackgroundRepeat.NoRepeat)
            .backgroundPosition(BackgroundPosition.Inherit)
    }
}

val GradientBackgroundVariant = BaseBackgroundStyle.addVariant {
    base {
        Modifier
            .padding(bottom = 20.px)
            .backgroundImage(
                linearGradient(
                    from = KhodedColors.PURPLE.rgb,
                    to = Colors.RebeccaPurple,
                    dir = LinearGradient.Direction.ToBottom,
                    interpolation = ColorInterpolationMethod.ProphotoRgb
                )
            )
    }

    Breakpoint.SM {
        Modifier.padding(bottom = 30.px)
    }

    Breakpoint.MD {
        Modifier.padding(bottom = 40.px)
    }
}

val ContactLandingBackgroundVariant = BaseBackgroundStyle.addVariant {
    base {
        Modifier
            .height(Height.FitContent)
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.SkyBlue,
                    to = Colors.WhiteSmoke,
                    interpolation = ColorInterpolationMethod.ProphotoRgb
                )
            )
    }
}

val ContactFooterBackgroundVariant = BaseBackgroundStyle.addVariant {
    base {
        Modifier
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.WhiteSmoke,
                    to = Colors.SkyBlue,
                    interpolation = ColorInterpolationMethod.ProphotoRgb
                )
            )
    }
}

// Form and input styles
val BaseFormStyle = CssStyle<FormKind> {
    base {
        Modifier
            .fillMaxWidth()
            .gap(12.px)
    }

    Breakpoint.SM {
        Modifier.gap(16.px)
    }

    Breakpoint.MD {
        Modifier.gap(20.px)
    }
}

val BaseTextInputStyle = CssStyle<TextInputKind> {
    base {
        Modifier
            .color(Colors.DarkGray)
            .margin(bottom = 8.px)
            .padding(8.px)
            .borderRadius(4.px)
            .fontSize(14.px)
    }

    Breakpoint.SM {
        Modifier
            .margin(bottom = 12.px)
            .padding(10.px)
            .fontSize(16.px)
    }

    Breakpoint.MD {
        Modifier
            .margin(bottom = 16.px)
            .padding(12.px)
            .fontSize(18.px)
    }
}

val TextAreaVariant = InputStyle.addVariant {
    base {
        Modifier
            .color(Colors.Purple)
            .fontSize(14.px)
            .padding(8.px)
            .minHeight(80.px)
    }

    Breakpoint.SM {
        Modifier
            .fontSize(16.px)
            .padding(10.px)
            .minHeight(100.px)
    }

    Breakpoint.MD {
        Modifier
            .fontSize(18.px)
            .padding(12.px)
            .minHeight(120.px)
    }

    Breakpoint.LG {
        Modifier
            .fontSize(20.px)
            .minHeight(140.px)
    }

    Breakpoint.XL {
        Modifier
            .fontSize(22.px)
            .minHeight(160.px)
    }
}

val TextBoxVariant = InputStyle.addVariant {
    base {
        Modifier
            .color(Colors.Purple)
            .fontSize(14.px)
            .textAlign(TextAlign.Center)
            .padding(8.px)
    }

    Breakpoint.SM {
        Modifier
            .fontSize(16.px)
            .padding(10.px)
    }

    Breakpoint.MD {
        Modifier
            .fontSize(18.px)
            .padding(12.px)
    }

    Breakpoint.LG {
        Modifier.fontSize(20.px)
    }

    Breakpoint.XL {
        Modifier.fontSize(22.px)
    }
}

// Button styles - Mobile First
val BlueButtonVariant = ButtonStyle.addVariant {
    base {
        Modifier
            .background(KhodedColors.BLUE.rgb)
            .color(Colors.White)
            .padding(leftRight = 16.px, topBottom = 8.px)
            .borderRadius(6.px)
            .fontSize(14.px)
            .minHeight(40.px)
            .cursor(Cursor.Pointer)
    }

    Breakpoint.SM {
        Modifier
            .padding(leftRight = 20.px, topBottom = 10.px)
            .fontSize(16.px)
            .minHeight(44.px)
    }

    Breakpoint.MD {
        Modifier
            .padding(leftRight = 24.px, topBottom = 12.px)
            .fontSize(18.px)
            .minHeight(48.px)
    }

    hover {
        Modifier.background(KhodedColors.HOVER_BLUE.rgb)
    }
}

val PurpleButtonVariant = ButtonStyle.addVariant {
    base {
        Modifier
            .background(KhodedColors.PURPLE.rgb)
            .color(Colors.White)
            .padding(leftRight = 16.px, topBottom = 8.px)
            .borderRadius(6.px)
            .fontSize(14.px)
            .minHeight(40.px)
            .cursor(Cursor.Pointer)
    }

    Breakpoint.SM {
        Modifier
            .padding(leftRight = 20.px, topBottom = 10.px)
            .fontSize(16.px)
            .minHeight(44.px)
    }

    Breakpoint.MD {
        Modifier
            .padding(leftRight = 24.px, topBottom = 12.px)
            .fontSize(18.px)
            .minHeight(48.px)
    }

    hover {
        Modifier.background(KhodedColors.HOVER_PURPLE.rgb)
    }
}

val ReadMoreButtonVariant = ButtonStyle.addVariant {
    base {
        Modifier
            .backgroundColor(Colors.Black.copy(alpha = 30))
            .padding(leftRight = 12.px, topBottom = 6.px)
            .borderRadius(4.px)
            .fontSize(14.px)
    }

    Breakpoint.SM {
        Modifier
            .padding(leftRight = 16.px, topBottom = 8.px)
            .fontSize(16.px)
    }

    hover {
        Modifier.background(Colors.Black.copy(alpha = 60))
    }
}

// Tab and card styles
val BaseTabStyle = CssStyle<TabKind> {
    base {
        Modifier
            .borderRadius(12.px)
            .background(Color.white)
            .color(Color.black)
            .textOverflow(TextOverflow.Ellipsis)
            .overflowWrap(OverflowWrap.BreakWord)
            .minHeight(MinHeight.FitContent)
            .padding(12.px)
            .margin(8.px)
            .boxShadow(0.px, 2.px, 4.px, color = Colors.Black.copy(alpha = 10))
    }

    Breakpoint.SM {
        Modifier
            .padding(16.px)
            .margin(12.px)
            .borderRadius(16.px)
    }

    Breakpoint.MD {
        Modifier
            .padding(20.px)
            .margin(16.px)
            .borderRadius(20.px)
    }

    Breakpoint.LG {
        Modifier
            .padding(24.px)
            .margin(20.px)
    }
}

val ServiceTabVariant = BaseTabStyle.addVariant {
    base {
        Modifier
            .background(color = BaseTheme.secondaryColor.rgb)
            .border(width = 2.px, style = LineStyle.Solid, color = BaseTheme.primaryColor.rgb)
    }

    Breakpoint.MD {
        Modifier.border(width = 3.px, style = LineStyle.Groove, color = BaseTheme.primaryColor.rgb)
    }

    Breakpoint.LG {
        Modifier.border(width = 4.px, style = LineStyle.Groove, color = BaseTheme.primaryColor.rgb)
    }
}

val BackingCardStyle = CssStyle<DivKind> {
    base {
        Modifier
            .fillMaxWidth(95.percent)
            .borderRadius(12.px)
            .color(Colors.Black)
            .boxShadow(
                offsetX = 0.px, offsetY = 2.px, blurRadius = 8.px,
                color = Colors.Black.copy(alpha = 15)
            )
    }

    Breakpoint.SM {
        Modifier
            .fillMaxWidth(92.percent)
            .borderRadius(16.px)
    }

    Breakpoint.MD {
        Modifier
            .fillMaxWidth(90.percent)
            .borderRadius(20.px)
    }
}

// Navigation styles
val NavItemStyle = CssStyle<NavItemKind> {
    base {
        Modifier
            .padding(8.px)
            .margin(4.px)
            .textAlign(TextAlign.Center)
            .borderRadius(4.px)
            .cursor(Cursor.Pointer)
    }

    Breakpoint.SM {
        Modifier
            .padding(10.px)
            .margin(6.px)
    }

    Breakpoint.MD {
        Modifier
            .padding(12.px)
            .margin(8.px)
    }
}

// Specialized container variants
val ContactSectionContainerVariant = BaseContainerStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.px)
    }

    Breakpoint.SM {
        Modifier.padding(20.px)
    }

    Breakpoint.MD {
        Modifier.padding(topBottom = 30.px)
    }

    Breakpoint.LG {
        Modifier.padding(topBottom = 40.px)
    }
}

val MessagingSectionContainerVariant = ContactSectionContainerVariant.extendedBy {
    base {
        Modifier.fillMaxWidth()
    }
}

val CompanyInfoContainerVariant = ContactSectionContainerVariant.extendedBy {
    base {
        Modifier
            .fillMaxHeight()
            .minHeight(40.vh)
    }

    Breakpoint.MD {
        Modifier.minHeight(50.vh)
    }
}

// Specialized section variants
val LandingSectionVariant = BaseRowStyle.addVariant {
    base {
        Modifier
            .minHeight(60.vh)
            .height(Height.FitContent)
    }

    Breakpoint.SM {
        Modifier.minHeight(55.vh)
    }

    Breakpoint.MD {
        Modifier.minHeight(50.vh)
    }
}

val MessagingSectionVariant = BaseRowStyle.addVariant {
    base {
        Modifier
            .height(Height.Inherit)
            .minHeight(40.vh)
            .margin(4.px)
    }

    Breakpoint.SM {
        Modifier
            .minHeight(45.vh)
            .margin(6.px)
    }

    Breakpoint.MD {
        Modifier
            .height(50.vh)
            .margin(8.px)
    }

    Breakpoint.LG {
        Modifier.margin(12.px)
    }
}

val ConsultationSectionVariant = BaseSectionStyle.addVariant {
    base {
        Modifier.fillMaxWidth(95.percent)
    }

    Breakpoint.SM {
        Modifier.fillMaxWidth(90.percent)
    }

    Breakpoint.MD {
        Modifier.fillMaxWidth(85.percent)
    }

    Breakpoint.LG {
        Modifier.fillMaxWidth(80.percent)
    }
}

// Form variants
val MessagingFormVariant = BaseFormStyle.addVariant {
    base {
        Modifier.fillMaxWidth(95.percent)
    }

    Breakpoint.SM {
        Modifier.fillMaxWidth(90.percent)
    }

    Breakpoint.MD {
        Modifier.fillMaxWidth(85.percent)
    }

    Breakpoint.LG {
        Modifier.fillMaxWidth(80.percent)
    }
}

val ConsultationRequestVariant = BaseFormStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(90.percent)
            .fillMaxHeight()
            .padding(topBottom = 16.px)
    }

    Breakpoint.SM {
        Modifier
            .fillMaxWidth(80.percent)
            .padding(topBottom = 20.px)
    }

    Breakpoint.MD {
        Modifier
            .fillMaxWidth(70.percent)
            .padding(topBottom = 24.px)
    }

    Breakpoint.LG {
        Modifier.fillMaxWidth(60.percent)
    }
}

// Input variants for contact forms
val ClientInfoTextBoxVariant = BaseTextInputStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .margin(topBottom = 8.px)
    }

    Breakpoint.SM {
        Modifier.margin(topBottom = 10.px)
    }

    Breakpoint.MD {
        Modifier.margin(topBottom = 12.px)
    }
}

val ClientInfoTextAreaVariant = ClientInfoTextBoxVariant.extendedBy {
    base {
        Modifier
            .fillMaxWidth()
            .minHeight(80.px)
    }

    Breakpoint.SM {
        Modifier.minHeight(100.px)
    }

    Breakpoint.MD {
        Modifier.minHeight(120.px)
    }
}

val ConsultationTextBoxVariant = InputStyle.addVariant {
    base {
        Modifier.fillMaxWidth(95.percent)
    }

    Breakpoint.SM {
        Modifier.fillMaxWidth(90.percent)
    }

    Breakpoint.MD {
        Modifier.fillMaxWidth(85.percent)
    }

    Breakpoint.LG {
        Modifier.fillMaxWidth(80.percent)
    }
}

// Footer
val FooterRowStyle = CssStyle<FooterKind> {
    base {
        Modifier
            .padding(16.px)
            .gap(12.px)
    }

    Breakpoint.SM {
        Modifier
            .padding(20.px)
            .gap(16.px)
    }

    Breakpoint.MD {
        Modifier
            .padding(24.px)
            .gap(20.px)
    }
}

// CTA styles
val BaseCTAStyle = CssStyle<CTAKind> {
    base {
        Modifier
            .width(Width.FitContent)
            .padding(leftRight = 16.px, topBottom = 8.px)
            .margin(topBottom = 12.px)
            .borderRadius(6.px)
            .cursor(Cursor.Pointer)
    }

    Breakpoint.SM {
        Modifier
            .padding(leftRight = 20.px, topBottom = 10.px)
            .margin(topBottom = 16.px)
    }

    Breakpoint.MD {
        Modifier
            .padding(leftRight = 24.px, topBottom = 12.px)
            .margin(topBottom = 20.px)
    }
}