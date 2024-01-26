package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.ReadMoreVariant
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.components.composables.TeamSectionCard
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.styles.TeamBioParagraphVaraiant
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.*
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
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val TeamSectionBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .background(Colors.Purple)
            .padding(topBottom = 20.px, leftRight = 10.px)
    }
}

val FounderTitleVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(48.px)
            .textAlign(TextAlign.Start)
            .padding(leftRight = 20.px)
            .color(Color.white)
    }
}

val FounderTextStyle by ComponentStyle {
    base {
        Modifier
            .color(Colors.White)
            .fontFamily("Roboto")
            .fontSize(FontSize.Medium)
            .overflow(Overflow.Hidden)
            .overflowWrap(OverflowWrap.Anywhere)
            .textOverflow(TextOverflow.Ellipsis)
            .fillMaxHeight()
    }
}


val FounderNameVariant by FounderTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Medium)
    }
}
val FounderPositionVariant by FounderTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Medium)

    }
}
val FounderBioVariant by FounderTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Small)
    }
}

val FounderImageVariant by ImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .objectFit(ObjectFit.Fill)
    }
}
val FounderBacking by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth(60.percent)
            .padding(15.px)
    }
}
val ImageSectionBacking by FounderBacking.addVariant {
    base {
        Modifier
            .borderLeft {
                width(2.px)
                style(LineStyle.Solid)
                color(Colors.White)
            }
            .borderRight {
                width(2.px)
                style(LineStyle.Solid)
                color(Colors.White)
            }
            .padding(20.px)
    }
}
val IntroSectionBacking by FounderBacking.addVariant {
    base {
        Modifier
            .backgroundColor(Colors.MediumPurple)
            .height(Height.MaxContent)
            .borderRadius(20.px)
            .margin(topBottom = 10.px)
    }
}
val BioSectionBacking by FounderBacking.addVariant {
    base {
        Modifier
            .backgroundColor(Colors.RebeccaPurple)
            .borderRadius(20.px)
            .maxHeight(200.px)
    }
}


@Composable
fun TeamSectionDisplay(modifier: Modifier = Modifier) = with(Pages.Story_Section.OurFounders) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = BackgroundStyle.toModifier(TeamSectionBackgroundVariant)
                .id(id),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            P(
                attrs = BaseTextStyle.toModifier(FounderTitleVariant)
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
                        modifier = Modifier
                            .fillMaxWidth(80.percent)
                    )
                }
                FounderImages(
                    image = jointFoundersImage,
                    modifier = FounderBacking.toModifier(ImageSectionBacking)
                        .fillMaxWidth()
                )
                joshBio.apply {
                    FounderText(
                        founderName = name,
                        founderTitle = position,
                        founderBio = story,
                        modifier = Modifier
                            .fillMaxWidth(80.percent)
                    )
                }
            }
        }
    }
}

@Composable
fun FounderImages(
    image: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            src = image,
            description = "Animated picture of founder",
            modifier = ImageStyle.toModifier(FounderImageVariant)
        )
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
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = FounderBacking.toModifier(IntroSectionBacking)
                .fillMaxWidth(80.percent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            P(
                attrs = FounderTextStyle.toModifier(FounderNameVariant)
                    .toAttrs()
            ) {
                Text(founderName)
            }
            P(
                attrs = FounderTextStyle.toModifier(FounderPositionVariant)
                    .toAttrs()
            ) {
                Text(founderTitle)
            }
        }
        Column(
            modifier = FounderBacking.toModifier(BioSectionBacking)
                .fillMaxWidth(80.percent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            P(
                attrs = FounderTextStyle.toModifier(FounderBioVariant)
                    .toAttrs()
            ) {
                Text(founderBio)
            }
            ButtonDisplay(
                state = ButtonState(
                    buttonText = "Read More",
                    onButtonClick = {

                    }
                ),
                buttonVariant = ReadMoreVariant
            ) {
                P(
                    attrs = BaseTextStyle.toModifier(ReadMoreTextVariant)
                        .toAttrs()
                ) {
                    Text(it)
                }
            }
        }
    }
}

val ReadMoreTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Medium)
            .color(Colors.White)
            .padding(leftRight = 15.px)
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