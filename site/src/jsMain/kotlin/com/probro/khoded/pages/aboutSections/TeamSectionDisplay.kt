package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.*
import com.probro.khoded.ReadMoreVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.components.composables.TeamSectionCard
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.styles.TeamBioParagraphVaraiant
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.fallInAnimation
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
import com.varabyte.kobweb.silk.components.animation.toAnimation
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val TeamSectionBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .background(Colors.Purple)
            .padding(topBottom = 20.px, leftRight = 10.px)
    }
}
val FounderTextStyle by ComponentStyle {
    base {
        Modifier
            .padding(0.px)
            .margin(0.px)
            .color(Colors.White)
            .fontFamily("Roboto")
            .fontSize(FontSize.Medium)
            .textOverflow(TextOverflow.Ellipsis)
            .overflow(Overflow.Clip)
            .overflowWrap(OverflowWrap.Anywhere)
    }
}

val FounderTitleVariant by FounderTextStyle.addVariant {
    base {
        Modifier
            .fontSize(48.px)
            .textAlign(TextAlign.Start)
            .padding(topBottom = 20.px)
            .color(Color.white)
    }
    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM {
        Modifier
    }
    Breakpoint.MD {
        Modifier
    }
    Breakpoint.LG {
        Modifier
    }
}

val FounderNameVariant by FounderTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Medium)
            .padding(topBottom = 10.px)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.Smaller)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.Small)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Medium)
    }
    Breakpoint.LG {
        Modifier
            .fontSize(FontSize.Large)
    }
}
val FounderPositionVariant by FounderTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Medium)

    }

    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.XSmall)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.Smaller)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Small)
    }
    Breakpoint.LG {
        Modifier
            .fontSize(FontSize.Medium)
    }
}
val FounderBioVariant by FounderTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Small)
    }

    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.XSmall)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.Smaller)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Small)
    }
    Breakpoint.LG {
        Modifier
            .fontSize(FontSize.Medium)
    }
}

val FounderImageVariant by ImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(60.percent)
            .height(70.vh)
            .objectFit(ObjectFit.Contain)
    }

    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM {
        Modifier
    }
    Breakpoint.MD {
        Modifier
    }
    Breakpoint.LG {
        Modifier
    }
}
val FounderBacking by ComponentStyle {
    base {
        Modifier
            .width(Width.FitContent)
            .height(Height.FitContent)
            .padding(leftRight = 10.px, topBottom = 20.px)
    }
}
val ImageSectionBacking by FounderBacking.addVariant {
    base {
        Modifier
            .padding(leftRight = 20.px)
    }
}

val CeoBackingSectionVariant by FounderBacking.addVariant {
    base {
        Modifier
            .backgroundColor(Colors.RebeccaPurple)
            .borderRadius(
                topLeft = 0.px,
                bottomLeft = 0.px,
                topRight = 20.px,
                bottomRight = 20.px,
            )
            .maxHeight(200.px)
            .borderRight {
                width(2.px)
                style(LineStyle.Solid)
                color(Colors.White)
            }
    }
}

val CtoBioSectionVariant by FounderBacking.addVariant {
    base {
        Modifier
            .backgroundColor(Colors.RebeccaPurple)
            .borderRadius(
                topLeft = 20.px,
                bottomLeft = 20.px,
                topRight = 0.px,
                bottomRight = 0.px,
            )
            .maxHeight(200.px)
            .borderLeft {
                width(2.px)
                style(LineStyle.Solid)
                color(Colors.White)
            }
    }
}


@Composable
fun TeamSectionDisplay() = with(Pages.Story_Section.OurFounders) {
    var state by remember { mutableStateOf(SectionPosition.IDLE) }
    Column(
        modifier = BackgroundStyle.toModifier(TeamSectionBackgroundVariant)
            .id(id),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        IsOnScreenObservable(id) {
            state = it
        }
        P(
            attrs = FounderTextStyle.toModifier(FounderTitleVariant)
                .fillMaxWidth(80.percent)
                .position(Position.Relative)
                .animation(
                    fallInAnimation.toAnimation(
                        duration = 600.ms,
                        timingFunction = AnimationTimingFunction.Ease,
                        direction = AnimationDirection.Normal,
                        playState = if (state == SectionPosition.ON_SCREEN) AnimationPlayState.Running
                        else AnimationPlayState.Paused
                    )
                )
                .toAttrs()
        ) {
            Text(title)
        }
        BackingCard(
            modifier = Modifier
                .fillMaxWidth(),
            variant = NoBorderBackingCardVariant,
            firstSection = {
                estherBio.apply {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.End
                    ) {
                        FounderText(
                            founderName = name,
                            founderTitle = position,
                            founderBio = story,
                            founder = founderType,
                        )
                        Image(
                            src = image,
                            description = "Animated picture of founder",
                            modifier = ImageStyle.toModifier(FounderImageVariant)
                                .fillMaxWidth(40.percent)
                        )
                    }
                }
            },
            secondSection = {
                joshBio.apply {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            src = image,
                            description = "Animated picture of founder",
                            modifier = ImageStyle.toModifier(FounderImageVariant)
                                .fillMaxWidth(40.percent)
                        )
                        FounderText(
                            founderName = name,
                            founderTitle = position,
                            founderBio = story,
                            founder = founderType,
                        )
                    }
                }
            }
        )
    }
}

enum class Founders {
    CEO, CTO
}


val FounderTextContainer by FounderBacking.addVariant {
    base {
        Modifier
            .width(Width.FitContent)
    }
}

@Composable
fun FounderText(
    founderName: String,
    founderTitle: String,
    founderBio: String,
    founder: Founders,
) {
    val mod = FounderBacking.toModifier(FounderTextContainer)
        .fillMaxWidth()
        .height(Height.Inherit)
    Column(
        modifier = when (founder) {
            Founders.CEO ->
                mod
                    .borderRight {
                        width(2.px)
                        style(LineStyle.Solid)
                        color(Colors.White)
                    }

            Founders.CTO ->
                mod
                    .borderLeft {
                        width(2.px)
                        style(LineStyle.Solid)
                        color(Colors.White)
                    }
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        FounderNameAndPosition(founderName, founderTitle)
        FounderBioSection(founder, founderBio)
    }
}

@Composable
fun FounderBioSection(founder: Founders, founderBio: String) {
    Column(
        modifier = FounderBacking.toModifier(
            when (founder) {
                Founders.CEO -> CeoBackingSectionVariant
                Founders.CTO -> CtoBioSectionVariant
            }
        )
            .fillMaxWidth(80.percent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = FounderTextStyle.toModifier(FounderBioVariant)
                .toAttrs()
        ) {
            Text(founderBio.uppercase())
        }
        ButtonDisplay(
            state = ButtonState(
                buttonText = "Read More",
                onButtonClick = {
                    println("LEAVE THEM WANTING MOOOOOOOREEE!!!")
                }
            ),
            buttonVariant = ReadMoreVariant,
            modifier = Modifier.padding(topBottom = 10.px)
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

@Composable
fun FounderNameAndPosition(founderName: String, founderTitle: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .margin(topBottom = 15.px),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = FounderTextStyle.toModifier(FounderNameVariant)
                .toAttrs()
        ) {
            Text(founderName.uppercase())
        }
        P(
            attrs = FounderTextStyle.toModifier(FounderPositionVariant)
                .toAttrs()
        ) {
            Text(founderTitle.uppercase())
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