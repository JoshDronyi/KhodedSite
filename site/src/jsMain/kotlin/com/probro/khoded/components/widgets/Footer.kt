package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.FooterNavItemVariant
import com.probro.khoded.components.composables.NavigationItem
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.varabyte.kobweb.compose.css.Width
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.px

@Composable
fun Footer(modifier: Modifier = Modifier, onNavItemSelect: (section: PageSection) -> Unit) {
    Row(
        modifier = modifier
            .padding(topBottom = 20.px, leftRight = 20.px),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FooterNavItemSet(
            section1 = Navigator.KeySections.PageRoots.story,
            section2 = Navigator.KeySections.TrafficStops.joinOurTeam,
            onNavItemSelect = onNavItemSelect
        )
        FooterNavItemSet(
            section1 = Navigator.KeySections.PageRoots.contact,
            section2 = Navigator.KeySections.TrafficStops.consultation,
            onNavItemSelect = onNavItemSelect
        )
        FooterNavItemSet(
            section1 = Navigator.KeySections.TrafficStops.termsAndCondtions,
            //TODO: WE NEED THE TEXT FOR A TERMS AND CONDITIONS PAGE IF THATS WHAT WE ARE DOING. OR AT LEAST A POP UP
            onNavItemSelect = onNavItemSelect
        )
    }
}

val FooterColumnStyle by ComponentStyle {
    base {
        Modifier.width(Width.FitContent)
    }
}

@Composable
fun FooterNavItemSet(
    section1: Navigator.KeySections,
    section2: Navigator.KeySections? = null,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Column(
        modifier = FooterColumnStyle.toModifier(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavigationItem(
            text = section1.primaryText,
            root = section1,
            navItemVariant = FooterNavItemVariant,
            onNavItemSelect = { section ->
                onNavItemSelect(section)
            }
        )
        section2?.let {
            NavigationItem(
                text = it.primaryText,
                root = it,
                navItemVariant = FooterNavItemVariant,
                onNavItemSelect = { section ->
                    onNavItemSelect(section)
                }
            )
        }
    }

}

