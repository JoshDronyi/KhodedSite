package com.probro.khoded.pages

import androidx.compose.runtime.*
import com.probro.khoded.BaseButtonTextVariant
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.EmailData
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.components.widgets.ContactPageHeaderVariant
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.pages.homeSections.MessageArea
import com.probro.khoded.pages.homeSections.TextBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.*
import com.probro.khoded.utils.Pages.Contact_Section.Landing.ctaButton
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.animation.toAnimation
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


val ContactLandingBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .height(Height.FitContent)
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.SkyBlue,
                    to = Colors.WhiteSmoke
                )
            )
    }
}

val ContactFooterBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.WhiteSmoke,
                    to = Colors.SkyBlue
                )
            )
    }
}

@Page
@Composable
fun Contact() {
    val ctx = rememberPageContext()
    val scope = rememberCoroutineScope()
    Scaffold(
        onNavigate = {
            ctx.router.navigateTo(it)
        }
    ) { header, footer, modifier ->
        with(Pages.Contact_Section.Landing) {
            var clientFilledData by remember {
                mutableStateOf(Pages.Contact_Section.MessaageUIModel())
            }
            Column(
                modifier = modifier
                    .id(Pages.Contact_Section.Landing.id)
                    .height(Height.FitContent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LandingSection(
                    header,
                    clientFilledData = clientFilledData,
                    onNameChange = {
                        clientFilledData = clientFilledData.copy(
                            fullName = it
                        )
                    },
                    onEmailChange = {
                        clientFilledData = clientFilledData.copy(
                            email = it
                        )
                    },
                    onSubjectChange = {
                        clientFilledData = clientFilledData.copy(
                            messageSubject = it
                        )
                    },
                    onOrganizationChange = {
                        clientFilledData = clientFilledData.copy(
                            organization = it
                        )
                    },
                )
                FooterSection(
                    data = placeholderMsgUIModel,
                    footer = footer,
                    onMessageSend = { message ->
                        scope.launch {
                            println("clientData was $clientFilledData")
                            println("Message was $message")
                            sendMessage(
                                name = clientFilledData.fullName,
                                email = clientFilledData.email,
                                organization = clientFilledData.organization,
                                subject = clientFilledData.messageSubject,
                                message = message
                            )
                        }
                    })
            }
        }
    }
}

suspend fun sendMessage(name: String, email: String, organization: String, subject: String, message: String) {
    MailClient.sendEmail(
        data = EmailData(
            name = name,
            email = email,
            organization = organization,
            subject = subject,
            message = message
        )
    )
}

@Composable
fun FooterSection(
    data: Pages.Contact_Section.MessaageUIModel,
    modifier: Modifier = Modifier,
    onMessageSend: (message: String) -> Unit,
    footer: @Composable () -> Unit
) {
    Column(
        modifier = BackgroundStyle.toModifier(ContactFooterBackgroundVariant)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MessageDisplay(
            data = data,
            modifier = modifier,
            onMessageSend = onMessageSend
        )
        footer()
    }
}

@Composable
fun LandingSection(
    header: @Composable (variant: ComponentVariant?) -> Unit,
    clientFilledData: Pages.Contact_Section.MessaageUIModel,
    onSubjectChange: (newText: String) -> Unit,
    onOrganizationChange: (newText: String) -> Unit,
    onNameChange: (newText: String) -> Unit,
    onEmailChange: (newText: String) -> Unit
) = with(Pages.Contact_Section.Landing) {
    Column(
        modifier = BackgroundStyle.toModifier(ContactLandingBackgroundVariant),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        header(ContactPageHeaderVariant)
        var state by remember { mutableStateOf(SectionPosition.ON_SCREEN) }
        BackingCard(
            modifier = ContactPageRowStyle.toModifier(LandingSectionVariant)
                .id(id),
            variant = NoBorderBackingCardVariant,
            firstSection = {
                IsOnScreenObservable(id) {
                    state = it
                }
                ClientContactInfoDisplay(
                    mainText = mainText,
                    subText = subText,
                    placeholderData = placeholderMsgUIModel,
                    sectionPosition = state,
                    clientFilledData = clientFilledData,
                    onNameChange = onNameChange,
                    onEmailChange = onEmailChange,
                    onOrganizationChange = onOrganizationChange,
                    onSubjectChange = onSubjectChange
                )
            },
            secondSection = {
                CompanyContactInfoSection(
                    image = mainImage,
                    contactInfoUIModel = contactInfoUIModel,
                )
            }
        )
    }
}

val ClientInfoPromptVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(48.px)
            .textAlign(TextAlign.Start)
            .fontWeight(FontWeight.Bold)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.LG {
        Modifier.fontSize(48.px)
    }
}

@Composable
fun ClientContactInfoDisplay(
    mainText: String,
    subText: String,
    sectionPosition: SectionPosition,
    placeholderData: Pages.Contact_Section.MessaageUIModel,
    clientFilledData: Pages.Contact_Section.MessaageUIModel,
    onSubjectChange: (newText: String) -> Unit,
    onOrganizationChange: (newText: String) -> Unit,
    onNameChange: (newText: String) -> Unit,
    onEmailChange: (newText: String) -> Unit
) = with(placeholderData) {
    Box(
        modifier = ContactSectionContainerStyle.toModifier(ClientInfoContainerVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(80.percent)
        ) {
            ClientInfoTitle(mainText, subText, sectionPosition)
            ClientInfoInputDisplay(
                placeholderData = placeholderData,
                clientFilledData = clientFilledData,
                onOrganizationChange = onOrganizationChange,
                onSubjectChange = onSubjectChange,
                onNameChange = onNameChange,
                onEmailChange = onEmailChange
            )
        }
    }
}

@Composable
fun ClientInfoTitle(
    mainText: String,
    subText: String,
    sectionPosition: SectionPosition
) {
    P(
        attrs = BaseTextStyle.toModifier(ClientInfoPromptVariant)
            .position(Position.Relative)
            .animation(
                fallInAnimation.toAnimation(
                    duration = 600.ms,
                    timingFunction = AnimationTimingFunction.Ease,
                    direction = AnimationDirection.Normal,
                    playState = if (sectionPosition == SectionPosition.ON_SCREEN) AnimationPlayState.Running
                    else AnimationPlayState.Paused
                )
            )
            .toAttrs()
    ) {
        Span(
            attrs = Modifier
                .color(Colors.Purple)
                .margin(right = 10.px)
                .toAttrs()
        ) {
            Text(mainText)
        }
        Span(
            attrs = Modifier
                .color(Colors.HotPink)
                .toAttrs()
        ) {
            Text(subText)
        }
    }
}

val ClientInfoTextBoxStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .margin(topBottom = 5.px)
    }
}

val ClientInfoTextAreaVariant by ClientInfoTextBoxStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    }
}

@Composable
fun ClientInfoInputDisplay(
    placeholderData: Pages.Contact_Section.MessaageUIModel,
    clientFilledData: Pages.Contact_Section.MessaageUIModel,
    onNameChange: (newText: String) -> Unit,
    onEmailChange: (newText: String) -> Unit,
    onOrganizationChange: (newText: String) -> Unit,
    onSubjectChange: (newText: String) -> Unit,
) =
    with(placeholderData) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight()
                .margin(top = 20.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            TextBox(
                value = clientFilledData.fullName,
                placeholder = fullName,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                onNameChange(it)
            }
            TextBox(
                value = clientFilledData.email,
                placeholder = email,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                onEmailChange(it)
            }
            TextBox(
                value = clientFilledData.organization,
                placeholder = organization,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                onOrganizationChange(it)
            }
            TextBox(
                value = clientFilledData.messageSubject,
                placeholder = messageSubject,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                onSubjectChange(it)
            }
        }
    }

@Composable
fun CompanyContactInfoSection(
    image: String,
    contactInfoUIModel: Pages.Contact_Section.ContactInfoUIModel
) {
    Box(
        modifier = ContactSectionContainerStyle.toModifier(CompanyInfoContainerVariant),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(80.percent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Image(
                src = image,
                description = "Planet 404",
                modifier = Modifier
                    .fillMaxWidth(70.percent)
                    .objectFit(ObjectFit.Contain)
            )
            ContactInfoDisplay(contactInfoUIModel)
        }
    }
}

val CompanyContactTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .padding(0.px)
            .margin(0.px)
            .fontSize(FontSize.Larger)
            .fontWeight(FontWeight.Bolder)
            .textAlign(TextAlign.End)
    }
}
val CompanyInfoColumnStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
    }
}

@Composable
fun ContactInfoDisplay(contactInfoUIModel: Pages.Contact_Section.ContactInfoUIModel) = with(contactInfoUIModel) {
    Column(
        modifier = CompanyInfoColumnStyle.toModifier(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(CompanyContactTextVariant)
                .toAttrs()
        ) {
            Text(phone)
        }
        P(
            attrs = BaseTextStyle.toModifier(CompanyContactTextVariant)
                .toAttrs()
        ) {
            Text(email)
        }
        P(
            attrs = BaseTextStyle.toModifier(CompanyContactTextVariant)
                .toAttrs()
        ) {
            Text(address)
        }
    }
}

@Composable
fun MessageDisplay(
    data: Pages.Contact_Section.MessaageUIModel,
    modifier: Modifier = Modifier,
    onMessageSend: (message: String) -> Unit
) = with(data) {
    val breakpoint = rememberBreakpoint()
    Row(
        modifier = ContactPageRowStyle.toModifier(MessagingSectionVariant)
            .then(modifier),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputDisplays(
            message = message,
            ctaButton = ctaButton,
            onMessageSend = onMessageSend
        )
        if (breakpoint >= Breakpoint.MD) SpacerSection()
    }
}


val ContactPageRowStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
    }
}

val LandingSectionVariant by ContactPageRowStyle.addVariant {
    base {
        Modifier
            .minHeight(50.vh)
            .height(Height.FitContent)
    }
}
val MessagingSectionVariant by ContactPageRowStyle.addVariant {
    base {
        Modifier
            .height(50.vh)
            .margin(1.px)
    }
}

@Composable
fun InputDisplays(
    message: String,
    ctaButton: ButtonState,
    onMessageSend: (message: String) -> Unit
) {
    Box(
        modifier = ContactSectionContainerStyle.toModifier(MessagingSectionContainerVariant),
        contentAlignment = Alignment.Center
    ) {
        var mutableMessage by remember { mutableStateOf(message) }
        Column(
            modifier = Modifier
                .fillMaxWidth(80.percent)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            MessageArea(
                value = mutableMessage,
                modifier = ClientInfoTextBoxStyle.toModifier(ClientInfoTextAreaVariant)
            ) { newText ->
                mutableMessage = newText
            }
            ButtonDisplay(
                state = ctaButton.copy(
                    onButtonClick = {
                        onMessageSend(mutableMessage)
                    }
                ),
                BlueButtonVariant,
                modifier = Modifier.width(Width.FitContent)
                    .margin(topBottom = 10.px)
                    .align(Alignment.Start)
            ) {
                P(
                    attrs = BaseTextStyle.toModifier(BaseButtonTextVariant)
                        .toAttrs()
                ) {
                    Text(it)
                }
            }
        }
    }
}

@Composable
fun SpacerSection() {
    Box(modifier = ContactSectionContainerStyle.toModifier(SpacerContainerVariant))
}

val ContactSectionContainerStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(topBottom = 30.px)
    }
}

val MessagingSectionContainerVariant by ContactSectionContainerStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
    }
}

val SpacerContainerVariant by ContactSectionContainerStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
    }
}

val CompanyInfoContainerVariant by ContactSectionContainerStyle.addVariant {
    base {
        Modifier
            .fillMaxHeight()
            .minHeight(50.vh)
    }
}


val ClientInfoContainerVariant by ContactSectionContainerStyle.addVariant {
    base {
        Modifier
            .height(Height.FitContent)
    }
}
