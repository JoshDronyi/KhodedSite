package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Constants.SECTION_HEIGHT
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun WebDesignSectionDisplay(data: Pages.Home_Section.WebDesign) = with(data) {
    val bgModifier = Modifier
        .fillMaxWidth(80.percent)
        .border(
            width = 2.px,
            color = Colors.LightGray,
            style = LineStyle.Solid
        )
        .borderRadius(r = 20.px)
        .background(Colors.White)
    Box(
        modifier = Modifier
            .id(id)
            .padding(topBottom = 15.px)
            .fillMaxWidth(80.percent)
            .height(SECTION_HEIGHT.px),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = bgModifier
                .zIndex(2), horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ImageBox(
                    image = image,
                    imageDesc = "Depiction of Web design.",
                    modifier = Modifier
                        .objectFit(ObjectFit.Fill)
                        .fillMaxHeight()
                )
            }
        }
        Box(
            modifier = bgModifier
                .zIndex(1)
                .fillMaxWidth(75.percent)
                .fillMaxHeight(50.percent)
                .translateY(ty = 110.px)
        )
    }
}
