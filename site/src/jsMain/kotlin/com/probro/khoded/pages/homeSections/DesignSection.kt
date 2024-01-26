package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
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
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


@Composable
fun DesignSectionDisplay(data: Pages.Home_Section.Design) = with(data) {
    Row(
        modifier = BackgroundStyle.toModifier(DesignBackgroundVariant)
            .id(id),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        DesignTextSection(
            upperText = mainText,
            underlineImage = underlineImage,
            lowerText = subText,
            modifier = Modifier
                .fillMaxWidth()
        )
        DesignImageSection(
            firstImage = mainImage,
            secondImage = subImage,
            modifier = Modifier
                .fillMaxWidth()
                .translateY(ty = (-150).px)
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
        Column(
            modifier = Modifier
                .borderBottom {
                    width(5.px)
                    style(LineStyle.Solid)
                    color(Colors.RebeccaPurple)
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DesignHeading(upperText)
            Image(
                src = underlineImage,
                modifier = Modifier
                    .fillMaxWidth(40.percent)
                    .translateY(ty = (-30).px)
            )
        }
        DesignSubText(lowerText)
    }
}

@Composable
fun DesignSubText(lowerText: String) {
    P(
        attrs = BaseTextStyle.toModifier(HomeSubTitleStyle)
            .color(Color.black)
            .toAttrs()
    ) {
        Text(value = lowerText)
    }
}

const val LENGTH_OF_JUST = 4

@Composable
fun DesignHeading(upperText: String) {
    val justIndex = remember { upperText.indexOf("just") }
    val firstText = remember { upperText.substring(0, justIndex) }
    val just = remember { upperText.substring(justIndex, justIndex + LENGTH_OF_JUST) }
    val secondText = remember { upperText.substring(justIndex + LENGTH_OF_JUST) }
    P(
        attrs = BaseTextStyle.toModifier(HomeTitleVariant)
            .color(Color.black)
            .toAttrs()
    ) {
        Text(value = firstText)
        Span(
            attrs = Modifier
                .color(Color.mediumblue)
                .toAttrs()
        ) {
            Text(just)
        }
        Text(value = secondText)
    }
}

@Composable
fun DesignImageSection(
    firstImage: String,
    secondImage: String,
    modifier: Modifier = Modifier
) = with(Pages.Home_Section.Design) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            src = firstImage,
            modifier = Modifier
                .zIndex(2)
                .fillMaxWidth(30.percent)
                .align(Alignment.CenterStart)
                .translate(tx = (-80).px, ty = (-100).px)
        )
        Image(
            src = secondImage,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1)
        )
    }
}
