package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.ErrorBoundary
import com.probro.khoded.components.ErrorBoundaryConfig
import com.probro.khoded.pages.aboutSections.OpportunitiesSectionDisplay
import com.probro.khoded.pages.aboutSections.StorySectionDisplay
import com.probro.khoded.pages.aboutSections.TeamSectionDisplay
import com.probro.khoded.utils.NavigationHeader
import com.probro.khoded.utils.WithNavigation
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext

@Page
@Composable
fun About() {
    val ctx = rememberPageContext()
    // Development vs Production configuration
    val errorConfig = ErrorBoundaryConfig(
        showStackTrace = false, // Set to true in development
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
        WithNavigation {
            NavigationHeader(navigationState = it)
            StorySectionDisplay()
            TeamSectionDisplay()
            OpportunitiesSectionDisplay()
        }
    }
}
