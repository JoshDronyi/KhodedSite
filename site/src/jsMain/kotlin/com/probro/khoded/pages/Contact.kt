package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.probro.khoded.BaseButtonTextVariant
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.widgets.ContactPageHeaderVariant
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.pages.homeSections.TextBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.InputSize
import com.varabyte.kobweb.silk.components.forms.TextInput
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Page
@Composable
fun Contact() {
    val ctx = rememberPageContext()
    val scope = rememberCoroutineScope()
    Scaffold(
        router = ctx.router
    ) { header, footer, modifier ->
        with(Pages.Contact_Section.Landing) {
            Column(
                modifier = modifier
                    .id(Pages.Contact_Section.Landing.id),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                LandingSection(header)
                MessageDisplay(
                    message = messaageUIModel.message,
                    ctaButton = ctaButton,
                    footer = footer
                ) { newText ->
                    messaageUIModel.message = newText
                }
            }
        }
    }
}

@Composable
fun LandingSection(header: @Composable (variant: ComponentVariant?) -> Unit) =
    with(Pages.Contact_Section.Landing) {
        Column(
            modifier = BackgroundStyle.toModifier(ClientContactBackgroundVariant)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            header(ContactPageHeaderVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth(80.percent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                MainContactDisplay(
                    mainText,
                    messaageUIModel,
                    Modifier
                )
                ContactInfoSection(
                    image = mainImage,
                    contactInfoUIModel = contactInfoUIModel
                )
            }
        }
    }

val ClientContactBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.SkyBlue,
                    to = Colors.WhiteSmoke
                )
            )
    }
}
val MessagingSectionVariant by BackgroundStyle.addVariant {
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

@Composable
fun ClientContactSection(
    mainText: String,
    messageUIModel: Pages.Contact_Section.MessaageUIModel,
    modifier: Modifier = Modifier
) = with(messageUIModel) {


}

@Composable
fun MainContactDisplay(
    mainText: String,
    data: Pages.Contact_Section.MessaageUIModel,
    modifier: Modifier,
) = with(data) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .toAttrs()
        ) {
            Text(mainText)
        }
        TextBox(fullName) {
            fullName = it
        }
        TextBox(email) {
            email = it
        }
        TextBox(organization) {
            organization = it
        }
        TextInput(
            size = InputSize.LG,
            text = messageSubject,
            onTextChanged = {
                messageSubject = it
            }
        )
    }
}

@Composable
fun ContactInfoSection(
    image: String,
    contactInfoUIModel: Pages.Contact_Section.ContactInfoUIModel
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            src = image,
            description = "Planet 404",
            modifier = Modifier
        )
        ContactInfoDisplay(contactInfoUIModel)
    }
}

@Composable
fun ContactInfoDisplay(contactInfoUIModel: Pages.Contact_Section.ContactInfoUIModel) = with(contactInfoUIModel) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .textAlign(TextAlign.End)
                .toAttrs()
        ) {
            Text(phone)
        }
        P(
            attrs = BaseTextStyle.toModifier()
                .textAlign(TextAlign.End)
                .toAttrs()
        ) {
            Text(email)
        }
        P(
            attrs = BaseTextStyle.toModifier()
                .textAlign(TextAlign.End)
                .toAttrs()
        ) {
            Text(address)
        }
    }
}

@Composable
fun MessageDisplay(
    ctaButton: ButtonState,
    message: String,
    footer: @Composable () -> Unit,
    onTextChange: (newText: String) -> Unit,
) {
    Column(
        modifier = BackgroundStyle.toModifier(MessagingSectionVariant),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessagingSection(message, onTextChange, ctaButton)
        footer()
    }
}

@Composable
fun MessagingSection(
    message: String,
    onTextChange: (newText: String) -> Unit,
    ctaButton: ButtonState
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextInput(
            text = message,
            onTextChanged = { newText ->
                onTextChange(newText)
            }
        )
        ButtonDisplay(
            state = ctaButton,
            BlueButtonVariant
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

