package com.probro.khoded.components.composables.popupscreen

import androidx.compose.runtime.Composable
import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.styles.components.BaseContainerStyle
import com.probro.khoded.styles.popups.*
import com.probro.khoded.utils.IDs
import com.probro.khoded.utils.popUp.FounderText
import com.probro.khoded.utils.popUp.PopUpScreenUIModel
import com.stevdza.san.kotlinbs.components.BSModal
import com.stevdza.san.kotlinbs.components.BSProgress
import com.stevdza.san.kotlinbs.models.ModalSize
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.CssStyleVariant
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun PopUpScreen(
    popUpUIModel: PopUpScreenUIModel,
    textVariant: CssStyleVariant<PopUpKind>? = null,
    modifier: Modifier = Modifier
) = with(popUpUIModel) {
    BSModal(
        id = IDs.PopUpID,
        modifier = BaseContainerStyle.toModifier(BasePopUpScreenVariant),
        title = "",
        body = {
            when (val model = this@with) {
                is PopUpScreenUIModel.FounderHighlightPopUpUIModel -> FounderPopUpDisplay(
                    textVariant = textVariant,
                    model = model
                )

                is PopUpScreenUIModel.MessagingPopUpUiModel -> MessagingStateDisplay(
                    textVariant = textVariant,
                    model = model
                )
            }
        },
        negativeButtonText = "Close",
        positiveButtonText = buttonState.buttonText,
        closableOutside = true,
        centered = true,
        size = ModalSize.Large,
        onNegativeButtonClick = { modifier::onClick },
        onPositiveButtonClick = buttonState.onButtonClick,
    )
}

@Composable
fun FounderPopUpDisplay(
    textVariant: CssStyleVariant<PopUpKind>?,
    model: PopUpScreenUIModel.FounderHighlightPopUpUIModel
) = with(model) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(Height.Inherit)
            .padding(topBottom = 15.px),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        FounderTextDisplay(
            text,
            BasePopUpTextStyle.toModifier(textVariant)
                .fillMaxWidth(80.percent)
        )
        image?.let {
            Image(
                src = it,
                description = "Animated image of founder.",
                modifier = BaseImageStyle.toModifier()
            )
        }
    }
}

@Composable
fun FounderTextDisplay(founderText: FounderText, modifier: Modifier) {
    Column(
        modifier = modifier
            .height(Height.Inherit)
            .fillMaxHeight(80.percent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        P(
            attrs = BasePopUpTextStyle.toModifier(FounderTitleVariant)
                .toAttrs()
        ) {
            Text(founderText.titleText)
        }
        P(
            attrs = BasePopUpTextStyle.toModifier(FounderShortDescVariant)
                .toAttrs()
        ) {
            Text(founderText.shortDesc)
        }
        P(
            attrs = BasePopUpTextStyle.toModifier(FounderDescVariant)
                .toAttrs()
        ) {
            Text(founderText.desc)
        }
    }
}


@Composable
fun MessagingStateDisplay(
    textVariant: CssStyleVariant<PopUpKind>?,
    model: PopUpScreenUIModel.MessagingPopUpUiModel
) = with(model) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            BSProgress(
                modifier = Modifier.fillMaxWidth(),
                label = "The label part.",
                striped = true,
                stripedAnimated = true,
                height = 20.vh
            )
        }
        P(
            attrs = BasePopUpTextStyle.toModifier(textVariant)
                .fillMaxWidth(80.percent)
                .toAttrs()
        ) {
            Text(promptText)
        }
    }
}