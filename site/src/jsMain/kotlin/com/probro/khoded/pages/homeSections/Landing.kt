package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.models.SPACE_GROTESK
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.stevdza.san.kotlinbs.components.BSButton
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun LandingSectionDisplay(data: Pages.Home_Section.LandingData) = with(data) {
    Row(
        modifier = Modifier.fillMaxWidth(80.percent)
            .padding(topBottom = 20.px)
            .id(Pages.Home_Section.LandingData.id),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            P(
                attrs = BaseTextStyle.toModifier()
                    .fontSize(60.px)
                    .fontWeight(FontWeight.Bolder)
                    .fontFamily(SPACE_GROTESK)
                    .toAttrs()
            ) {
                Text(mainText)
            }
            P(
                attrs = BaseTextStyle.toModifier()
                    .fontSize(FontSize.Large)
                    .fontStyle(FontStyle.Italic)
                    .toAttrs()
            ) {
                Text(subText)
            }
            Row(
                modifier = Modifier.fillMaxWidth(70.percent)
                    .padding(leftRight = 30.px),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ButtonDisplay(startButton, BlueButtonVariant)
                ButtonDisplay(learnMoreButton, BlueButtonVariant)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(50.percent)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                src = image,
                description = "Landing section image.",
                modifier = Modifier
                    .fillMaxSize()
                    .objectFit(ObjectFit.Fill)
            )
        }
    }
}

@Composable
fun ButtonDisplay(state: ButtonState, variant: ComponentVariant, modifier: Modifier = Modifier) = with(state) {
    BSButton(
        modifier = ButtonStyle.toModifier(variant)
            .borderRadius(r = 16.px)
            .margin(right = 16.px)
            .padding(16.px)
            .then(modifier),
        text = buttonText,
        onClick = onButtonClick
    )
}
