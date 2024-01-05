package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.SingleBorderBackingCardVaiant
import com.probro.khoded.styles.*
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
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
            .height(Height.FitContent)
            .padding(topBottom = 20.px, leftRight = 10.px)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BackingCard(
            modifier = Modifier.fillMaxWidth()
                .zIndex(2),
            variant = SingleBorderBackingCardVaiant,
            firstSection = { ConsultationText(data) },
            secondSection = { ConsultationSteps(data) }
        )
        Box(
            modifier = Modifier
                .border(
                    width = 2.px,
                    style = LineStyle.Solid,
                    color = Colors.DarkGray
                )
                .borderRadius(r = 20.px)
                .background(Colors.White)
                .fillMaxWidth(75.percent)
                .translateY(ty = 140.px)
                .zIndex(1)
        )
    }
}

@Composable
fun ConsultationText(data: Pages.Home_Section.Consultation) = with(data) {
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
            modifier = ImageStyle.toModifier(ConsultationImageVariant)
        )
        P(
            attrs = BaseTextStyle.toModifier(MainTextVariant)
                .toAttrs()
        ) { Text(mainText) }
        P(
            attrs = BaseTextStyle.toModifier(SubTextVariant)
                .toAttrs()
        ) { Text(subText) }
        ButtonDisplay(startButton, PinkButtonVariant)
    }
}

@Composable
fun ConsultationSteps(data: Pages.Home_Section.Consultation) = with(data) {
    Column(
        modifier = Modifier.fillMaxWidth(70.percent)
            .padding(all = 20.px),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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

@Composable
fun StepDisplay(step: Pair<String, String>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(bottom = 20.px),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        P(
            attrs = BaseTextStyle.toModifier(ParagraphTitleVariant)
                .toAttrs()
        ) { Text(step.first) }
        P(
            attrs = BaseTextStyle.toModifier(ParagraphTextVariant)
                .toAttrs()
        ) { Text(step.second) }
    }
}

