/*
package com.probro.khoded.pages.serviceSections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.SingleBorderBackingCardVaiant
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.styles.MainTextVariant
import com.probro.khoded.styles.SubTextVariant
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun GetStartedService(
    modifier: Modifier = Modifier,
    data: Pages.Services_Section.GetStarted,
    onCTAClicked: () -> Unit
) = with(data) {
    val shadowBoxModifier = remember {
        Modifier
            .border {
                width(1.px)
                style(LineStyle.Solid)
                color(Colors.DarkGray)
            }
            .borderRadius(20.px)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Height.MaxContent),
        contentAlignment = Alignment.Center
    ) {
        BackingCard(
            modifier = shadowBoxModifier
                .zIndex(2),
            variant = SingleBorderBackingCardVaiant,
            firstSection = { GetStartedText(data, onCTAClicked) },
            secondSection = { GetStartedImage() }
        )
    }
}

@Composable
fun GetStartedText(data: Pages.Services_Section.GetStarted, onCTAClicked: () -> Unit) = with(data) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(20.px),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier(MainTextVariant)
                .toAttrs()
        ) {
            Text(mainText)
        }
        P(
            attrs = BaseTextStyle.toModifier(SubTextVariant)
                .toAttrs()
        ) {
            Text(subText)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ButtonDisplay(
                signUpButtonState.copy(onButtonClick = onCTAClicked),
                PinkButtonVariant,
            )
        }
    }
}

@Composable
fun GetStartedImage() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ImageBox(
            image = Pages.Services_Section.GetStarted.image,
            imageDesc = "Image of someone at work.",
            modifier = ImageStyle.toModifier()
        )
    }
}*/
