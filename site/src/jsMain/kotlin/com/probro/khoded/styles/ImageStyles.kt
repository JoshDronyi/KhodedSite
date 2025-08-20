package com.probro.khoded.styles

import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh


sealed interface ImageKind : ComponentKind

// Image styles - Mobile First
val BaseImageStyle = CssStyle<ImageKind> {
    base {
        Modifier
            .fillMaxWidth()
            .borderRadius(8.px)
            .objectFit(ObjectFit.Cover) // Cover is usually better than Fill
            .maxWidth(100.percent)
            .height(Height.Inherit)
    }

    Breakpoint.SM {
        Modifier.borderRadius(12.px)
    }

    Breakpoint.MD {
        Modifier.borderRadius(16.px)
    }

    Breakpoint.LG {
        Modifier.borderRadius(20.px)
    }
}

val LandingImageVariant = BaseImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .padding(top = 20.px)
            .maxHeight(40.vh)
    }

    Breakpoint.SM {
        Modifier
            .fillMaxWidth(90.percent)
            .padding(top = 30.px)
            .maxHeight(45.vh)
    }

    Breakpoint.MD {
        Modifier
            .fillMaxWidth(85.percent)
            .padding(top = 40.px)
            .maxHeight(50.vh)
    }

    Breakpoint.LG {
        Modifier
            .fillMaxWidth(80.percent)
            .padding(top = 60.px)
            .maxHeight(60.vh)
    }
}

val ServiceImageVariant = BaseImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .padding(top = 20.px)
            .maxHeight(30.vh)
    }

    Breakpoint.SM {
        Modifier
            .padding(top = 30.px)
            .maxHeight(35.vh)
    }

    Breakpoint.MD {
        Modifier
            .padding(top = 40.px)
            .maxHeight(40.vh)
    }

    Breakpoint.LG {
        Modifier
            .padding(top = 60.px)
            .maxHeight(50.vh)
            .translateX(tx = (-50).px) // Reduced transform for better mobile experience
    }

    Breakpoint.XL {
        Modifier.translateX(tx = (-100).px)
    }
}

val ConsultationImageVariant = BaseImageStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .maxHeight(40.vh)
    }

    Breakpoint.SM {
        Modifier.maxHeight(45.vh)
    }

    Breakpoint.MD {
        Modifier
            .fillMaxSize()
            .translateY(ty = (-25).px)
    }

    Breakpoint.LG {
        Modifier.translateY(ty = (-50).px)
    }
}

val UnderlineImageVariant = BaseImageStyle.addVariant {
    base {
        Modifier.maxWidth(15.percent)
    }
}