package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.ErrorBoundary
import com.probro.khoded.components.ErrorBoundaryConfig
import com.probro.khoded.components.widgets.Footer
import com.probro.khoded.design.KhodedDesignSystem
import com.probro.khoded.utils.NavigationHeader
import com.probro.khoded.utils.WithNavigation
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.navigation.Link
import org.jetbrains.compose.web.css.*
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.graphics.Colors

/**
 * Services Page - Professional Service Offerings
 */
@Page
@Composable
fun ServicesPage() {
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
                        "Our Services",
                        modifier = Modifier
                            .fontSize(48.px)
                            .fontWeight(700)
                            .color(Color.white)
                            .textAlign(TextAlign.Center)
                            .margin(bottom = 20.px)
                    )
                    
                    SpanText(
                        "Kotlin Multiplatform solutions delivering 80% code reuse and native performance",
                        modifier = Modifier
                            .fontSize(20.px)
                            .color(Color.white)
                            .textAlign(TextAlign.Center)
                            .maxWidth(600.px)
                    )
                }

                // Services Grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .maxWidth(1200.px)
                        .padding(80.px, 40.px),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(60.px)
                ) {
                    // Service 1: Web Development
                    ServiceCard(
                        title = "Code-Base Transformations",
                        description = "Transform your existing mobile and web applications to Kotlin Multiplatform, achieving 80% code reuse and native performance.",
                        features = listOf(
                            "Legacy system modernization",
                            "Cross-platform migration",
                            "Performance optimization",
                            "80% code reuse efficiency"
                        )
                    )

                    // Service 2: E-commerce
                    ServiceCard(
                        title = "Ready-Made Solutions",
                        description = "Pre-built Kotlin Multiplatform dashboard templates and business intelligence tools ready for instant deployment.",
                        features = listOf(
                            "Pre-built dashboard templates",
                            "Business intelligence tools",
                            "Analytics platforms",
                            "Instant deployment"
                        )
                    )

                    // Service 3: Branding
                    ServiceCard(
                        title = "Custom Development",
                        description = "Bespoke Kotlin Multiplatform applications, MVPs, and enterprise solutions with native mobile and scalable web platforms.",
                        features = listOf(
                            "Native mobile applications",
                            "Scalable web platforms",
                            "MVP prototyping",
                            "Enterprise architecture"
                        )
                    )

                    // Service 4: Hosting
                    ServiceCard(
                        title = "Connecticut Industry Focus",
                        description = "Specialized solutions for Connecticut's key industries with compliance-ready and industry-specific expertise.",
                        features = listOf(
                            "Finance industry experience",
                            "Healthcare compliance ready",
                            "Manufacturing solutions",
                            "Personal support guarantee"
                        )
                    )
                }

                // CTA Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .backgroundColor(rgb(15, 23, 42))
                        .padding(80.px, 40.px),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(30.px)
                ) {
                    SpanText(
                        "Transform Your Development Process?",
                        modifier = Modifier
                            .fontSize(32.px)
                            .fontWeight(700)
                            .color(Color.white)
                            .textAlign(TextAlign.Center)
                    )
                    
                    SpanText(
                        "Get 80% code reuse and 40% faster development with Kotlin Multiplatform",
                        modifier = Modifier
                            .fontSize(18.px)
                            .color(Color.white)
                            .textAlign(TextAlign.Center)
                    )
                    
                    Column(
                        modifier = Modifier
                            .padding(16.px, 24.px)
                            .backgroundColor(Color.white)
                            .borderRadius(50.px)
                            .cursor(Cursor.Pointer),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SpanText(
                            "Schedule Free Consultation",
                            modifier = Modifier
                                .fontSize(16.px)
                                .fontWeight(600)
                                .color(rgb(6, 182, 212))
                        )
                    }
                }
                
                Footer()
            }
        }
    }
}

@Composable
private fun ServiceCard(
    title: String,
    description: String,
    features: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(800.px)
            .backgroundColor(Color.white)
            .borderRadius(12.px)
            .padding(40.px)
            .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 16.px, color = rgba(0, 0, 0, 0.15)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.px)
    ) {
        SpanText(
            title,
            modifier = Modifier
                .fontSize(28.px)
                .fontWeight(700)
                .color(rgb(15, 23, 42))
        )
        
        SpanText(
            description,
            modifier = Modifier
                .fontSize(16.px)
                .lineHeight(1.6)
                .color(rgb(100, 116, 139))
        )
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.px)
        ) {
            features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.px)
                ) {
                    SpanText(
                        "✓",
                        modifier = Modifier
                            .fontSize(16.px)
                            .color(rgb(34, 197, 94))
                            .fontWeight(600)
                    )
                    SpanText(
                        feature,
                        modifier = Modifier
                            .fontSize(14.px)
                            .color(rgb(100, 116, 139))
                    )
                }
            }
        }
    }
}