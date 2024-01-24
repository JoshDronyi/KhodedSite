package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.probro.khoded.BaseButtonStyle
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.components.widgets.HomePageHeaderVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
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
            .fontSize(72.px)
    }
}

val HomeSubTitleStyle by BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.XLarge)
            .textAlign(TextAlign.Start)
            .color(Color.white)
            .margin(bottom = 20.px)
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
            modifier = Modifier.fillMaxWidth(20.percent)
                .textAlign(TextAlign.Center)
        )
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

@Composable
private fun LandingTitle(
    firstLine: String,
    tells: String,
    secondLine: String
) = with(Pages.Home_Section.LandingData) {
    P(
        attrs = BaseTextStyle.toModifier(HomeTitleVariant)
            .toAttrs()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(firstLine)
            Image(
                src = underlineImage,
                description = "Blue underline",
                modifier = Modifier.fillMaxWidth(50.percent)
//                    .margin(topBottom = 0.px)
//                    .padding(topBottom = 0.px)
                    .translateY(ty = (-20).px)
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = Modifier
                    .translateY(ty = (-50).px),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Span(
                    attrs = Modifier
                        .color(Colors.DeepPink)
                        .fontStyle(FontStyle.Italic)
                        .margin(right = 20.px)
                        .toAttrs()
                ) {
                    Text(tells)
                }
                Text(secondLine)
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
fun ButtonDisplay(state: ButtonState, variant: ComponentVariant, modifier: Modifier = Modifier) = with(state) {
    Button(
        attrs = BaseButtonStyle.toModifier(variant)
            .then(modifier)
            .onClick { onButtonClick() }
            .toAttrs(),
    ) {
        Text(buttonText)
    }
}
