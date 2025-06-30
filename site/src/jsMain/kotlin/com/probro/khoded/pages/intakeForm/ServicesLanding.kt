/*
package com.probro.khoded.pages.serviceSections

import androidx.compose.runtime.Composable
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.MainTextVariant
import com.probro.khoded.styles.SubTextVariant
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
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
                .fillMaxWidth()
                .padding(20.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            P(
                attrs = BaseTextStyle.toModifier(MainTextVariant)
                    .textAlign(TextAlign.Center)
                    .toAttrs()
            ) {
                Text(mainText)
            }
            P(
                attrs = BaseTextStyle.toModifier(SubTextVariant)
                    .fontStyle(FontStyle.Italic)
                    .textAlign(TextAlign.Center)
                    .toAttrs()
            ) {
                Text(subText)
            }
        }
    }
}
*/
