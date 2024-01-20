package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.BaseButtonStyle
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.components.widgets.HomePageHeaderVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.models.SPACE_GROTESK
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.styles.MainTextVariant
import com.probro.khoded.styles.SubTextVariant
import com.probro.khoded.utils.Pages
import com.stevdza.san.kotlinbs.components.BSButton
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
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
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


val BackgroundStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .padding(topBottom = 20.px, leftRight = 10.px)
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
                .padding(topBottom = 20.px, leftRight = 10.px)
                .height(Height.FitContent),
            variant = NoBorderBackingCardVariant,
            firstSection = { LandingText(data) },
            secondSection = { LandingImage() }
        )
    }
}

@Composable
fun LandingText(data: Pages.Home_Section.LandingData) = with(data) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(leftRight = 16.px, topBottom = 20.px),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(MainTextVariant)
                .fontFamily(SPACE_GROTESK)
                .toAttrs()
        ) {
            Text(mainText)
        }
        P(
            attrs = BaseTextStyle.toModifier(SubTextVariant)
                .fontStyle(FontStyle.Italic)
                .toAttrs()
        ) {
            Text(subText)
        }
        ButtonDisplay(
            ctaButton,
            BlueButtonVariant,
        )
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
    BSButton(
        modifier = BaseButtonStyle.toModifier(variant)
            .then(modifier),
        text = buttonText,
        onClick = onButtonClick
    )
}
