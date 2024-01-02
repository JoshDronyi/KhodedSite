package com.probro.khoded.pages.serviceSections

import androidx.compose.runtime.Composable
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
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
fun ServicesLanding(modifier: Modifier = Modifier) = with(Pages.Services_Section.Landing) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .id(Pages.Services_Section.Landing.id)
            .height(Height.MinContent),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(80.percent)
                .padding(20.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            P(
                attrs = BaseTextStyle.toModifier()
                    .fontSize(72.px)
                    .fontWeight(FontWeight.Bold)
                    .toAttrs()
            ) {
                Text(mainText)
            }
            P(
                attrs = BaseTextStyle.toModifier()
                    .fontSize(FontSize.Large)
                    .fontWeight(FontWeight.Medium)
                    .fontStyle(FontStyle.Italic)
                    .toAttrs()
            ) {
                Text(subText)
            }
        }
    }
}
