package com.probro.khoded.pages.contactSections

import androidx.compose.runtime.*
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.models.Images
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.MainTextVariant
import com.probro.khoded.utils.Pages
import com.stevdza.san.kotlinbs.forms.BSInput
import com.stevdza.san.kotlinbs.forms.BSTextArea
import com.stevdza.san.kotlinbs.models.InputSize
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun ContactUsSection(
    modifier: Modifier = Modifier,
    onMessageSend: (name: String, email: String, organization: String, message: String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(30.px)
            .height(Height.FitContent),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(MainTextVariant)
                .toAttrs()
        ) {
            Text(Pages.Contact_Section.Landing.title)
        }
        ImageBox(
            image = Images.ContactPage.planet404,
            imageDesc = "Planet 404"
        )
        BackingCard(
            modifier = Modifier
                .fillMaxWidth(),
            variant = NoBorderBackingCardVariant,
            firstSection = {
                ComposeMessageSection(onMessageSend = onMessageSend)
            },
            secondSection = { ContactInformationSection() }
        )
    }
}

@Composable
fun ComposeMessageSection(
    modifier: Modifier = Modifier,
    onMessageSend: (name: String, email: String, organization: String, message: String) -> Unit
) = with(Pages.Contact_Section.Landing) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        messaageUIModel.apply {
            MessageSubSection(fullName, email, message, organization, onMessageSend)
        }
    }
}

val MessageTextInputStyle by ComponentStyle {
    base {
        Modifier.color(Colors.DarkGray)
            .margin(bottom = 10.px)
    }
}

@Composable
fun MessageSubSection(
    name: String,
    email: String,
    organization: String,
    message: String,
    onMessageSend: (name: String, email: String, organization: String, message: String) -> Unit
) {
    var messengerName by remember { mutableStateOf(name) }
    var messengerEmail by remember { mutableStateOf(email) }
    var messengerOrganization by remember { mutableStateOf(organization) }
    var messengerMessage by remember { mutableStateOf(message) }
    Column(
        modifier = Modifier
            .fillMaxSize(80.percent),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BSInput(
            modifier = MessageTextInputStyle.toModifier()
                .fillMaxWidth(),
            value = messengerName,
            placeholder = "Your Name Here",
            id = "Name",
            type = InputType.Text,
            required = true,
            onValueChange = {
                messengerName = it
            }
        )
        BSInput(
            modifier = MessageTextInputStyle.toModifier()
                .fillMaxWidth(),
            value = messengerEmail,
            placeholder = "Email",
            id = "Email",
            type = InputType.Email,
            required = true,
            onValueChange = {
                messengerEmail = it
            }
        )
        BSInput(
            modifier = MessageTextInputStyle.toModifier()
                .fillMaxWidth(),
            value = messengerOrganization,
            placeholder = "Organization",
            id = "Organization",
            type = InputType.Text,
            onValueChange = {
                messengerOrganization = it
            }
        )
        BSTextArea(
            modifier = MessageTextInputStyle.toModifier()
                .fillMaxSize(),
            value = messengerMessage,
            placeholder = "Message...",
            id = "Message",
            size = InputSize.Large,
            onValueChange = {
                messengerMessage = it
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .weight(1)
                .margin(topBottom = 20.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ButtonDisplay(
                state = ButtonState(
                    buttonText = "Send Message",
                    onButtonClick = {
                        onMessageSend(
                            messengerName,
                            messengerEmail,
                            messengerOrganization,
                            messengerMessage
                        )
                    }
                ),
                variant = BlueButtonVariant,
                modifier = Modifier
                    .textAlign(TextAlign.Center)
            )
        }
    }
}

val ContactInfoStyle by ComponentStyle {
    base {
        Modifier
            .borderRadius(20.px)
            .border {
                width(1.px)
                style(LineStyle.Solid)
                color(Colors.Black)
            }
            .boxShadow(
                blurRadius = 2.px,
                color = Colors.DarkGray
            )
            .fillMaxHeight()
            .fillMaxWidth(80.percent)
    }
}
val ContactHeadingVariant by BaseTextStyle.addVariant {
    base {
        Modifier.fillMaxWidth()
            .padding(leftRight = 30.px)
            .fontWeight(FontWeight.Medium)
            .fontSize(FontSize.Large)
            .textAlign(TextAlign.Start)
    }
}
val ContactValueVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontWeight(FontWeight.Bold)
            .fontSize(FontSize.Medium)
            .padding(topBottom = 2.px, leftRight = 5.px)
            .textAlign(TextAlign.Start)
    }
}

val ContactTitleVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .textAlign(TextAlign.Center)
            .fontSize(FontSize.XXLarge)
    }
}

@Composable
fun ContactText(title: String, content: String, variant: ComponentVariant = ContactHeadingVariant) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        P(
            attrs = BaseTextStyle.toModifier(ContactHeadingVariant)
                .toAttrs()
        ) { Text(title) }
        P(
            attrs = BaseTextStyle.toModifier(ContactValueVariant)
                .toAttrs()
        ) { Text(content) }
    }
}

@Composable
fun ContactInformationSection(
    modifier: Modifier = Modifier
) = with(Pages.Contact_Section.Landing) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .height(Height.FitContent),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = ContactInfoStyle.toModifier()
                .fillMaxSize(80.percent)
                .height(Height.Inherit),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            P(
                attrs = BaseTextStyle.toModifier(ContactTitleVariant)
                    .toAttrs()
            ) {
                Text(title)
            }
            contactInfoUIModel.apply {
                ContactText("Address", address)
                ContactText("Phone Number", phone)
                ContactText("Email", email)
            }
        }
    }
}
