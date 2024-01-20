package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.JobDescriptionVariant
import com.probro.khoded.styles.JobTitleVariant
import com.probro.khoded.styles.MainTextVariant
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun OpportunitiesSectionDisplay(
    footer: @Composable () -> Unit,
    baseModifier: Modifier
) = with(Pages.Story_Section.JoinOurTeam) {
    Column(
        modifier = baseModifier
            .id(id)
            .fillMaxWidth()
            .padding(topBottom = 15.px),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(MainTextVariant)
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text(title)
        }
        SimpleGrid(
            numColumns = numColumns(base = 1, sm = 2, md = 3),
            modifier = Modifier
                .height(Height.MinContent)
                .fillMaxWidth()
                .padding(topBottom = 20.px, leftRight = 10.px),
        ) {
            positions.forEach {
                JobPositionDisplay(it)
            }
        }
        footer()
    }
}

@Composable
fun JobPositionDisplay(position: Pages.Story_Section.JobPosition) = with(position) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(20.px)
            .height(Height.FitContent),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(80.percent)
                .padding(topBottom = 10.px, leftRight = 15.px)
                .border {
                    width(1.px)
                    color(Color.black)
                    style(LineStyle.Solid)
                }
                .borderRadius(20.px)
                .height(Height.FitContent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            P(
                attrs = BaseTextStyle.toModifier(JobTitleVariant)
                    .toAttrs()
            ) {
                Text(positionTitle)
            }
            P(
                attrs = BaseTextStyle.toModifier(JobDescriptionVariant)
                    .toAttrs()
            ) {
                Text(positionDesc)
            }
            ButtonDisplay(
                state = ButtonState(
                    buttonText = "Learn More",
                    onButtonClick = { }
                ),
                variant = BlueButtonVariant,
            )
        }
    }
}