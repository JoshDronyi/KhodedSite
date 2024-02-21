package com.probro.khoded.components.composables

import androidx.compose.runtime.Composable
import com.probro.khoded.PopUpCTAVariant
import com.probro.khoded.models.ButtonState
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.models.Res.TextStyle.FONT_FAMILY
import com.probro.khoded.pages.homeSections.ButtonDisplay
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
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
            .height(Height.FitContent)
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

val PopUpBioScreenVariant by PopUpScreenStyle.addVariant {
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
val PopUpTextVariant by PopUpTextStyle.addVariant {
    base {
        Modifier
            .color(KhodedColors.POWDER_BLUE.rgb)
            .margin(right = 10.px)
    }
}

val ConsultationPopUpVariant by PopUpScreenStyle.addVariant {
    base {
        Modifier
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

val ConsultationPopUpTextVariant by PopUpTextStyle.addVariant {
    base {
        Modifier
            .color(KhodedColors.PURPLE.rgb)
    }
}

val ContactPopUpTextVariant by PopUpTextStyle.addVariant {
    base {
        Modifier
    }
}
val ContactPopUpVariant by PopUpScreenStyle.addVariant {
    base {
        Modifier
    }
}

data class PopUpScreenUIModel(
    val promptText: String,
    val image: String? = null,
    val buttonState: ButtonState,
    var isShowing: Boolean = false
)

@Composable
fun PopUpScreen(
    popUpUIModel: PopUpScreenUIModel,
    variant: ComponentVariant? = null,
    textVariant: ComponentVariant? = null,
    modifier: Modifier = Modifier
) = with(popUpUIModel) {
    println("${popUpUIModel.promptText}")

    Column(
        modifier = PopUpScreenStyle.toModifier(variant)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            P(
                attrs = PopUpTextStyle.toModifier(textVariant)
                    .fillMaxWidth(80.percent)
                    .toAttrs()
            ) {
                Text(promptText)
            }
            image?.let {
                Image(
                    src = it,
                    description = "Animated image of founder.",
                    modifier = PopUpImageStyle.toModifier(BioImageVariant)
                )
            }
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

