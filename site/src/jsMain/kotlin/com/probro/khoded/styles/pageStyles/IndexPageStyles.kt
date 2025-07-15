package com.probro.khoded.styles.pageStyles

import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.styles.base.BaseTextStyle
import com.probro.khoded.styles.components.BaseColumnStyle
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val PinkUnderLineVaraint = BaseImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(30.percent)
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
}
val ServiceSectionVariant = BaseColumnStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .borderBottom {
                width(2.px)
                style(LineStyle.Solid)
                color(Color.purple)
            }
    }
    hover {
        Modifier
            .cursor(Cursor.Pointer)
    }

}

val ServiceTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .color(Color.black)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.XXSmall)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Smaller)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.LG
    Breakpoint.XL {
        Modifier
            .fontSize(FontSize.XXLarge)
    }
}
val ServiceDescriptionVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .color(Color.black)
            .fillMaxWidth(80.percent)
    }
}

val BlackUnderlineVariant = BaseImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(40.percent)
    }
    Breakpoint.ZERO {
        Modifier
    }
    Breakpoint.SM {
        Modifier
//            .translateY(ty = (-15).px)
    }
    Breakpoint.MD {
        Modifier
//            .translateY(ty = (-30).px)
    }
}

val PlaneImageVariant = BaseImageStyle.addVariant {
    base {
        Modifier
            .zIndex(2)
            .overflow(Overflow.Hidden)
    }
    Breakpoint.ZERO {
        Modifier.fillMaxWidth(30.percent)
            .translate(tx = 50.px, ty = (-100).px)
    }
    Breakpoint.SM {
        Modifier
            .fillMaxWidth(40.percent)
    }
    Breakpoint.MD {
        Modifier.fillMaxWidth(30.percent)
    }
}
