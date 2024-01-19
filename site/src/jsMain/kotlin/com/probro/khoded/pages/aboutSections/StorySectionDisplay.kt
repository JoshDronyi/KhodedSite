package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.MainTextVariant
import com.probro.khoded.styles.StoryParagraphVariant
import com.probro.khoded.styles.StoryTitleVariant
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun StorySectionDisplay(baseModifier: Modifier) = with(Pages.Story_Section.OurStory) {
    Column(
        modifier = baseModifier
            .height(Height.MaxContent)
            .fillMaxWidth(80.percent)
            .border {
                width(1.px)
                color(Color.darkgray)
                style(LineStyle.Solid)
            }
            .borderRadius(20.px)
            .padding(20.px)
            .margin(topBottom = 20.px),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(MainTextVariant)
                .fillMaxWidth()
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text(mainText)
        }
        storySections.forEach {
            StoryParagraph(it)
        }
    }
}

@Composable
fun StoryParagraph(storySection: Pages.Story_Section.StorySection) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(StoryTitleVariant)
                .toAttrs()
        ) {
            Text(storySection.title)
        }
        P(
            attrs = BaseTextStyle.toModifier(StoryParagraphVariant)
                .toAttrs()
        ) {
            Text(storySection.text)
        }
    }
}
