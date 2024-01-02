package com.probro.khoded.pages.contactSections

import androidx.compose.runtime.*
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.stevdza.san.kotlinbs.forms.BSInput
import com.stevdza.san.kotlinbs.forms.BSTextArea
import com.stevdza.san.kotlinbs.models.InputSize
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
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
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.ComponentStyle
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
            .fillMaxWidth(80.percent)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fillMaxWidth()
                .fontSize(FontSize.XXLarge)
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Start)
                .toAttrs()
        ) {
            Text(Pages.Contact_Section.Landing.title)
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ComposeMessageSection(onMessageSend = onMessageSend)
            ContactInformationSection(
                Pages.Contact_Section.ContactInfo
            )
        }
    }
}

@Composable
fun ComposeMessageSection(
    modifier: Modifier = Modifier,
    onMessageSend: (name: String, email: String, organization: String, message: String) -> Unit
) = with(Pages.Contact_Section.Landing) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        message.apply {
            MessageSubSection(name, email, message, organization, onMessageSend)
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
            .fillMaxWidth()
            .fillMaxHeight(),
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
                .fillMaxWidth(),
            value = messengerMessage,
            placeholder = "Message...",
            id = "Message",
            size = InputSize.Large,
            onValueChange = {
                messengerMessage = it
            }
        )

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
            modifier = Modifier.fillMaxWidth()
                .margin(topBottom = 20.px)
                .textAlign(TextAlign.Center)
        )
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
    }
}
val ContactTextStyle by ComponentStyle {
    base {
        Modifier.fillMaxWidth()
            .padding(leftRight = 30.px)
    }
}

@Composable
fun ContactText(title: String, content: String, modifier: Modifier = ContactTextStyle.toModifier()) {
    SimpleGrid(
        numColumns(base = 1, md = 2),
        modifier = modifier
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fillMaxWidth(60.percent)
                .fontWeight(FontWeight.Medium)
                .fontSize(FontSize.Large)
                .textAlign(TextAlign.Start)
                .toAttrs()
        ) { Text(title) }
        P(
            attrs = BaseTextStyle.toModifier()
                .fillMaxWidth()
                .fontWeight(FontWeight.Bold)
                .fontSize(FontSize.Medium)
                .padding(topBottom = 2.px, leftRight = 5.px)
                .textAlign(TextAlign.Start)
                .toAttrs()
        ) { Text(content) }
    }
}

@Composable
fun ContactInformationSection(
    contactInfo: Pages.Contact_Section.ContactInfo,
    modifier: Modifier = Modifier
) = with(contactInfo) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = ContactInfoStyle.toModifier()
                .fillMaxWidth(60.percent),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            P(
                attrs = BaseTextStyle.toModifier()
                    .fontWeight(FontWeight.Bold)
                    .fontSize(FontSize.XLarge)
                    .toAttrs()
            ) {
                Text(title)
            }
            uiModel.apply {
                ContactText("Address", "$address\n$city, $zip")
            }
            ContactText("Phone Number", uiModel.phone)
            ContactText("Email", uiModel.email)
        }
    }
}
