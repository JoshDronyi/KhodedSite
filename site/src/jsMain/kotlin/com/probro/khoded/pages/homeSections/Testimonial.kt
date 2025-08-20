/*
package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.models.Testimonial
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.fa.FaStar
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.icons.fa.IconStyle
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import com.probro.khoded.utils.Constants


@Composable
fun TestimonialSectionDisplay(data: Pages.Home_Section.Testimonials) = with(data) {
    Box(
        modifier = Modifier
            .id(id)
            .height(Constants.SECTION_HEIGHT.px)
            .fillMaxWidth(80.percent)
            .padding(all = 10.px),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            P(
                attrs = BaseTextStyle.toModifier()
                    .fontSize(FontSize.XXLarge)
                    .fontWeight(FontWeight.Bold)
                    .toAttrs()
            ) {
                Text(title)
            }
            P(
                attrs = BaseTextStyle.toModifier()
                    .fontSize(FontSize.Large)
                    .toAttrs()
            ) {
                Text(mainText)
            }
            SimpleGrid(
                numColumns = numColumns(base = 1, md = 3),
                modifier = Modifier.fillMaxWidth(80.percent)
                    .fillMaxHeight(),
            ) {
                testimonialList.forEach {
                    TestimonialDisplay(it)
                }
            }
        }
    }
}

@Composable
fun TestimonialDisplay(testimonial: Testimonial, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(80.percent)
                .border(width = 2.px, style = LineStyle.Solid, color = Colors.DarkGray)
                .borderRadius(r = 20.px)
                .padding(25.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            P(
                attrs = BaseTextStyle.toModifier()
                    .fillMaxWidth()
                    .padding(10.px)
                    .fontSize(FontSize.Medium)
                    .toAttrs()
            ) {
                Text(testimonial.review)
            }
            StarRating(testimonial.rating, Modifier.padding(topBottom = 10.px))
            P(
                attrs = BaseTextStyle.toModifier()
                    .fontSize(FontSize.Medium)
                    .fontWeight(FontWeight.Bold)
                    .padding(top = 20.px)
                    .toAttrs()
            ) {
                Text(testimonial.from)
            }
            P(
                attrs = BaseTextStyle.toModifier()
                    .fontSize(FontSize.Smaller)
                    .color(Colors.DarkGray)
                    .toAttrs()
            ) {
                Text("${testimonial.position}/${testimonial.organization}")
            }
        }
    }
}

@Composable
fun StarRating(rating: Int, modifier: Modifier) {
    val value = if (rating > 5) 5 else if (rating < 0) 0 else rating
    val remainder = 5 - value
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(times = value) {
            FaStar(
                modifier = Modifier,
                style = IconStyle.FILLED,
                size = IconSize.SM
            )
        }
        repeat(times = remainder) {
            FaStar(
                modifier = Modifier,
                style = IconStyle.OUTLINE,
                size = IconSize.SM
            )
        }
    }
}
*/
