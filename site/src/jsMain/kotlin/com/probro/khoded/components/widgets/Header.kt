package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.HeaderLogoVariant
import com.probro.khoded.components.composables.HeaderNavItemVariant
import com.probro.khoded.components.composables.LogoDisplay
import com.probro.khoded.components.composables.NavigationItem
import com.probro.khoded.models.Images
import com.probro.khoded.models.Routes
import com.probro.khoded.utils.Navigator
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val HeaderStyle by ComponentStyle {
    base {
        Modifier
            .background(Color.black)
            .maxHeight(300.px)
    }
}


@Composable
fun Header(
    modifier: Modifier,
    onShouldShowClick: () -> Unit,
    onNavItemSelect: (path: String) -> Unit
) {
    val breakpoint = rememberBreakpoint()
    Row(
        modifier = HeaderStyle.toModifier()
            .fillMaxHeight()
            .padding(40.px)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (breakpoint <= Breakpoint.MD) Box(
            modifier = Modifier
                .fillMaxWidth(30.percent),
            contentAlignment = Alignment.Center
        ) {
            FaBars(
                modifier = Modifier
                    .color(Color.white)
                    .onClick {
                        println("Clicked")
                        onShouldShowClick()
                    },
                size = IconSize.X3
            )
        }

        LogoDisplay(
            image = Images.Logos.circleLogo,
            variant = HeaderLogoVariant,
            modifier = Modifier.onClick { onNavItemSelect(Routes.Home.SLUG) }
                .fillMaxWidth(
                    if (breakpoint > Breakpoint.MD) 45.percent else 70.percent
                )
        )

        if (breakpoint > Breakpoint.MD) HeaderNavItemDisplay(
            modifier = Modifier
                .fillMaxWidth(80.percent)
                .padding(top = 15.px),
            onNavItemSelect = onNavItemSelect
        )
    }
}

@Composable
fun HeaderNavItemDisplay(
    modifier: Modifier,
    onNavItemSelect: (path: String) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Navigator.sections.forEach {
            NavigationItem(
                text = it.key.primaryText,
                sections = null,
                modifier = Modifier.fillMaxWidth(),
                navItemVariant = HeaderNavItemVariant,
            ) { path ->
                println("Clicked on ${it.key.primaryText}")
                onNavItemSelect(it.value.first().slug)
            }
        }
    }
}