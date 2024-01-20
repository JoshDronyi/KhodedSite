package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.DoubleBorderBackingCardVaraint
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.px
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
            firstSection = { Text(data.path) },
            secondSection = { Text(data.title) }
        )
    }
}