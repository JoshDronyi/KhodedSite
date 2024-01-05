package com.probro.khoded.components.composables

import androidx.compose.runtime.Composable
import com.probro.khoded.utils.PageSection
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val NavItemStyle by ComponentStyle {
    base {
        Modifier
            .background(Color.black)
            .color(Color.white)
            .padding(0.px)
            .margin(0.px)
            .textAlign(TextAlign.Center)
    }
}

val HeaderNavItemVariant by NavItemStyle.addVariant {
    base {
        Modifier
            .padding(leftRight = 10.px, topBottom = 5.px)
            .textAlign(TextAlign.Center)
            .border {
                width(1.px)
                style(LineStyle.Solid)
                color(Color.white)
            }
            .borderRadius(20.px)
    }
}
val FooterNavItemVariant by NavItemStyle.addVariant {
    base {
        Modifier.fillMaxWidth(80.percent)
            .padding(topBottom = 15.px)
            .border {
                width(1.px)
                color(Color.white)
                style(LineStyle.Ridge)
            }
            .borderRadius(20.px)
    }
}
val SideNavItemVariant by NavItemStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
    }
}


@Composable
fun NavigationItem(
    text: String,
    sections: List<PageSection>? = null,
    modifier: Modifier = Modifier,
    navItemVariant: ComponentVariant? = null,
    onNavItemSelect: (path: String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavSectionTitle(
            navItemVariant = navItemVariant,
            onNavItemSelect = onNavItemSelect,
            slug = sections?.first()?.slug
                ?: Pages.Home_Section.LandingData.slug,
            text = text
        )
    }
}

@Composable
private fun NavSectionTitle(
    slug: String,
    text: String,
    navItemVariant: ComponentVariant? = null,
    onNavItemSelect: (path: String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        P(
            attrs = NavItemStyle.toModifier(navItemVariant)
                .onClick { onNavItemSelect(slug) }
                .toAttrs()
        ) {
            Text(text)
        }
    }
}

val NavSubItemStyle by ComponentStyle {
    base {
        Modifier
            .margin(0.px)
            .padding(0.px)
    }
}

val FooterSubItemVariant by NavSubItemStyle.addVariant {
    base {
        Modifier
    }
}

val SideSubItemVariant by NavSubItemStyle.addVariant {
    base {
        Modifier
            .color(Color.white)
    }
}

@Composable
fun NavSubItem(
    section: PageSection,
    modifier: Modifier = Modifier,
    variant: ComponentVariant?,
    onNavItemSelect: (path: String) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        P(
            attrs = NavSubItemStyle.toModifier(variant)
                .onClick { onNavItemSelect(section.path) }
                .toAttrs()
        ) {
            Text(section.title)
        }
    }
}