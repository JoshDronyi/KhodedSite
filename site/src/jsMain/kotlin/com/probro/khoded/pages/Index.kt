package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.probro.khoded.components.ErrorBoundary
import com.probro.khoded.components.ErrorBoundaryConfig
import com.probro.khoded.components.seo.KhodedSEOHead
import com.probro.khoded.components.seo.SEOConfigs
import com.probro.khoded.components.widgets.Footer
import com.probro.khoded.pages.homeSections.ModernAboutSection
import com.probro.khoded.pages.homeSections.ModernContactSection
import com.probro.khoded.pages.homeSections.ModernLandingHero
import com.probro.khoded.pages.homeSections.ModernServicesSection
import com.probro.khoded.utils.NavigationHeader
import com.probro.khoded.utils.NavigationRoute
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.WithNavigation
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Main

@Page
@Composable
fun Index() {
    val ctx = rememberPageContext()
    KhodedSEOHead(SEOConfigs.homePage())

    // Development vs Production configuration
    val errorConfig = ErrorBoundaryConfig(
        showStackTrace = true, // Set to true in development
        enableErrorReporting = true,
        fallbackTitle = "Khoded - Service Temporarily Unavailable",
        fallbackMessage = "We're experiencing technical difficulties." +
                " Our team has been notified and is working on a fix."
    )

    ErrorBoundary(
        config = errorConfig,
        onError = { error, errorInfo ->
            // Custom error handling for your agency
            console.error("Khoded website error:", error)
            // TODO: Integrate with your analytics/monitoring service
        }
    ) {
        WithNavigation { navigationState ->
            HomePageSections(
                Modifier
                    .maxWidth(100.vw)
                    .fillMaxWidth()
            ) { page ->
                ctx.router.navigateTo(page.path)
            }
            Footer()
        }
    }
}

@Composable
fun HomePageSections(
    modifier: Modifier = Modifier,
    onNavigate: (path: NavigationRoute) -> Unit
) {
    // Main content wrapper for accessibility
    Main(
        attrs = {
            id("main-content")
            attr("role", "main")
            attr("aria-label", "Main page content")
            style {
                width(100.vw)
                maxWidth(100.vw)
                margin(0.px)
                padding(0.px)
            }
        }
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top
        ) {
            ModernLandingHero(
                onNavigate = onNavigate
            )
            ModernServicesSection()
            ModernAboutSection()
            ModernContactSection()
        }
    }
}

