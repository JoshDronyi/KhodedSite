package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.components.widgets.StoryPageHeaderVariant
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.utils.Pages
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
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
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
                    from = Colors.MediumPurple,
                    to = Colors.RebeccaPurple
                )
            )
    }
}

val StoryTitleVariant by BaseTextStyle.addVariant {
    base {
        Modifier.fillMaxWidth()
            .fontStyle(FontStyle.Italic)
            .fontWeight(FontWeight.Thin)
            .textAlign(TextAlign.Start)
            .fontSize(FontSize.Large)

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

val StoryParagraphVariant by BaseTextStyle.addVariant {
    base {
        Modifier.fillMaxWidth()
            .textAlign(TextAlign.Start)
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
    Breakpoint.XL {
        Modifier.fontSize(FontSize.XLarge)
    }
}

val StoryPageTitleVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.XXLarge)
            .textAlign(TextAlign.Start)
    }
}

@Composable
fun StorySectionDisplay(
    header: @Composable (variant: ComponentVariant?) -> Unit
) = with(Pages.Story_Section.OurStory) {
    Column(
        modifier = BackgroundStyle.toModifier(StoryBackgroundVariant)
            .id(id),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        header(StoryPageHeaderVariant)
        Column(
            modifier = Modifier
                .fillMaxWidth(80.percent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            P(
                attrs = BaseTextStyle.toModifier(StoryPageTitleVariant)
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
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (storySection.title.isEmpty().not())
            ParagraphTitle(storySection.title)
        ParagraphContent(storySection.text)
    }
}

@Composable
fun ParagraphContent(text: String) {
    P(
        attrs = BaseTextStyle.toModifier(StoryParagraphVariant)
            .toAttrs()
    ) {
        Text(text)
    }
}

@Composable
fun ParagraphTitle(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            attrs = BaseTextStyle.toModifier(StoryTitleVariant)
                .toAttrs()
        ) {
            Text(title)
        }
    }
}
