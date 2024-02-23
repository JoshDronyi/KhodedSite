package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.*
import com.probro.khoded.models.Images
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.*
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Span

val HeaderStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .height(Height.FitContent)
            .padding(10.px)
    }
    hover {
        Modifier
            .cursor(Cursor.Pointer)
    }
}

val HomePageHeaderVariant by HeaderStyle.addVariant {
    base {
        Modifier
            .color(Colors.White)
            .background(Colors.Black.copy(alpha = 70))
            .fillMaxWidth()
    }
}
val StoryPageHeaderVariant by HeaderStyle.addVariant {
    base { Modifier.color(Colors.White) }
}
val ContactPageHeaderVariant by HeaderStyle.addVariant {
    base {
        Modifier
            .color(KhodedColors.PURPLE.rgb)
            .backgroundColor(Colors.Transparent)
    }
}


@Composable
fun Header(
    modifier: Modifier,
    variant: ComponentVariant? = null,
    textVariant: ComponentVariant? = HeaderLogoTextVariant,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    val breakpoint = rememberBreakpoint()
    Box(
        modifier = HeaderStyle.toModifier(variant),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Span(
                attrs = Modifier
                    .onClick {
                        onNavItemSelect(Pages.Home_Section.Landing)
                    }
                    .toAttrs()
            ) {
                LogoDisplay(
                    image = Images.Logos.minimalLogo,
                    variant = HeaderLogoContainerVariant,
                    imageVariant = HeaderImageVariant,
                    textVariant = textVariant,
                )
            }
            HeaderNavItemDisplay(
                modifier = Modifier
                    .fillMaxWidth(getWidthFromBreakpoint(breakpoint)),
                onNavItemSelect = onNavItemSelect
            )
        }
    }
}

private fun getWidthFromBreakpoint(breakpoint: Breakpoint): CSSSizeValue<CSSUnit.percent> {
    return when (breakpoint) {
        Breakpoint.ZERO, Breakpoint.SM -> 90.percent
        Breakpoint.MD -> 70.percent
        Breakpoint.LG -> 60.percent
        Breakpoint.XL -> 50.percent
    }
}

/*
@Composable
private fun SideNavButton(
    breakpoint: Breakpoint,
    onShouldShowClick: () -> Unit
) {
    if (breakpoint <= Breakpoint.MD) Box(
        modifier = Modifier
            .fillMaxWidth(
                when (breakpoint) {
                    Breakpoint.ZERO -> 20.percent
                    Breakpoint.SM -> 25.percent
                    Breakpoint.MD -> 30.percent
                    else -> 0.percent
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        FaBars(
            modifier = Modifier
                .color(Color.white)
                .onClick {
                    println("Clicked")
                    onShouldShowClick()
                },
            size = when (breakpoint) {
                Breakpoint.ZERO -> IconSize.LG
                Breakpoint.SM -> IconSize.XL
                Breakpoint.MD -> IconSize.XXL
                else -> IconSize.XL
            }
        )
    }
}
*/

@Composable
fun HeaderNavItemDisplay(
    modifier: Modifier,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        NavigationItem(
            text = Navigator.KeySections.TrafficStops.consultation.text,
            root = Navigator.KeySections.TrafficStops.consultation,
            navItemVariant = HeaderNavItemVariant,
        ) { path ->
            println("Clicked on ${path.title}")
            onNavItemSelect(Pages.Home_Section.Consultation)
        }
        NavigationItem(
            text = Navigator.KeySections.PageRoots.story.text,
            root = Navigator.KeySections.PageRoots.story,
            navItemVariant = HeaderNavItemVariant,
        ) { path ->
            println("Clicked on ${path.title}")
            onNavItemSelect(path)
        }
        NavigationItem(
            text = Navigator.KeySections.PageRoots.contact.text,
            root = Navigator.KeySections.PageRoots.contact,
            navItemVariant = HeaderNavItemVariant,
        ) { path ->
            println("Clicked on ${path.title}")
            onNavItemSelect(path)
        }
    }
}