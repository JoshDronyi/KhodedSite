package com.probro.khoded.styles.pageStyles

import com.probro.khoded.models.KhodedColors
import com.probro.khoded.models.Res.TextStyle.FONT_FAMILY
import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.styles.base.BaseTextStyle
import com.probro.khoded.styles.components.BaseBackgroundStyle
import com.varabyte.kobweb.compose.css.ColorInterpolationMethod
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px


val StoryBackgroundVariant = BaseBackgroundStyle.addVariant {
    base {
        Modifier
            .height(Height.MaxContent)
            .color(Color.white)
            .backgroundImage(
                linearGradient(
                    from = KhodedColors.PURPLE.rgb,
                    to = Colors.RebeccaPurple,
                    dir = LinearGradient.Direction.ToBottom,
                    interpolation = ColorInterpolationMethod.ProphotoRgb
                )
            )
            .padding(bottom = 15.px)
    }
}

val StoryParagraphVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .margin(bottom = 20.px)
    }
}

val StoryTextStyle = BaseTextStyle.addVariant {
    base {
        Modifier.fillMaxWidth()
            .fontFamily(FONT_FAMILY)
            .textAlign(TextAlign.Start)
            .fontSize(FontSize.Medium)
            .padding(0.px)
            .margin(0.px)

    }

    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.XLarge)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XXLarge)
    }
}

val StoryTitleTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .cursor(Cursor.Pointer)
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
        Modifier.fontSize(FontSize.Larger)
    }
}

val StoryParagraphTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier.fillMaxWidth()
            .textAlign(TextAlign.Start)
            .padding(topBottom = 10.px)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Smaller)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Small)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Medium)
    }

    Breakpoint.LG {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.XL {
        Modifier.fontSize(FontSize.Larger)
    }

}

val StoryPageTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .textAlign(TextAlign.Start)
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
        Modifier.fontSize(36.px)
    }
    Breakpoint.XL {
        Modifier.fontSize(48.px)
    }
}

val OpportunitiesBackgroundVariant = BaseBackgroundStyle.addVariant {
    base {
        Modifier
            .padding(topBottom = 40.px)
            .backgroundImage(
                linearGradient(
                    from = Colors.RebeccaPurple,
                    to = KhodedColors.PURPLE.rgb,
                    dir = LinearGradient.Direction.ToBottom,
                    interpolation = ColorInterpolationMethod.ProphotoRgb
                )
            )
    }
}

val OpportuinitesImageVariant = BaseImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(30.percent)
            .translateY(ty = (-100).px)
            .objectFit(ObjectFit.Contain)
    }
}

val PostingsTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(48.px)
            .color(Color.white)
            .fontWeight(FontWeight.Bold)
            .textAlign(TextAlign.Start)
            .padding(leftRight = 15.px)
    }
}
