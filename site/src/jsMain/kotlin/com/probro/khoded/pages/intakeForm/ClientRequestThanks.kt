/*
package com.probro.khoded.pages.serviceSections

import androidx.compose.runtime.Composable
import com.probro.khoded.models.Images
import com.probro.khoded.styles.BaseTextStyle
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
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun ClientRequestThanks(
    uiModel: ClientRequestThanksUiModel,
    modifier: Modifier = Modifier
) = with(uiModel) {
    Box(
        modifier = Modifier.fillMaxWidth(80.percent),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .background(Colors.White)
                .border {
                    width(1.px)
                    color(Colors.DarkGray)
                    style(LineStyle.Solid)
                }
                .borderRadius(20.px)
                .padding(10.px)
                .margin(topBottom = 20.px),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(50.percent)
                    .padding(leftRight = 20.px),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                P(
                    attrs = BaseTextStyle.toModifier()
                        .fontSize(FontSize.XXLarge)
                        .fontWeight(FontWeight.Bold)
                        .textAlign(TextAlign.Start)
                        .toAttrs()
                ) {
                    Text(thanksMessage)
                }
                P(
                    attrs = BaseTextStyle.toModifier()
                        .fontSize(FontSize.Large)
                        .fontWeight(FontWeight.Medium)
                        .fontStyle(FontStyle.Italic)
                        .textAlign(TextAlign.Start)
                        .toAttrs()
                ) {
                    Text(thankssubMessage)
                }
            }
            Image(
                src = Images.ServicePage.faq,
                modifier = Modifier.fillMaxWidth(40.percent)
                    .objectFit(ObjectFit.Fill)
            )
        }
    }
}

data class ClientRequestThanksUiModel(
    val thanksMessage: String,
    val thankssubMessage: String
)*/
