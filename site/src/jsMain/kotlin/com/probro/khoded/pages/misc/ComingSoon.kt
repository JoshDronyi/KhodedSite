package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.models.Res
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.BackgroundClip
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.layout.Surface
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text


@Composable
fun ComingSoon(
    page: Pages.Home_Section
) {
    val theme = Res.Themes.baseTheme
    with(theme) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(backgroundColor.rgb)
                    .borderRadius(r = 16.px)
                    .border(2.px, color = onSurfaceColor.rgb)
                    .fillMaxWidth(80.percent)
                    .height(1000.px)
                    .backgroundClip(BackgroundClip.Unset)
                    .margin(16.px)
                    .boxShadow(
                        offsetY = 1.px,
                        color = secondaryColor.rgb,
                        blurRadius = 8.px,
                    )
            ) {
                H1 {
                    Text("${page.title.uppercase()} Page Coming Soon")
                }
            }

        }
    }
}
