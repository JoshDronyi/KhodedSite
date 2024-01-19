package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.styles.*
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun AboutLandingDisplay(modifier: Modifier = Modifier) = with(Pages.Story_Section.Landing) {
    val breakpoint = rememberBreakpoint()
    when (breakpoint) {
        Breakpoint.ZERO, Breakpoint.SM, Breakpoint.MD -> {
            CompactLandingDisplay(
                breakpoint = breakpoint,
                data = Pages.About_Section.Landing,
                modifier = modifier
                    .fillMaxWidth()
            )
        }

        Breakpoint.LG, Breakpoint.XL -> {
            LargeLandingDisplay(
                breakpoint = breakpoint,
                data = Pages.About_Section.Landing,
                modifier = modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CompactLandingDisplay(breakpoint: Breakpoint, data: Pages.Story_Section.Landing, modifier: Modifier) =
    with(data) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            OurTeamImage(image)
            OurTeamText(
                breakpoint = breakpoint,
                text = mainText,
                subText = subText
            )
        }
    }

@Composable
private fun LargeLandingDisplay(breakpoint: Breakpoint, data: Pages.Story_Section.Landing, modifier: Modifier) =
    with(data) {
        Box(
            modifier = modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            BackingCard(
                modifier = modifier
                    .fillMaxWidth(),
                variant = NoBorderBackingCardVariant,
                firstSection = {
                    OurTeamText(
                        breakpoint = breakpoint,
                        text = mainText,
                        subText = subText
                    )
                },
                secondSection = {
                    OurTeamImage(image)
                }
            )
        }
    }

@Composable
fun OurTeamText(breakpoint: Breakpoint, text: String, subText: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .backgroundColor(
                Colors.Aquamarine.copy(
                    alpha = when (breakpoint) {
                        Breakpoint.ZERO, Breakpoint.SM, Breakpoint.MD -> 20
                        Breakpoint.LG, Breakpoint.XL -> 100
                    }
                )
            )
            .color(
                when (breakpoint) {
                    Breakpoint.ZERO, Breakpoint.SM, Breakpoint.MD -> Color.white
                    Breakpoint.LG, Breakpoint.XL -> Color.black
                }
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(AboutLandingTextVariant)
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text(text)
        }
        P(
            attrs = BaseTextStyle.toModifier(SubTextVariant)
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text(subText)
        }
    }
}

@Composable
fun OurTeamImage(image: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            src = image,
            modifier = ImageStyle.toModifier()
                .borderRadius(0.px)
        )
    }
}