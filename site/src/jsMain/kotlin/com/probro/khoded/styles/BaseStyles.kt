package com.probro.khoded.styles

import com.probro.khoded.utils.Constants
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val BaseTextStyle by ComponentStyle {
    base {
        Modifier.fontSize(FontSize.Medium)
            .fontFamily("Times New Roman")
            .padding(0.px)
            .margin(0.px)
            .textAlign(TextAlign.Center)
            .textOverflow(TextOverflow.Ellipsis)
            .overflow(Overflow.Hidden)
            .overflowWrap(OverflowWrap.Anywhere)
            .fillMaxWidth()

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
    Breakpoint.XL {
        Modifier.fontSize(FontSize.XXLarge)
    }
}

val TitleTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontSize(FontSize.Medium)
            .overflow(Overflow.Auto)
            .overflowWrap(OverflowWrap.Anywhere)
            .padding(leftRight = 15.px)
            .textAlign(TextAlign.Start)
    }
}

val MainTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontSize(48.px)
            .textAlign(TextAlign.Start)
            .fontWeight(FontWeight.Bolder)
            .padding(leftRight = 15.px)
            .textAlign(TextAlign.Start)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.XLarge)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.XL {
        Modifier.fontSize(36.px)
    }
}
val SubTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontSize(FontSize.Large)
            .padding(leftRight = 15.px)
            .textAlign(TextAlign.Start)

    }

    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
}

val ParagraphTitleVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontWeight(FontWeight.Bold)
            .textAlign(TextAlign.Center)
            .fontSize(30.px)
    }
}
val ParagraphTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(16.px)
            .textAlign(TextAlign.Start)
            .color(Colors.DarkGray)
    }
}

val TeamBioParagraphVaraiant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .padding(0.px)
            .margin(0.px)
            .textAlign(TextAlign.Center)
    }
}

val JobTitleVariant by BaseTextStyle.addVariant {
    base {
        Modifier.fillMaxWidth()
            .fontSize(FontSize.Larger)
            .fontWeight(FontWeight.Bold)
    }
}
val JobDescriptionVariant by BaseTextStyle.addVariant {
    base {
        Modifier.fillMaxWidth()
            .fontSize(FontSize.Large)
    }
}

val BaseSectionStyles by ComponentStyle {
    base {
        Modifier.fillMaxWidth()
            .minHeight(Constants.SECTION_HEIGHT.px)
            .borderBottom(width = 2.px, color = Color.black)
    }
}

val ImageStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .borderRadius(20.px)
            .objectFit(ObjectFit.Fill)
    }
}

val ConsultationImageVariant by ImageStyle.addVariant {
    base {
        Modifier.fillMaxWidth(50.percent)
            .objectFit(ObjectFit.Fill)
    }
    Breakpoint.ZERO {
        Modifier.fillMaxWidth(70.percent)
    }
    Breakpoint.SM
    Breakpoint.MD
    Breakpoint.LG
    Breakpoint.XL
}

val AboutLandingTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontSize(48.px)
            .textAlign(TextAlign.Start)
            .fontWeight(FontWeight.Bolder)
            .padding(leftRight = 15.px)
            .textAlign(TextAlign.Start)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(36.px)
    }
    Breakpoint.LG {
        Modifier.fontSize(48.px)
    }
    Breakpoint.XL {
        Modifier.fontSize(48.px)
    }
}
val AboutLandingSubTextVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontSize(FontSize.Large)
            .padding(leftRight = 15.px)
            .textAlign(TextAlign.Start)

    }

    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.LG
    Breakpoint.XL
}
