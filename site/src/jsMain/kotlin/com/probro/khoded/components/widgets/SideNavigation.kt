package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.probro.khoded.components.composables.*
import com.probro.khoded.models.Images
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.stevdza.san.kotlinbs.components.BSCollapse
import com.stevdza.san.kotlinbs.components.showCollapse
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.icons.fa.FaArrowDown
import com.varabyte.kobweb.silk.components.icons.fa.FaArrowLeft
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import org.jetbrains.compose.web.css.Color
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
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            FaArrowLeft(
                modifier = Modifier
                    .padding(leftRight = 10.px)
                    .onClick {
                        onSideNavToggle()
                    },
                size = IconSize.XL
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
    var shouldShow: Boolean by mutableStateOf(false)
    //TODO: FIGURE OUT THE BSCOLLAPSE TRIGGERING MECHANISM AND IMPLEMENT.
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        NavigationItem(
            text = section.key.primaryText,
            sections = section.value,
            navItemVariant = SideNavItemVariant
        ) { path ->
            onNavItemSelect(path)
        }
        FaArrowDown(
            modifier = Modifier.fillMaxWidth()
                .padding(leftRight = 10.px)
                .color(Color.white)
                .onClick { shouldShow = !shouldShow },
            size = IconSize.XL
        )
    }
    SideNavSubItems(
        id = section.key.name,
        items = section.value,
        onNavItemSelect = onNavItemSelect
    )
}

@Composable
fun SideNavSubItems(
    id: String,
    items: List<PageSection>,
    onNavItemSelect: (path: String) -> Unit
) {
    BSCollapse(
        modifier = Modifier
            .showCollapse(id),
        id = id
    ) {
        items.forEach {
            NavSubItem(
                section = it,
                variant = SideSubItemVariant,
                modifier = Modifier.fillMaxWidth(),
                onNavItemSelect = onNavItemSelect
            )
        }
    }
}