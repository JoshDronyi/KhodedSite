package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.components.OptimizedImage
import com.probro.khoded.models.ButtonState
import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.styles.UnderlineImageVariant
import com.probro.khoded.styles.base.AccentTextVariant
import com.probro.khoded.styles.base.BaseTextStyle
import com.probro.khoded.styles.base.HighlightTextVariant
import com.probro.khoded.styles.base.MainTextVariant
import com.probro.khoded.styles.components.BaseBackgroundStyle
import com.probro.khoded.styles.components.BlueButtonVariant
import com.probro.khoded.styles.components.GradientBackgroundVariant
import com.probro.khoded.utils.*
import com.probro.khoded.utils.Constants.LENGTH_OF_TELLS
import com.stevdza.san.kotlinbs.components.BSButton
import com.varabyte.kobweb.compose.css.ScrollSnapStop
import com.varabyte.kobweb.compose.css.ScrollSnapType
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.ButtonKind
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
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
    onNavigate: (path: NavigationRoute) -> Unit,
    data: Pages.Home_Section.Landing
) = with(data) {
    data.apply {
        ctaButton = ctaButton.copy(onButtonClick = {
            println("Doing the navigation.")
            onNavigate(NavigationRoute.CONTACT)
        })
    }
    Column(
        modifier = BaseBackgroundStyle.toModifier(GradientBackgroundVariant),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(80.percent)
                .id(id),
            contentAlignment = Alignment.Center
        ) {
            OptimizedImage(
                src = mainImage,
                description = "Landing section image.",
                modifier = BaseImageStyle.toModifier()
            )
            LandingText(
                modifier = BaseTextStyle.toModifier(MainTextVariant)
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
    }
}

@Composable
private fun LandingSubTitle() = with(Pages.Home_Section.Landing) {
    P(
        attrs = BaseTextStyle.toModifier(HighlightTextVariant)
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
    val commaIndex: Int = remember { mainText.indexOf(',') }
    val firstLine = remember { mainText.substring(startIndex = 0, endIndex = commaIndex) }
    val goodSite = remember { mainText.substring(startIndex = commaIndex, endIndex = indexOfTells) }
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
        Column {
            Span(
                attrs = BaseTextStyle.toModifier(MainTextVariant)
                    .toAttrs()
            ) {
                Text(firstLine.trim())
                Text(goodSite.trimEnd())
            }
            OptimizedImage(
                src = underlineImage,
                description = "Blue underline",
                modifier = BaseImageStyle.toModifier(UnderlineImageVariant)
            )
        }
        Span(
            attrs = BaseTextStyle.toModifier(AccentTextVariant)
                .toAttrs()
        ) {
            Text(tells.trim())

        }
        Text(secondLine.trim())
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
