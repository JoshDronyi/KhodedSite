package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.*
import com.probro.khoded.components.OptimizedImage
import com.probro.khoded.models.Images
import com.probro.khoded.styles.BaseImageStyle
import com.probro.khoded.styles.animations.jobPostingShiftDownKeyFrames
import com.probro.khoded.styles.animations.jobPostingShiftUPKeyFrames
import com.probro.khoded.styles.base.BaseTextStyle
import com.probro.khoded.styles.base.JobDescriptionVariant
import com.probro.khoded.styles.base.JobTitleVariant
import com.probro.khoded.styles.components.BaseBackgroundStyle
import com.probro.khoded.styles.components.BaseTabStyle
import com.probro.khoded.styles.pageStyles.OpportuinitesImageVariant
import com.probro.khoded.styles.pageStyles.OpportunitiesBackgroundVariant
import com.probro.khoded.styles.pageStyles.PostingsTitleVariant
import com.probro.khoded.utils.IsOnScreenObservable
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.SectionPosition
import com.probro.khoded.utils.TitleIDs
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.framework.annotations.DelicateApi
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun OpportunitiesSectionDisplay() = with(Pages.Story_Section.JoinOurTeam) {
    Box(
        modifier = BaseBackgroundStyle.toModifier(OpportunitiesBackgroundVariant)
            .id(id),
        contentAlignment = Alignment.Center
    ) {
        OptimizedImage(
            src = Images.StoryPage.megaphone,
            description = "Megaphone",
            modifier = BaseImageStyle.toModifier(OpportuinitesImageVariant)
                .align(Alignment.TopEnd)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            JobPostings(
                title = title,
                positions = positions,
                modifier = Modifier
                    .fillMaxWidth(getWidthFromBreakpoint())
                    .padding(bottom = 40.px)
            )
        }
    }
}

@OptIn(DelicateApi::class)
@Composable
private fun getWidthFromBreakpoint(): CSSNumericValue<out CSSUnitLengthOrPercentage> {
    return when (rememberBreakpoint()) {
        Breakpoint.ZERO, Breakpoint.SM -> 100.percent
        Breakpoint.MD -> 90.percent
        Breakpoint.LG, Breakpoint.XL -> 80.percent
    }
}

@Composable
fun JobPostings(
    title: String,
    positions: List<Pages.Story_Section.JobPosition>,
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf(SectionPosition.IDLE) }
    val animations by remember {
        mutableStateOf(
            when (state) {
                SectionPosition.ABOVE -> jobPostingShiftDownKeyFrames
                SectionPosition.ON_SCREEN -> jobPostingShiftUPKeyFrames
                SectionPosition.BELOW -> jobPostingShiftDownKeyFrames
                SectionPosition.IDLE -> {
                    jobPostingShiftUPKeyFrames
                }
            }
        )
    }

    Column(
        modifier = modifier,
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
                .animation(
                    animations.toAnimation(600.ms)
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


@Composable
fun JobPositionDisplay(
    position: Pages.Story_Section.JobPosition,
    onClick: (title: String) -> Unit
) = with(position) {
    var shouldShow by remember { mutableStateOf(false) }
    Column(
        modifier = BaseTabStyle.toModifier()
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