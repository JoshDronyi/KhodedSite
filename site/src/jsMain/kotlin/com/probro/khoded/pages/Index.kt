package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.pages.homeSections.ConsultationSectionDisplay
import com.probro.khoded.pages.homeSections.HostingSectionDisplay
import com.probro.khoded.pages.homeSections.LandingSectionDisplay
import com.probro.khoded.pages.homeSections.WebDesignSectionDisplay
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.style.ComponentVariant

@Page("/index")
@Composable
fun Index() {
    val ctx = rememberPageContext()
    Scaffold(
        router = ctx.router
    ) { header, footer, modifier ->
        HomePageSections(header, footer, modifier)
    }
}

@Composable
fun HomePageSections(
    header: @Composable (variant: ComponentVariant?) -> Unit,
    footer: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LandingSectionDisplay(header, Pages.Home_Section.LandingData)
        WebDesignSectionDisplay(Pages.Home_Section.Services)
        HostingSectionDisplay(Pages.Home_Section.Design)
        ConsultationSectionDisplay(footer, Pages.Home_Section.Consultation)
    }
}
