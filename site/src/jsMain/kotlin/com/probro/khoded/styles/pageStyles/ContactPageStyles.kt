package com.probro.khoded.styles.pageStyles

import com.probro.khoded.styles.textStyles.BackgroundStyle
import com.probro.khoded.styles.textStyles.BaseRowStyle
import com.probro.khoded.styles.textStyles.BaseTextStyle
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.extendedBy
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

val ContactLandingBackgroundVariant = BackgroundStyle.addVariant {
    base {
        Modifier
            .height(Height.FitContent)
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.SkyBlue,
                    to = Colors.WhiteSmoke
                )
            )
    }
}
val ContactFooterBackgroundVariant = BackgroundStyle.addVariant {
    base {
        Modifier
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.WhiteSmoke,
                    to = Colors.SkyBlue
                )
            )
    }
}


val ClientInfoPromptVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(48.px)
            .textAlign(TextAlign.Start)
            .fontWeight(FontWeight.Bold)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Larger)
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
}

val ContactPageRowVariant = BaseRowStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
    }
}

val LandingSectionVariant = ContactPageRowVariant.extendedBy {
    base {
        Modifier
            .minHeight(50.vh)
            .height(Height.FitContent)
    }
}
val MessagingSectionVariant = ContactPageRowVariant.extendedBy {
    base {
        Modifier
            .height(50.vh)
            .margin(1.px)
    }
}