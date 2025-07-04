package com.probro.khoded.pages

import androidx.compose.runtime.*
import com.probro.khoded.components.ErrorBoundary
import com.probro.khoded.components.ErrorBoundaryConfig
import com.probro.khoded.components.KhodedSEO
import com.probro.khoded.components.OptimizedImage
import com.probro.khoded.components.SEOHead
import com.probro.khoded.components.composables.MessageArea
import com.probro.khoded.components.composables.TextBox
import com.probro.khoded.components.composables.popupscreen.PopUpScreen
import com.probro.khoded.messaging.messageData.MessageData
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.contactSections.ContactFormState
import com.probro.khoded.pages.contactSections.ContactPageStateHolder
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.animations.makeInvisibleKeyFrames
import com.probro.khoded.styles.animations.makeVisibleKeyFrames
import com.probro.khoded.styles.animations.shiftBackwardKeyframes
import com.probro.khoded.styles.animations.shiftForwardKeyFrames
import com.probro.khoded.styles.base.BaseTextStyle
import com.probro.khoded.styles.base.CompanyContactTextVariant
import com.probro.khoded.styles.base.SectionTitleVariant
import com.probro.khoded.styles.components.*
import com.probro.khoded.styles.popups.MessagingPopUpTextVariant
import com.probro.khoded.utils.*
import com.probro.khoded.utils.Pages.Contact_Section.Landing.ctaButton
import com.probro.khoded.utils.popUp.PopUpStateHolders
import com.stevdza.san.kotlinbs.models.InputValidation
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Width
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
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
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
    SEOHead(KhodedSEO.contactPage, ctx)
    val formState by ContactPageStateHolder.formState.collectAsState()
    val popUpState by PopUpStateHolders.MessagingPopUpStateHolder.popUpState.collectAsState()

    // Development vs Production configuration
    val errorConfig = ErrorBoundaryConfig(
        showStackTrace = false, // Set to true in development
        enableErrorReporting = true,
        fallbackTitle = "Khoded - Service Temporarily Unavailable",
        fallbackMessage = "We're experiencing technical difficulties." +
                " Our team has been notified and is working on a fix."
    )

    ErrorBoundary(
        config = errorConfig,
        onError = { error, errorInfo ->
            // Custom error handling for your agency
            console.error("Khoded website error:", error)
            // TODO: Integrate with your analytics/monitoring service
        }
    ) {
        WithNavigation {
            NavigationHeader(navigationState = it)
            with(Pages.Contact_Section.Landing) {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    ContactPageSections(formState = formState)
                    with(popUpState) {
                        PopUpScreen(
                            popUpUIModel = this,
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
}

@Composable
fun ContactPageSections(
    formState: ContactFormState,
    modifier: Modifier = Modifier,
) = with(Pages.Contact_Section.Landing) {
    // Background Colors used to make the gradient
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            BaseBackgroundStyle.toModifier(ContactLandingBackgroundVariant)
                .fillMaxSize()
        )
        Box(
            BaseBackgroundStyle.toModifier(ContactFooterBackgroundVariant)
                .fillMaxSize()
        )
    }
    Column(
        modifier = modifier
            .height(Height.FitContent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
        modifier = BaseContainerStyle.toModifier(CompanyInfoContainerVariant),
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
            attrs = BaseCTAStyle.toModifier()
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
        attrs = BaseTextStyle.toModifier(SectionTitleVariant)
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
            modifier = Modifier.fillMaxSize()
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
            OptimizedImage(
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
        modifier = BaseContainerStyle.toModifier(CompanyInfoContainerVariant),
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