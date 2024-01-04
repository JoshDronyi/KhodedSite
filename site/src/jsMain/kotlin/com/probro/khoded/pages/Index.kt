package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.pages.homeSections.*
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint

@Page("/index")
@Composable
fun Index() {
    val ctx = rememberPageContext()
    Scaffold(
        router = ctx.router
    ) {
        HomePageSections()
    }
}

@Composable
fun HomePageSections(modifier: Modifier = Modifier) {
    val breakpoint = rememberBreakpoint()
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LandingSectionDisplay(breakpoint, Pages.Home_Section.LandingData)
        WebDesignSectionDisplay(breakpoint, Pages.Home_Section.WebDesign)
        HostingSectionDisplay(breakpoint, Pages.Home_Section.Hosting)
        BrandingSectionDisplay(breakpoint, Pages.Home_Section.Branding)
        ConsultationSectionDisplay(breakpoint, Pages.Home_Section.Consultation)
//        TestimonialSectionDisplay(Pages.Home_Section.Testimonials)
        GetStartedSectionDisplay(breakpoint, Pages.Home_Section.GET_STARTED)
    }
}
