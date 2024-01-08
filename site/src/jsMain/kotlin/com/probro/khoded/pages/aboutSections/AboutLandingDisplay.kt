package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.MainTextVariant
import com.probro.khoded.styles.SubTextVariant
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.ObjectFit
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
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun AboutLandingDisplay(modifier: Modifier = Modifier) = with(Pages.About_Section.Landing) {
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
                OurTeamText(mainText, subText)
            },
            secondSection = {
                OurTeamImage(image)
            }
        )
    }
}

@Composable
fun OurTeamText(text: String, subText: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .backgroundColor(Colors.Aquamarine),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(MainTextVariant)
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
            modifier = Modifier
                .fillMaxSize()
                .objectFit(ObjectFit.Fill)
        )
    }
}