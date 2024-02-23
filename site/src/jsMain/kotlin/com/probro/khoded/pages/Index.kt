package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.widgets.Scaffold
import com.probro.khoded.pages.homeSections.ConsultationSectionDisplay
import com.probro.khoded.pages.homeSections.DesignSectionDisplay
import com.probro.khoded.pages.homeSections.LandingSectionDisplay
import com.probro.khoded.pages.homeSections.ServicesSectionDisplay
import com.probro.khoded.utils.PageSection
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
        onNavigate = { path ->
            ctx.router.navigateTo(path)
        }
    ) { header, footer, modifier ->
        HomePageSections(header, footer, modifier) { page ->
            ctx.router.navigateTo(page.path)
        }
    }
}

@Composable
fun HomePageSections(
    header: @Composable (variant: ComponentVariant?, textVariant: ComponentVariant?) -> Unit,
    footer: @Composable (variant: ComponentVariant?) -> Unit,
    modifier: Modifier = Modifier,
    onNavigate: (page: PageSection) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LandingSectionDisplay(
            header = header,
            data = Pages.Home_Section.Landing,
            onNavigate = onNavigate
        )
        ServicesSectionDisplay(Pages.Home_Section.Services)
        DesignSectionDisplay(Pages.Home_Section.Design)
        ConsultationSectionDisplay(footer, Pages.Home_Section.Consultation)
    }
}
