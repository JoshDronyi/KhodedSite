package com.probro.khoded.components.composables

import androidx.compose.runtime.Composable
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.fa.FaArrowRightLong
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
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
            .padding(20.px)
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
    root: Navigator.KeySections,
    modifier: Modifier = Modifier,
    navItemVariant: ComponentVariant? = null,
    onNavItemSelect: (section: PageSection) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavSectionTitle(
            navItemVariant = navItemVariant,
            onNavItemSelect = {
                onNavItemSelect(
                    when (root) {
                        Navigator.KeySections.Home -> Pages.Home_Section.LandingData
                        Navigator.KeySections.About -> Pages.Story_Section.Landing
                        Navigator.KeySections.Services -> Pages.Services_Section.Landing
                        Navigator.KeySections.Contact -> Pages.Contact_Section.Landing
                    }
                )
            },
            text = text
        )
    }
}

@Composable
private fun NavSectionTitle(
    text: String,
    navItemVariant: ComponentVariant? = null,
    onNavItemSelect: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        P(
            attrs = NavItemStyle.toModifier(navItemVariant)
                .onClick { onNavItemSelect() }
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
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(20.percent),
            contentAlignment = Alignment.Center
        ) {
            FaArrowRightLong(
                modifier = Modifier
                    .color(Color.white),
                size = IconSize.XXS
            )
        }
        P(
            attrs = NavSubItemStyle.toModifier(variant)
                .textAlign(TextAlign.Start)
                .onClick { onNavItemSelect(section) }
                .toAttrs()
        ) {
            Text(section.title)
        }
    }
}