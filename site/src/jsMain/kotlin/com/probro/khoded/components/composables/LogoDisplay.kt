package com.probro.khoded.components.composables

import androidx.compose.runtime.Composable
import com.probro.khoded.styles.BaseTextStyle
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
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val LogoDisplayStyle by ComponentStyle {
    base {
        Modifier
    }
}

val HeaderLogoVariant by LogoDisplayStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(30.percent)
            .fillMaxHeight(80.percent)
    }
}

val FooterLogoVariant by LogoDisplayStyle.addVariant {
    base {
        Modifier
    }
}
val SideNavLogoVariant by LogoDisplayStyle.addVariant {
    base {
        Modifier
    }
}

val SeparatorLogoVariant by LogoDisplayStyle.addVariant {
    base {
        Modifier
    }
}


@Composable
fun LogoDisplay(
    image: String,
    modifier: Modifier = Modifier,
    variant: ComponentVariant? = null,
    textVariant: ComponentVariant? = null
) {
    val breakpoint = rememberBreakpoint()
    Row(
        modifier = LogoDisplayStyle.toModifier(variant).then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            src = image,
            modifier = Modifier
                .fillMaxWidth(50.percent)
                .fillMaxHeight(75.percent)
                .objectFit(ObjectFit.Fill)
        )
        P(
            attrs = BaseTextStyle.toModifier(textVariant)
                .fontSize(
                    when (breakpoint) {
                        Breakpoint.MD, Breakpoint.LG -> FontSize.XLarge
                        else -> FontSize.XXLarge
                    }
                )
                .textAlign(TextAlign.Start)
                .letterSpacing(20.px)
                .translate(tx = (-10).px)
                .padding(leftRight = 0.px)
                .margin(leftRight = 0.px)
                .color(Color.white)
                .fillMaxWidth()
                .toAttrs()
        ) {
            Text("KHODED")
        }
    }
}