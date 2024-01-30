package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.navigation.Router
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.vh

@Composable
fun Scaffold(
    router: Router,
    modifier: Modifier = Modifier,
    context: @Composable (
        header: @Composable (variant: ComponentVariant?) -> Unit,
        footer: @Composable () -> Unit,
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
            { variant ->
                Header(
                    modifier = Modifier.fillMaxWidth(
                        when (breakpoint) {
                            Breakpoint.ZERO, Breakpoint.SM -> 100.percent
                            Breakpoint.MD, Breakpoint.LG, Breakpoint.XL -> 90.percent
                        }
                    ),
                    variant = variant
                ) { path: PageSection ->
                    Navigator.navigateTo(path)
                }
            },
            {
                Footer(
                    modifier = Modifier.fillMaxWidth()
                ) { path ->
                    Navigator.navigateTo(path)
                }
            },
            modifier.height(100.vh)
                .zIndex(1)
                .fillMaxWidth()
                .scrollBehavior(ScrollBehavior.Smooth)
        )
    }
    LaunchedEffect(navState.currentSection) {
        router.navigateTo(navState.currentSection.path)
    }
}
