//package com.probro.khoded.sections
//
//import androidx.compose.runtime.Composable
//import com.probro.khoded.models.Images
//import com.probro.khoded.components.ImageState
//import com.probro.khoded.components.SideBySide
//import com.probro.khoded.components.StatementSection
//import com.probro.khoded.models.Res
//import com.probro.khoded.styles.BaseSectionStyles
//import com.varabyte.kobweb.compose.foundation.layout.Arrangement
//import com.varabyte.kobweb.compose.foundation.layout.Column
//import com.varabyte.kobweb.compose.ui.Alignment
//import com.varabyte.kobweb.compose.ui.Modifier
//import com.varabyte.kobweb.compose.ui.modifiers.background
//import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
//import com.varabyte.kobweb.silk.components.style.toModifier
//import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
//import com.probro.khoded.utils.Constants.Strings
//
//@Composable
//fun Statements(
//    modifier: Modifier = Modifier
//        .background(Res.Themes.baseTheme.surfaceColor.rgb),
//    breakpoint: Breakpoint = rememberBreakpoint()
//) {
//    Column(
//        modifier = BaseSectionStyles.toModifier()
//            .then(modifier),
//        verticalArrangement = Arrangement.SpaceEvenly,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        StatementSection(
//            breakpoint = breakpoint,
//            imageState = ImageState(
//                image = Images.HomePage.consultation,
//                imageDesc = "Generic mission picture.",
//                title = Strings.missionStatementTitle,
//                statement = Strings.missionStatement
//            )
//        )
//        StatementSection(
//            breakpoint = breakpoint,
//            imageState = ImageState(
//                image = Images.HomePage.branding,
//                imageDesc = "Generic vision picture",
//                title = Strings.visionStatementTitle,
//                statement = Strings.visionStatement
//            ),
//            direction = SideBySide.Direction.IMAGE_RIGHT
//        )
//    }
//    /* val sectionID = Pages.Home.Section.About.id
//     OnViewPortEnteredObservable(
//         sectionID = sectionID,
//         distanceFromTop = SECTION_HEIGHT * 2,
//         onEnterViewPort = { println("$sectionID was entered") },
//         onExitViewPort = { println("$sectionID was exited.") }
//     ) {}*/
//}