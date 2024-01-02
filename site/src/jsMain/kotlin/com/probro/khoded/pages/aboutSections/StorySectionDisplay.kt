package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun StorySectionDisplay(baseModifier: Modifier) = with(Pages.About_Section.Story) {
    Column(
        modifier = baseModifier
            .height(Height.MaxContent)
            .fillMaxWidth(80.percent)
            .padding(topBottom = 20.px),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(48.px)
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
fun StoryParagraph(storySection: Pages.About_Section.StorySection) {
    Column(
        modifier = Modifier
            .fillMaxWidth(80.percent)
            .padding(15.px),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fillMaxWidth()
                .textAlign(TextAlign.Start)
                .fontSize(FontSize.Large)
                .padding(0.px)
                .toAttrs()
        ) {
            Text(storySection.title)
        }
        P(
            attrs = BaseTextStyle.toModifier()
                .fillMaxWidth()
                .textAlign(TextAlign.Start)
                .fontSize(FontSize.Large)
                .padding(0.px)
                .toAttrs()
        ) {
            Text(storySection.text)
        }
    }
}
