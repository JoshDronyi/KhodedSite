package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.styles.animations.jobPostingShiftDownKeyFrames
import com.probro.khoded.styles.animations.jobPostingShiftUPKeyFrames
import com.probro.khoded.styles.pageStyles.ServiceColumText
import com.probro.khoded.styles.pageStyles.ServiceSectionDisplayVariant
import com.probro.khoded.styles.textStyles.*
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
import com.varabyte.kobweb.framework.annotations.DelicateApi
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun ServicesSectionDisplay(data: Pages.Home_Section.Services) = with(data) {
    Box(
        modifier = BaseSectionStyles.toModifier(ServicesVariant)
            .id(id)
    ) {
        Image(
            src = data.mainImage,
            description = "Man sitting on laptop",
            modifier = ImageStyle.toModifier(ServiceSectionDisplayVariant)
        )
        Column(
            modifier = ColumnStyle.toModifier(ServiceColumText)
                .align(Alignment.TopEnd)
        ) {
            ServicesTitle(
                title = title,
                modifier = Modifier
                    .scrollSnapStop(ScrollSnapStop.Normal)
                    .scrollSnapType(ScrollSnapType.Initial)
            )
            Column(
                modifier = ColumnStyle.toModifier(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                khodedServices.forEach {
                    WebServiceDisplay(it)
                }
            }
        }
    }
}

val ServicesVariant = BaseSectionStyles.addVariant {
    base {
        Modifier
            .height(Height.MaxContent)
            .fillMaxWidth(75.percent)
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
    val animation = when (sectionPosition) {
        SectionPosition.ABOVE -> jobPostingShiftDownKeyFrames
        SectionPosition.ON_SCREEN -> jobPostingShiftUPKeyFrames
        SectionPosition.BELOW -> jobPostingShiftDownKeyFrames
        SectionPosition.IDLE -> jobPostingShiftUPKeyFrames
    }
    P(
        attrs = BaseTextStyle.toModifier(ServicesTitleVariant)
            .then(modifier)
            .id(TitleIDs.servicesTitleID)
            .animation(animation.toAnimation(600.ms))
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

val PinkUnderLineVaraint = ImageStyle.addVariant {
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
        modifier = ColumnStyle.toModifier(ServiceSectionVariant),
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

val ServiceSectionVariant = ColumnStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .borderBottom {
                width(2.px)
                style(LineStyle.Solid)
                color(Color.purple)
            }
    }
    hover {
        Modifier
            .cursor(Cursor.Pointer)
    }

}

val ServiceTextVariant = BaseTextStyle.addVariant {
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
val ServiceDescriptionVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .color(Color.black)
            .fillMaxWidth(80.percent)
    }
}

@OptIn(DelicateApi::class)
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
