package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.WebService
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun ServicesSectionDisplay(data: Pages.Home_Section.Services) = with(data) {
    Row(
        modifier = BackgroundStyle.toModifier(ServicesBackgroundVariant)
            .id(id),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageBox(
            image = data.mainImage,
            imageDesc = "Man sitting on laptop",
            modifier = Modifier
                .fillMaxWidth()
        )
        ServicesDisplay(
            title = title,
            services = khodedServices,
            underLineImage = underlineImage,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}


@Composable
fun ServicesDisplay(
    title: String,
    services: List<WebService>,
    underLineImage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ServicesTitle(title, underLineImage)
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
fun ServicesTitle(title: String, underLineImage: String) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(HomeTitleVariant)
                .color(Colors.Black)
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text(title)
        }
        Image(
            src = underLineImage,
            modifier = Modifier
                .fillMaxWidth(30.percent)
                .translateY(ty = (-20).px)
        )
    }
}

@Composable
fun WebServiceDisplay(service: Pair<String, String>) {
    var isShown by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .borderBottom {
                width(4.px)
                style(LineStyle.Solid)
                color(Color.purple)
            },
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ServiceTitleDisplay(
            service = service.first,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            isShown = !isShown
        }
        if (isShown) {
            ServiceDescriptionDisplay(service.second)
        }
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
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        FaPlus(
            modifier = Modifier
                .color(Color.black)
                .onClick { onIconClick() },
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
