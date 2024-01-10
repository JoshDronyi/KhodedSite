package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.SingleBorderBackingCardVaiant
import com.probro.khoded.styles.*
import com.probro.khoded.utils.Navigator
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
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
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
            modifier = Modifier
                .zIndex(2),
            variant = SingleBorderBackingCardVaiant,
            firstSection = { ConsultationText(data) },
            secondSection = { ConsultationSteps(data) }
        )
    }
}

@Composable
fun ConsultationText(data: Pages.Home_Section.Consultation) = with(data) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(leftRight = 16.px, topBottom = 20.px)
            .translateY(ty = getTranslationFromBreakpoint()),
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
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(topBottom = 15.px),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ButtonDisplay(startButton.copy(
                onButtonClick = {
                    Navigator.navigateTo(Pages.Services_Section.GetStarted)
                }
            ), PinkButtonVariant)
        }
    }
}

@Composable
private fun getTranslationFromBreakpoint() = when (rememberBreakpoint()) {
    Breakpoint.ZERO, Breakpoint.SM -> (-50).px
    Breakpoint.MD -> (-80).px
    Breakpoint.LG, Breakpoint.XL -> (-100).px

}

@Composable
fun ConsultationSteps(data: Pages.Home_Section.Consultation) = with(data) {
    Column(
        modifier = Modifier.fillMaxWidth(getWidthFromBreakpoint(rememberBreakpoint()))
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

fun getWidthFromBreakpoint(breakpoint: Breakpoint) = when (breakpoint) {
    Breakpoint.ZERO -> 100.percent
    Breakpoint.SM -> 90.percent
    Breakpoint.MD -> 80.percent
    Breakpoint.LG -> 70.percent
    Breakpoint.XL -> 60.percent
}

@Composable
fun StepDisplay(step: Pair<String, String>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(bottom = 20.px),
        horizontalAlignment = Alignment.CenterHorizontally,
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

