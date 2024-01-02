package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Constants.SECTION_HEIGHT
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun HostingSectionDisplay(data: Pages.Home_Section.Hosting) = with(data) {
    Box(
        modifier = Modifier
            .id(id)
            .height(SECTION_HEIGHT.px)
            .fillMaxWidth(70.percent),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(80.percent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(60.percent)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageBox(
                    image = image,
                    imageDesc = "Image depicting hosting",
                    modifier = Modifier.objectFit(ObjectFit.Fill)
                        .fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                P(
                    attrs = BaseTextStyle.toModifier()
                        .fontSize(FontSize.Large)
                        .toAttrs()
                ) {
                    Text(title)
                }
                P(
                    attrs = BaseTextStyle.toModifier()
                        .fontSize(FontSize.XXLarge)
                        .toAttrs()
                ) {
                    Text(mainText)
                }
                P(
                    attrs = BaseTextStyle.toModifier()
                        .toAttrs()
                ) {
                    Text(subText)
                }
                ButtonDisplay(learnMoreButton, BlueButtonVariant)

            }
        }
    }
}