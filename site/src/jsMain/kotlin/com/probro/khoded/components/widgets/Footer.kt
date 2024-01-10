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
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.fa.FaAngleRight
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun Footer(modifier: Modifier = Modifier, onNavItemSelect: (section: PageSection) -> Unit) {
    SimpleGrid(
        numColumns = numColumns(
            base = 1,
            sm = 2,
            md = 3,
            lg = 5
        ),
        modifier = modifier
            .background(Colors.Black)
            .color(Colors.White)
            .padding(topBottom = 20.px),
    ) {
        LogoSection()
        Navigator.sections.forEach {
            NavigationDisplay(
                modifier = Modifier
                    .fillMaxWidth(),
                entry = it,
                onNavItemSelect = onNavItemSelect
            )
        }
    }
}

@Composable
fun NavigationDisplay(
    modifier: Modifier = Modifier,
    entry: Map.Entry<Navigator.PageRoot, List<PageSection>>,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        NavigationItem(
            text = entry.key.primaryText,
            root = entry.key,
            navItemVariant = FooterNavItemVariant,
            onNavItemSelect = { section ->
                onNavItemSelect(section)
            }
        )
        FooterNavSubItems(
            items = entry.value,
            navSubItemVariant = FooterSubItemVariant,
            onNavItemSelect = onNavItemSelect
        )
    }
}

@Composable
fun FooterNavSubItems(
    items: List<PageSection>,
    navSubItemVariant: ComponentVariant? = null,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(90.percent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.drop(1).forEach { item ->
            FooterSubItem(
                item = item,
                navSubItemVariant = navSubItemVariant,
                onNavItemSelect = onNavItemSelect
            )
        }
    }
}

@Composable
fun FooterSubItem(
    item: PageSection,
    navSubItemVariant: ComponentVariant? = null,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(topBottom = 10.px, leftRight = 20.px),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FaAngleRight(
            modifier = Modifier.color(Color.white)
                .margin(right = 10.px),
            size = IconSize.XL
        )
        P(
            attrs = NavSubItemStyle.toModifier(navSubItemVariant)
                .onClick { onNavItemSelect(item) }
                .toAttrs()
        ) {
            Text(item.title)
        }
    }
}

@Composable
fun LogoSection(modifier: Modifier = Modifier) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoDisplay(
            image = Images.Logos.circleLogo,
            variant = FooterLogoContainerVariant,
            imageVariant = FooterImageVariant,
            textVariant = FooterLogoTextVariant
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