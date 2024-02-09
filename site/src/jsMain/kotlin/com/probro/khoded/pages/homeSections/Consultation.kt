package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.BaseButtonTextVariant
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.sendMessage
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.TitleIDs
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
import com.varabyte.kobweb.silk.components.forms.InputStyle
import com.varabyte.kobweb.silk.components.forms.TextInput
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.common.PlaceholderColor
import com.varabyte.kobweb.silk.components.style.toModifier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


@Composable
fun ConsultationSectionDisplay(
    footer: @Composable () -> Unit,
    data: Pages.Home_Section.Consultation
) = with(data) {
    var sectionPosition by remember { mutableStateOf(SectionPosition.IDLE) }
    Column(
        modifier = BackgroundStyle.toModifier(ConsultationBackgroundVariant)
            .id(id),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = ConsultationSectionStyle.toModifier(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                src = mainImage,
                description = "Message Bubbles",
                modifier = Modifier.fillMaxWidth(40.percent)
                    .align(Alignment.Start)
            )
            ConsultationDisplaySection(
                sectionPosition = sectionPosition,
                modifier = Modifier
                    .fillMaxWidth(80.percent)
            )
            QuoteSection(leftQuote, subText, rightQuote)
        }
        footer()
    }
}

val ConsultationSectionStyle by ComponentStyle {
    base {
        Modifier.fillMaxWidth()
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = (-50).px)
    }
    Breakpoint.SM {
        Modifier
            .translateY(ty = (-100).px)
    }
    Breakpoint.MD {
        Modifier
            .translateY(ty = (-150).px)
    }
    Breakpoint.LG {
        Modifier
            .translateY(ty = (-200).px)
    }
}

@Composable
fun ConsultationDisplaySection(
    sectionPosition: SectionPosition,
    modifier: Modifier = Modifier
) = with(Pages.Home_Section.Consultation) {
    BackingCard(
        modifier = modifier,
        variant = NoBorderBackingCardVariant,
        firstSection = {
            ConsultationTextSection(
                mainText = mainText,
                clientRequestUIModel = consultationRequestUIModel,
                ctaButton = ctaButton,
                sectionPosition = sectionPosition,
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
            )
        },
        secondSection = {
            Image(
                src = subImage,
                modifier = Modifier
                    .fillMaxWidth(40.percent)
            )
        }
    )
}

@Composable
fun QuoteSection(
    leftQuote: String,
    subText: String,
    rightQuote: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(80.percent)
            .margin(topBottom = 50.px),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(10.percent)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                src = leftQuote,
                description = "Open Quote",
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        P(
            attrs = BaseTextStyle.toModifier()
                .fillMaxWidth()
                .color(Color.white)
                .toAttrs()
        ) {
            Text(subText)
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(10.percent),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                src = rightQuote,
                description = "Close Quote",
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

const val FREE_TEXT = "Free 30 Min"

@Composable
fun ConsultationTextSection(
    mainText: String,
    clientRequestUIModel: Pages.Home_Section.ConsultationRequestUIModel,
    ctaButton: ButtonState,
    sectionPosition: SectionPosition,
    modifier: Modifier = Modifier
) {
    var localName by remember { mutableStateOf(clientRequestUIModel.fullName) }
    var localEmail by remember { mutableStateOf(clientRequestUIModel.email) }
    var localMessage by remember { mutableStateOf(clientRequestUIModel.projectSynopsis) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        ConsultationTitle(mainText, sectionPosition)
        MessagingSection(
            clientRequestUIModel,
            name = localName,
            email = localEmail,
            message = localMessage,
            onNameChange = {
                localName = it
            },
            onEmailChange = {
                localEmail = it
            },
            onMessageChange = {
                localMessage = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1)
        )
        ButtonDisplay(
            state = ctaButton.copy(
                onButtonClick = {
                    sendConsultationMessage(localName, localEmail, localMessage)
                }
            ),
            buttonVariant = PinkButtonVariant,
            modifier = Modifier
                .width(Width.FitContent)
                .padding(10.px)
                .margin(topBottom = 40.px)
        ) { text ->
            val index = remember { text.indexOf(FREE_TEXT, ignoreCase = true) }
            val before = remember { text.substring(0, index) }
            val after = remember { text.substring(index + FREE_TEXT.length) }
            P(
                attrs = BaseTextStyle.toModifier(BaseButtonTextVariant)
                    .toAttrs()
            ) {
                Span {
                    Text(before)
                }
                Span(
                    attrs = Modifier.fontWeight(FontWeight.Bolder)
                        .fontSize(FontSize.Large)
                        .padding(leftRight = 5.px)
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
}

val scope = CoroutineScope(Dispatchers.Default + CoroutineExceptionHandler { coroutineContext, throwable ->
    println(throwable.message)
})

private fun sendConsultationMessage(name: String, email: String, message: String) = scope.launch {
    sendMessage(
        name = name,
        email = email,
        message = message,
        subject = "Consultation Request",
        organization = "N/A"
    )
}


@Composable
fun ConsultationTitle(mainText: String, sectionPosition: SectionPosition) {
    val splitIndex = remember { mainText.indexOf("about") }
    val firstText = remember { mainText.substring(0, splitIndex) }
    val secondText = remember { mainText.substring(splitIndex) }
    var scrollOffset by remember { mutableIntStateOf(0) }

    println("Consultation title.... ${sectionPosition.name}")
    var sectionPosition by remember { mutableStateOf(SectionPosition.IDLE) }

    P(
        attrs = BaseTextStyle.toModifier(HomeTitleVariant)
            .id(TitleIDs.consultationTitleID)
            .position(Position.Relative)
            .translateY(
                ty = when (sectionPosition) {
                    SectionPosition.ABOVE -> (-100).px
                    SectionPosition.ON_SCREEN -> 0.px
                    SectionPosition.BELOW -> (-100).px
                    SectionPosition.IDLE -> 0.px
                }
            )
            .opacity(
                when (sectionPosition) {
                    SectionPosition.ABOVE -> 0.percent
                    SectionPosition.ON_SCREEN -> 100.percent
                    SectionPosition.BELOW -> 0.percent
                    SectionPosition.IDLE -> 100.percent
                }
            )
            .transition(
                CSSTransition(property = "translate", duration = 600.ms),
                CSSTransition(property = "opacity", duration = 600.ms)
            )
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
        sectionPosition = it
        println("New Position for ${Pages.Home_Section.Consultation.id} is $it")
    }
}

val ContactInfoRowStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .margin(topBottom = 20.px)
    }
}


@Composable
fun MessagingSection(
    clientRequestUIModel: Pages.Home_Section.ConsultationRequestUIModel,
    name: String,
    email: String,
    message: String,
    onNameChange: (newText: String) -> Unit,
    onEmailChange: (newText: String) -> Unit,
    onMessageChange: (newText: String) -> Unit,
    modifier: Modifier = Modifier
) = with(clientRequestUIModel) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = ContactInfoRowStyle.toModifier(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBox(
                value = name,
                modifier = Modifier.fillMaxWidth()
                    .margin(right = 20.px)
            ) {
                onNameChange(it)
            }
            TextBox(
                value = email,
                modifier = Modifier.fillMaxWidth()
            ) {
                onEmailChange(it)
            }
        }
        MessageArea(
            value = message,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1)
                .minHeight(400.px)
        ) {
            onMessageChange(it)
        }
    }
}

val TextAreaVariant by InputStyle.addVariant {
    base {
        Modifier
            .background(Color.white)
            .padding(20.px)
            .color(Colors.Purple)
            .fontSize(FontSize.XLarge)
    }
}
val TextBoxVariant by InputStyle.addVariant {
    base {
        Modifier
            .background(Color.white)
            .padding(topBottom = 30.px)
            .color(Colors.Purple)
            .fontSize(FontSize.XLarge)
            .textAlign(TextAlign.Center)
    }
}

@Composable
fun MessageArea(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (newText: String) -> Unit
) {
    TextInput(
        text = value,
        onTextChanged = {
            onValueChange(it)
        },
        modifier = InputStyle.toModifier(TextAreaVariant)
            .then(modifier)
    )
}

@Composable
fun TextBox(
    value: String,
    placeholder: String = "",
    placeholderColor: PlaceholderColor? = null,
    modifier: Modifier = Modifier,
    onValueChange: (newText: String) -> Unit
) {
    TextInput(
        text = value,
        placeholder = placeholder,
        placeholderColor = placeholderColor,
        onTextChanged = {
            onValueChange(it)
        },
        modifier = InputStyle.toModifier(TextBoxVariant)
            .then(modifier)
    )
}
