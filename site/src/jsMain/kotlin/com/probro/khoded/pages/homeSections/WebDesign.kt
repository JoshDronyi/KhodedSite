package com.probro.khoded.pages.homeSections

import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.SingleBorderBackingCardVaiant
import androidx.compose.runtime.Composable
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.DoubleBorderBackingCardVaraint
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Constants.SECTION_HEIGHT
import com.probro.khoded.utils.Pages
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
fun WebDesignSectionDisplay(breakpoint: Breakpoint, data: Pages.Home_Section.WebDesign) = with(data) {
    Box(
        modifier = Modifier
            .id(id)
            .fillMaxWidth()
            .padding(topBottom = 15.px)
            .height(SECTION_HEIGHT.px),
        contentAlignment = Alignment.Center
    ) {
        BackingCard(
            breakpoint = breakpoint,
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
fun WebDesignText(data: Pages.Home_Section.WebDesign) = with(data) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .fontSize(48.px)
                .toAttrs()
        ) {
            Text(mainText)
        }
        P(
            attrs = Modifier.toAttrs()
        ) {
            Text(subText)
        }
        ButtonDisplay(learnMoreButton, PinkButtonVariant)
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
            image = Pages.Home_Section.WebDesign.image,
            imageDesc = "Depiction of Web design.",
            modifier = Modifier
                .objectFit(ObjectFit.Fill)
                .fillMaxHeight()
        )
    }
}
