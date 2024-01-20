package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.SingleBorderBackingCardVaiant
import com.probro.khoded.styles.*
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Text


@Composable
fun HostingSectionDisplay(data: Pages.Home_Section.Design) = with(data) {
    Box(
        modifier = Modifier
            .id(id)
            .height(Height.FitContent)
            .padding(topBottom = 20.px, leftRight = 10.px)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BackingCard(
            modifier = Modifier,
            variant = SingleBorderBackingCardVaiant,
            firstSection = { Text(data.mainText) },
            secondSection = { Text(data.subText) }
        )
    }
}
