package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.*
import com.probro.khoded.components.widgets.StoryPageHeaderVariant
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.models.Res.TextStyle.FONT_FAMILY
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.fallInAnimation
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.animation.toAnimation
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val StoryBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .height(Height.MaxContent)
            .color(Color.white)
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = KhodedColors.PURPLE.rgb,
                    to = Colors.RebeccaPurple
                )
            )
            .padding(bottom = 15.px)
    }
}

val StoryParagraphStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .margin(bottom = 20.px)
    }
}

@Composable
fun StorySectionDisplay(
    header: @Composable (variant: ComponentVariant?) -> Unit
) = with(Pages.Story_Section.OurStory) {
    var sectionPosition by remember { mutableStateOf(SectionPosition.ON_SCREEN) }
    Column(
        modifier = BackgroundStyle.toModifier(StoryBackgroundVariant)
            .id(id),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        header(StoryPageHeaderVariant)
        Column(
            modifier = Modifier
                .fillMaxWidth(80.percent)
                .padding(topBottom = 20.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IsOnScreenObservable(
                sectionID = id,
            ) {
                sectionPosition = it
            }
            P(
                attrs = StoryTextStyle.toModifier(StoryPageTitleVariant)
                    .fillMaxWidth()
                    .position(Position.Relative)
                    .animation(
                        fallInAnimation.toAnimation(
                            duration = 600.ms,
                            timingFunction = AnimationTimingFunction.Ease
                        )
                    )
//                    .translateY(
//                        ty = when (sectionPosition) {
//                            SectionPosition.ABOVE -> (-100).px
//                            SectionPosition.ON_SCREEN -> 0.px
//                            SectionPosition.BELOW -> (-100).px
//                            SectionPosition.IDLE -> 0.px
//                        }
//                    )
//                    .opacity(
//                        when (sectionPosition) {
//                            SectionPosition.ABOVE -> 0.percent
//                            SectionPosition.ON_SCREEN -> 100.percent
//                            SectionPosition.BELOW -> 0.percent
//                            SectionPosition.IDLE -> 100.percent
//                        }
//                    )
//                    .transition(
//                        CSSTransition(property = "translate", duration = 600.ms),
//                        CSSTransition(property = "opacity", duration = 600.ms)
//                    )
                    .toAttrs()
            ) {
                Text(title)
            }
            storySections.forEach {
                StoryParagraph(it)
            }
        }
    }
}


@Composable
fun StoryParagraph(storySection: Pages.Story_Section.StorySection) {
    Column(
        modifier = StoryParagraphStyle.toModifier(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var shouldShow by remember { mutableStateOf(true) }
        if (storySection.title.isEmpty().not())
            ParagraphTitle(storySection.title) {
                shouldShow = !shouldShow
            }
        if (shouldShow || storySection.title.isEmpty())
            ParagraphContent(storySection.text)
    }
}

@Composable
fun ParagraphContent(text: String) {
    P(
        attrs = StoryTextStyle.toModifier(StoryParagraphTextVariant)
            .fillMaxWidth(95.percent)
            .toAttrs()
    ) {
        Text(text)
    }
}

@Composable
fun ParagraphTitle(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .onClick { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FaPlus(
            modifier = Modifier
                .color(Color.white)
                .margin(right = 10.px),
            size = IconSize.LG
        )
        P(
            attrs = StoryTextStyle.toModifier(StoryTitleTextVariant)
                .toAttrs()
        ) {
            Text(title)
        }
    }
}

val StoryTextStyle by ComponentStyle {
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

val StoryTitleTextVariant by StoryTextStyle.addVariant {
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

val StoryParagraphTextVariant by StoryTextStyle.addVariant {
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

val StoryPageTitleVariant by StoryTextStyle.addVariant {
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
