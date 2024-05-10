package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.TitleIDs
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.hover
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun ServicesSectionDisplay(data: Pages.Home_Section.Services) = with(data) {
    Box(
        modifier = BackgroundStyle.toModifier(ServicesBackgroundVariant)
            .id(id),
        contentAlignment = Alignment.CenterEnd
    ) {
        ImageBox(
            image = data.mainImage,
            imageDesc = "Man sitting on laptop",
            modifier = Modifier
                .fillMaxWidth()
        )
        ServicesTitle(
            title = title,
            modifier = Modifier.align(Alignment.TopEnd)
                .scrollSnapStop(ScrollSnapStop.Normal)
                .scrollSnapType(ScrollSnapType.Initial)
        )
        Column(
            modifier = ServicesStyle.toModifier()
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            khodedServices.forEach {
                WebServiceDisplay(it)
            }
        }
    }
}

val ServicesStyle by ComponentStyle {
    base {
        Modifier
            .height(Height.MaxContent)
            .fillMaxWidth(40.percent)
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = 60.px)
    }
    Breakpoint.SM
    Breakpoint.MD {
        Modifier.translateY(ty = 20.px)
    }
    Breakpoint.LG
    Breakpoint.XL {
        Modifier
            .translateY(ty = 40.px)
    }
}

@Composable
fun ServicesTitle(title: String, modifier: Modifier = Modifier) {
    var sectionPosition by remember { mutableStateOf(SectionPosition.IDLE) }
    P(
        attrs = HomeTitleTextStyle.toModifier(ServicesTitleVariant)
            .then(modifier)
            .id(TitleIDs.servicesTitleID)
            .translateY(
                ty = when (sectionPosition) {
                    SectionPosition.ABOVE -> (-100).px
                    SectionPosition.ON_SCREEN -> 0.px
                    SectionPosition.BELOW -> (-100).px
                    SectionPosition.IDLE -> 0.px
                }
            )
            .opacity(
                when (sectionPosition) {
                    SectionPosition.ABOVE -> 0.percent
                    SectionPosition.ON_SCREEN -> 100.percent
                    SectionPosition.BELOW -> 0.percent
                    SectionPosition.IDLE -> 100.percent
                }
            )
            .transition(
                CSSTransition(property = "translate", duration = 600.ms),
                CSSTransition(property = "opacity", duration = 600.ms)
            )
            .toAttrs()
    ) {
        Text(title)
    }

    IsOnScreenObservable(
        sectionID = TitleIDs.servicesTitleID,
    ) {
        sectionPosition = it
        println("New Position for ${Pages.Home_Section.Services.id} is $it")
    }
}

val PinkUnderLineVaraint by ImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(30.percent)
    }
    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM {
        Modifier
    }
    Breakpoint.MD {
        Modifier
    }
}

@Composable
fun WebServiceDisplay(service: Pair<String, String>) {
    var isShown by remember { mutableStateOf(false) }
    Column(
        modifier = ServiceSectionStyle.toModifier(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ServiceTitleDisplay(
            service = service.first,
            modifier = Modifier
                .fillMaxWidth()
                .onClick {
                    isShown = !isShown
                    println("isShown was $isShown")
                }
        )
        if (isShown) {
            ServiceDescriptionDisplay(service.second)
        }
    }
}

val ServiceSectionStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .borderBottom {
                width(2.px)
                style(LineStyle.Solid)
                color(Color.purple)
            }
            .padding(leftRight = 5.px, topBottom = 10.px)
    }
    hover {
        Modifier
            .cursor(Cursor.Pointer)
    }

}

val ServiceTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .color(Color.black)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.XXSmall)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Smaller)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.LG
    Breakpoint.XL {
        Modifier
            .fontSize(FontSize.XXLarge)
    }
}
val ServiceDescriptionVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .color(Color.black)
            .fillMaxWidth(80.percent)
    }
}

@Composable
fun ServiceTitleDisplay(
    service: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        FaPlus(
            modifier = Modifier
                .color(Color.black),
            size = when (rememberBreakpoint()) {
                Breakpoint.ZERO, Breakpoint.SM -> IconSize.XXS
                Breakpoint.MD, Breakpoint.LG, Breakpoint.XL -> IconSize.SM
            }
        )
        P(
            attrs = BaseTextStyle.toModifier(ServiceTextVariant)
                .toAttrs()
        ) { Text(service) }
    }
}

@Composable
fun ServiceDescriptionDisplay(description: String) {
    P(
        attrs = BaseTextStyle.toModifier(ServiceDescriptionVariant)
            .toAttrs()
    ) {
        Text(description)
    }
}
