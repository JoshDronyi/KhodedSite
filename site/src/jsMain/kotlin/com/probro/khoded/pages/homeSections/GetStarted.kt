package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.SingleBorderBackingCardVaiant
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Constants
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun GetStartedSectionDisplay(breakpoint: Breakpoint, data: Pages.Home_Section.GET_STARTED) = with(data) {
    Box(
        modifier = Modifier
            .id(id)
            .height(Constants.SECTION_HEIGHT.px)
            .fillMaxSize()
            .padding(20.px),
        contentAlignment = Alignment.Center
    ) {
        BackingCard(
            breakpoint = breakpoint,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.px),
            variant = SingleBorderBackingCardVaiant,
            firstSection = { GetStartedText(data) },
            secondSection = { GetStartedImage(data) }
        )
    }
}

@Composable
fun GetStartedText(data: Pages.Home_Section.GET_STARTED) = with(data) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
            .padding(leftRight = 16.px),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(48.px)
                .textAlign(TextAlign.Start)
                .toAttrs()
        ) {
            Text(mainText)
        }
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(20.px)
                .textAlign(TextAlign.Start)
                .toAttrs()
        ) {
            Text(subText)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(20.px),
            horizontalArrangement = Arrangement.Start
        ) {
            ButtonDisplay(startButton, BlueButtonVariant)
        }
    }
}

@Composable
fun GetStartedImage(data: Pages.Home_Section.GET_STARTED) = with(data) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
            .padding(10.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImageBox(
            image = image,
            imageDesc = "Depiction of a new client committing."
        )
    }
}