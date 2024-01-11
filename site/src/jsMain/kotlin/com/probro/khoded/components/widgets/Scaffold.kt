package com.probro.khoded.components.widgets

import androidx.compose.runtime.*
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.OnAmbiguousScaffoldClick
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
import com.varabyte.kobweb.core.PageContext
import com.varabyte.kobweb.navigation.Router
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import org.jetbrains.compose.web.css.*

const val SCAFFOLD_ID = "#scaffold"
const val MAIN_CONTENT_ID = "#mainContent"
const val SIDE_NAV_ID = "#sideNav"

@Composable
fun Scaffold(
    route: PageContext.RouteInfo,
    router: Router,
    modifier: Modifier = Modifier,
    context: @Composable ColumnScope.(modifier: Modifier) -> Unit
) {
    var showSideNav by remember { mutableStateOf(false) }
    val navState by Navigator.pageState.collectAsState()
    OnAmbiguousScaffoldClick {
        showSideNav = false
    }

    Box(
        modifier = Modifier
            .id(SCAFFOLD_ID)
            .height(100.vh)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SideNavigation(
            modifier = Modifier
                .id(SIDE_NAV_ID)
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
            modifier = modifier
                .id(MAIN_CONTENT_ID)
                .height(100.vh)
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
            }
            context(
                Modifier
                    .onClick {
                        if (showSideNav) showSideNav = false
                    }
                    .scrollBehavior(ScrollBehavior.Smooth)

            )
            Footer(
                modifier = Modifier.fillMaxWidth()
            ) { path ->
                println("path was ${path.path}")
                Navigator.navigateTo(path)
            }
        }
    }
    LaunchedEffect(navState.currentSection.path) {
        val path = navState.currentSection.path
        println("Moving to path $path")
        val (pathPart, sectionPart) = path.split("#")
        router.navigateTo(pathPart)
        focusOnSection(sectionPart)
    }
}

private fun focusOnSection(sectionID: String) {
    println("The received path was $sectionID")
    println("The params were $sectionID")
    println("Navigating to $sectionID")
    document.getElementById("#$sectionID")?.scrollIntoView()
//        .apply {
//            println("Requested section was ${this}")
//            val clientBounds = getBoundingClientRect()
//            val centerX = (clientBounds.left + clientBounds.width) / 2
//            val centerY = (clientBounds.top + clientBounds.height) / 2
//            scrollTo(centerX, centerY)
//        }
}

const val PAGE_SECTION = "pageSection"

@Composable
private fun getWidthFromBreakpoint(): CSSSizeValue<CSSUnit.percent> {
    return when (rememberBreakpoint()) {
        Breakpoint.ZERO, Breakpoint.SM, Breakpoint.MD -> 90.percent
        Breakpoint.LG, Breakpoint.XL -> 50.percent
    }
}
