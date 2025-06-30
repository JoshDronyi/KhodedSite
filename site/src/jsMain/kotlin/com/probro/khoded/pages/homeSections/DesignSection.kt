package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.styles.animations.jobPostingShiftDownKeyFrames
import com.probro.khoded.styles.animations.jobPostingShiftUPKeyFrames
import com.probro.khoded.styles.base.BaseTextStyle
import com.probro.khoded.styles.base.SectionTitleVariant
import com.probro.khoded.styles.base.SubTitleVariant
import com.probro.khoded.styles.components.BaseBackgroundStyle
import com.probro.khoded.styles.components.BaseColumnStyle
import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.TitleIDs
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


@Composable
fun DesignSectionDisplay(data: Pages.Home_Section.Design) = with(data) {
    Box(
        modifier = BaseBackgroundStyle.toModifier()
            .id(id)
    ) {
        Image(
            src = mainImage,
            modifier = BaseImageStyle.toModifier()
                .align(Alignment.TopEnd)
        )
        DesignTextSection(
            upperText = mainText,
            underlineImage = underlineImage,
            lowerText = subText,
            modifier = BaseColumnStyle.toModifier()
        )
    }
}

@Composable
fun DesignTextSection(
    upperText: String,
    underlineImage: String,
    lowerText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DesignHeading(upperText)
        Image(
            src = underlineImage,
            modifier = BaseImageStyle.toModifier(BlackUnderlineVariant)
        )
        DesignSubText(lowerText)
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

@Composable
fun DesignSubText(lowerText: String) {
    P(
        attrs = BaseTextStyle.toModifier(SubTitleVariant)
            .toAttrs()
    ) {
        Text(value = lowerText)
    }
}

const val LENGTH_OF_JUST = 4

@Composable
fun DesignHeading(
    upperText: String
) {
    val justIndex = remember { upperText.indexOf("just") }
    val firstText = remember { upperText.substring(0, justIndex) }
    val just = remember { upperText.substring(justIndex, justIndex + LENGTH_OF_JUST) }
    val secondText = remember { upperText.substring(justIndex + LENGTH_OF_JUST) }

    var sectionPosition by remember { mutableStateOf(SectionPosition.IDLE) }
    val animation = when (sectionPosition) {
        SectionPosition.ABOVE -> jobPostingShiftDownKeyFrames
        SectionPosition.ON_SCREEN -> jobPostingShiftUPKeyFrames
        SectionPosition.BELOW -> jobPostingShiftDownKeyFrames
        SectionPosition.IDLE -> jobPostingShiftUPKeyFrames
    }
    P(
        attrs = BaseTextStyle.toModifier(SectionTitleVariant)
            .id(TitleIDs.designTitleID)
            .animation(animation.toAnimation(600.ms))
            .toAttrs()
    ) {
        Text(value = firstText)
        Span(
            attrs = Modifier
                .color(KhodedColors.LIGHT_BLUE.rgb)
                .toAttrs()
        ) {
            Text(just)
        }
        Text(value = secondText)
    }

    IsOnScreenObservable(
        sectionID = TitleIDs.designTitleID,
    ) {
        sectionPosition = it
        println("New Position for ${Pages.Home_Section.Design.id} is $it")
    }
}