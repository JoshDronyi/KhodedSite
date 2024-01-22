package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.InputSize
import com.varabyte.kobweb.silk.components.forms.TextInput
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun ConsultationSectionDisplay(
    footer: @Composable () -> Unit,
    data: Pages.Home_Section.Consultation
) = with(data) {
    Column(
        modifier = BackgroundStyle.toModifier(ConsultationBackgroundVariant)
            .id(id),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                src = mainImage,
                description = "Message Bubbles",
                modifier = Modifier.fillMaxWidth(60.percent)
                    .align(Alignment.Start)
            )
            Row(
                modifier = Modifier
                    .zIndex(2)
                    .fillMaxWidth(80.percent),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ConsultationTextSection(
                    mainText = mainText,
                    clientRequestUIModel = consultationRequestUIModel,
                    ctaButton = data.ctaButton,
                    modifier = Modifier.fillMaxWidth()
                        .fillMaxHeight()
                )
                Image(
                    src = data.subImage,
                    modifier = Modifier
                        .fillMaxWidth(40.percent)
                )
            }
        }
        QuoteSection(leftQuote, subText, rightQuote)
        footer()
    }
}

@Composable
fun QuoteSection(
    leftQuote: String,
    subText: String,
    rightQuote: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(80.percent)
            .margin(topBottom = 50.px),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(10.percent)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                src = leftQuote,
                description = "Open Quote",
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        P(
            attrs = BaseTextStyle.toModifier()
                .fillMaxWidth()
                .color(Color.white)
                .toAttrs()
        ) {
            Text(subText)
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(10.percent),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                src = rightQuote,
                description = "Close Quote",
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun ConsultationTextSection(
    mainText: String,
    clientRequestUIModel: Pages.Home_Section.ConsultationRequestUIModel,
    ctaButton: ButtonState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .toAttrs()
        ) {
            Text(mainText)
        }
        MessagingSection(
            clientRequestUIModel,
        )
        ButtonDisplay(
            state = ctaButton,
            variant = PinkButtonVariant,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun MessagingSection(
    clientRequestUIModel: Pages.Home_Section.ConsultationRequestUIModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        with(clientRequestUIModel) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextBox(fullName) {
                    fullName = it
                }
                TextBox(email) {
                    email = it
                }
            }
            TextInput(
                text = projectSynopsis,
                size = InputSize.LG,
                onTextChanged = {
                    projectSynopsis = it
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun TextBox(
    value: String,
    onValueChange: (newText: String) -> Unit
) {
    TextInput(
        text = value,
        onTextChanged = {
            onValueChange(it)
        }
    )
}
