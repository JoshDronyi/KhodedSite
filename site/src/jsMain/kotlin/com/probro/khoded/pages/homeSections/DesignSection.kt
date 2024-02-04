package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.NoBorderBackingCardVariant
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.utils.*
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.animation.toAnimation
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


@Composable
fun DesignSectionDisplay(data: Pages.Home_Section.Design) = with(data) {
    var sectionPosition by remember { mutableStateOf(SectionPosition.IDLE) }
    BackingCard(
        modifier = BackgroundStyle.toModifier(DesignBackgroundVariant)
            .id(id),
        variant = NoBorderBackingCardVariant,
        firstSection = {
            DesignTextSection(
                upperText = mainText,
                underlineImage = underlineImage,
                lowerText = subText,
                image = mainImage,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        secondSection = {
            DesignImageSection(
                sectionPosition = sectionPosition,
                secondImage = subImage,
                modifier = DesignImageStyle.toModifier()
            )
        }
    )
    IsOnScreenObservable(id) {
        sectionPosition = it
        println("New Position for $id is $it")
    }
}

val DesignImageStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
    }
    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM {
        Modifier
    }
    Breakpoint.MD {
        Modifier
            .translateY(ty = (-80).px)
    }
}

@Composable
fun DesignTextSection(
    upperText: String,
    underlineImage: String,
    lowerText: String,
    image: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .borderBottom {
                    width(2.px)
                    style(LineStyle.Solid)
                    color(Colors.RebeccaPurple)
                }
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DesignHeading(upperText)
            Image(
                src = underlineImage,
                modifier = ImageStyle.toModifier(BlackUnderlineVariant)
            )
            DesignSubText(lowerText)
        }
        Image(
            src = image,
            modifier = ImageStyle.toModifier(PlaneImageVariant)
                .align(Alignment.TopEnd)
        )
    }
}

val BlackUnderlineVariant by ImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(40.percent)
    }
    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM {
        Modifier
//            .translateY(ty = (-15).px)
    }
    Breakpoint.MD {
        Modifier
//            .translateY(ty = (-30).px)
    }
}

val PlaneImageVariant by ImageStyle.addVariant {
    base {
        Modifier
            .zIndex(2)
            .overflow(Overflow.Hidden)
    }
    Breakpoint.ZERO {
        Modifier.fillMaxWidth(30.percent)
            .translate(tx = 50.px, ty = (-100).px)
    }
    Breakpoint.SM {
        Modifier
            .fillMaxWidth(40.percent)
    }
    Breakpoint.MD {
        Modifier.fillMaxWidth(30.percent)
    }
}

@Composable
fun DesignSubText(lowerText: String) {
    P(
        attrs = BaseTextStyle.toModifier(HomeSubTitleStyle)
            .color(Color.black)
            .toAttrs()
    ) {
        Text(value = lowerText)
    }
}

const val LENGTH_OF_JUST = 4

@Composable
fun DesignHeading(upperText: String) {
    var isFocused by remember { mutableStateOf(false) }
    val justIndex = remember { upperText.indexOf("just") }
    val firstText = remember { upperText.substring(0, justIndex) }
    val just = remember { upperText.substring(justIndex, justIndex + LENGTH_OF_JUST) }
    val secondText = remember { upperText.substring(justIndex + LENGTH_OF_JUST) }
    P(
        attrs = BaseTextStyle.toModifier(HomeTitleVariant)
            .color(Color.black)
            .animation(
                if (isFocused) fallInAnimation.toAnimation()
                else flyUpAnimation.toAnimation()
            )
            .onFocusIn { isFocused = true }
            .onFocusOut { isFocused = false }
            .toAttrs()
    ) {
        Text(value = firstText)
        Span(
            attrs = Modifier
                .color(Color.mediumblue)
                .toAttrs()
        ) {
            Text(just)
        }
        Text(value = secondText)
    }
}

@Composable
fun DesignImageSection(
    sectionPosition: SectionPosition,
    secondImage: String,
    modifier: Modifier = Modifier
) = with(Pages.Home_Section.Design) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            src = secondImage,
            modifier = ImageStyle.toModifier(ComputerPicVariant)
        )
    }
}

val ComputerPicVariant by ImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .zIndex(1)
    }
}
