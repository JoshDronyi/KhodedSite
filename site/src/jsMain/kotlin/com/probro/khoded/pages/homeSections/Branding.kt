package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.components.composables.SingleBorderBackingCardVaiant
import com.probro.khoded.styles.*
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun BrandingSectionDisplay(data: Pages.Home_Section.Branding) = with(data) {
    Box(
        modifier = Modifier
            .id(id)
            .fillMaxWidth()
            .height(Height.FitContent)
            .padding(topBottom = 20.px, leftRight = 10.px),
        contentAlignment = Alignment.Center
    ) {
        BackingCard(
            modifier = Modifier,
            variant = NoBorderBackingCardVariant,
            firstSection = { BrandingText(data) },
            secondSection = { BrandingImage(data) }
        )
    }
}

@Composable
fun BrandingText(data: Pages.Home_Section.Branding) = with(data) {
    Column(
        modifier = Modifier.fillMaxWidth(80.percent)
            .padding(leftRight = 16.px, topBottom = 20.px),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier(TitleTextVariant)
                .toAttrs()
        ) {
            Text(title)
        }
        P(attrs = BaseTextStyle.toModifier(MainTextVariant).toAttrs()) {
            Text(mainText)
        }
        P(attrs = BaseTextStyle.toModifier(SubTextVariant).toAttrs()) {
            Text(subText)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(topBottom = 15.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ButtonDisplay(tryItButton, BlueButtonVariant)
        }
    }
}

@Composable
fun BrandingImage(data: Pages.Home_Section.Branding) = with(data) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ImageBox(
            image = image,
            imageDesc = "Depiction of branding.",
            modifier = ImageStyle.toModifier()
        )
    }
}
