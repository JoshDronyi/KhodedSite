package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.components.composables.TeamSectionCard
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.styles.MainTextVariant
import com.probro.khoded.styles.TeamBioParagraphVaraiant
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun TeamSectionDisplay(modifier: Modifier = Modifier) = with(Pages.Story_Section.OurFounders) {
    Column(
        modifier = modifier
            .id(id)
            .fillMaxWidth(80.percent)
            .height(Height.FitContent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier(MainTextVariant)
                .textAlign(TextAlign.Center)
                .margin(bottom = 15.px)
                .toAttrs()
        ) {
            Text(title)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            estherBio.apply {
                FounderText(
                    founderName = name,
                    founderTitle = position,
                    founderBio = story,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            FounderImages(
                leftImage = estherBio.image,
                rigtImage = joshBio.image,
                modifier = Modifier.fillMaxWidth()
            )
            joshBio.apply {
                FounderText(
                    founderName = name,
                    founderTitle = position,
                    founderBio = story,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun FounderImages(
    leftImage: String,
    rigtImage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            src = leftImage,
            description = "Animated picture of founder",
            modifier = Modifier
        )
        Image(
            src = rigtImage,
            description = "Animated picture of founder",
            modifier = Modifier
        )

    }
}

val FounderTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
    }
}

@Composable
fun FounderText(
    founderName: String,
    founderTitle: String,
    founderBio: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(80.percent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            P(
                attrs = BaseTextStyle.toModifier(FounderTextVariant)
                    .toAttrs()
            ) {
                Text(founderName)
            }
            P(
                attrs = BaseTextStyle.toModifier(FounderTextVariant)
                    .toAttrs()
            ) {
                Text(founderTitle)
            }
        }
        P(
            attrs = BaseTextStyle.toModifier(FounderTextVariant)
                .fillMaxWidth(80.percent)
                .toAttrs()
        ) {
            Text(founderBio)
        }

    }

}

@Composable
fun TeamBioDisplay(
    bio: Pages.Story_Section.TeamBio,
    modifier: Modifier = Modifier
) = with(bio) {
    TeamSectionCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.px),
        variant = NoBorderBackingCardVariant,
        firstSection = {
            TeammateImage(image, name, position)
        },
        secondSection = {
            TeammateStory(bio.story)
        }
    )
}

@Composable
fun TeammateImage(
    image: String,
    name: String,
    position: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(60.percent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageBox(
                image = image,
                imageDesc = "Teammate bio pic",
                modifier = ImageStyle.toModifier()
                    .fillMaxWidth()
            )
            ImageText(name, position)
        }
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
            attrs = BaseTextStyle.toModifier(TeamBioParagraphVaraiant)
                .toAttrs()
        ) {
            Text(story)
        }
    }
}