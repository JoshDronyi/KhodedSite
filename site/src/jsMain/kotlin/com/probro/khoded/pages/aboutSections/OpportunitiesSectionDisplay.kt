package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.probro.khoded.models.Images
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.JobDescriptionVariant
import com.probro.khoded.styles.JobTitleVariant
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
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
            modifier = Modifier
                .fillMaxWidth(),
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

val PostingsTitleVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.XXLarge)
            .color(Color.white)
            .fontWeight(FontWeight.Bold)
            .textAlign(TextAlign.Start)
    }
}

@Composable
fun JobPostings(title: String, positions: List<Pages.Story_Section.JobPosition>) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val fullyDisplayedItems by remember { mutableStateOf(mutableListOf<String>()) }
        P(
            attrs = BaseTextStyle.toModifier(PostingsTitleVariant)
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
                JobPositionDisplay(
                    position = it,
                    shouldShow = fullyDisplayedItems.contains(it.positionTitle)
                ) { title ->
                    if (fullyDisplayedItems.contains(title)) {
                        fullyDisplayedItems.remove(title)
                    } else {
                        fullyDisplayedItems.add(title)
                    }
                }
            }
        }
    }
}

val JobPositionStyle by ComponentStyle {
    base {
        Modifier
            .padding(20.px)
            .margin(20.px)
            .borderRadius(20.px)
            .background(Color.white)
            .height(Height.FitContent)
    }
}

val JobPositionContainerStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .padding(20.px)
            .height(Height.Inherit)
    }
}

@Composable
fun JobPositionDisplay(
    position: Pages.Story_Section.JobPosition,
    shouldShow: Boolean,
    onClick: (title: String) -> Unit
) = with(position) {
    Column(
        modifier = JobPositionStyle.toModifier()
            .fillMaxSize(90.percent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(JobTitleVariant)
                .onClick { onClick(positionTitle) }
                .toAttrs()
        ) {
            Text(positionTitle)
        }
        if (shouldShow) {
            P(
                attrs = BaseTextStyle.toModifier(JobDescriptionVariant)
                    .toAttrs()
            ) {
                Text(positionDesc)
            }
        }
    }
}