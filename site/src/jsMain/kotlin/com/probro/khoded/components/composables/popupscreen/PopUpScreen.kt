package com.probro.khoded.components.composables.popupscreen

import androidx.compose.runtime.Composable
import com.probro.khoded.PopUpCTAVariant
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.models.Res.TextStyle.FONT_FAMILY
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.probro.khoded.utils.popUp.FounderText
import com.probro.khoded.utils.popUp.PopUpScreenUIModel
import com.stevdza.san.kotlinbs.components.BSProgress
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val PopUpTextStyle by ComponentStyle {
    base {
        Modifier
            .padding(0.px)
            .margin(0.px)
            .fontFamily(FONT_FAMILY)
            .fillMaxWidth()
            .textAlign(TextAlign.Center)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Small)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XLarge)
    }
}
val PopUpScreenStyle by ComponentStyle {
    base {
        Modifier
            .height(60.vh)
            .borderRadius(20.px)
            .padding(10.px)
    }
}
val PopUpImageStyle by ComponentStyle {
    base {
        Modifier
    }
}
val BioImageVariant by PopUpImageStyle.addVariant {
    base {
        Modifier
    }
    Breakpoint.ZERO {
        Modifier.maxHeight(20.vh)
    }
    Breakpoint.SM {
        Modifier.maxHeight(30.vh)
    }
    Breakpoint.MD {
        Modifier.maxHeight(40.vh)
    }
}

val FounderPopUpVariant by PopUpScreenStyle.addVariant {
    base {
        Modifier
            .backgroundColor(KhodedColors.PURPLE.rgb)
            .color(KhodedColors.POWDER_BLUE.rgb)
            .border {
                width(3.px)
                style(LineStyle.Solid)
                color(KhodedColors.POWDER_BLUE.rgb)
            }
    }
    Breakpoint.ZERO {
        Modifier
            .fillMaxWidth(90.percent)
    }
    Breakpoint.SM {
        Modifier.fillMaxWidth(80.percent)
    }
    Breakpoint.MD {
        Modifier
            .fillMaxWidth(75.percent)
    }
    Breakpoint.LG {
        Modifier
            .fillMaxWidth(50.percent)
    }

}

val FounderPopUpTextVariant by PopUpTextStyle.addVariant {
    base {
        Modifier
            .color(KhodedColors.POWDER_BLUE.rgb)
            .overflow(Overflow.Auto)
            .overflowWrap(OverflowWrap.BreakWord)
            .scrollBehavior(ScrollBehavior.Smooth)
            .scrollMargin(20.px)
            .styleModifier {
                property("scrollbar-color", "${Colors.BlueViolet.toRgb()} ${Colors.Transparent.toRgb()}")
                property("scrollbar-width", "thin")
            }
    }
}

val MessagingPopUpTextVariant by PopUpTextStyle.addVariant {
    base {
        Modifier
            .color(KhodedColors.PURPLE.rgb)
    }
}
val MessagingPopUpVariant by PopUpScreenStyle.addVariant {
    base {
        Modifier
            .maxHeight(50.vh)
            .background(KhodedColors.LIGHT_BLUE.rgb)
            .padding(10.px)
            .border {
                width(2.px)
                color(KhodedColors.PURPLE.rgb)
                style(LineStyle.Groove)
            }
    }
    Breakpoint.ZERO {
        Modifier.fillMaxWidth(90.percent)
    }
    Breakpoint.SM {
        Modifier.fillMaxWidth(80.percent)
    }
    Breakpoint.MD {
        Modifier.fillMaxWidth(50.percent)
    }
}
val StoryPopUpTextVariant by PopUpTextStyle.addVariant {
    base {
        Modifier
    }
}
val StoryPopUpVariant by PopUpScreenStyle.addVariant {
    base {
        Modifier
    }
}

@Composable
fun PopUpScreen(
    popUpUIModel: PopUpScreenUIModel,
    variant: ComponentVariant? = null,
    textVariant: ComponentVariant? = null,
    modifier: Modifier = Modifier
) = with(popUpUIModel) {
    Column(
        modifier = PopUpScreenStyle.toModifier(variant)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        when (val model = this@with) {
            is PopUpScreenUIModel.FounderHighlightPopUpUIModel -> FounderPopUpDisplay(
                textVariant = textVariant,
                model = model
            )

            is PopUpScreenUIModel.MessagingPopUpUiModel -> MessagingStateDisplay(
                textVariant = textVariant,
                model = model
            )
        }
        ButtonDisplay(
            state = buttonState,
            buttonVariant = PopUpCTAVariant,
            modifier = Modifier
                .margin(top = 10.px)
        ) {
            P(
                attrs = PopUpTextStyle.toModifier(textVariant)
                    .toAttrs()
            ) {
                Text(it)
            }
        }
    }
}

@Composable
fun FounderPopUpDisplay(
    textVariant: ComponentVariant?,
    model: PopUpScreenUIModel.FounderHighlightPopUpUIModel
) = with(model) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(Height.Inherit)
            .padding(topBottom = 15.px),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        FounderTextDisplay(
            text,
            PopUpTextStyle.toModifier(textVariant)
                .fillMaxWidth(80.percent)
        )
        image?.let {
            Image(
                src = it,
                description = "Animated image of founder.",
                modifier = PopUpImageStyle.toModifier(BioImageVariant)
            )
        }
    }
}

val FounderTitleVariant by PopUpTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontStyle(FontStyle.Oblique)
            .fontWeight(FontWeight.Bolder)
            .textDecorationLine(TextDecorationLine.Underline)
    }
}

val FounderDescVariant by PopUpTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontStyle(FontStyle.Normal)
            .fontWeight(FontWeight.Normal)
    }
}

val FounderShortDescVariant by PopUpTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontStyle(FontStyle.Italic)
            .fontWeight(FontWeight.ExtraLight)
            .margin(topBottom = 20.px)
            .padding(leftRight = 10.px)
            .backgroundColor(Colors.BlueViolet.copy(alpha = 30))
            .borderRadius(15.px)
    }
}

@Composable
fun FounderTextDisplay(founderText: FounderText, modifier: Modifier) {
    Column(
        modifier = modifier
            .height(Height.Inherit)
            .fillMaxHeight(80.percent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        P(
            attrs = PopUpTextStyle.toModifier(FounderTitleVariant)
                .toAttrs()
        ) {
            Text(founderText.titleText)
        }
        P(
            attrs = PopUpTextStyle.toModifier(FounderShortDescVariant)
                .toAttrs()
        ) {
            Text(founderText.shortDesc)
        }
        P(
            attrs = PopUpTextStyle.toModifier(FounderDescVariant)
                .toAttrs()
        ) {
            Text(founderText.desc)
        }
    }
}


@Composable
fun MessagingStateDisplay(
    textVariant: ComponentVariant?,
    model: PopUpScreenUIModel.MessagingPopUpUiModel
) = with(model) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            BSProgress(
                modifier = Modifier.fillMaxWidth(),
                label = "The label part.",
                striped = true,
                stripedAnimated = true,
                height = 20.vh
            )
        }
        P(
            attrs = PopUpTextStyle.toModifier(textVariant)
                .fillMaxWidth(80.percent)
                .toAttrs()
        ) {
            Text(promptText)
        }
    }
}

