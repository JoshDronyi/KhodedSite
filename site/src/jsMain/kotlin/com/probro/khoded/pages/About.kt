package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.ErrorBoundary
import com.probro.khoded.components.ErrorBoundaryConfig
import com.probro.khoded.design.KhodedDesignSystem
import com.probro.khoded.utils.WithNavigation
import com.varabyte.kobweb.compose.css.ColorInterpolationMethod
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.css.functions.clamp
import com.probro.khoded.models.Founders

/**
 * About Page - Company Story and Team
 */
@Page
@Composable
fun AboutPage() {
    ErrorBoundary(config = ErrorBoundaryConfig()) {
        WithNavigation { navigationState ->
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Hero Section - Mobile-first responsive
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .minHeight(50.vh)  // Reduced for mobile
                        .backgroundImage(
                            linearGradient(
                                dir = LinearGradient.Direction.ToBottomRight,
                                from = rgb(15, 23, 42),    // Deep navy
                                to = rgb(30, 41, 59),       // Lighter navy,
                                interpolation = ColorInterpolationMethod.ProphotoRgb
                            )
                        )
                        // Mobile-first responsive padding
                        .padding(
                            topBottom = clamp(40.px, 10.vw, 80.px),
                            leftRight = clamp(20.px, 5.vw, 40.px)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    H1(
                        attrs = {
                            style {
                                property(
                                    "font-size",
                                    KhodedDesignSystem.typography.sectionLarge
                                )  // Responsive clamp string
                                fontWeight(700)
                                color(Color.white)
                                textAlign("center")
                                marginBottom(20.px)
                                property("text-shadow", "0 2px 10px rgba(0, 0, 0, 0.3)")  // Enhanced readability
                            }
                        }
                    ) {
                        Text("About Khoded")
                    }

                    SpanText(
                        "Connecticut's leading Kotlin Multiplatform specialists, delivering 80% code reuse and native performance",
                        modifier = Modifier
                            .fontSize(clamp(16.px, 3.vw, 20.px))  // Responsive using Kobweb clamp
                            .color(Color.white)
                            .textAlign(TextAlign.Center)
                            .maxWidth(600.px)
                    )
                }

                // Story Section - Mobile responsive
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .backgroundColor(KhodedDesignSystem.colors.backgroundDarkSecondary)
                        .padding(
                            topBottom = clamp(40.px, 8.vw, 80.px),
                            leftRight = clamp(20.px, 5.vw, 40.px)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(clamp(30.px, 5.vw, 40.px))
                ) {
                    SpanText(
                        "Our Story",
                        modifier = Modifier
                            .fontSize(clamp(28.px, 6.vw, 36.px))  // Responsive heading
                            .fontWeight(700)
                            .color(Color.white)
                            .textAlign(TextAlign.Center)
                    )

                    SpanText(
                        "Founded as Connecticut's early specialists in Kotlin Multiplatform technology, Khoded bridges the gap between innovative cross-platform development and business results. We help forward-thinking companies in Finance, Healthcare, and Manufacturing achieve 80% code reuse, 40% faster development, and 100% native performance through our proven expertise.",
                        modifier = Modifier
                            .fontSize(clamp(16.px, 4.vw, 18.px))  // Responsive body text
                            .lineHeight(1.6)
                            .color(rgba(255, 255, 255, 0.8))
                            .textAlign(TextAlign.Center)
                            .maxWidth(800.px)
                            .padding(leftRight = 16.px)  // Mobile padding
                    )
                }

                // Team Section - Mobile responsive
                ResponsiveTeamSection()

                // Values Section - Mobile responsive
                ResponsiveValuesSection()

            }
        }
    }
}

/**
 * Responsive Team Section - Mobile-first design
 * Stacks team members vertically on mobile, side-by-side on desktop
 */
@Composable
private fun ResponsiveTeamSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(KhodedDesignSystem.colors.backgroundDark)
            .padding(
                topBottom = clamp(40.px, 8.vw, 80.px),
                leftRight = clamp(20.px, 5.vw, 40.px)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(clamp(40.px, 6.vw, 60.px))
    ) {
        SpanText(
            "Meet Our Team",
            modifier = Modifier
                .fontSize(clamp(28.px, 6.vw, 36.px))
                .fontWeight(700)
                .color(Color.white)
                .textAlign(TextAlign.Center)
        )

        // Team members in responsive grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(900.px),
            verticalArrangement = Arrangement.spacedBy(clamp(30.px, 5.vw, 40.px)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TeamMemberCard(
                name = Founders.ESTHER.name,
                role = Founders.ESTHER.title,
                description = Founders.ESTHER.bio
            )

            TeamMemberCard(
                name = Founders.JOSHUA.name,
                role = Founders.JOSHUA.title,
                description = Founders.JOSHUA.bio
            )
        }
    }
}

/**
 * Responsive Values Section - Mobile-first design  
 * Stacks values vertically on mobile, grid on desktop
 */
@Composable
private fun ResponsiveValuesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(KhodedDesignSystem.colors.backgroundDarkSecondary)
            .padding(
                topBottom = clamp(60.px, 8.vw, 80.px),
                leftRight = clamp(32.px, 5.vw, 40.px)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(clamp(50.px, 6.vw, 60.px))
    ) {
        SpanText(
            "Our Values",
            modifier = Modifier
                .fontSize(clamp(28.px, 6.vw, 36.px))
                .fontWeight(700)
                .color(Color.white)
                .textAlign(TextAlign.Center)
        )

        // Values in responsive grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(1000.px),
            verticalArrangement = Arrangement.spacedBy(clamp(32.px, 4.vw, 30.px)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ValueCard(
                title = "Early Expertise",
                description = "Connecticut's first Kotlin Multiplatform specialists, ahead of the curve with proven results."
            )

            ValueCard(
                title = "Proven Results", 
                description = "80% code reuse, 40% faster development, 100% native performance - backed by JetBrains & Google."
            )

            ValueCard(
                title = "Local Focus",
                description = "Deep understanding of Connecticut's Finance, Healthcare, and Manufacturing industry needs."
            )
        }
    }
}

@Composable
private fun TeamMemberCard(
    name: String,
    role: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(500.px)  // Prevent cards from getting too wide
            .backgroundColor(rgba(255, 255, 255, 0.08))
            .borderRadius(12.px)
            .padding(clamp(24.px, 4.vw, 30.px))  // Responsive padding
            .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 16.px, color = rgba(0, 0, 0, 0.3))
            .minHeight(KhodedDesignSystem.touchTargets.minimum),  // Minimum touch target
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(clamp(12.px, 3.vw, 15.px))
    ) {
        // Profile image placeholder - responsive size
        Column(
            modifier = Modifier
                .size(clamp(100.px, 20.vw, 120.px))  // Responsive image size
                .backgroundColor(rgba(255, 255, 255, 0.15))
                .borderRadius(50.percent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SpanText(
                "Photo",
                modifier = Modifier
                    .fontSize(clamp(12.px, 2.5.vw, 14.px))
                    .color(rgba(255, 255, 255, 0.6))
            )
        }

        SpanText(
            name,
            modifier = Modifier
                .fontSize(clamp(20.px, 4.vw, 24.px))  // Responsive name
                .fontWeight(700)
                .color(Color.white)
                .textAlign(TextAlign.Center)
        )

        SpanText(
            role,
            modifier = Modifier
                .fontSize(clamp(14.px, 3.vw, 16.px))  // Responsive role
                .fontWeight(500)
                .color(rgb(6, 182, 212))
                .textAlign(TextAlign.Center)
        )

        SpanText(
            description,
            modifier = Modifier
                .fontSize(clamp(16.px, 2.5.vw, 14.px))  // Responsive description - WCAG AA compliant minimum
                .lineHeight(1.5)
                .color(rgba(255, 255, 255, 0.7))
                .textAlign(TextAlign.Center)
                .padding(leftRight = 8.px)  // Mobile padding
        )
    }
}

@Composable
private fun ValueCard(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(600.px)  // Prevent cards from getting too wide
            .backgroundColor(Color.white)
            .borderRadius(12.px)
            .padding(clamp(32.px, 4.vw, 30.px))  // Responsive padding - Increased mobile minimum
            .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 16.px, color = rgba(0, 0, 0, 0.15))
            .minHeight(48.px),  // WCAG AAA compliant touch target
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(clamp(16.px, 3.vw, 15.px))
    ) {
        SpanText(
            title,
            modifier = Modifier
                .fontSize(clamp(20.px, 4.vw, 24.px))  // Responsive title
                .fontWeight(700)
                .color(rgb(15, 23, 42))
                .textAlign(TextAlign.Center)
        )

        SpanText(
            description,
            modifier = Modifier
                .fontSize(clamp(14.px, 3.vw, 16.px))  // Responsive description
                .lineHeight(1.5)
                .color(rgb(100, 116, 139))
                .textAlign(TextAlign.Center)
                .padding(leftRight = 8.px)  // Mobile padding
        )
    }
}