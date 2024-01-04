package com.probro.khoded.components.widgets

import androidx.compose.runtime.*
import com.probro.khoded.components.composables.*
import com.probro.khoded.models.Images
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.icons.fa.FaAngleDown
import com.varabyte.kobweb.silk.components.icons.fa.FaAngleRight
import com.varabyte.kobweb.silk.components.icons.fa.FaArrowLeft
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

@Composable
fun SideNavigation(
    modifier: Modifier = Modifier,
    onSideNavToggle: () -> Unit,
    onNavItemSelect: (path: String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            FaArrowLeft(
                modifier = Modifier
                    .padding(15.px)
                    .color(Color.white)
                    .onClick {
                        onSideNavToggle()
                    },
                size = IconSize.LG
            )
            LogoDisplay(
                image = Images.Logos.circleLogo,
                modifier = Modifier
                    .fillMaxWidth(),
                variant = SideNavLogoVariant
            )
        }
        Navigator.sections.forEach { section ->
            SideNavItem(
                section = section,
                onNavItemSelect = onNavItemSelect
            )
        }
    }
}

@Composable
fun SideNavItem(
    section: Map.Entry<Navigator.PageRoot, List<PageSection>>,
    onNavItemSelect: (path: String) -> Unit
) {
    var shouldShow: Boolean by remember { mutableStateOf(false) }
    //TODO: FIGURE OUT THE BSCOLLAPSE TRIGGERING MECHANISM AND IMPLEMENT.
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(10.px)
            .borderBottom {
                width(1.px)
                color(Color.white)
                style(LineStyle.Ridge)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val angleModifier = Modifier
            .padding(leftRight = 15.px)
            .color(Color.white)

        if (shouldShow) {
            FaAngleDown(
                modifier = angleModifier
                    .onClick {
                        println("Clicked down")
                        shouldShow = false
                    },
                size = IconSize.LG
            )
        } else {
            FaAngleRight(
                modifier = angleModifier
                    .onClick {
                        println("Clicked right")
                        shouldShow = true
                    },
                size = IconSize.LG
            )
        }
        NavigationItem(
            text = section.key.primaryText,
            sections = section.value,
            navItemVariant = SideNavItemVariant
        ) { path ->
            onNavItemSelect(path)
        }
    }
    if (shouldShow) {
        SideNavSubItems(
            items = section.value,
            onNavItemSelect = onNavItemSelect
        )
    }
}

@Composable
fun SideNavSubItems(
    items: List<PageSection>,
    onNavItemSelect: (path: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.drop(1).forEach {
            NavSubItem(
                section = it,
                variant = SideSubItemVariant,
                modifier = Modifier.fillMaxWidth(),
                onNavItemSelect = onNavItemSelect
            )
        }
    }
}