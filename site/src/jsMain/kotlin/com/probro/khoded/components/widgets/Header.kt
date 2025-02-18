package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import com.probro.khoded.models.Images
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.probro.khoded.utils.Pages
import com.stevdza.san.kotlinbs.components.BSNavBar
import com.stevdza.san.kotlinbs.models.BackgroundStyle
import com.stevdza.san.kotlinbs.models.navbar.NavBarBrand
import com.stevdza.san.kotlinbs.models.navbar.NavLink
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

sealed interface HeaderKind : ComponentKind

val HeaderStyle = CssStyle<HeaderKind> {
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

val HomePageHeaderVariant = HeaderStyle.addVariant {
    base {
        Modifier
            .color(Colors.White)
            .background(Colors.Black.copy(alpha = 70))
            .fillMaxWidth()
            .padding(leftRight = 40.px, topBottom = 20.px)
    }
}
val StoryPageHeaderVariant = HeaderStyle.addVariant {
    base { Modifier.color(Colors.White) }
}
val ContactPageHeaderVariant = HeaderStyle.addVariant {
    base {
        Modifier
            .color(KhodedColors.PURPLE.rgb)
            .backgroundColor(Colors.Transparent)
    }
}


@Composable
fun Header(
    modifier: Modifier = Modifier,
    onNavItemSelect: (section: PageSection) -> Unit
) {
//    val breakpoint = rememberBreakpoint()
    BSNavBar(
        modifier = HeaderStyle.toModifier(HomePageHeaderVariant)
            .then(modifier),
        stickyTop = true,
        brand = NavBarBrand(
            title = "Khoded",
            image = Images.Logos.minimalLogo,
            imageWidth = 50.px,
            href = "#",
        ),
        items = listOf(
            NavLink(
                id = "consultation",
                title = Navigator.KeySections.TrafficStops.consultation.text,
                onClick = { onNavItemSelect(Pages.Home_Section.Consultation) }
            ),
            NavLink(
                id = "story",
                title = Navigator.KeySections.PageRoots.story.text,
                onClick = { onNavItemSelect(Pages.Story_Section.OurStory) }
            ),
            NavLink(
                id = "contact",
                title = Navigator.KeySections.PageRoots.contact.text,
                onClick = { onNavItemSelect(Pages.Contact_Section.Landing) }
            )),
        itemsAlignment = Alignment.CenterHorizontally,
        backgroundStyle = BackgroundStyle.Dark
//            BackgroundStyle.toModifier(HeaderBackground)
    )
//    Box(
//        modifier = HeaderStyle.toModifier(variant),
//        contentAlignment = Alignment.Center
//    ) {
//        Row(
//            modifier = modifier,
//            horizontalArrangement = Arrangement.SpaceAround,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Span(
//                attrs = Modifier
//                    .onClick {
//                        onNavItemSelect(Pages.Home_Section.Landing)
//                    }
//                    .toAttrs()
//            ) {
//                LogoDisplay(
//                    image = Images.Logos.minimalLogo,
//                    variant = HeaderLogoContainerVariant,
//                    imageVariant = HeaderImageVariant,
//                    textVariant = textVariant,
//                )
//            }
//            HeaderNavItemDisplay(
//                modifier = Modifier
//                    .fillMaxWidth(getWidthFromBreakpoint(breakpoint)),
//                onNavItemSelect = onNavItemSelect
//            )
//        }
//    }
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

    }
}