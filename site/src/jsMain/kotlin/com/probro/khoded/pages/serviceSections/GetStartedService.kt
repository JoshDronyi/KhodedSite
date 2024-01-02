package com.probro.khoded.pages.serviceSections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun GetStartedService(
    modifier: Modifier = Modifier,
    onCTAClicked: () -> Unit
) = with(Pages.Services_Section.GetStarted) {
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
            .fillMaxWidth(80.percent),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = shadowBoxModifier
                .fillMaxSize(80.percent)
                .background(Colors.White)
                .borderRadius(20.px)
                .padding(10.px)
                .zIndex(2),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(20.px),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                P(
                    attrs = BaseTextStyle.toModifier()
                        .fontSize(48.px)
                        .fontWeight(FontWeight.Bold)
                        .textAlign(TextAlign.Start)
                        .toAttrs()
                ) {
                    Text(mainText)
                }
                P(
                    attrs = BaseTextStyle.toModifier()
                        .fontSize(FontSize.Large)
                        .fontStyle(FontStyle.Italic)
                        .textAlign(TextAlign.Start)
                        .toAttrs()
                ) {
                    Text(subText)
                }
                ButtonDisplay(
                    signUpButtonState.copy(onButtonClick = onCTAClicked),
                    PinkButtonVariant
                )
            }
            Image(
                src = image,
                modifier = Modifier
                    .fillMaxWidth(70.percent)
                    .fillMaxHeight()
                    .objectFit(ObjectFit.Fill)
            )
        }
        Box(
            modifier = shadowBoxModifier.fillMaxHeight(80.percent)
                .fillMaxWidth(75.percent)
                .translateY(15.px)
                .zIndex(1)
        )
    }
}