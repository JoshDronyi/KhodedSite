package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.BaseButtonTextVariant
import com.probro.khoded.BlueButtonVariant
import com.probro.khoded.components.widgets.HomePageHeaderVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.ImageStyle
import com.probro.khoded.styles.LandingImageVariant
import com.probro.khoded.utils.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.animation.toAnimation
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


val BackgroundStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .height(Height.FitContent)
    }
}

val HomeLandingBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .padding(bottom = 40.px)
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = KhodedColors.PURPLE.rgb,
                    to = Colors.RebeccaPurple
                )
            )
    }
}
val ServicesBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(80.percent)
            .padding(topBottom = 40.px)
            .color(Color.white)
    }
}
val DesignBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(80.percent)
            .color(Colors.Black)
            .margin(top = 100.px, bottom = 200.px)
    }
}
val ConsultationBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.SkyBlue,
                    to = KhodedColors.PURPLE.rgb
                )
            )
    }
}


@Composable
fun LandingSectionDisplay(
    header: @Composable (variant: ComponentVariant?, textVariant: ComponentVariant?) -> Unit,
    onNavigate: (page: PageSection) -> Unit,
    data: Pages.Home_Section.Landing
) = with(data) {
    data.apply {
        ctaButton = ctaButton.copy(onButtonClick = {
            println("Doing the navigation.")
            onNavigate(Pages.Home_Section.Consultation)
        })
    }
    Column(
        modifier = BackgroundStyle.toModifier(HomeLandingBackgroundVariant)
            .id(id),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        header(HomePageHeaderVariant, null)
        Box(
            modifier = Modifier
                .fillMaxWidth(80.percent),
            contentAlignment = Alignment.Center
        ) {
            Image(
                src = mainImage,
                description = "Landing section image.",
                modifier = ImageStyle.toModifier(LandingImageVariant)
            )
            LandingText(
                modifier = LandingTextStyle.toModifier()
                    .align(Alignment.TopStart)
            )
        }
    }
}

val LandingTextStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth(50.percent)
            .fillMaxHeight()
            .margin(bottom = 40.px)
    }
}

val HomeTitleTextStyle by ComponentStyle {
    base {
        Modifier
            .fontFamily("Times New Roman")
            .padding(0.px)
            .margin(0.px)
            .color(Color.white)
            .textAlign(TextAlign.Start)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.LG {
        Modifier.fontSize(48.px)
    }
    Breakpoint.XL {
        Modifier.fontSize(72.px)
    }
}

val ServicesTitleVariant by HomeTitleTextStyle.addVariant {
    base {
        Modifier
            .color(Colors.Black)
            .textAlign(TextAlign.Center)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(48.px)
    }
    Breakpoint.LG {
        Modifier.fontSize(72.px)
    }
    Breakpoint.XL
}
val DesignTitleVariant by HomeTitleTextStyle.addVariant {
    base {
        Modifier
            .color(Color.black)
    }

    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.SM {
        Modifier.fontSize(48.px)
    }
    Breakpoint.MD
    Breakpoint.LG {
        Modifier.fontSize(60.px)
    }
    Breakpoint.XL
}
val ConsultationTitleVariant by HomeTitleTextStyle.addVariant {
    base {
        Modifier
    }
}
val DesignSubTitleVariant by HomeTitleTextStyle.addVariant {
    base {
        Modifier
            .color(Color.black)
            .margin(topBottom = 40.px)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.XSmall)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.Medium)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XLarge)
    }
}

val HomeSubTitleVariant by HomeTitleTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(80.percent)
            .textAlign(TextAlign.Start)
            .color(Color.white)
            .margin(topBottom = 30.px)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.XXSmall)
            .translateY(ty = 20.px)
            .fillMaxWidth(60.percent)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.XSmall)
            .margin(topBottom = 10.px)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Medium)
            .margin(topBottom = 20.px)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XLarge)
    }
}

const val LENGTH_OF_TELLS = 5

@Composable
fun LandingText(
    modifier: Modifier = Modifier
) = with(Pages.Home_Section.Landing) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        LandingTitle(
            mainText
        )
        LandingSubTitle()
        ButtonDisplay(
            ctaButton,
            BlueButtonVariant,
            modifier = Modifier
                .fillMaxWidth(40.percent)
                .textAlign(TextAlign.Center)
        ) {
            P(
                attrs = BaseTextStyle.toModifier(BaseButtonTextVariant)
                    .toAttrs()
            ) {
                Text(it)
            }
        }
    }
}

@Composable
private fun LandingSubTitle() = with(Pages.Home_Section.Landing) {
    P(
        attrs = HomeTitleTextStyle.toModifier(HomeSubTitleVariant)
            .toAttrs()
    ) {
        Text(subText)
    }
}

val BlueUnderLineVariant by ImageStyle.addVariant {
    base {
        Modifier.fillMaxWidth(50.percent)
            .maxHeight(MaxHeight.MinContent)
    }
    Breakpoint.ZERO {
        Modifier
    }
}

val firstLineVariant by HomeTitleTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .textAlign(TextAlign.Center)
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = 70.px)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL {
        Modifier
            .translate(tx = 40.px, ty = 20.px)
            .fontSize(60.px)
    }
}
val PinkTextVariant by HomeTitleTextStyle.addVariant {
    base {
        Modifier
            .minWidth(Width.MaxContent)
            .color(Colors.DeepPink)
            .fontStyle(FontStyle.Italic)
            .textAlign(TextAlign.End)
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = 70.px)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL {
        Modifier
            .translate(tx = 40.px, ty = 20.px)
//            .fontSize(60.px)
    }
}
val SecondLineVariant by HomeTitleTextStyle.addVariant {
    base {
        Modifier
            .width(Width.FitContent)
            .textAlign(TextAlign.Start)
            .margin(left = 10.px)
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = 70.px)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL {
        Modifier
            .translate(tx = 40.px, ty = 20.px)
            .fontSize(60.px)
    }
}

@Composable
private fun LandingTitle(
    mainText: String,
    modifier: Modifier = Modifier
) = with(Pages.Home_Section.Landing) {
    var sectionPosition by remember { mutableStateOf(SectionPosition.IDLE) }

    val indexOfTells: Int = remember { mainText.indexOf("tells") }
    val firstLine = remember { mainText.substring(startIndex = 0, endIndex = indexOfTells) }
    val tells = remember { mainText.substring(indexOfTells, indexOfTells + LENGTH_OF_TELLS) }
    val secondLine = remember { mainText.substring(startIndex = indexOfTells + LENGTH_OF_TELLS) }

    P(
        attrs = modifier
            .id(TitleIDs.landingTitleID)
            .animation(
                fallInAnimation.toAnimation(
                    duration = 600.ms,
                    timingFunction = AnimationTimingFunction.Ease
                )
            )
            .toAttrs()
    ) {
        Column(
            modifier = LandingTextColumnStyle.toModifier(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            Span(
                attrs = HomeTitleTextStyle.toModifier(firstLineVariant)
                    .toAttrs()
            ) {
                Text(firstLine.trim())
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Span(
                    attrs = HomeTitleTextStyle.toModifier(PinkTextVariant)
                        .toAttrs()
                ) {
                    Text(tells.trim())
                }
                Span(
                    attrs = HomeTitleTextStyle.toModifier(SecondLineVariant)
                        .toAttrs()
                ) {
                    Text(secondLine.trim())
                }
            }
        }
    }

    IsOnScreenObservable(
        sectionID = id
    ) {
        sectionPosition = it
        println("New Position for $id is $it")
    }
}

val LandingTextColumnStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
    }
    Breakpoint.ZERO {
        Modifier
            .translateY(ty = (-5).px)
    }
    Breakpoint.SM {
        Modifier
            .translateY(ty = (-40).px)
    }
    Breakpoint.MD {
        Modifier
            .translateY(ty = (-30).px)
    }
    Breakpoint.LG {
        Modifier
            .translateY(ty = (-10).px)
    }
    Breakpoint.XL {
        Modifier
            .translateY(ty = 25.px)
    }
}


@Composable
fun ButtonDisplay(
    state: ButtonState,
    buttonVariant: ComponentVariant? = null,
    modifier: Modifier = Modifier,
    clickableContent: @Composable (buttonText: String) -> Unit,
) = with(state) {
    Box(
        modifier = ButtonStyle.toModifier(buttonVariant)
            .then(modifier)
            .onClick { onButtonClick() },
        contentAlignment = Alignment.Center
    ) {
        clickableContent(buttonText)
    }
}
