package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.models.ButtonState
import com.probro.khoded.styles.textStyles.*
import com.probro.khoded.utils.*
import com.probro.khoded.utils.Constants.LENGTH_OF_TELLS
import com.stevdza.san.kotlinbs.components.BSButton
import com.varabyte.kobweb.compose.css.ScrollSnapStop
import com.varabyte.kobweb.compose.css.ScrollSnapType
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.ButtonKind
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.CssStyleVariant
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun LandingSectionDisplay(
    header: @Composable () -> Unit,
    onNavigate: (page: PageSection) -> Unit,
    data: Pages.Home_Section.Landing
) = with(data) {
    data.apply {
        ctaButton = ctaButton.copy(onButtonClick = {
            println("Doing the navigation.")
            onNavigate(Pages.Home_Section.Consultation)
        })
    }
    Column(
        modifier = BackgroundStyle.toModifier(HomeLandingBackgroundVariant),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        header()
        Box(
            modifier = Modifier
                .fillMaxWidth(80.percent)
                .id(id),
            contentAlignment = Alignment.Center
        ) {
            Image(
                src = mainImage,
                description = "Landing section image.",
                modifier = ImageStyle.toModifier(LandingImageVariant)
            )
            LandingText(
                modifier = BaseTextStyle.toModifier(LandingTextVariant)
                    .align(Alignment.TopStart)
            )
        }
    }
}

@Composable
fun LandingText(
    modifier: Modifier = Modifier
) = with(Pages.Home_Section.Landing) {
    Column(
        modifier = modifier
            .scrollSnapStop(ScrollSnapStop.Always)
            .scrollSnapType(ScrollSnapType.Inherit),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        LandingTitle(
            mainText
        )
        LandingSubTitle()
        BSButton(
            id = "LandingCTA",
            modifier = ButtonStyle.toModifier(BlueButtonVariant)
                .translate(tx = 40.px, ty = (-50).px),
            text = ctaButton.buttonText,
            onClick = ctaButton.onButtonClick,
        )
//        ButtonDisplay(
//            ctaButton,
//            BlueButtonVariant,
//            modifier = Modifier
//                .fillMaxWidth(40.percent)
//                .textAlign(TextAlign.Center)
//        ) {
//            P(
//                attrs = BaseTextStyle.toModifier(BaseButtonTextVariant)
//                    .toAttrs()
//            ) {
//                Text(it)
//            }
//        }
    }
}

@Composable
private fun LandingSubTitle() = with(Pages.Home_Section.Landing) {
    P(
        attrs = BaseTextStyle.toModifier(HomeSubTitleVariant)
            .toAttrs()
    ) {
        Text(subText)
    }
}


@Composable
private fun LandingTitle(
    mainText: String,
    modifier: Modifier = Modifier
) = with(Pages.Home_Section.Landing) {
    var sectionPosition by remember { mutableStateOf(SectionPosition.IDLE) }

    val indexOfTells: Int = remember { mainText.indexOf("tells") }
    val firstLine = remember { mainText.substring(startIndex = 0, endIndex = indexOfTells) }
    val tells = remember { mainText.substring(indexOfTells, indexOfTells + LENGTH_OF_TELLS) }
    val secondLine = remember { mainText.substring(startIndex = indexOfTells + LENGTH_OF_TELLS) }

    P(
        attrs = modifier
            .id(TitleIDs.landingTitleID)
            .animation(
                fallInAnimation.toAnimation(
                    duration = 600.ms,
                    timingFunction = AnimationTimingFunction.Ease
                )
            )
            .toAttrs()
    ) {
        Column(
            modifier = ColumnStyle.toModifier(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            Span(
                attrs = BaseTextStyle.toModifier(firstLineVariant)
                    .toAttrs()
            ) {
                Text(firstLine.trim())
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Span(
                    attrs = BaseTextStyle.toModifier(PinkTextVariant)
                        .toAttrs()
                ) {
                    Text(tells.trim())
                }
                Span(
                    attrs = BaseTextStyle.toModifier(SecondLineVariant)
                        .toAttrs()
                ) {
                    Text(secondLine.trim())
                }
            }
        }
    }

    IsOnScreenObservable(
        sectionID = id
    ) {
        sectionPosition = it
        println("New Position for $id is $it")
    }
}

@Composable
fun ButtonDisplay(
    state: ButtonState,
    buttonVariant: CssStyleVariant<ButtonKind>? = null,
    modifier: Modifier = Modifier,
    clickableContent: @Composable (buttonText: String) -> Unit,
) = with(state) {
    Box(
        modifier = ButtonStyle.toModifier(buttonVariant)
            .then(modifier)
            .onClick { onButtonClick() },
        contentAlignment = Alignment.Center
    ) {
        clickableContent(buttonText)
    }
}
