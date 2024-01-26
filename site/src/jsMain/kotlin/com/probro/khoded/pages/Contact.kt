package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.BaseButtonTextVariant
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.widgets.ContactPageHeaderVariant
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.pages.homeSections.MessageArea
import com.probro.khoded.pages.homeSections.TextBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
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
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
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
    Scaffold(
        router = ctx.router
    ) { header, footer, modifier ->
        with(Pages.Contact_Section.Landing) {
            Column(
                modifier = modifier
                    .id(Pages.Contact_Section.Landing.id)
                    .height(Height.FitContent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LandingSection(header)
                FooterSection(
                    data = messaageUIModel,
                    footer = footer
                )
            }
        }
    }
}

@Composable
fun FooterSection(
    data: Pages.Contact_Section.MessaageUIModel,
    modifier: Modifier = Modifier,
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
            modifier = modifier
        )
        footer()
    }
}

@Composable
fun LandingSection(
    header: @Composable (variant: ComponentVariant?) -> Unit
) = with(Pages.Contact_Section.Landing) {
    Column(
        modifier = BackgroundStyle.toModifier(ContactLandingBackgroundVariant),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        header(ContactPageHeaderVariant)
        Row(
            modifier = ContactPageRowStyle.toModifier(LandingSectionVariant),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ClientContactInfoDisplay(
                mainText,
                subText,
                messaageUIModel,
            )
            CompanyContactInfoSection(
                image = mainImage,
                contactInfoUIModel = contactInfoUIModel,
            )
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
}

@Composable
fun ClientContactInfoDisplay(
    mainText: String,
    subText: String,
    data: Pages.Contact_Section.MessaageUIModel
) = with(data) {
    Box(
        modifier = ContactSectionContainerStyle.toModifier(ClientInfoContainerVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(80.percent)
        ) {
            ClientInfoTitle(mainText, subText)
            ClientInfoInputDisplay(data)
        }
    }
}

@Composable
fun ClientInfoTitle(mainText: String, subText: String) {
    P(
        attrs = BaseTextStyle.toModifier(ClientInfoPromptVariant)
            .toAttrs()
    ) {
        Span(
            attrs = Modifier
                .color(Colors.Purple)
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
    data: Pages.Contact_Section.MessaageUIModel
) =
    with(data) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight()
                .margin(top = 20.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            TextBox(
                value = fullName,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                fullName = it
            }
            TextBox(
                value = email,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                email = it
            }
            TextBox(
                value = organization,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                organization = it
            }
            TextBox(
                value = messageSubject,
                modifier = ClientInfoTextBoxStyle.toModifier()
            ) {
                messageSubject = it
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
) = with(data) {
    Row(
        modifier = ContactPageRowStyle.toModifier(MessagingSectionVariant)
            .then(modifier),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputDisplays(
            message = message,
            ctaButton = ctaButton
        ) { newText ->
            message = newText
        }
        SpacerSection()
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
    onTextChange: (newText: String) -> Unit
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
            MessageArea(
                value = message,
                modifier = ClientInfoTextBoxStyle.toModifier(ClientInfoTextAreaVariant)
            ) { newText ->
                onTextChange(newText)
            }
            ButtonDisplay(
                state = ctaButton,
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
