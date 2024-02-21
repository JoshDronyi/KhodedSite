package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.BaseButtonTextVariant
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.ConsultationPopUpTextVariant
import com.probro.khoded.components.composables.ConsultationPopUpVariant
import com.probro.khoded.components.composables.PopUpScreen
import com.probro.khoded.components.composables.PopUpScreenUIModel
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.sendMessage
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.*
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
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


@Composable
fun ConsultationSectionDisplay(
    footer: @Composable () -> Unit,
    data: Pages.Home_Section.Consultation
) = with(data) {
    Column(
        modifier = BackgroundStyle.toModifier(ConsultationBackgroundVariant),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = ConsultationSectionStyle.toModifier(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ConsultationDisplaySection(
                mainImage = mainImage,
                modifier = Modifier
                    .id(id)
            )
            QuoteSection(quotes, subText)
        }
        footer()
    }
}

val ConsultationSectionStyle by ComponentStyle {
    base {
        Modifier.fillMaxWidth()
    }
}

val ConsultationImageStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxSize()
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = (-100).px)
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
    mainImage: String,
    modifier: Modifier = Modifier
) = with(Pages.Home_Section.Consultation) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        var isShowing by remember { mutableStateOf(false) }
        Image(
            src = mainImage,
            description = "Message Bubbles",
            modifier = ConsultationImageStyle.toModifier()
        )

        ConsultationTextSection(
            mainText = mainText,
            clientRequestUIModel = consultationRequestUIModel,
            ctaButton = ctaButton,
            modifier = ConsultationRequestStyle.toModifier()
                .align(Alignment.BottomStart)
        ) { response ->
            isShowing = true
        }

        PopUpScreen(
            popUpUIModel = PopUpScreenUIModel(
                promptText = Strings.consultationThanksMessage,
                buttonState = ButtonState(
                    buttonText = "See you soon.",
                    onButtonClick = {
                        isShowing = false
                    }
                ),
                isShowing = isShowing
            ),
            variant = ConsultationPopUpVariant,
            textVariant = ConsultationPopUpTextVariant,
            modifier = Modifier
                .visibility(if (isShowing) Visibility.Visible else Visibility.Hidden)
                .opacity(if (isShowing) 100.percent else 0.percent)
        )
    }
}

val ConsultationRequestStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth(70.percent)
            .fillMaxHeight()
            .padding(topBottom = 10.px, leftRight = 20.px)
    }
    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM
    Breakpoint.MD {
        Modifier.fillMaxWidth(70.percent)
    }
    Breakpoint.LG {
        Modifier
            .fillMaxWidth(60.percent)
    }
    Breakpoint.XL
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
        Image(
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

const val FREE_TEXT = "Free 30 Min"

val MessagingSectionStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(60.percent)
    }
    Breakpoint.ZERO
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
}

@Composable
fun ConsultationTextSection(
    mainText: String,
    clientRequestUIModel: Pages.Home_Section.ConsultationRequestUIModel,
    ctaButton: ButtonState,
    modifier: Modifier = Modifier,
    onMessageSent: (response: String) -> Unit,
) {
    var localName by remember { mutableStateOf(clientRequestUIModel.fullName) }
    var localEmail by remember { mutableStateOf(clientRequestUIModel.email) }
    var localMessage by remember { mutableStateOf(clientRequestUIModel.projectSynopsis) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        ConsultationTitle(mainText)
        MessagingSection(
            clientRequestUIModel,
            name = localName,
            email = localEmail,
            message = localMessage,
            onNameChange = { localName = it },
            onEmailChange = { localEmail = it },
            onMessageChange = { localMessage = it },
            modifier = MessagingSectionStyle.toModifier()
        )
        ButtonDisplay(
            state = ctaButton.copy(
                onButtonClick = {
                    sendConsultationMessage(localName, localEmail, localMessage) { response ->
                        onMessageSent(response)
                    }
                }
            ),
            buttonVariant = PinkButtonVariant,
            modifier = ConsultationButtonDisplay.toModifier()
        ) { text ->
            val index = remember { text.indexOf(FREE_TEXT, ignoreCase = true) }
            val before = remember { text.substring(0, index) }
            val after = remember { text.substring(index + FREE_TEXT.length) }
            P(
                attrs = BaseTextStyle.toModifier(BaseButtonTextVariant)
                    .toAttrs()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Span {
                        Text(before)
                    }
                    Span(
                        attrs = BaseTextStyle.toModifier(FreeTextVariant)
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
}

val ConsultationButtonDisplay by ComponentStyle {
    base {
        Modifier
            .width(Width.FitContent)
            .padding(leftRight = 10.px, topBottom = 5.px)
            .margin(topBottom = 10.px)
    }
}

val FreeTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fontWeight(FontWeight.Bolder)
            .fontSize(FontSize.Large)
            .padding(leftRight = 5.px)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.Small)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
}


val scope = CoroutineScope(Dispatchers.Default + CoroutineExceptionHandler { _, throwable ->
    println(throwable.message)
})

private fun sendConsultationMessage(
    name: String,
    email: String,
    message: String,
    onMessageSent: (response: String) -> Unit
) = scope.launch {
    val msgResponse = sendMessage(
        name = name,
        email = email,
        message = message,
        subject = "Consultation Request",
        organization = "N/A"
    )
    onMessageSent(msgResponse)
}


@Composable
fun ConsultationTitle(mainText: String) {
    val splitIndex = remember { mainText.indexOf("about") }
    val firstText = remember { mainText.substring(0, splitIndex) }
    val secondText = remember { mainText.substring(splitIndex) }

    var position by remember { mutableStateOf(SectionPosition.IDLE) }
    println("Consultation title.... ${position.name}")

    P(
        attrs = HomeTitleTextStyle.toModifier(ConsultationTitleVariant)
            .id(TitleIDs.consultationTitleID)
            .translateY(
                ty = when (position) {
                    SectionPosition.ABOVE -> (-100).px
                    SectionPosition.ON_SCREEN -> 0.px
                    SectionPosition.BELOW -> (-100).px
                    SectionPosition.IDLE -> 0.px
                }
            )
            .opacity(
                when (position) {
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
        position = it
        println("New Position for ${Pages.Home_Section.Consultation.id} is $it")
    }
}

val ContactInfoRowStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .margin(topBottom = 5.px)
    }
    Breakpoint.ZERO {
        Modifier
            .margin(topBottom = 5.px)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
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
                modifier = ConsultationTextBox.toModifier()
                    .margin(right = 10.px)
            ) {
                onNameChange(it)
            }
            TextBox(
                value = email,
                modifier = ConsultationTextBox.toModifier()
            ) {
                onEmailChange(it)
            }
        }
        MessageArea(
            value = message,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1)
        ) {
            onMessageChange(it)
        }
    }
}

val ConsultationTextBox by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
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
    Breakpoint.ZERO {
        Modifier
            .padding(5.px)
            .fontSize(FontSize.XXSmall)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.XSmall)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Medium)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.XL {
        Modifier.fontSize(FontSize.Larger)
    }
}
val TextBoxVariant by InputStyle.addVariant {
    base {
        Modifier
            .background(Color.white)
            .padding(topBottom = 15.px)
            .color(Colors.Purple)
            .fontSize(FontSize.XLarge)
            .textAlign(TextAlign.Center)
    }

    Breakpoint.ZERO {
        Modifier
            .padding(5.px)
            .fontSize(FontSize.XXSmall)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.XSmall)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Medium)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.XL {
        Modifier.fontSize(FontSize.Larger)
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
