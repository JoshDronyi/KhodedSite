package com.probro.khoded.components.composables

import androidx.compose.runtime.Composable
import com.probro.khoded.styles.componentStyles.NavItemKind
import com.probro.khoded.styles.componentStyles.NavItemStyle
import com.probro.khoded.utils.Navigator
import com.probro.khoded.utils.PageSection
import com.probro.khoded.utils.Pages
import com.stevdza.san.kotlinbs.models.navbar.NavLink
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.Width
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.fa.FaArrowRightLong
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.style.CssStyleVariant
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.extendedBy
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val HeaderNavItemVariant = NavItemStyle.addVariant {
    base {
        Modifier
            .width(Width.MinContent)
            .fontSize(FontSize.Medium)
            .margin(right = 10.px)
    }

    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Small)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Medium)
    }
}
val FooterNavItemVariant = NavItemStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .padding(leftRight = 20.px, topBottom = 5.px)
            .height(Height.Inherit)
            .fontSize(FontSize.Large)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Small)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.Larger)
    }
}
val SideNavItemVariant = NavItemStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
    }
}


@Composable
fun NavigationItem(
    text: String,
    root: Navigator.KeySections,
    navItemVariant: CssStyleVariant<NavItemKind>? = null,
    onNavItemSelect: (section: PageSection) -> Unit,
) {
    NavLink(
        id = root.root,
        title = text,
        onClick = {
            onNavItemSelect(
                when (root) {
                    is Navigator.KeySections.PageRoots.Contact -> Pages.Contact_Section.Landing
                    is Navigator.KeySections.PageRoots.Home -> Pages.Home_Section.Landing
                    is Navigator.KeySections.PageRoots.STORY -> Pages.Story_Section.OurStory
                    is Navigator.KeySections.TrafficStops.CONSULTATION -> Pages.Home_Section.Consultation
                    is Navigator.KeySections.TrafficStops.JOIN_OUR_TEAM -> Pages.Story_Section.JoinOurTeam
                    is Navigator.KeySections.TrafficStops.TERMS_AND_CONDTIONS ->
                        Pages.Misc.Sections.TermsAndConditions
                }
            )
        }
    )
//    NavSectionTitle(
//        navItemVariant = navItemVariant,
//        onNavItemSelect = {
//            onNavItemSelect(
//            )
//        },
//        text = text
//    )
}

@Composable
private fun NavSectionTitle(
    text: String,
    linkPath: String? = null,
    navItemVariant: CssStyleVariant<NavItemKind>? = null,
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

val NavSubItemVariant = NavItemStyle.addVariant {
    base {
        Modifier
            .margin(0.px)
            .padding(0.px)
    }
}

val FooterSubItemVariant = NavSubItemVariant.extendedBy {
    base {
        Modifier
    }
}

val SideSubItemVariant = NavSubItemVariant.extendedBy {
    base {
        Modifier
            .color(Color.white)
    }
}

@Composable
fun NavSubItem(
    section: PageSection,
    modifier: Modifier = Modifier,
    variant: CssStyleVariant<NavItemKind>?,
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
            attrs = NavItemStyle.toModifier(NavSubItemVariant)
                .textAlign(TextAlign.Start)
                .onClick { onNavItemSelect(section) }
                .toAttrs()
        ) {
            Text(section.title)
        }
    }
}