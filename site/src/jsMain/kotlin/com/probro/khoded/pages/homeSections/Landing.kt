package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.probro.khoded.BaseButtonTextVariant
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.components.widgets.HomePageHeaderVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


val BackgroundStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .height(Height.FitContent)
    }
}

val HomeLandingBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.MediumPurple,
                    to = Colors.RebeccaPurple
                )
            )
    }
}
val ServicesBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(80.percent)
            .color(Color.white)
    }
}
val DesignBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(80.percent)
            .color(Colors.Black)
            .margin(top = 100.px, bottom = 200.px)
    }
}
val ConsultationBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.SkyBlue,
                    to = Colors.MediumPurple
                )
            )
    }
}


@Composable
fun LandingSectionDisplay(
    header: @Composable (variant: ComponentVariant?) -> Unit,
    data: Pages.Home_Section.LandingData
) = with(data) {
    Column(
        modifier = BackgroundStyle.toModifier(HomeLandingBackgroundVariant)
            .id(id),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        header(HomePageHeaderVariant)
        BackingCard(
            modifier = Modifier
                .height(Height.FitContent),
            variant = NoBorderBackingCardVariant,
            firstSection = { LandingText(data) },
            secondSection = { LandingImage() }
        )
    }
}

val HomeTitleVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .color(Color.white)
            .textAlign(TextAlign.Start)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.SM {
        Modifier.fontSize(48.px)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.XXLarge)
    }
}

val HomeSubTitleStyle by BaseTextStyle.addVariant {
    base {
        Modifier
            .textAlign(TextAlign.Start)
            .color(Color.white)
            .margin(bottom = 40.px)
//            .padding(topBottom = 15.px, leftRight = 30.px)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.Small)
//            .padding(leftRight = 10.px)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.Medium)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Large)
    }
}

const val LENGTH_OF_TELLS = 5

@Composable
fun LandingText(data: Pages.Home_Section.LandingData) = with(data) {
    val indexOfTells: Int = remember { mainText.indexOf("tells") }
    val firstLine = remember { mainText.substring(startIndex = 0, endIndex = indexOfTells) }
    val tells = remember { mainText.substring(indexOfTells, indexOfTells + LENGTH_OF_TELLS) }
    val secondLine = remember { mainText.substring(startIndex = indexOfTells + LENGTH_OF_TELLS) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(leftRight = 16.px, topBottom = 20.px),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        LandingTitle(firstLine, tells, secondLine)
        LandingSubTitle()
        ButtonDisplay(
            ctaButton,
            BlueButtonVariant,
            modifier = Modifier
                .width(Width.MaxContent)
                .textAlign(TextAlign.Center)
        ) {
            P(
                attrs = BaseTextStyle.toModifier(BaseButtonTextVariant)
                    .toAttrs()
            ) {
                Text(it)
            }
        }
    }
}

@Composable
private fun LandingSubTitle() = with(Pages.Home_Section.LandingData) {
    P(
        attrs = BaseTextStyle.toModifier(HomeSubTitleStyle)
            .toAttrs()
    ) {
        Text(subText)
    }
}

val BlueUnderLineVariant by ImageStyle.addVariant {
    base {
        Modifier.fillMaxWidth(50.percent)
            .maxHeight(MaxHeight.MinContent)
    }
    Breakpoint.ZERO {
        Modifier
    }
}

val LandingTitleStyle by ComponentStyle {
    base {
        Modifier
            .textAlign(TextAlign.Center)
    }

    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.SM {
        Modifier.fontSize(48.px)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.LG {
        Modifier.fontSize(48.px)
    }
    Breakpoint.LG {
        Modifier.fontSize(72.px)
    }
}
val firstLineVariant by LandingTitleStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
    }
}
val PinkTextVariant by LandingTitleStyle.addVariant {
    base {
        Modifier
            .minWidth(Width.MaxContent)
            .color(Colors.DeepPink)
            .fontStyle(FontStyle.Italic)
            .textAlign(TextAlign.End)
    }
}
val SecondLineVariant by LandingTitleStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .textAlign(TextAlign.Start)
    }
}

@Composable
private fun LandingTitle(
    firstLine: String,
    tells: String,
    secondLine: String
) = with(Pages.Home_Section.LandingData) {
    P(
        attrs = BaseTextStyle.toModifier(HomeTitleVariant)
            .fillMaxWidth()
            .toAttrs()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Span(
                attrs = LandingTitleStyle.toModifier(firstLineVariant)
                    .fillMaxWidth()
                    .toAttrs()
            ) {
                Text(firstLine.trim())
            }
            Image(
                src = underlineImage,
                description = "Blue underline",
                modifier = ImageStyle.toModifier(BlueUnderLineVariant)
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Span(
                    attrs = LandingTitleStyle.toModifier(PinkTextVariant)
                        .toAttrs()
                ) {
                    Text(tells.trim())
                }
                Span(
                    attrs = LandingTitleStyle.toModifier(SecondLineVariant)
                        .margin(left = 10.px)
                        .width(Width.FitContent)
                        .toAttrs()
                ) {
                    Text(secondLine.trim())
                }
            }
        }
    }
}

@Composable
fun LandingImage() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            src = Pages.Home_Section.LandingData.mainImage,
            description = "Landing section image.",
            modifier = ImageStyle.toModifier()
        )
    }
}

@Composable
fun ButtonDisplay(
    state: ButtonState,
    buttonVariant: ComponentVariant? = null,
    modifier: Modifier = Modifier,
    clickableContent: @Composable (buttonText: String) -> Unit,
) = with(state) {
    Box(
        modifier = ButtonStyle.toModifier(buttonVariant)
            .then(modifier)
            .onClick { onButtonClick() },
        contentAlignment = Alignment.Center
    ) {
        clickableContent(buttonText)
    }
}
