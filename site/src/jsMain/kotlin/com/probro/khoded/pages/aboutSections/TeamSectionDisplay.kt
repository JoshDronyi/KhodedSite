package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun TeamSectionDisplay(modifier: Modifier = Modifier) = with(Pages.About_Section.Team) {
    Column(
        modifier = modifier
            .fillMaxWidth(80.percent)
            .height(Height.FitContent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(48.px)
                .fontWeight(FontWeight.Bold)
                .toAttrs()
        ) {
            Text(mainText)
        }
        teambios.forEach { bio ->
            TeamBioDisplay(bio)
        }
    }
}

@Composable
fun TeamBioDisplay(
    bio: Pages.About_Section.TeamBio,
    modifier: Modifier = Modifier
) = with(bio) {
    Row(
        modifier = modifier
            .fillMaxWidth(80.percent)
            .padding(15.px),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeammateImage(image, name, position)
        TeammateStory(bio.story)
    }
}

@Composable
fun TeammateImage(
    image: String,
    name: String,
    position: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(40.percent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            src = image,
            modifier = Modifier
                .objectFit(ObjectFit.Fill)
                .borderRadius(r = 20.px)
        )
        ImageText(name, position)
    }
}

@Composable
fun ImageText(name: String, position: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(FontSize.Medium)
                .fontStyle(FontStyle.Normal)
                .toAttrs()
        ) {
            Text(name)
        }
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(FontSize.Medium)
                .fontStyle(FontStyle.Italic)
                .toAttrs()
        ) {
            Text(position)
        }
    }
}

@Composable
fun TeammateStory(story: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(FontSize.Large)
                .padding(20.px)
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text(story) //TODO: CHANGE TO ACTUAL STORY VALUE
        }
    }
}