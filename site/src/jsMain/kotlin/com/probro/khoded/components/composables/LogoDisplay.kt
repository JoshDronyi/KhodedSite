package com.probro.khoded.components.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.styles.textStyles.*
import com.probro.khoded.utils.fallInAnimation
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.Width
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.*
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.time.Duration.Companion.seconds

sealed interface LogoKind : ComponentKind

val LogoContainerVariant = BaseContainerStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
    }
}
val LogoImageVariant = ImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(50.percent)
            .fillMaxHeight(75.percent)
            .objectFit(ObjectFit.Fill)
    }
//    Breakpoint.ZERO { Modifier.fillMaxWidth(20.percent) }
//    Breakpoint.SM { Modifier.fillMaxWidth(20.percent) }
//    Breakpoint.MD { Modifier.fillMaxWidth(20.percent) }
//    Breakpoint.LG { Modifier.fillMaxWidth(20.percent) }
//    Breakpoint.XL { Modifier.fillMaxWidth(20.percent) }
}

val LogoTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .textAlign(TextAlign.Start)
            .padding(leftRight = 0.px)
            .margin(leftRight = 0.px)
            .color(Color.white)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XLarge)
    }
}

val HeaderLogoContainerVariant = LogoContainerVariant.extendedBy {
    base {
        Modifier
            .fillMaxWidth()
            .width(Width.FitContent)
    }
}

val HeaderImageVariant = LogoImageVariant.extendedBy {
    base {
        Modifier
            .maxHeight(3.vh)
            .width(Width.FitContent)
            .objectFit(ObjectFit.Contain)
    }
}

val HeaderLogoTextVariant = LogoTextVariant.extendedBy {
    base {
        Modifier
            .fillMaxWidth()
            .margin(leftRight = 10.px)
    }
}

val DarkLogoTextVariant = LogoTextVariant.extendedBy {
    base {
        Modifier
            .color(KhodedColors.PURPLE.rgb)
    }
}
val FooterLogoContainerVariant = LogoContainerVariant.extendedBy {
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
val FooterImageVariant = LogoImageVariant.extendedBy {
    base {
        Modifier
            .fillMaxWidth(40.percent)
    }
}
val FooterLogoTextVariant = LogoTextVariant.extendedBy {
    base {
        Modifier
            .fillMaxWidth()
            .letterSpacing(10.px)
    }

    Breakpoint.MD {
        Modifier.fontSize(FontSize.Larger)
    }
}
val SideNavLogoContainerVariant = LogoContainerVariant.extendedBy {
    base {
        Modifier
    }
}
val SideNavImageVariant = LogoImageVariant.extendedBy {
    base {
        Modifier
    }
}

val SideNavLogoTextVariant = LogoTextVariant.extendedBy {
    base {
        Modifier
    }
}

val SeparatorLogoContainerVariant = LogoContainerVariant.extendedBy {
    base {
        Modifier
    }
}

val SeparatorImageVariant = LogoImageVariant.extendedBy {
    base {
        Modifier
    }
}

val SeparatorLogoTextVariant = LogoTextVariant.extendedBy {
    base {
        Modifier
    }
}


@Composable
fun LogoDisplay(
    image: String,
    imageVariant: CssStyleVariant<ImageKind>? = null,
    variant: CssStyleVariant<ContainerKind>? = null,
    textVariant: CssStyleVariant<BaseTextKind>? = null
) {
    Row(
        modifier = BaseContainerStyle.toModifier(LogoContainerVariant),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Image(
            src = image,
            modifier = ImageStyle.toModifier(LogoImageVariant)
        )
        P(
            attrs = BaseTextStyle.toModifier(LogoTextVariant)
                .toAttrs()
        ) {
            "KHODED".forEachIndexed { index, letter ->
                val state by playState.collectAsState(AnimationPlayState.Running)
                Span(
                    attrs = Modifier
                        .position(Position.Relative)
                        .animation(
                            fallInAnimation.toAnimation(
                                duration = 600.ms,
                                timingFunction = AnimationTimingFunction.Ease,
                                delay = (index * 100).ms,
                                direction = AnimationDirection.Normal,
                                playState = state
                            )
                        )
                        .toAttrs()
                ) {
                    Text("$letter")
                }
            }
        }
    }
}

var running = true
var shouldRun = false

val playState = flow<AnimationPlayState> {
    while (shouldRun) {
        delay(3.seconds)
        if (running) {
            AnimationPlayState.Paused
        } else {
            AnimationPlayState.Running
        }
        running = !running
    }
}
