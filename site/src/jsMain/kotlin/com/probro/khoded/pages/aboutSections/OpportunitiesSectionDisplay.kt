package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.*
import com.probro.khoded.models.Images
import com.probro.khoded.models.KhodedColors
import com.probro.khoded.pages.homeSections.BackgroundStyle
import com.probro.khoded.styles.BaseTextStyle
import com.probro.khoded.styles.JobDescriptionVariant
import com.probro.khoded.styles.JobTitleVariant
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.TitleIDs
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
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val OpportunitiesBackgroundVariant by BackgroundStyle.addVariant {
    base {
        Modifier
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottom,
                    from = Colors.RebeccaPurple,
                    to = KhodedColors.PURPLE.rgb,
                )
            )
    }
}

@Composable
fun OpportunitiesSectionDisplay(
    footer: @Composable () -> Unit
) = with(Pages.Story_Section.JoinOurTeam) {
    Column(
        modifier = BackgroundStyle.toModifier(OpportunitiesBackgroundVariant)
            .id(id),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(getWidthFromBreakpoint())
                .padding(bottom = 40.px),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            JobPostings(title, positions)
            Image(
                src = Images.StoryPage.megaphone,
                description = "Megaphone",
                modifier = Modifier.fillMaxWidth(30.percent)
                    .align(Alignment.Top)
                    .translateY(ty = (-50).px)
                    .objectFit(ObjectFit.Contain)

            )
        }
        footer()
    }
}

@Composable
private fun getWidthFromBreakpoint(): CSSNumericValue<out CSSUnitLengthOrPercentage> {
    return when (rememberBreakpoint()) {
        Breakpoint.ZERO, Breakpoint.SM -> 100.percent
        Breakpoint.MD -> 90.percent
        Breakpoint.LG, Breakpoint.XL -> 80.percent
    }
}

val PostingsTitleVariant by BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(48.px)
            .color(Color.white)
            .fontWeight(FontWeight.Bold)
            .textAlign(TextAlign.Start)
            .padding(leftRight = 15.px)
    }
}

@Composable
fun JobPostings(
    title: String, positions: List<Pages.Story_Section.JobPosition>
) {
    var state by remember { mutableStateOf(SectionPosition.IDLE) }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IsOnScreenObservable(
            sectionID = TitleIDs.opportunitiesTitle
        ) {
            state = it
        }
        P(
            attrs = BaseTextStyle.toModifier(PostingsTitleVariant)
                .id(TitleIDs.opportunitiesTitle)
                .fillMaxWidth()
                .translateY(
                    ty = when (state) {
                        SectionPosition.ABOVE -> (-100).px
                        SectionPosition.ON_SCREEN -> 0.px
                        SectionPosition.BELOW -> (-100).px
                        SectionPosition.IDLE -> 0.px
                    }
                )
                .opacity(
                    when (state) {
                        SectionPosition.ABOVE -> 0.percent
                        SectionPosition.ON_SCREEN -> 100.percent
                        SectionPosition.BELOW -> 0.percent
                        SectionPosition.IDLE -> 100.percent
                    }
                )
                .transition(
                    CSSTransition(property = "translate", duration = 600.ms),
                    CSSTransition(property = "opacity", duration = 600.ms)
                )
                .toAttrs()
        ) {
            Text(title)
        }
        SimpleGrid(
            numColumns = numColumns(base = 1, sm = 2, md = 3),
            modifier = Modifier
                .height(Height.FitContent)
                .fillMaxWidth()
                .padding(topBottom = 20.px, leftRight = 10.px),
        ) {
            positions.forEach {
                JobPositionDisplay(
                    position = it,
                ) { title ->
                    println("Clicked on $title")
                }
            }
        }
    }
}

val JobPositionStyle by ComponentStyle {
    base {
        Modifier
            .borderRadius(20.px)
            .background(Color.white)
            .color(Color.black)
            .textOverflow(TextOverflow.Ellipsis)
            .overflowWrap(OverflowWrap.Anywhere)
            .minHeight(Height.FitContent)
            .padding(10.px)
            .margin(10.px)
    }
}

val JobPositionContainerStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .padding(20.px)
            .height(Height.Inherit)
    }
}

@Composable
fun JobPositionDisplay(
    position: Pages.Story_Section.JobPosition,
    onClick: (title: String) -> Unit
) = with(position) {
    var shouldShow by remember { mutableStateOf(false) }
    Column(
        modifier = JobPositionStyle.toModifier()
            .fillMaxWidth(80.percent)
            .fillMaxHeight(90.percent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        P(
            attrs = BaseTextStyle.toModifier(JobTitleVariant)
                .onClick {
                    shouldShow = !shouldShow
                    onClick(positionTitle)
                }
                .toAttrs()
        ) {
            Text(positionTitle)
        }
        if (shouldShow) {
            P(
                attrs = BaseTextStyle.toModifier(JobDescriptionVariant)
                    .toAttrs()
            ) {
                Text(positionDesc)
            }
        }
    }
}