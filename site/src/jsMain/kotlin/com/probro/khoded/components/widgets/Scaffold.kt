package com.probro.khoded.components.widgets

import androidx.compose.runtime.*
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.navigation.Router
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.*

@Composable
fun Scaffold(
    router: Router,
    modifier: Modifier = Modifier,
    context: @Composable ColumnScope.() -> Unit
) {
    var showSideNav by remember { mutableStateOf(false) }
    val navState by Navigator.pageState.collectAsState()
    Box(
        modifier = Modifier
            .height(100.vh)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SideNavigation(
            modifier = Modifier
                .fillMaxWidth(if (showSideNav) getWidthFromBreakpoint() else 0.percent)
                .align(Alignment.TopStart)
                .fillMaxHeight()
                .zIndex(if (showSideNav) 2 else 0)
                .opacity(if (showSideNav) 100.percent else 0.percent)
                .background(Color.black)
                .transition(
                    CSSTransition("opacity", duration = 300.ms),
                    CSSTransition("width", duration = 300.ms)
                ),
            onSideNavToggle = {
                showSideNav = false
            }
        ) { section ->
            Navigator.navigateTo(section)
        }
        Column(
            modifier = modifier.height(100.vh)
                .zIndex(1)
                .fillMaxWidth()
                .scrollBehavior(ScrollBehavior.Smooth),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(
                modifier = Modifier.fillMaxWidth()
                    .padding(topBottom = 20.px),
                onShouldShowClick = {
                    showSideNav = !showSideNav
                    println("Showside nav is now $showSideNav")
                }
            ) { path: PageSection ->
                Navigator.navigateTo(path)
                //router.navigateTo(path)
            }
            context()
            Footer(
                modifier = Modifier.fillMaxWidth()
            ) { path ->
                Navigator.navigateTo(path)
                // router.navigateTo(path)
            }
        }
    }
    LaunchedEffect(navState.currentSection) {
        router.navigateTo(navState.currentSection.path)
    }
}

@Composable
private fun getWidthFromBreakpoint(): CSSSizeValue<CSSUnit.percent> {
    return when (rememberBreakpoint()) {
        Breakpoint.ZERO, Breakpoint.SM, Breakpoint.MD -> 90.percent
        Breakpoint.LG, Breakpoint.XL -> 50.percent
    }
}