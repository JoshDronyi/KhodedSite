package com.probro.khoded.styles.pageStyles

import com.probro.khoded.models.KhodedColors
import com.probro.khoded.models.Res.TextStyle.FONT_FAMILY
import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.styles.base.BaseTextStyle
import com.probro.khoded.styles.components.BaseBackgroundStyle
import com.probro.khoded.styles.components.BaseSectionStyle
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.Width
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderLeft
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.borderRight
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.overflowWrap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.layout.SimpleGridStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.extendedBy
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh


val TeamSectionBackgroundVariant = BaseBackgroundStyle.addVariant {
    base {
        Modifier
            .background(Colors.RebeccaPurple)
            .padding(topBottom = 20.px, leftRight = 10.px)
            .height(Height.FitContent)
    }
}
val FounderTextStyle = BaseTextStyle.addVariant {
    base {
        Modifier
            .padding(0.px)
            .margin(0.px)
            .color(Colors.White)
            .fontFamily("Roboto")
            .fontSize(FontSize.Medium)
            .textOverflow(TextOverflow.Ellipsis)
            .overflow(Overflow.Clip)
            .overflowWrap(OverflowWrap.Anywhere)
    }
}

val FounderTitleVariant = FounderTextStyle.extendedBy {
    base {
        Modifier
            .fontSize(48.px)
            .textAlign(TextAlign.Start)
            .padding(topBottom = 20.px)
            .color(Color.white)
    }
    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM {
        Modifier
    }
    Breakpoint.MD {
        Modifier
    }
    Breakpoint.LG {
        Modifier
    }
}

val FounderNameVariant = FounderTextStyle.extendedBy {
    base {
        Modifier
            .fontSize(FontSize.Medium)
            .padding(topBottom = 10.px)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.Smaller)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.Small)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Medium)
    }
    Breakpoint.LG {
        Modifier
            .fontSize(FontSize.Large)
    }
}
val FounderPositionVariant = FounderTextStyle.extendedBy {
    base {
        Modifier
            .fontSize(FontSize.Medium)

    }

    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.XSmall)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.Smaller)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Small)
    }
    Breakpoint.LG {
        Modifier
            .fontSize(FontSize.Medium)
    }
}
val FounderBioVariant = FounderTextStyle.extendedBy {
    base {
        Modifier
            .fontSize(FontSize.Small)
    }

    Breakpoint.ZERO {
        Modifier
            .fontSize(FontSize.XSmall)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(FontSize.Smaller)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(FontSize.Small)
    }
    Breakpoint.LG {
        Modifier
            .fontSize(FontSize.Medium)
    }
}

val FounderImageVariant = BaseImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .height(70.vh)
            .objectFit(ObjectFit.Contain)
    }

    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM {
        Modifier
    }
    Breakpoint.MD {
        Modifier
    }
    Breakpoint.LG {
        Modifier
    }
}
val FounderSectionVariant = BaseSectionStyle.addVariant {
    base {
        Modifier
            .width(Width.FitContent)
            .height(60.vh)
            .padding(10.px)
    }
}
val ImageSectionBacking = FounderSectionVariant.extendedBy {
    base {
        Modifier
            .padding(leftRight = 20.px)
    }
}

val CeoBackingSectionVariant = FounderSectionVariant.extendedBy {
    base {
        Modifier
            .backgroundColor(Colors.MediumPurple)
            .borderRadius(
                topLeft = 0.px,
                bottomLeft = 0.px,
                topRight = 20.px,
                bottomRight = 20.px,
            )
            .maxHeight(200.px)
            .borderRight {
                width(2.px)
                style(LineStyle.Solid)
                color(Colors.White)
            }
    }
}

val CtoBioSectionVariant = FounderSectionVariant.extendedBy {
    base {
        Modifier
            .backgroundColor(KhodedColors.PURPLE.rgb)
            .borderRadius(
                topLeft = 20.px,
                bottomLeft = 20.px,
                topRight = 0.px,
                bottomRight = 0.px,
            )
            .maxHeight(200.px)
            .borderLeft {
                width(2.px)
                style(LineStyle.Solid)
                color(Colors.White)
            }
    }
}


val FoundersGridVariant = SimpleGridStyle.addVariant {
    base {
        Modifier
    }
}


val FounderTextContainer = FounderSectionVariant.extendedBy {
    base {
        Modifier
            .width(Width.FitContent)
    }
}


val ReadMoreTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Medium)
            .color(Colors.White)
            .padding(leftRight = 15.px)
    }
}
