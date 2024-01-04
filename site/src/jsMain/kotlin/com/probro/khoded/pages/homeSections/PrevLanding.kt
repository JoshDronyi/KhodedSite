//package com.probro.khoded.sections
//
//import androidx.compose.runtime.*
//import com.probro.khoded.components.com.probro.khoded.components.composables.BackingCard.kt
//import com.probro.khoded.components.com.probro.khoded.components.composables.getBackingCardStyle
//import com.probro.khoded.components.TitleCardDisplay
//import com.probro.khoded.styles.BaseSectionStyles
//import com.probro.khoded.styles.BaseTextStyle
//import com.probro.khoded.utils.OnViewPortEnteredObservable
//import com.probro.khoded.utils.Pages
//import com.varabyte.kobweb.compose.css.*
//import com.varabyte.kobweb.compose.foundation.layout.Arrangement
//import com.varabyte.kobweb.compose.foundation.layout.Box
//import com.varabyte.kobweb.compose.foundation.layout.Column
//import com.varabyte.kobweb.compose.ui.Alignment
//import com.varabyte.kobweb.compose.ui.Modifier
//import com.varabyte.kobweb.compose.ui.modifiers.*
//import com.varabyte.kobweb.compose.ui.toAttrs
//import com.varabyte.kobweb.silk.components.layout.SimpleGrid
//import com.varabyte.kobweb.silk.components.layout.numColumns
//import com.varabyte.kobweb.silk.components.style.toModifier
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import org.jetbrains.compose.web.css.ms
//import org.jetbrains.compose.web.css.percent
//import org.jetbrains.compose.web.css.px
//import org.jetbrains.compose.web.dom.P
//import org.jetbrains.compose.web.dom.Text
//import com.probro.khoded.utils.Constants
//import kotlin.time.Duration.Companion.seconds
//
//
//@Composable
//fun LandingSection(
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = BaseSectionStyles.toModifier()
//            .then(modifier),
//        contentAlignment = Alignment.Center
//    ) {
//        SimpleGrid(
//            numColumns = numColumns(base = 1, md = 2),
//            modifier = Modifier.fillMaxSize()
//        ) {
//            TitleCardDisplay(
//                title = "Khoded",
//                subTitle = "The right website tells your story in all the moments and places you can't."
//            )
//            TestimonialSection(
//                testimonials = listOf(
//                    Testimonial(),
//                    Testimonial(),
//                    Testimonial(),
//                    Testimonial(),
//                    Testimonial(),
//                )
//            )
//        }
//    }
//}
//
//data class Testimonial(
//    val from: String = "A reputable source",
//    val comments: String = Constants.Strings.LOREM_IPSUM_PARAGRAPH
//)
//
//
//const val TESTIMONIAL_DELAY_SECONDS = 3
//
//@Composable
//fun TestimonialSection(
//    modifier: Modifier = Modifier,
//    testimonials: List<Testimonial>
//) {
//    var shouldAnimate by remember { mutableStateOf(true) }
//    var currentDisplay by remember { mutableStateOf(0) }
//    val scope = rememberCoroutineScope()
//    TestimonialDisplay(
//        modifier
//            .fillMaxWidth()
//            .fillMaxHeight(),
//        testimonials,
//        currentDisplay
//    )
//    OnViewPortEnteredObservable(
//        sectionID = Pages.StylingClass.Testimonials.name,
//        onEnterViewPort = {
//            println("Entered viewport for Testimonials")
//            shouldAnimate = true
//        },
//        onExitViewPort = {
//            println("Exited viewport for Testimonials")
//            shouldAnimate = false
//        }
//    )
//    LaunchedEffect(shouldAnimate) {
//        scope.launch {
//            while (shouldAnimate) {
//                delay(TESTIMONIAL_DELAY_SECONDS.seconds)
//                if (currentDisplay + 1 >= testimonials.size) currentDisplay = 0
//                else currentDisplay++
//            }
//        }
//    }
//}
//
//@Composable
//private fun TestimonialDisplay(
//    modifier: Modifier,
//    testimonials: List<Testimonial>,
//    currentDisplay: Int
//) {
//    Box(
//        modifier,
//        contentAlignment = Alignment.Center
//    ) {
//        testimonials.forEachIndexed() { index, testimonial ->
//            if (index == 0) {
//                TestimonialItem(
//                    modifier = Modifier
//                        .id(Pages.Misc.Sections.Testimonials.id),
//                    testimonial = testimonial,
//                    isVisible = currentDisplay == index
//                )
//            } else {
//                TestimonialItem(
//                    testimonial = testimonial,
//                    isVisible = currentDisplay == index
//                )
//            }
//        }
//    }
//}
//
//val chars = 10..Constants.Strings.LOREM_IPSUM_PARAGRAPH.length
//
//@Composable
//fun TestimonialItem(
//    modifier: Modifier = Modifier,
//    testimonial: Testimonial = Testimonial(),
//    isVisible: Boolean = false
//) {
//    com.probro.khoded.components.composables.BackingCard.kt(
//        modifier = com.probro.khoded.components.composables.getBackingCardStyle.toModifier().then(modifier)
//            .fillMaxWidth()
//            .maxWidth(60.percent)
//            .opacity(if (isVisible) 90.percent else 0.percent)
//            .visibility(
//                if (isVisible) Visibility.Visible else Visibility.Collapse
//            )
//            .translate(tx = if (isVisible) 0.px else (100).px)
//            .height(Height.FitContent)
//            .transition(
//                CSSTransition("visibility", duration = 500.ms),
//                CSSTransition("maxWidth", duration = 500.ms),
//                CSSTransition("opacity", duration = 500.ms),
//                CSSTransition("translate", duration = 500.ms)
//            )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth(75.percent)
//                .padding(leftRight = 8.px),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            P(
//                attrs = BaseTextStyle.toModifier()
//                    .fontSize(FontSize.Larger)
//                    .fontWeight(FontWeight.ExtraBold)
//                    .toAttrs()
//            ) {
//                Text("\"${testimonial.comments.take(chars.random())}\"")
//            }
//            P(
//                attrs = BaseTextStyle.toModifier()
//                    .fillMaxWidth()
//                    .fontSize(FontSize.Medium)
//                    .fontWeight(FontWeight.Bolder)
//                    .textAlign(TextAlign.End)
//                    .toAttrs()
//            ) {
//                Text("- ${testimonial.from}")
//            }
//        }
//    }
//}