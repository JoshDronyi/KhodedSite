package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.*
import com.probro.khoded.models.Images
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val HeaderStyle by ComponentStyle {
    base {
        Modifier
            .background(Color.black)
            .fillMaxWidth()
            .fillMaxHeight()
            .maxHeight(300.px)
            .padding(40.px)
    }
}

val HomePageHeaderVariant by HeaderStyle.addVariant {
    base { Modifier }
}
val StoryPageHeaderVariant by HeaderStyle.addVariant {
    base { Modifier }
}
val ContactPageHeaderVariant by HeaderStyle.addVariant {
    base { Modifier }
}


@Composable
fun Header(
    modifier: Modifier,
    variant: ComponentVariant? = null,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Row(
        modifier = HeaderStyle.toModifier(variant)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
//        SideNavButton(breakpoint, onShouldShowClick)
        Box(
            modifier = Modifier.onClick {
                //Take user to the home page.
                onNavItemSelect(Pages.Home_Section.LandingData)
            }
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            LogoDisplay(
                image = Images.Logos.minimalLogo,
                variant = HeaderLogoContainerVariant,
                imageVariant = HeaderImageVariant,
                textVariant = HeaderLogoTextVariant
            )
        }
        HeaderNavItemDisplay(
            modifier = Modifier
                .fillMaxWidth(60.percent),
            onNavItemSelect = onNavItemSelect
        )
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
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NavigationItem(
            text = Navigator.KeySections.PageRoots.home.primaryText,
            root = Navigator.KeySections.PageRoots.home,
            navItemVariant = HeaderNavItemVariant,
        ) { path ->
            println("Clicked on ${path.title}")
            onNavItemSelect(path)
        }
        NavigationItem(
            text = Navigator.KeySections.PageRoots.story.primaryText,
            root = Navigator.KeySections.PageRoots.story,
            navItemVariant = HeaderNavItemVariant,
        ) { path ->
            println("Clicked on ${path.title}")
            onNavItemSelect(path)
        }
        NavigationItem(
            text = Navigator.KeySections.PageRoots.contact.primaryText,
            root = Navigator.KeySections.PageRoots.contact,
            navItemVariant = HeaderNavItemVariant,
        ) { path ->
            println("Clicked on ${path.title}")
            onNavItemSelect(path)
        }
    }
}