package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.FooterNavItemVariant
import com.probro.khoded.components.composables.NavigationItem
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import org.jetbrains.compose.web.css.px

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
            section1 = Navigator.KeySections.TrafficStops.termsAndCondtions, //TODO: WE NEED THE TEXT FOR A TERMS AND CONDITIONS PAGE IF THATS WHAT WE ARE DOING. OR AT LEAST A POP UP
            onNavItemSelect = onNavItemSelect
        )
    }
}

@Composable
fun FooterNavItemSet(
    modifier: Modifier = Modifier,
    section1: Navigator.KeySections,
    section2: Navigator.KeySections? = null,
    onNavItemSelect: (section: PageSection) -> Unit
) {
    Column(
        modifier = modifier,
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
//
//@Composable
//fun FooterNavSubItems(
//    items: List<PageSection>,
//    navSubItemVariant: ComponentVariant? = null,
//    onNavItemSelect: (section: PageSection) -> Unit
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth(90.percent),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        items.drop(1).forEach { item ->
//            FooterSubItem(
//                item = item,
//                navSubItemVariant = navSubItemVariant,
//                onNavItemSelect = onNavItemSelect
//            )
//        }
//    }
//}
//
//@Composable
//fun FooterSubItem(
//    item: PageSection,
//    navSubItemVariant: ComponentVariant? = null,
//    onNavItemSelect: (section: PageSection) -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth()
//            .padding(topBottom = 10.px, leftRight = 20.px),
//        horizontalArrangement = Arrangement.Start,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        FaAngleRight(
//            modifier = Modifier.color(Color.white)
//                .margin(right = 10.px),
//            size = IconSize.XL
//        )
//        P(
//            attrs = NavSubItemStyle.toModifier(navSubItemVariant)
//                .onClick { onNavItemSelect(item) }
//                .toAttrs()
//        ) {
//            Text(item.title)
//        }
//    }
//}
//
//@Composable
//fun LogoSection(modifier: Modifier = Modifier) {
//    Column(
//        modifier,
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        LogoDisplay(
//            image = Images.Logos.circleLogo,
//            variant = FooterLogoContainerVariant,
//            imageVariant = FooterImageVariant,
//            textVariant = FooterLogoTextVariant
//        )
//        FooterSubText("Khoded Est. 2023")
//    }
//}
//
//@Composable
//fun FooterSubText(subText: String, modifier: Modifier = Modifier) {
//    Column(
//        modifier,
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        P(
//            attrs = BaseTextStyle.toModifier()
//                .toAttrs()
//        ) {
//            Text(subText)
//        }
//    }
//}