package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.components.OptimizedImage
import com.probro.khoded.components.composables.MessageArea
import com.probro.khoded.components.composables.TextBox
import com.probro.khoded.models.ButtonState
import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.styles.animations.jobPostingShiftDownKeyFrames
import com.probro.khoded.styles.animations.jobPostingShiftUPKeyFrames
import com.probro.khoded.styles.base.AccentTextVariant
import com.probro.khoded.styles.base.BaseTextStyle
import com.probro.khoded.styles.base.SectionTitleVariant
import com.probro.khoded.styles.components.*
import com.probro.khoded.utils.*
import com.probro.khoded.utils.Constants.FREE_TEXT
import com.probro.khoded.utils.popUp.PopUpStateHolders
import com.stevdza.san.kotlinbs.components.showModalOnClick
import com.varabyte.kobweb.compose.css.ScrollSnapStop
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.InputStyle
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Form
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


@Composable
fun ConsultationSectionDisplay(
    data: Pages.Home_Section.Consultation,
) = with(data) {
    Column(
        modifier = BaseBackgroundStyle.toModifier(GradientBackgroundVariant),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ConsultationDisplaySection(mainImage = mainImage)
        QuoteSection(quotes, subText)
    }
}


@Composable
fun ConsultationDisplaySection(
    mainImage: String,
    modifier: Modifier = Modifier,
) = with(Pages.Home_Section.Consultation) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        OptimizedImage(
            src = mainImage,
            description = "Message Bubbles",
            modifier = BaseImageStyle.toModifier()
                .zIndex(0)
        )
        ConsultationTextSection(
            mainText = mainText,
            ctaButton = ctaButton,
            modifier = BaseFormStyle.toModifier(ConsultationRequestVariant)
                .id(id)
                .align(Alignment.BottomStart)
                .scrollSnapStop(ScrollSnapStop.Always)
        )
    }
}


@Composable
fun QuoteSection(
    quotes: String,
    subText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(80.percent),
        contentAlignment = Alignment.Center
    ) {
        OptimizedImage(
            src = quotes,
            description = "Quotes",
            modifier = Modifier
                .fillMaxWidth()
        )
        P(
            attrs = BaseTextStyle.toModifier()
                .fillMaxWidth(80.percent)
                .color(Color.white)
                .toAttrs()
        ) {
            Text(subText)
        }
    }
}

@Composable
fun ConsultationTextSection(
    mainText: String,
    ctaButton: ButtonState,
    modifier: Modifier = Modifier
) {
    val formState by ConsultationStateHolder.formState.collectAsState()
    Column(
        modifier = modifier
            .zIndex(1),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConsultationTitle(mainText)
        MessagingSection(
            modifier = BaseFormStyle.toModifier(MessagingFormVariant),
            placeHolder = formState.placeholderData
        )
        ConsultationRequestButton(
            ctaButton = ButtonState(
                buttonText = ctaButton.buttonText,
                onButtonClick = {
                    println("Clicked consultation")
                    ConsultationStateHolder.onMessageSend(formState.messageData.message)
                }
            ),
            modifier = BaseCTAStyle.toModifier()
                .align(Alignment.CenterHorizontally)
                .showModalOnClick(IDs.PopUpID)
        )
    }
    LaunchedEffect(formState.stage) {
        formState.stage.let {
            println("Going to stage $it")
            PopUpStateHolders.MessagingPopUpStateHolder.adjustPopUpText(it)
        }
    }
}

@Composable
private fun ConsultationRequestButton(
    ctaButton: ButtonState,
    modifier: Modifier = Modifier
) {
    ButtonDisplay(
        state = ctaButton,
        buttonVariant = PurpleButtonVariant,
        modifier = modifier
    ) { text ->
        val index = remember { text.indexOf(FREE_TEXT, ignoreCase = true) }
        val before = remember { text.substring(0, index) }
        val after = remember { text.substring(index + FREE_TEXT.length) }
        P(
            attrs = BaseCTAStyle.toModifier()
                .toAttrs()
        ) {
            Span {
                Text(before)
            }
            Span(
                attrs = BaseTextStyle.toModifier(AccentTextVariant)
                    .toAttrs()
            ) {
                Text(FREE_TEXT.uppercase())
            }
            Span {
                Text(after)
            }
        }
    }
}

@Composable
fun ConsultationTitle(mainText: String) {
    val splitIndex = remember { mainText.indexOf("about") }
    val firstText = remember { mainText.substring(0, splitIndex) }
    val secondText = remember { mainText.substring(splitIndex) }

    var position by remember { mutableStateOf(SectionPosition.IDLE) }
    println("Consultation title.... ${position.name}")
    val animation = when (position) {
        SectionPosition.ABOVE -> jobPostingShiftDownKeyFrames
        SectionPosition.ON_SCREEN -> jobPostingShiftUPKeyFrames
        SectionPosition.BELOW -> jobPostingShiftDownKeyFrames
        SectionPosition.IDLE -> jobPostingShiftUPKeyFrames
    }

    P(
        attrs = BaseTextStyle.toModifier(SectionTitleVariant)
            .id(TitleIDs.consultationTitleID)
            .animation(animation.toAnimation(600.ms))
            .toAttrs()
    ) {
        Span(
            attrs = Modifier
                .color(Color.purple)
                .margin(right = 10.px)
                .toAttrs()
        ) {
            Text(firstText)
        }
        Text(secondText)
    }
    IsOnScreenObservable(
        sectionID = TitleIDs.consultationTitleID,
    ) {
        position = it
        println("New Position for ${Pages.Home_Section.Consultation.id} is $it")
    }
}

@Composable
fun MessagingSection(
    placeHolder: Pages.Home_Section.ConsultationRequestUIModel,
    modifier: Modifier = Modifier
) {
    Form(
        attrs = modifier.toAttrs()
    ) {
        Row(
            modifier = BaseRowStyle.toModifier(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBox(
                placeholder = placeHolder.fullName,
                required = true,
                modifier = InputStyle.toModifier(TextBoxVariant)
            ) {
                ConsultationStateHolder.updateName(it)
            }
            TextBox(
                placeholder = placeHolder.email,
                required = true,
                modifier = InputStyle.toModifier(TextBoxVariant)
            ) {
                ConsultationStateHolder.updateEmail(it)
            }
        }
        MessageArea(
            placeholder = placeHolder.projectSynopsis
        ) {
            ConsultationStateHolder.updateMessage(it)
        }
    }
}