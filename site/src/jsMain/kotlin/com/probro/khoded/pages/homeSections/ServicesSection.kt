package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.utils.*
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.animation.toAnimation
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.hover
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun ServicesSectionDisplay(data: Pages.Home_Section.Services) = with(data) {
    var sectionPosition by remember { mutableStateOf(SectionPosition.IDLE) }
    BackingCard(
        modifier = BackgroundStyle.toModifier(ServicesBackgroundVariant)
            .id(id),
        variant = NoBorderBackingCardVariant,
        firstSection = {
            ImageBox(
                image = data.mainImage,
                imageDesc = "Man sitting on laptop",
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        secondSection = {
            IsOnScreenObservable(id) {
                sectionPosition = it
                println("New Position for $id is $it")
            }
            ServicesDisplay(
                title = title,
                services = khodedServices,
                underLineImage = underlineImage,
                sectionPosition = sectionPosition,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    )

}


@Composable
fun ServicesDisplay(
    title: String,
    services: List<WebService>,
    underLineImage: String,
    sectionPosition: SectionPosition,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ServicesTitle(title, underLineImage, sectionPosition)
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            services.forEach {
                WebServiceDisplay(it)
            }
        }
    }
}

@Composable
fun ServicesTitle(title: String, underLineImage: String, sectionPosition: SectionPosition) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.px),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        println("Services title.... ${sectionPosition.name}")
        P(
            attrs = BaseTextStyle.toModifier(HomeTitleVariant)
                .color(Colors.Black)
                .textAlign(TextAlign.Center)
                .position(Position.Relative)
                .animation(
                    fallInAnimation.toAnimation(
                        duration = 600.ms,
                        timingFunction = AnimationTimingFunction.EaseIn,
                        direction = AnimationDirection.Normal,
                        playState = if (sectionPosition == SectionPosition.ON_SCREEN) AnimationPlayState.Running
                        else AnimationPlayState.Paused
                    )
                )
                .toAttrs()
        ) {
            Text(title)
        }
        Image(
            src = underLineImage,
            modifier = ImageStyle.toModifier(PinkUnderLineVaraint)
        )
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
                width(4.px)
                style(LineStyle.Solid)
                color(Color.purple)
            }
            .padding(leftRight = 5.px, topBottom = 15.px)
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
        horizontalArrangement = Arrangement.Center
    ) {
        FaPlus(
            modifier = Modifier
                .color(Color.black),
            size = IconSize.SM
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
