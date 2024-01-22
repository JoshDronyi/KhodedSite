package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.SingleBorderBackingCardVariant
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.P
import org.w3c.dom.Text


@Composable
fun DesignSectionDisplay(data: Pages.Home_Section.Design) = with(data) {
    Box(
        modifier = BackgroundStyle.toModifier(DesignBackgroundVariant)
            .id(id),
        contentAlignment = Alignment.Center
    ) {
        BackingCard(
            modifier = Modifier,
            variant = SingleBorderBackingCardVariant,
            firstSection = {
                DesignTextSection(
                    upperText = mainText,
                    underlineImage = underlineImage,
                    lowerText = subText
                )
            },
            secondSection = {
                DesignImageSection(
                    firstImage = mainImage,
                    secondImage = subImage
                )
            }
        )
    }
}

@Composable
fun DesignTextSection(
    upperText: String,
    underlineImage: String,
    lowerText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .color(Color.black)
                .fontWeight(FontWeight.Bold)
                .toAttrs()
        ) {
            Text(upperText)
        }
        Image(
            src = underlineImage,
            modifier = Modifier
                .fillMaxWidth(30.percent)
        )
        P(
            attrs = BaseTextStyle.toModifier()
                .color(Color.black)
                .toAttrs()
        ) {
            Text(lowerText)
        }

    }
}

@Composable
fun DesignImageSection(
    firstImage: String,
    secondImage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            src = firstImage,
            modifier = Modifier
        )
        Image(
            src = secondImage,
            modifier = Modifier
        )
    }
}
