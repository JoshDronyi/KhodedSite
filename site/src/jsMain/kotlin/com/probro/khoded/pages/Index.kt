package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.pages.homeSections.*
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.css.px

@Page
@Composable
fun Index() {
    val ctx = rememberPageContext()
    Scaffold(
        route = ctx.route,
        router = ctx.router
    ) {
        HomePageSections()
    }
}

@Composable
fun HomePageSections(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(leftRight = 10.px),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LandingSectionDisplay(Pages.Home_Section.LandingData)
        WebDesignSectionDisplay(Pages.Home_Section.WebDesign)
        HostingSectionDisplay(Pages.Home_Section.Hosting)
        BrandingSectionDisplay(Pages.Home_Section.Branding)
        ConsultationSectionDisplay(Pages.Home_Section.Consultation)
//        TestimonialSectionDisplay(Pages.Home_Section.Testimonials)
        GetStartedSectionDisplay(Pages.Home_Section.GET_STARTED)
    }
}
