package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Constants
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun ConsultationSectionDisplay(data: Pages.Home_Section.Consultation) = with(data) {
    Box(
        modifier = Modifier
            .id(id)
            .height(Constants.SECTION_HEIGHT.px)
            .fillMaxWidth(80.percent),
        contentAlignment = Alignment.Center
    ) {
        val cardModifier = Modifier
            .border(
                width = 2.px,
                style = LineStyle.Solid,
                color = Colors.DarkGray
            )
            .borderRadius(r = 20.px)
            .background(Colors.White)
        Row(
            modifier = cardModifier.fillMaxWidth(80.percent)
                .zIndex(2),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(leftRight = 20.px)
                    .translateY(ty = (-100).px),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                ImageBox(
                    image = image,
                    imageDesc = "Depiction of scheduling a consultation",
                    modifier = Modifier
                        .fillMaxWidth(60.percent)
                )
                P(
                    attrs = BaseTextStyle.toModifier()
                        .fontSize(36.px)
                        .textAlign(TextAlign.Start)
                        .fontWeight(FontWeight.Bolder)
                        .toAttrs()
                ) { Text(mainText) }
                P(
                    attrs = BaseTextStyle.toModifier()
                        .fontSize(FontSize.Medium)
                        .textAlign(TextAlign.Start)
                        .toAttrs()
                ) { Text(subText) }
                ButtonDisplay(startButton, PinkButtonVariant)
            }
            Column(
                modifier = Modifier.fillMaxWidth(50.percent)
                    .padding(all = 20.px),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                steps.forEachIndexed { index, step ->
                    StepDisplay(
                        step = step,
                        modifier = Modifier.borderBottom(
                            width = if (index < steps.lastIndex) 2.px else 0.px,
                            style = LineStyle.Solid,
                            color = Colors.DarkGray
                        )
                    )
                }
            }
        }
        Box(
            modifier = cardModifier.fillMaxWidth(75.percent)
                .fillMaxHeight(45.percent)
                .translateY(ty = 140.px)
                .zIndex(1)
        )
    }
}

@Composable
fun StepDisplay(step: Pair<String, String>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(bottom = 20.px),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center)
                .fontSize(30.px)
                .toAttrs()
        ) { Text(step.first) }
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(16.px)
                .textAlign(TextAlign.Start)
                .color(Colors.DarkGray)
                .toAttrs()
        ) { Text(step.second) }
    }
}

