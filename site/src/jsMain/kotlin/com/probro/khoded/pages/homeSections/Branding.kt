package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.SingleBorderBackingCardVaiant
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Constants
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun BrandingSectionDisplay(breakpoint: Breakpoint, data: Pages.Home_Section.Branding) = with(data) {
    Box(
        modifier = Modifier
            .id(id)
            .height(Constants.SECTION_HEIGHT.px)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BackingCard(
            breakpoint = breakpoint,
            modifier = Modifier
                .fillMaxWidth(),
            variant = SingleBorderBackingCardVaiant,
            firstSection = { BrandingText(data) },
            secondSection = { BrandingImage(data) }
        )
    }
}

@Composable
fun BrandingText(data: Pages.Home_Section.Branding) = with(data) {
    Column(
        modifier = Modifier
            .fillMaxWidth(60.percent),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(FontSize.Medium)
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
                .fontSize(FontSize.Large)
                .toAttrs()
        ) {
            Text(subText)
        }
        ButtonDisplay(tryItButton, BlueButtonVariant)
    }
}

@Composable
fun BrandingImage(data: Pages.Home_Section.Branding) = with(data) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImageBox(
            image = image,
            imageDesc = "Depiction of branding.",
            modifier = Modifier.objectFit(ObjectFit.Fill)
                .fillMaxSize()
        )
    }
}
