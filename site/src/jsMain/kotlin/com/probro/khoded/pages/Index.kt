package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.probro.khoded.components.ErrorBoundary
import com.probro.khoded.components.ErrorBoundaryConfig
import com.probro.khoded.components.seo.KhodedSEOHead
import com.probro.khoded.components.seo.SEOConfigs
import com.probro.khoded.components.widgets.Footer
// import com.probro.khoded.components.composables.popupscreen.PopUpScreen - disabled
import com.probro.khoded.pages.homeSections.ModernAboutSection
import com.probro.khoded.pages.homeSections.ModernContactSection
import com.probro.khoded.pages.homeSections.ModernLandingHero
import com.probro.khoded.pages.homeSections.ModernServicesSection
// import com.probro.khoded.styles.animations.makeInvisibleKeyFrames - disabled
// import com.probro.khoded.styles.animations.makeVisibleKeyFrames - disabled
// import com.probro.khoded.styles.animations.shiftBackwardKeyframes - disabled
// import com.probro.khoded.styles.animations.shiftForwardKeyFrames - disabled
// import com.probro.khoded.styles.base.BodyStyle - not found, removed
// import com.probro.khoded.styles.popups.MessagingPopUpTextVariant - disabled
import com.probro.khoded.utils.NavigationHeader
import com.probro.khoded.utils.NavigationRoute
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.WithNavigation
// import com.probro.khoded.utils.popUp.PopUpStateHolders - disabled
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
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw

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
            // PopUpComposable() - disabled
        }
    }
}

@Composable
fun HomePageSections(
    modifier: Modifier = Modifier,
    onNavigate: (path: NavigationRoute) -> Unit
) {
    // Main content wrapper for accessibility
    org.jetbrains.compose.web.dom.Main(
        attrs = {
            id("main-content")
            attr("role", "main")
            attr("aria-label", "Main page content")
        }
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
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

// @Composable
// fun PopUpComposable(modifier: Modifier = Modifier) {
//     val popUpState by PopUpStateHolders.MessagingPopUpStateHolder.popUpState.collectAsState()
//
//     Box(
//         contentAlignment = Alignment.Center,
//         modifier = modifier
//     ) {
//         with(popUpState) {
//             PopUpScreen(
//                 popUpUIModel = this,
//                 textVariant = MessagingPopUpTextVariant,
//                 modifier = Modifier
//                     .animation(
//                         if (isShowing) makeVisibleKeyFrames.toAnimation(300.ms)
//                         else makeInvisibleKeyFrames.toAnimation(300.ms),
//                         if (isShowing) shiftForwardKeyFrames.toAnimation(300.ms)
//                         else shiftBackwardKeyframes.toAnimation(300.ms)
//                     )
//             )
//         }
//     }
// } - disabled

