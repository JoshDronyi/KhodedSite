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
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Column(
        modifier = modifier.borderBottom {
            width(1.px)
            color(Color.darkgray)
            style(LineStyle.Solid)
        }
            .borderLeft {
                width(3.px)
                color(Color.darkgray)
                style(LineStyle.Groove)
            },
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
                variant = SideNavLogoContainerVariant,
                imageVariant = SideNavImageVariant,
                textVariant = SideNavLogoTextVariant,
            )
        }
        Navigator.sections.forEach { section ->
            SideNavItem(
                section = section,
                onNavItemSelect = { selected ->
                    onNavItemSelect(selected)
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun SideNavItem(
    section: Map.Entry<Navigator.KeySections, List<PageSection>>,
    modifier: Modifier = Modifier,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    var shouldShow: Boolean by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
                .padding(topBottom = 15.px),
            verticalAlignment = Alignment.Top,
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
                text = section.key.root,
                root = section.key,
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
}

@Composable
fun SideNavSubItems(
    items: List<PageSection>,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(left = 30.px),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        items.drop(1).forEach {
            NavSubItem(
                section = it,
                variant = SideSubItemVariant,
                modifier = Modifier.fillMaxWidth()
                    .padding(10.px),
                onNavItemSelect = onNavItemSelect
            )
        }
    }
}