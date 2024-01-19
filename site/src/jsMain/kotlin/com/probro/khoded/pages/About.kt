package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.models.Routes
import com.probro.khoded.pages.aboutSections.*
import com.probro.khoded.utils.Navigator
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext

@Page
@Composable
fun About(modifier: Modifier = Modifier.fillMaxSize()) {
    val ctx = rememberPageContext()
    Scaffold(
        router = ctx.router,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .height(Height.FitContent)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val baseModifier = Modifier
                .height(Height.FitContent)
            Navigator.sections[Navigator.KeySections.About]?.forEach { section ->
                baseModifier.id(section.id)
                when (section.id) {
                    Routes.Story.LANDING -> AboutLandingDisplay(baseModifier)

                    Routes.Story.FOUNDERS -> TeamSectionDisplay(baseModifier)

                    Routes.Story.OUR_STORY -> StorySectionDisplay(baseModifier)
                    Routes.Story.SEPARATOR -> SeparatorSectionDisplay(baseModifier)
                    Routes.Story.JOIN_OUR_TEAM -> OpportunitiesSectionDisplay(baseModifier)
                }
            }
        }
    }
}
