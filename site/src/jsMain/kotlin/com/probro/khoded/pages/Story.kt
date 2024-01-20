package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.pages.aboutSections.OpportunitiesSectionDisplay
import com.probro.khoded.pages.aboutSections.StorySectionDisplay
import com.probro.khoded.pages.aboutSections.TeamSectionDisplay
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext

@Page
@Composable
fun Story(modifier: Modifier = Modifier.fillMaxSize()) {
    val ctx = rememberPageContext()
    Scaffold(
        router = ctx.router,
        modifier = Modifier.fillMaxSize()
    ) { header, footer, modifier ->
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val baseModifier = Modifier
                .height(Height.FitContent)
            StorySectionDisplay(header, baseModifier)
            TeamSectionDisplay(baseModifier)
            OpportunitiesSectionDisplay(footer, baseModifier)
        }
    }
}
