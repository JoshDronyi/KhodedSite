package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.DoubleBorderBackingCardVaraint
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.styles.MainTextVariant
import com.probro.khoded.styles.SubTextVariant
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
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
fun WebDesignSectionDisplay(data: Pages.Home_Section.Services) = with(data) {
    Box(
        modifier = Modifier
            .id(id)
            .fillMaxWidth()
            .padding(topBottom = 20.px, leftRight = 10.px)
            .height(Height.FitContent),
        contentAlignment = Alignment.Center
    ) {
        BackingCard(
            modifier = Modifier
                .zIndex(2),
            variant = DoubleBorderBackingCardVaraint,
            firstSection = { WebDesignText(data) },
            secondSection = { WebDesignImage() }
        )
        Box(
            modifier = Modifier
                .zIndex(1)
                .fillMaxWidth(75.percent)
                .fillMaxHeight(50.percent)
                .translateY(ty = 110.px)
        )
    }
}

@Composable
fun WebDesignText(data: Pages.Home_Section.Services) = with(data) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(leftRight = 16.px, topBottom = 20.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier(MainTextVariant)
                .toAttrs()
        ) {
            Text(mainText)
        }
        P(
            attrs = BaseTextStyle.toModifier(SubTextVariant)
                .toAttrs()
        ) {
            Text(subText)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(topBottom = 15.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ButtonDisplay(learnMoreButton, PinkButtonVariant)
        }
    }
}

@Composable
fun WebDesignImage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImageBox(
            image = Pages.Home_Section.Services.mainImage,
            imageDesc = "Depiction of Web design.",
            modifier = ImageStyle.toModifier()
        )
    }
}
