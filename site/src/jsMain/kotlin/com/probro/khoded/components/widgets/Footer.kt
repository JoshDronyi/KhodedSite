package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.*
import com.probro.khoded.models.Images
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun Footer(modifier: Modifier = Modifier, onNavItemSelect: (path: String) -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LogoSection(Modifier)
        Navigator.sections.forEach {
            NavigationItem(
                text = it.key.primaryText,
                sections = it.value,
                navItemVariant = FooterNavItemVariant,
                onNavItemSelect = onNavItemSelect,
                modifier = Modifier
            )
            FooterNavSubItems(
                items = it.value,
                navSubItemVariant = FooterSubItemVariant,
                onNavItemSelect = onNavItemSelect
            )
        }
    }
}

@Composable
fun FooterNavSubItems(
    items: List<PageSection>,
    navSubItemVariant: ComponentVariant? = null,
    onNavItemSelect: (path: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.drop(1).forEach { item ->
            P(
                attrs = NavSubItemStyle.toModifier(navSubItemVariant)
                    .onClick { onNavItemSelect(item.path) }
                    .toAttrs()
            ) {
                Text(item.title)
            }
        }
    }
}

@Composable
fun LogoSection(modifier: Modifier = Modifier) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoDisplay(
            image = Images.Logos.circleLogo,
            variant = FooterLogoVariant
        )
        FooterSubText("Khoded Est. 2023")
    }
}

@Composable
fun FooterSubText(subText: String, modifier: Modifier = Modifier) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier()
                .toAttrs()
        ) {
            Text(subText)
        }
    }
}