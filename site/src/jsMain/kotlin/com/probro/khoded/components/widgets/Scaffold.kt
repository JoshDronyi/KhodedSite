package com.probro.khoded.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.navigation.Router
import org.jetbrains.compose.web.css.*

@Composable
fun Scaffold(
    router: Router,
    modifier: Modifier = Modifier,
    context: @Composable ColumnScope.() -> Unit
) {
    var showSideNav by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .height(100.vh)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SideNavigation(
            modifier = Modifier
                .fillMaxWidth(if (showSideNav) 40.percent else 0.percent)
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
        ) { path ->
            router.navigateTo(path)
        }
        Column(
            modifier = modifier.height(Height.MaxContent)
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
            ) { path: String ->
                router.navigateTo(path)
            }
            context()
            Footer(
                modifier = Modifier.fillMaxWidth()
            ) { path ->
                router.navigateTo(path)
            }
        }
    }
}
