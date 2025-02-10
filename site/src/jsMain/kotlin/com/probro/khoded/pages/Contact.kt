package com.probro.khoded.pages

import androidx.compose.runtime.*
import com.probro.khoded.components.composables.DarkLogoTextVariant
import com.probro.khoded.components.composables.popupscreen.PopUpScreen
import com.probro.khoded.components.widgets.ContactFooterVariant
import com.probro.khoded.components.widgets.ContactPageHeaderVariant
import com.probro.khoded.components.widgets.HeaderKind
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.messaging.messageData.MessageData
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.contactSections.ContactFormState
import com.probro.khoded.pages.contactSections.ContactPageStateHolder
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.pages.homeSections.MessageArea
import com.probro.khoded.pages.homeSections.TextBox
import com.probro.khoded.styles.animations.makeInvisibleKeyFrames
import com.probro.khoded.styles.animations.makeVisibleKeyFrames
import com.probro.khoded.styles.animations.shiftBackwardKeyframes
import com.probro.khoded.styles.animations.shiftForwardKeyFrames
import com.probro.khoded.styles.componentStyles.MessagingPopUpTextVariant
import com.probro.khoded.styles.componentStyles.MessagingPopUpVariant
import com.probro.khoded.styles.pageStyles.ClientInfoPromptVariant
import com.probro.khoded.styles.pageStyles.ContactFooterBackgroundVariant
import com.probro.khoded.styles.pageStyles.ContactLandingBackgroundVariant
import com.probro.khoded.styles.pageStyles.LandingSectionVariant
import com.probro.khoded.styles.textStyles.*
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.Pages.Contact_Section.Landing.ctaButton
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.fallInAnimation
import com.probro.khoded.utils.popUp.PopUpStateHolders
import com.stevdza.san.kotlinbs.models.InputValidation
import com.varabyte.kobweb.compose.css.*
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
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.style.CssStyleVariant
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


@Page
@Composable
fun Contact() {
    val ctx = rememberPageContext()
    val formState by ContactPageStateHolder.formState.collectAsState()
    val popUpState by PopUpStateHolders.MessagingPopUpStateHolder.popUpState.collectAsState()

    Scaffold(
        onNavigate = {
            ctx.router.navigateTo(it)
        },
    ) { header, footer, modifier ->
        with(Pages.Contact_Section.Landing) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                ContactPageSections(modifier, header, formState, footer)
                with(popUpState) {
                    PopUpScreen(
                        popUpUIModel = this,
                        variant = MessagingPopUpVariant,
                        textVariant = MessagingPopUpTextVariant,
                        modifier = Modifier
                            .animation(
                                if (isVisible) makeVisibleKeyFrames.toAnimation(300.ms)
                                else makeInvisibleKeyFrames.toAnimation(duration = 300.ms),
                                if (isVisible) shiftForwardKeyFrames.toAnimation(300.ms)
                                else shiftBackwardKeyframes.toAnimation(duration = 300.ms)
                            )
                    )
                }
            }
        }
        LaunchedEffect(formState.stage) {
            PopUpStateHolders.MessagingPopUpStateHolder.adjustPopUpText(formState.stage)
        }
    }
}

@Composable
fun ContactPageSections(
    modifier: Modifier,
    header: @Composable (variant: CssStyleVariant<HeaderKind>?, textVariant: CssStyleVariant<BaseTextKind>?) -> Unit,
    formState: ContactFormState,
    footer: @Composable (variant: CssStyleVariant<ColumnKind>?) -> Unit
) = with(Pages.Contact_Section.Landing) {
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
            modifier = BaseRowStyle.toModifier(LandingSectionVariant)
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
                messageData = messageData,
                onMessageSend = { message ->
                    ContactPageStateHolder.onMessageSend(message)
                },
                onStateChange = {
                    with(ContactPageStateHolder) {
                        messageData.apply {
                            updateName(name)
                            updateEmail(email)
                            updateSubject(subject)
                            updateOrganization(organization)
                            updateMessage(message)
                        }
                    }
                })
        }
    }

}

@Composable
fun ClientContactInfoDisplay(
    mainText: String,
    subText: String,
    placeholderData: Pages.Contact_Section.MessageUIModel,
    messageData: MessageData.ContactMessageData,
    onStateChange: () -> Unit,
    onMessageSend: (message: String) -> Unit,
) = with(placeholderData) {
    Box(
        modifier = BaseContainerStyle.toModifier(ClientInfoContainerVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(80.percent)
        ) {
            ClientInfoTitle(mainText, subText)
            Span(
                attrs = Modifier
                    .background(Colors.Black.copy(alpha = 30))
                    .borderRadius(20.px)
                    .padding(topBottom = 10.px, leftRight = 20.px)
                    .fillMaxWidth()
                    .toAttrs()
            ) {
                ClientInfoInputDisplay(
                    placeholderData = placeholderData,
                    messageData = messageData,
                    onOrganizationChange = {
                        messageData.organization = it
                    },
                    onSubjectChange = {
                        messageData.subject = it
                    },
                    onNameChange = {
                        messageData.name = it
                    },
                    onEmailChange = {
                        messageData.email = it
                    }
                )
                MessageArea(
                    modifier = BaseTextInputStyle.toModifier(ClientInfoTextAreaVariant)
                ) { newText ->
                    messageData.message = newText
                }
                ContactUsCTA(
                    onStateChange,
                    onMessageSend = {
                        onMessageSend(message)
                    },
                    modifier = Modifier.width(Width.FitContent)
                        .margin(topBottom = 10.px)
                        .align(Alignment.CenterHorizontally)
                )
            }
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

@Composable
fun ClientInfoInputDisplay(
    placeholderData: Pages.Contact_Section.MessageUIModel,
    messageData: MessageData.ContactMessageData,
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
                modifier = BaseTextInputStyle.toModifier(ClientInfoTextBoxVariant)
            ) {
                onNameChange(it)
            }
            TextBox(
                placeholder = email,
                modifier = BaseTextInputStyle.toModifier(ClientInfoTextBoxVariant)
            ) {
                onEmailChange(it)
            }
            TextBox(
                placeholder = organization,
                modifier = BaseTextInputStyle.toModifier(ClientInfoTextBoxVariant)
            ) {
                onOrganizationChange(it)
            }
            TextBox(
                placeholder = messageSubject,
                modifier = BaseTextInputStyle.toModifier(ClientInfoTextBoxVariant),
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
        modifier = BaseContainerStyle.toModifier(CompanyInfoContainerVariant),
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


@Composable
fun ContactInfoDisplay(contactInfoUIModel: Pages.Contact_Section.ContactInfoUIModel) = with(contactInfoUIModel) {
    Column(
        modifier = ColumnStyle.toModifier(CompanyInfoColumnStyle),
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
fun InputDisplays(
    message: String,
    ctaButton: ButtonState,
    onMessageSend: (message: String) -> Unit
) {
    Box(
        modifier = BaseContainerStyle.toModifier(MessagingSectionContainerVariant),
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
    Box(modifier = BaseContainerStyle.toModifier(SpacerContainerVariant))
}