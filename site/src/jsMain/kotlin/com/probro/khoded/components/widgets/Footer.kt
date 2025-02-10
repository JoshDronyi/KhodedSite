package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.FooterNavItemVariant
import com.probro.khoded.components.composables.NavigationItem
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.styles.textStyles.ColumnKind
import com.probro.khoded.styles.textStyles.ColumnStyle
import com.probro.khoded.styles.textStyles.FooterRow
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.varabyte.kobweb.compose.css.Width
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.style.CssStyleVariant
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.extendedBy
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.px

@Composable
fun Footer(
    modifier: Modifier = Modifier,
    variant: CssStyleVariant<ColumnKind>? = null,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Row(
        modifier = FooterRow.toModifier().then(modifier)
            .padding(topBottom = 20.px, leftRight = 20.px),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FooterNavItemSet(
            section1 = Navigator.KeySections.PageRoots.story,
            section2 = Navigator.KeySections.TrafficStops.joinOurTeam,
            variant = variant,
            onNavItemSelect = onNavItemSelect
        )
        FooterNavItemSet(
            section1 = Navigator.KeySections.PageRoots.contact,
            section2 = Navigator.KeySections.TrafficStops.consultation,
            variant = variant,
            onNavItemSelect = onNavItemSelect
        )
        FooterNavItemSet(
            section1 = Navigator.KeySections.TrafficStops.termsAndCondtions,
            variant = variant,
            //TODO: WE NEED THE TEXT FOR A TERMS AND CONDITIONS PAGE IF THATS WHAT WE ARE DOING. OR AT LEAST A POP UP
            onNavItemSelect = onNavItemSelect
        )
    }
}

val FooterColumnVariant = ColumnStyle.addVariant {
    base {
        Modifier
            .color(Color.white)
            .width(Width.FitContent)
    }
}

val ContactFooterVariant = FooterColumnVariant.extendedBy {
    base {
        Modifier
            .color(KhodedColors.PURPLE.rgb)
    }
}

@Composable
fun FooterNavItemSet(
    section1: Navigator.KeySections,
    section2: Navigator.KeySections? = null,
    variant: CssStyleVariant<ColumnKind>? = FooterColumnVariant,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Column(
        modifier = ColumnStyle.toModifier(variant),
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

