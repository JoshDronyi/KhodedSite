package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.BaseButtonTextVariant
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.popupscreen.MessagingPopUpTextVariant
import com.probro.khoded.components.composables.popupscreen.MessagingPopUpVariant
import com.probro.khoded.components.composables.popupscreen.PopUpScreen
import com.probro.khoded.models.ButtonState
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.TitleIDs
import com.probro.khoded.utils.popUp.PopUpStateHolders
import com.stevdza.san.kotlinbs.forms.BSInput
import com.stevdza.san.kotlinbs.models.InputSize
import com.stevdza.san.kotlinbs.models.InputValidation
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
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


@Composable
fun ConsultationSectionDisplay(
    footer: @Composable (variant: ComponentVariant?) -> Unit,
    data: Pages.Home_Section.Consultation,
) = with(data) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
                ConsultationDisplaySection(mainImage = mainImage)
                QuoteSection(quotes, subText)
            }
            footer(null)
        }
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
    modifier: Modifier = Modifier,
) = with(Pages.Home_Section.Consultation) {
    val popUpState by PopUpStateHolders.MessagingPopUpStateHolder.popUpState.collectAsState()
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            src = mainImage,
            description = "Message Bubbles",
            modifier = ConsultationImageStyle.toModifier()
                .zIndex(0)
        )
        ConsultationTextSection(
            mainText = mainText,
            ctaButton = ctaButton,
            modifier = ConsultationRequestStyle.toModifier()
                .id(id)
                .align(Alignment.BottomStart)
                .scrollSnapStop(ScrollSnapStop.Always)
                .scrollSnapType(ScrollSnapAxis.Block, ScrollSnapMode.Mandatory),
        )
        with(popUpState) {
            PopUpScreen(
                popUpUIModel = this,
                variant = MessagingPopUpVariant,
                textVariant = MessagingPopUpTextVariant,
                modifier = Modifier
                    .align(Alignment.Center)
                    .visibility(if (isShowing) Visibility.Visible else Visibility.Hidden)
                    .opacity(if (isShowing) 100.percent else 0.percent)
                    .zIndex(if (isShowing) 2 else 0)
                    .transition(
                        CSSTransition(property = "visibility", duration = 300.ms),
                        CSSTransition(property = "opacity", duration = 300.ms),
                        CSSTransition(property = "zIndex", duration = 300.ms)
                    )
            )
        }
    }
}

val ConsultationRequestStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth(60.percent)
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
    ctaButton: ButtonState,
    modifier: Modifier = Modifier
) {
    val formState by ConsultationStateHolder.formState.collectAsState()
    Column(
        modifier = modifier
            .zIndex(1),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        ConsultationTitle(mainText)
        MessagingSection(
            modifier = MessagingSectionStyle.toModifier(),
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
            modifier = ConsultationButtonDisplay.toModifier()
                .align(Alignment.CenterHorizontally)
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
        buttonVariant = PinkButtonVariant,
        modifier = modifier
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

//private fun sendConsultationMessage(
//    clientRequestUIModel: Pages.Home_Section.ConsultationRequestUIModel,
//    onMessageResult: (response: String) -> Unit
//) = scope.launch {
//    with(clientRequestUIModel) {
//        val msgResponse = onMessageSend().await()
//        onMessageResult(msgResponse)
//    }
//}


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
    placeHolder: Pages.Home_Section.ConsultationRequestUIModel,
    modifier: Modifier = Modifier
) {
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
                placeholder = placeHolder.fullName,
                required = true,
                modifier = ConsultationTextBox.toModifier()
            ) {
                ConsultationStateHolder.updateName(it)
            }
            TextBox(
                placeholder = placeHolder.email,
                required = true,
                modifier = ConsultationTextBox.toModifier()
            ) {
                ConsultationStateHolder.updateEmail(it)
            }
        }
        MessageArea(
            placeholder = placeHolder.projectSynopsis,
            modifier = Modifier
                .fillMaxWidth()
                .height(Height.MaxContent)
        ) {
            ConsultationStateHolder.updateMessage(it)
        }
    }
}

val ConsultationTextBox by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth(80.percent)
    }
}


val TextAreaVariant by InputStyle.addVariant {
    base {
        Modifier
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
    placeholder: String = "Enter Message here.",
    modifier: Modifier = Modifier,
    onValueChange: (newText: String) -> Unit
) {
    var value by remember { mutableStateOf("") }
    BSInput(
        value = value,
        label = placeholder,
        type = InputType.Text,
        size = InputSize.Large,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        required = true,
        modifier = InputStyle.toModifier(TextAreaVariant)
            .then(modifier)
    )
}

@Composable
fun TextBox(
    placeholder: String = "",
    required: Boolean = false,
    validation: InputValidation = InputValidation(),
    modifier: Modifier = Modifier,
    onValueChange: (newText: String) -> Unit
) {
    var value by remember { mutableStateOf("") }
    BSInput(
        value = value,
        placeholder = placeholder,
        required = required,
        validation = validation,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        modifier = InputStyle.toModifier(TextBoxVariant)
            .then(modifier)
    )
}
