package com.probro.khoded.pages

import androidx.compose.runtime.*
import com.probro.khoded.BaseButtonTextVariant
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.composables.ContactPopUpTextVariant
import com.probro.khoded.components.composables.ContactPopUpVariant
import com.probro.khoded.components.composables.DarkLogoTextVariant
import com.probro.khoded.components.composables.PopUpScreen
import com.probro.khoded.components.widgets.ContactFooterVariant
import com.probro.khoded.components.widgets.ContactPageHeaderVariant
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.contactSections.ContactFormState
import com.probro.khoded.pages.contactSections.ContactPageStateHolder
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.pages.homeSections.MessageArea
import com.probro.khoded.pages.homeSections.TextBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.Pages.Contact_Section.Landing.ctaButton
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.fallInAnimation
import com.probro.khoded.utils.popUp.PopUpStateHolder
import com.stevdza.san.kotlinbs.models.InputValidation
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.animation.toAnimation
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
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
    val formState by ContactPageStateHolder.formState.collectAsState()
    val popUpState by PopUpStateHolder.popUpState.collectAsState()
    Scaffold(
        onNavigate = {
            ctx.router.navigateTo(it)
        }
    ) { header, footer, modifier ->
        with(Pages.Contact_Section.Landing) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                // Background Colors used to make the gradient
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        BackgroundStyle.toModifier(ContactLandingBackgroundVariant)
                            .fillMaxSize()
                    )
                    Box(
                        BackgroundStyle.toModifier(ContactFooterBackgroundVariant)
                            .fillMaxSize()
                    )
                }
                Column(
                    modifier = modifier
                        .height(Height.FitContent),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    header(ContactPageHeaderVariant, DarkLogoTextVariant)
                    SimpleGrid(
                        numColumns = numColumns(base = 1, md = 2),
                        modifier = ContactPageRowStyle.toModifier(LandingSectionVariant)
                            .id(id)
                    ) {
                        ContactForm(
                            contactFormState = formState
                        )
                        CompanyContactInfoSection(
                            image = mainImage,
                            contactInfoUIModel = contactInfoUIModel,
                        )
                    }
                    footer(ContactFooterVariant)
                }
                with(popUpState) {
                    PopUpScreen(
                        popUpUIModel = this,
                        variant = ContactPopUpVariant,
                        textVariant = ContactPopUpTextVariant,
                        modifier = Modifier
                            .visibility(if (isShowing) Visibility.Visible else Visibility.Hidden)
                            .opacity(if (isShowing) 100.percent else 0.percent)
                    )
                }
            }
        }
    }
}

@Composable
fun ContactForm(
    contactFormState: ContactFormState,
    modifier: Modifier = Modifier,
) = with(Pages.Contact_Section.Landing) {
    Column(
        modifier = modifier
            .id(id)
    ) {
        var state by remember { mutableStateOf(SectionPosition.ON_SCREEN) }
        IsOnScreenObservable(
            sectionID = id,
        ) {
            state = it
        }
        with(contactFormState) {
            ClientContactInfoDisplay(
                mainText = mainText,
                subText = subText,
                placeholderData = placeholderData,
                clientFilledData = clientFilledData,
                onMessageSend = { message ->
                    ContactPageStateHolder.onMessageSend(message)
                },
                onStateChange = {
                    with(clientFilledData) {
                        ContactPageStateHolder.updateName(fullName)
                        ContactPageStateHolder.updateEmail(email)
                        ContactPageStateHolder.updateSubject(messageSubject)
                        ContactPageStateHolder.updateOgranization(organization)
                        ContactPageStateHolder.updateMessage(message)
                    }
                })
        }
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
    placeholderData: Pages.Contact_Section.MessageUIModel,
    clientFilledData: Pages.Contact_Section.MessageUIModel,
    onStateChange: () -> Unit,
    onMessageSend: (message: String) -> Unit,
) = with(placeholderData) {
    val popUpState by PopUpStateHolder.popUpState.collectAsState()
    Box(
        modifier = ContactSectionContainerStyle.toModifier(ClientInfoContainerVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(80.percent)
        ) {
            ClientInfoTitle(mainText, subText)
            ClientInfoInputDisplay(
                placeholderData = placeholderData,
                clientFilledData = clientFilledData,
                onOrganizationChange = {
                    clientFilledData.organization = it
                },
                onSubjectChange = {
                    clientFilledData.messageSubject = it
                },
                onNameChange = {
                    clientFilledData.fullName = it
                },
                onEmailChange = {
                    clientFilledData.email = it
                }
            )
            MessageArea(
                modifier = ClientInfoTextBoxStyle.toModifier(ClientInfoTextAreaVariant)
            ) { newText ->
                message = newText
                clientFilledData.message = newText
            }
            ContactUsCTA(
                onStateChange,
                onMessageSend = {
                    onMessageSend(message)
                },
                modifier = Modifier.width(Width.FitContent)
                    .margin(topBottom = 10.px)
                    .align(Alignment.Start)
            )
        }
        with(popUpState) {
            PopUpScreen(
                popUpUIModel = this,
                variant = ContactPopUpVariant,
                textVariant = ContactPopUpTextVariant,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun ContactUsCTA(
    onStateChange: () -> Unit,
    onMessageSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    ButtonDisplay(
        state = ctaButton.copy(
            onButtonClick = {
                onStateChange()
                onMessageSend()
            }
        ),
        BlueButtonVariant,
        modifier = modifier
    ) {
        P(
            attrs = BaseTextStyle.toModifier(BaseButtonTextVariant)
                .toAttrs()
        ) {
            Text(it)
        }
    }
}

@Composable
fun ClientInfoTitle(
    mainText: String,
    subText: String
) {
    P(
        attrs = BaseTextStyle.toModifier(ClientInfoPromptVariant)
            .position(Position.Relative)
            .animation(
                fallInAnimation.toAnimation(
                    duration = 600.ms,
                    timingFunction = AnimationTimingFunction.Ease,
                    direction = AnimationDirection.Normal,
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
    placeholderData: Pages.Contact_Section.MessageUIModel,
    clientFilledData: Pages.Contact_Section.MessageUIModel,
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
                placeholder = fullName,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                onNameChange(it)
            }
            TextBox(
                placeholder = email,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                onEmailChange(it)
            }
            TextBox(
                placeholder = organization,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                onOrganizationChange(it)
            }
            TextBox(
                placeholder = messageSubject,
                modifier = ClientInfoTextBoxStyle.toModifier(),
                required = true,
                validation = InputValidation()
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

//@Composable
//fun MessageDisplay(
//    placeholderData: Pages.Contact_Section.MessageUIModel,
//    clientFilledData: Pages.Contact_Section.MessageUIModel,
//    modifier: Modifier = Modifier,
//    onMessageSend: (message: String) -> Unit
//) = with(clientFilledData) {
//    val breakpoint = rememberBreakpoint()
//    //TODO: Handle placeholder data and changing state.
//
//    Row(
//        modifier = ContactPageRowStyle.toModifier(MessagingSectionVariant)
//            .then(modifier),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        if (breakpoint >= Breakpoint.MD) SpacerSection()
//    }
//}


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
        Column(
            modifier = Modifier
                .fillMaxWidth(80.percent)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

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
