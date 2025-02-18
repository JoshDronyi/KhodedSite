package com.probro.khoded.styles.pageStyles

import com.probro.khoded.models.BaseTheme
import com.probro.khoded.styles.textStyles.BackgroundStyle
import com.probro.khoded.styles.textStyles.BaseTabStyle
import com.probro.khoded.styles.textStyles.ColumnStyle
import com.probro.khoded.styles.textStyles.ImageStyle
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.ScrollSnapStop
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val ServiceTabVariant = BaseTabStyle.addVariant {
    base {
        Modifier.margin(24.px).padding(8.px)
            .background(color = BaseTheme.secondaryColor.rgb)
            .border(width = 4.px, style = LineStyle.Groove, color = BaseTheme.primaryColor.rgb)
    }
}

val ServiceSectionDisplayVariant = ImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .padding(top = 60.px)
            .translate(tx = (-100).px, ty = (-60).px)
    }
}
val ServiceColumText = ColumnStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(50.percent)
    }
}


val DesignImageVariant = ImageStyle.addVariant {
    base {
        Modifier
            .fillMaxSize()
    }
    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM {
        Modifier
    }
    Breakpoint.MD {
        Modifier
            .translateY(ty = (-80).px)
    }
}

val DesignTextVariant = BackgroundStyle.addVariant {
    base {
        Modifier.fillMaxWidth()
            .margin(10.px)
    }
}

val ComputerPicVariant = ImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(60.percent)
            .objectFit(ObjectFit.Fill)
            .zIndex(1)
            .translate(tx = 40.px, ty = (-200).px)
    }
}

val DesignTextColumnVariant = ColumnStyle.addVariant {
    base {
        Modifier.fillMaxWidth(50.percent)
            .borderBottom {
                width(2.px)
                style(LineStyle.Solid)
                color(Colors.RebeccaPurple)
            }
            .scrollSnapStop(ScrollSnapStop.Normal)
    }
}


