package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.*
import com.probro.khoded.components.OptimizedImage
import com.probro.khoded.components.composables.popupscreen.PopUpScreen
import com.probro.khoded.models.ButtonState
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.animations.jobPostingShiftDownKeyFrames
import com.probro.khoded.styles.animations.jobPostingShiftUPKeyFrames
import com.probro.khoded.styles.base.BaseTextStyle
import com.probro.khoded.styles.components.BaseBackgroundStyle
import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.styles.components.BaseSectionStyle
import com.probro.khoded.styles.components.ReadMoreButtonVariant
import com.probro.khoded.styles.popups.FounderPopUpTextVariant
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.TitleIDs
import com.probro.khoded.utils.popUp.PopUpStateHolders
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.SimpleGridStyle
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.extendedBy
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val TeamSectionBackgroundVariant = BaseBackgroundStyle.addVariant {
    base {
        Modifier
            .background(Colors.RebeccaPurple)
            .padding(topBottom = 20.px, leftRight = 10.px)
            .height(Height.FitContent)
    }
}
val FounderTextStyle = BaseTextStyle.addVariant {
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

val FounderTitleVariant = FounderTextStyle.extendedBy {
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

val FounderNameVariant = FounderTextStyle.extendedBy {
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
val FounderPositionVariant = FounderTextStyle.extendedBy {
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
val FounderBioVariant = FounderTextStyle.extendedBy {
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

val FounderImageVariant = BaseImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
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
val FounderSectionVariant = BaseSectionStyle.addVariant {
    base {
        Modifier
            .width(Width.FitContent)
            .height(60.vh)
            .padding(10.px)
    }
}
val ImageSectionBacking = FounderSectionVariant.extendedBy {
    base {
        Modifier
            .padding(leftRight = 20.px)
    }
}

val CeoBackingSectionVariant = FounderSectionVariant.extendedBy {
    base {
        Modifier
            .backgroundColor(Colors.MediumPurple)
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

val CtoBioSectionVariant = FounderSectionVariant.extendedBy {
    base {
        Modifier
            .backgroundColor(KhodedColors.PURPLE.rgb)
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
    val popUpState by PopUpStateHolders.FounderPopUiStateHolder.popUpState.collectAsState()
    Box(
        modifier = BaseBackgroundStyle.toModifier(TeamSectionBackgroundVariant)
            .id(id),
        contentAlignment = Alignment.Center
    ) {
        FounderContentSection(
            modifier = Modifier
                .zIndex(1)
                .fillMaxWidth(80.percent)
        ) { founder ->
            PopUpStateHolders.FounderPopUiStateHolder.adjustPopUpText(founder)
        }

        with(popUpState) {
            PopUpScreen(
                popUpUIModel = this,
                textVariant = FounderPopUpTextVariant,
                modifier = Modifier
                    .visibility(if (isVisible) Visibility.Visible else Visibility.Hidden)
                    .zIndex(if (isVisible) 2 else 0)
            )
        }
    }
}

val FoundersGridVariant = SimpleGridStyle.addVariant {
    base {
        Modifier
    }
}

@Composable
fun FounderContentSection(
    modifier: Modifier = Modifier,
    onFounderBioClicked: (founder: Founders) -> Unit
) = with(Pages.Story_Section.OurFounders) {
    var state by remember { mutableStateOf(SectionPosition.IDLE) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        FoundersTitle(
            state,
            modifier = Modifier
                .id(TitleIDs.founderTitle)
                .align(Alignment.Start)
        )
        SimpleGrid(
            numColumns = numColumns(base = 1, md = 3),
            modifier = Modifier
                .fillMaxWidth(),
            variant = FoundersGridVariant
        ) {
            FounderText(
                estherBio,
                onFounderBioClicked = {
                    onFounderBioClicked(Founders.CEO)
                }
            )
            OptimizedImage(
                src = jointFoundersImage,
                description = "Animated image of Esther and Josh.",
                modifier = BaseImageStyle.toModifier(FounderImageVariant)
                    .align(Alignment.CenterHorizontally)
            )
            FounderText(
                teamBio = joshBio,
                onFounderBioClicked = {
                    onFounderBioClicked(Founders.CTO)
                }
            )
        }
        IsOnScreenObservable(
            sectionID = TitleIDs.founderTitle,
        ) {
            state = it
        }
    }
}

@Composable
fun FoundersTitle(
    state: SectionPosition,
    modifier: Modifier = Modifier
) = with(Pages.Story_Section.OurFounders) {
    val animation = when (state) {
        SectionPosition.ABOVE -> jobPostingShiftDownKeyFrames
        SectionPosition.ON_SCREEN -> jobPostingShiftUPKeyFrames
        SectionPosition.BELOW -> jobPostingShiftDownKeyFrames
        SectionPosition.IDLE -> jobPostingShiftUPKeyFrames
    }
    P(
        attrs = BaseTextStyle.toModifier(FounderTitleVariant)
            .then(modifier)
            .animation(animation.toAnimation(600.ms))
            .toAttrs()
    ) {
        Text(title)
    }
}

enum class Founders {
    CEO, CTO
}


val FounderTextContainer = FounderSectionVariant.extendedBy {
    base {
        Modifier
            .width(Width.FitContent)
    }
}

@Composable
fun FounderText(
    teamBio: Pages.Story_Section.TeamBio,
    onFounderBioClicked: () -> Unit
) = with(teamBio) {
    val mod = BaseSectionStyle.toModifier(FounderTextContainer)
        .fillMaxWidth()
    Column(
        modifier = mod,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        FounderNameAndPosition(name, position)
        FounderBioSection(founderType, desc, onFounderBioClicked)
    }
}

@Composable
fun FounderBioSection(
    founder: Founders,
    founderBio: String,
    onFounderBioClicked: () -> Unit
) {
    Column(
        modifier = BaseSectionStyle.toModifier(
            when (founder) {
                Founders.CEO -> CeoBackingSectionVariant
                Founders.CTO -> CtoBioSectionVariant
            }
        )
            .fillMaxWidth(80.percent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        P(
            attrs = BaseTextStyle.toModifier(FounderBioVariant)
                .weight(1)
                .toAttrs()
        ) {
            Text(founderBio.uppercase())
        }
        ButtonDisplay(
            state = ButtonState(
                buttonText = "Read More",
                onButtonClick = {
                    onFounderBioClicked()
                }
            ),
            buttonVariant = ReadMoreButtonVariant,
            modifier = Modifier.padding(topBottom = 10.px)
                .margin(top = 10.px)
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
            attrs = BaseTextStyle.toModifier(FounderNameVariant)
                .toAttrs()
        ) {
            Text(founderName.uppercase())
        }
        P(
            attrs = BaseTextStyle.toModifier(FounderPositionVariant)
                .toAttrs()
        ) {
            Text(founderTitle.uppercase())
        }
    }
}

val ReadMoreTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Medium)
            .color(Colors.White)
            .padding(leftRight = 15.px)
    }
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
            OptimizedImage(
                src = image,
                description = "Teammate bio pic",
                modifier = BaseImageStyle.toModifier()
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
            attrs = BaseTextStyle.toModifier()
                .toAttrs()
        ) {
            Text(story)
        }
    }
}