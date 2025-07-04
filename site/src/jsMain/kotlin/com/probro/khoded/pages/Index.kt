package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.probro.khoded.components.ErrorBoundary
import com.probro.khoded.components.ErrorBoundaryConfig
import com.probro.khoded.components.KhodedSEO
import com.probro.khoded.components.SEOHead
import com.probro.khoded.components.composables.popupscreen.PopUpScreen
import com.probro.khoded.pages.homeSections.ConsultationSectionDisplay
import com.probro.khoded.pages.homeSections.DesignSectionDisplay
import com.probro.khoded.pages.homeSections.LandingSectionDisplay
import com.probro.khoded.pages.homeSections.ServicesSectionDisplay
import com.probro.khoded.styles.animations.makeInvisibleKeyFrames
import com.probro.khoded.styles.animations.makeVisibleKeyFrames
import com.probro.khoded.styles.animations.shiftBackwardKeyframes
import com.probro.khoded.styles.animations.shiftForwardKeyFrames
import com.probro.khoded.styles.base.BodyStyle
import com.probro.khoded.styles.popups.MessagingPopUpTextVariant
import com.probro.khoded.utils.NavigationHeader
import com.probro.khoded.utils.NavigationRoute
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.WithNavigation
import com.probro.khoded.utils.popUp.PopUpStateHolders
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.ms

@Page
@Composable
fun Index() {
    val ctx = rememberPageContext()
    SEOHead(KhodedSEO.homePage, ctx)

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
        WithNavigation {
            NavigationHeader(navigationState = it)
            HomePageSections(BodyStyle.toModifier()) { page ->
                ctx.router.navigateTo(page.path)
            }
            PopUpComposable()
        }
    }
}

@Composable
fun HomePageSections(
    modifier: Modifier = Modifier,
    onNavigate: (path: NavigationRoute) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LandingSectionDisplay(
            data = Pages.Home_Section.Landing,
            onNavigate = onNavigate
        )
        ServicesSectionDisplay(Pages.Home_Section.Services)
        DesignSectionDisplay(Pages.Home_Section.Design)
        ConsultationSectionDisplay(
            data = Pages.Home_Section.Consultation
        )
    }
}

@Composable
fun PopUpComposable(modifier: Modifier = Modifier) {
    val popUpState by PopUpStateHolders.MessagingPopUpStateHolder.popUpState.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        with(popUpState) {
            PopUpScreen(
                popUpUIModel = this,
                textVariant = MessagingPopUpTextVariant,
                modifier = Modifier
                    .animation(
                        if (isShowing) makeVisibleKeyFrames.toAnimation(300.ms)
                        else makeInvisibleKeyFrames.toAnimation(300.ms),
                        if (isShowing) shiftForwardKeyFrames.toAnimation(300.ms)
                        else shiftBackwardKeyframes.toAnimation(300.ms)
                    )
            )
        }
    }
}
