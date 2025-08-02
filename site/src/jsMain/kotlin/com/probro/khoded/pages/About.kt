package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.ErrorBoundary
import com.probro.khoded.components.ErrorBoundaryConfig
import com.probro.khoded.design.KhodedDesignSystem
import com.probro.khoded.components.widgets.Footer
import com.probro.khoded.utils.NavigationHeader
import com.probro.khoded.utils.WithNavigation
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
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient

/**
 * About Page - Company Story and Team
 */
@Page
@Composable
fun AboutPage() {
    ErrorBoundary(config = ErrorBoundaryConfig()) {
        WithNavigation { navigationState ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hero Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .minHeight(60.vh)
                        .backgroundImage(
                            linearGradient(
                                dir = LinearGradient.Direction.ToBottomRight,
                                from = rgb(15, 23, 42),    // Deep navy
                                to = rgb(30, 41, 59)       // Lighter navy
                            )
                        )
                        .padding(80.px, 40.px),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    SpanText(
                        text = "About Khoded",
                        modifier = Modifier
                            .fontSize(48.px)
                            .fontWeight(700)
                            .color(Color.white)
                            .textAlign(TextAlign.Center)
                            .margin(bottom = 20.px)
                    )

                    SpanText(
                        "Connecticut's leading Kotlin Multiplatform specialists, delivering 80% code reuse and native performance",
                        modifier = Modifier
                            .fontSize(20.px)
                            .color(Color.white)
                            .textAlign(TextAlign.Center)
                            .maxWidth(600.px)
                    )
                }

                // Story Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .maxWidth(1200.px)
                        .padding(80.px, 40.px),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(40.px)
                ) {
                    SpanText(
                        "Our Story",
                        modifier = Modifier
                            .fontSize(36.px)
                            .fontWeight(700)
                            .color(rgb(15, 23, 42))
                            .textAlign(TextAlign.Center)
                    )

                    SpanText(
                        "Founded as Connecticut's early specialists in Kotlin Multiplatform technology, Khoded bridges the gap between innovative cross-platform development and business results. We help forward-thinking companies in Finance, Healthcare, and Manufacturing achieve 80% code reuse, 40% faster development, and 100% native performance through our proven expertise.",
                        modifier = Modifier
                            .fontSize(18.px)
                            .lineHeight(1.6)
                            .color(rgb(100, 116, 139))
                            .textAlign(TextAlign.Center)
                            .maxWidth(800.px)
                    )
                }

                // Team Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .backgroundColor(rgb(248, 250, 252))
                        .padding(80.px, 40.px),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(60.px)
                ) {
                    SpanText(
                        "Meet Our Team",
                        modifier = Modifier
                            .fontSize(36.px)
                            .fontWeight(700)
                            .color(rgb(15, 23, 42))
                            .textAlign(TextAlign.Center)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .maxWidth(800.px),
                        horizontalArrangement = Arrangement.spacedBy(40.px),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Team member cards would go here
                        TeamMemberCard(
                            name = "Esther Founder",
                            role = "Co-Founder & UX Strategy",
                            description = "Expert in Kotlin Multiplatform UX patterns and Connecticut market insights, ensuring our solutions meet local business needs."
                        )

                        TeamMemberCard(
                            name = "Josh Founder",
                            role = "Co-Founder & KMP Architect",
                            description = "Kotlin Multiplatform specialist with deep expertise in shared codebase architecture and enterprise-grade solutions."
                        )
                    }
                }

                // Values Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .maxWidth(1200.px)
                        .padding(80.px, 40.px),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(60.px)
                ) {
                    SpanText(
                        "Our Values",
                        modifier = Modifier
                            .fontSize(36.px)
                            .fontWeight(700)
                            .color(rgb(15, 23, 42))
                            .textAlign(TextAlign.Center)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(40.px),
                        verticalAlignment = Alignment.Top
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

                Footer()
            }
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
            .backgroundColor(Color.white)
            .borderRadius(12.px)
            .padding(30.px)
            .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 16.px, color = rgba(0, 0, 0, 0.15)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.px)
    ) {
        // Profile image placeholder
        Column(
            modifier = Modifier
                .size(120.px)
                .backgroundColor(rgb(229, 231, 235))
                .borderRadius(50.percent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SpanText(
                "Photo",
                modifier = Modifier
                    .fontSize(14.px)
                    .color(rgb(107, 114, 128))
            )
        }

        SpanText(
            name,
            modifier = Modifier
                .fontSize(24.px)
                .fontWeight(700)
                .color(rgb(15, 23, 42))
                .textAlign(TextAlign.Center)
        )

        SpanText(
            role,
            modifier = Modifier
                .fontSize(16.px)
                .fontWeight(500)
                .color(rgb(6, 182, 212))
                .textAlign(TextAlign.Center)
        )

        SpanText(
            description,
            modifier = Modifier
                .fontSize(14.px)
                .lineHeight(1.5)
                .color(rgb(100, 116, 139))
                .textAlign(TextAlign.Center)
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
            .backgroundColor(Color.white)
            .borderRadius(12.px)
            .padding(30.px)
            .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 16.px, color = rgba(0, 0, 0, 0.15)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.px)
    ) {
        SpanText(
            title,
            modifier = Modifier
                .fontSize(24.px)
                .fontWeight(700)
                .color(rgb(15, 23, 42))
                .textAlign(TextAlign.Center)
        )

        SpanText(
            description,
            modifier = Modifier
                .fontSize(16.px)
                .lineHeight(1.5)
                .color(rgb(100, 116, 139))
                .textAlign(TextAlign.Center)
        )
    }
}