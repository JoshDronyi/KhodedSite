package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.BaseButtonTextVariant
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.models.Images
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.JobDescriptionVariant
import com.probro.khoded.styles.JobTitleVariant
import com.probro.khoded.styles.MainTextVariant
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
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
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val OpportunitiesBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.Purple,
                    to = Colors.MediumPurple
                )
            )
    }
}

@Composable
fun OpportunitiesSectionDisplay(
    footer: @Composable () -> Unit
) = with(Pages.Story_Section.JoinOurTeam) {
    Column(
        modifier = BackgroundStyle.toModifier(OpportunitiesBackgroundVariant)
            .id(id),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            JobPostings(title, positions)
            Image(
                src = Images.StoryPage.megaphone,
                description = "Megaphone",
                modifier = Modifier.fillMaxWidth(20.percent)
            )
        }
        footer()
    }
}

@Composable
fun JobPostings(title: String, positions: List<Pages.Story_Section.JobPosition>) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
    }
}

val JobPositionStyle by ComponentStyle {
    base {
        Modifier
            .padding(topBottom = 10.px, leftRight = 15.px)
            .border {
                width(1.px)
                color(Color.white)
                style(LineStyle.Solid)
            }
            .borderRadius(20.px)
            .color(Color.white)
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
            modifier = JobPositionStyle.toModifier()
                .fillMaxSize(80.percent),
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
                buttonVariant = BlueButtonVariant,
            ) { text ->
                P(
                    attrs = BaseTextStyle.toModifier(BaseButtonTextVariant)
                        .toAttrs()
                ) {
                    Text(text)
                }
            }
        }
    }
}