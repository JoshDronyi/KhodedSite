package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.*
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.models.Res.TextStyle.FONT_FAMILY
import com.probro.khoded.styles.base.BaseTextStyle
import com.probro.khoded.styles.components.BaseBackgroundStyle
import com.probro.khoded.utils.*
import com.varabyte.kobweb.compose.css.*
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
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

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

@Composable
fun StorySectionDisplay() = with(Pages.Story_Section.OurStory) {
    var sectionPosition by remember { mutableStateOf(SectionPosition.ON_SCREEN) }
    Column(
        modifier = BaseBackgroundStyle.toModifier(StoryBackgroundVariant)
            .id(id),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(80.percent)
                .padding(topBottom = 20.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IsOnScreenObservable(
                sectionID = TitleIDs.storyTitle,
            ) {
                sectionPosition = it
            }
            P(
                attrs = BaseTextStyle.toModifier(StoryPageTitleVariant)
                    .id(TitleIDs.storyTitle)
                    .fillMaxWidth()
                    .position(Position.Relative)
                    .animation(
                        fallInAnimation.toAnimation(
                            duration = 600.ms,
                            timingFunction = AnimationTimingFunction.Ease
                        )
                    )
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
        modifier = BaseTextStyle.toModifier(StoryParagraphVariant),
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
        attrs = BaseTextStyle.toModifier(StoryParagraphTextVariant)
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
            attrs = BaseTextStyle.toModifier(StoryTitleTextVariant)
                .toAttrs()
        ) {
            Text(title)
        }
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