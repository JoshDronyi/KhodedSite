package com.probro.khoded.components.composables

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
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
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val LogoContainerStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
    }
}
val LogoImageStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth(50.percent)
            .fillMaxHeight(75.percent)
            .objectFit(ObjectFit.Fill)
    }
    Breakpoint.ZERO { Modifier.fillMaxWidth(20.percent) }
    Breakpoint.SM { Modifier.fillMaxWidth(20.percent) }
    Breakpoint.MD { Modifier.fillMaxWidth(20.percent) }
    Breakpoint.LG { Modifier.fillMaxWidth(20.percent) }
    Breakpoint.XL { Modifier.fillMaxWidth(20.percent) }
}

val LogoTextStyle by ComponentStyle {
    base {
        Modifier
            .textAlign(TextAlign.Start)
            .translate(tx = (-10).px)
            .padding(leftRight = 0.px)
            .margin(leftRight = 0.px)
            .color(Color.white)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.XLarge)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(48.px)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.XL
}

val HeaderLogoContainerVariant by LogoContainerStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
    }
}

val HeaderImageVariant by LogoImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(10.percent)
    }
    Breakpoint.MD {
        Modifier.letterSpacing(20.px)
    }
}

val HeaderLogoTextVariant by LogoTextStyle.addVariant {
    base {
        Modifier
    }
}
val FooterLogoContainerVariant by LogoContainerStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .border {
                width(1.px)
                color(Color.white)
                style(LineStyle.Solid)
            }
    }
}
val FooterImageVariant by LogoImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(40.percent)
    }
}
val FooterLogoTextVariant by LogoTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .letterSpacing(10.px)
    }

    Breakpoint.MD {
        Modifier.fontSize(FontSize.Larger)
    }
}
val SideNavLogoContainerVariant by LogoContainerStyle.addVariant {
    base {
        Modifier
    }
}
val SideNavImageVariant by LogoImageStyle.addVariant {
    base {
        Modifier
    }
}

val SideNavLogoTextVariant by LogoTextStyle.addVariant {
    base {
        Modifier
    }
}

val SeparatorLogoContainerVariant by LogoContainerStyle.addVariant {
    base {
        Modifier
    }
}

val SeparatorImageVariant by LogoImageStyle.addVariant {
    base {
        Modifier
    }
}

val SeparatorLogoTextVariant by LogoTextStyle.addVariant {
    base {
        Modifier
    }
}


@Composable
fun LogoDisplay(
    image: String,
    imageVariant: ComponentVariant? = null,
    variant: ComponentVariant? = null,
    textVariant: ComponentVariant? = null
) {
    Row(
        modifier = LogoContainerStyle.toModifier(variant),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            src = image,
            modifier = LogoImageStyle.toModifier(imageVariant)
        )
        P(
            attrs = LogoTextStyle.toModifier(textVariant)
                .toAttrs()
        ) {
            Text("KHODED")
        }
    }
}