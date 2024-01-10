package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.components.composables.SingleBorderBackingCardVaiant
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
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun GetStartedSectionDisplay(data: Pages.Home_Section.GET_STARTED) = with(data) {
    Box(
        modifier = Modifier
            .id(id)
            .height(Height.FitContent)
            .fillMaxSize()
            .padding(topBottom = 20.px, leftRight = 10.px),
        contentAlignment = Alignment.Center
    ) {
        BackingCard(
            modifier = Modifier
                .padding(all = 10.px),
            variant = NoBorderBackingCardVariant,
            firstSection = { GetStartedText(data) },
            secondSection = { GetStartedImage(data) }
        )
    }
}

@Composable
fun GetStartedText(data: Pages.Home_Section.GET_STARTED) = with(data) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(leftRight = 16.px, topBottom = 20.px),
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
            modifier = Modifier.fillMaxWidth()
                .padding(topBottom = 15.px),
            horizontalArrangement = Arrangement.Start
        ) {
            ButtonDisplay(startButton, BlueButtonVariant)
        }
    }
}

@Composable
fun GetStartedImage(data: Pages.Home_Section.GET_STARTED) = with(data) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(10.px),
        contentAlignment = Alignment.Center
    ) {
        ImageBox(
            image = image,
            imageDesc = "Depiction of a new client committing.",
            modifier = ImageStyle.toModifier()
        )
    }
}