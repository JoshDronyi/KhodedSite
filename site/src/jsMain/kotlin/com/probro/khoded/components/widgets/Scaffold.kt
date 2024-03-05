package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.probro.khoded.components.composables.HeaderLogoTextVariant
import com.probro.khoded.models.Routes
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    onNavigate: (path: String) -> Unit,
    context: @Composable (
        header: @Composable (variant: ComponentVariant?, textVariant: ComponentVariant?) -> Unit,
        footer: @Composable (variant: ComponentVariant?) -> Unit,
        modifier: Modifier,
    ) -> Unit
) {
    val navState by Navigator.pageState.collectAsState()
    val breakpoint = rememberBreakpoint()
    Box(
        modifier = Modifier
            .height(100.vh)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        context(
            { variant, textVariant ->
                Header(
                    modifier = Modifier.fillMaxWidth(
                        when (breakpoint) {
                            Breakpoint.ZERO, Breakpoint.SM -> 100.percent
                            Breakpoint.MD, Breakpoint.LG, Breakpoint.XL -> 90.percent
                        }
                    ),
                    variant = variant,
                    textVariant = textVariant ?: HeaderLogoTextVariant
                ) {
                    with(it) {
                        onNavigate(path)
//                        if (navState.currentSection?.slug == slug) {
////                            val id = path.substring(path.indexOf("/"))
//                            onNavigate(path)
//                        } else onNavigate(slug)
                    }
                }
            },
            { variant ->
                Footer(
                    modifier = Modifier.fillMaxWidth()
                        .margin(top = 40.px, bottom = 10.px),
                    variant = variant
                ) {
                    onNavigate(it.path)
                }
            },
            modifier.height(100.vh)
                .zIndex(1)
                .fillMaxWidth()
                .scrollBehavior(ScrollBehavior.Smooth)
        )
    }
    LaunchedEffect(navState.currentSection) {
        when (navState.currentSection?.path) {
            Pages.Home_Section.Landing.path -> onNavigate(Routes.Home.SLUG)
            Pages.Story_Section.OurStory.path -> onNavigate(Routes.Story.SLUG)
            Pages.Contact_Section.Landing.path -> onNavigate(Routes.Contact.SLUG)
            else -> navState.currentSection?.path?.let {
                println("Navigating to $it")
                onNavigate(it)
            }
        }
    }
}
