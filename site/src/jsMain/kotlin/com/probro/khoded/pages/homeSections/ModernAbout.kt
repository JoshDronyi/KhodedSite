package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.design.KhodedDesignSystem
import com.probro.khoded.utils.*
import com.probro.khoded.components.layout.KhodedSection
import com.probro.khoded.components.layout.VerticalSpacer
import com.probro.khoded.components.branding.KhodedLogo
import com.probro.khoded.components.branding.LogoSize
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.clamp
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

/**
 * Modern About/Why Choose Us Section
 * Highlights Khoded's competitive advantages and key differentiators
 * Based on the brand materials showing technical excellence and business results
 */
@Composable
fun ModernAboutSection() {
    KhodedSection(
        id = "about",
        ariaLabel = "About Khoded", 
        backgroundColor = KhodedDesignSystem.colors.backgroundDarkSecondary
    ) {
        // Section header
        AboutHeader()
        
        VerticalSpacer(80.px)
        
        // Key advantages grid
        AdvantagesGrid()
        
        VerticalSpacer(80.px)
        
        // Stats and credibility section
        StatsSection()
    }
}


@Composable
private fun AboutHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.px)
    ) {
        // Section badge with logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.px),
            modifier = Modifier
                .backgroundColor(rgba(6, 182, 212, 0.15))
                .padding(10.px, 24.px)
                .borderRadius(50.px)
        ) {
            // Mini logo - Using unified component
            KhodedLogo(size = LogoSize.Medium)
            
            SpanText(
                text = "Our Competitive Advantage",
                modifier = Modifier
                    .fontSize(clamp(12.px, 2.5.vw, 16.px))  // Responsive typography using Kobweb clamp
                    .fontWeight(600)
                    .color(KhodedDesignSystem.colors.primary)  // Design system teal
            )
        }
        
        // Main heading - Using responsive design system typography
        H2(
            attrs = Modifier
                .fontSize(clamp(28.px, 5.vw, 48.px))  // Responsive section heading using Kobweb clamp
                .fontWeight(800)
                .color(Color.white)
                .textAlign(TextAlign.Center)
                .lineHeight(1.2)
                .letterSpacing((-0.5).px)
                .maxWidth(800.px)
                .toAttrs {
                    style {
                        property("text-shadow", "0 2px 10px rgba(0, 0, 0, 0.3)")  // Landing.kt text shadow
                    }
                }
        ) {
            Text("Why Smart Businesses Choose Kotlin Multiplatform")
        }
        
        // Description - Using responsive typography
        P(
            attrs = Modifier
                .fontSize(clamp(16.px, 3.vw, 22.px))  // Responsive body text using Kobweb clamp
                .fontWeight(400)
                .color(rgba(255, 255, 255, 0.95))  // Enhanced readability
                .textAlign(TextAlign.Center)
                .lineHeight(1.6)
                .maxWidth(800.px)
                .toAttrs {
                    style {
                        property("text-shadow", "0 1px 8px rgba(0, 0, 0, 0.4)")  // Landing.kt text shadow
                    }
                }
        ) {
            Text("Fortune 500 companies like Netflix and McDonald's trust Kotlin Multiplatform. Smart small businesses partner with us to access the same technology advantages at a fraction of the cost.")
        }
    }
}

@Composable
private fun AdvantagesGrid() {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .toAttrs {
                style {
                    display(DisplayStyle.Grid)
                    property("grid-template-columns", "repeat(auto-fit, minmax(300px, 1fr))")
                    property("gap", "32px")
                    property("align-items", "stretch")
                }
            }
    ) {
        // Cost Advantage
        AdvantageCard(
            icon = "💰",
            title = "60% Lower Development Costs",
            description = "Why hire separate iOS and Android teams? Our Kotlin Multiplatform approach lets you build once and deploy everywhere, dramatically reducing both development and maintenance costs.",
            highlights = listOf(
                "Single development team vs. multiple",
                "80% shared code = 60% cost savings",
                "One codebase to maintain",
                "Faster feature rollouts"
            ),
            accentColor = rgb(6, 182, 212)
        )
        
        // Market Position
        AdvantageCard(
            icon = "🎯",
            title = "3-6 Months Faster Launch",
            description = "Beat your competition to market. While they're building separate iOS and Android apps, you'll already be serving customers on both platforms with a single development cycle.",
            highlights = listOf(
                "Simultaneous platform launches",
                "50% faster development cycles", 
                "Quick market validation",
                "Rapid feature iterations"
            ),
            accentColor = rgb(139, 92, 246)
        )
        
        // Global Reach
        AdvantageCard(
            icon = "🛡️",
            title = "Risk-Free Technology Choice", 
            description = "Choose the same technology trusted by Netflix, McDonald's, and Cash App. Kotlin Multiplatform is backed by Google & JetBrains with enterprise-grade support and a growing ecosystem.",
            highlights = listOf(
                "Fortune 500 company adoption",
                "Google & JetBrains backing",
                "Enterprise-grade stability",
                "Growing developer community"
            ),
            accentColor = rgb(34, 197, 94)
        )
    }
}

@Composable
private fun AdvantageCard(
    icon: String,
    title: String,
    description: String,
    highlights: List<String>,
    accentColor: CSSColorValue
) {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .backgroundColor(KhodedDesignSystem.card.backgroundLight)  // Design system card background
            .borderRadius(KhodedDesignSystem.card.borderRadiusLarge)   // Design system border radius
            .padding(KhodedDesignSystem.card.padding)                  // Design system padding
            .toAttrs {
                style {
                    border(KhodedDesignSystem.card.borderWidth, LineStyle.Solid, KhodedDesignSystem.card.border)
                    property("backdrop-filter", KhodedDesignSystem.card.backdropFilter)  // Design system backdrop filter
                    property("transition", "all 0.3s ease")
                    property("text-shadow", "0 1px 6px rgba(0, 0, 0, 0.3)")  // Landing.kt text shadow for readability
                }
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.px)
        ) {
            // Icon and title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.px)
            ) {
                Div(
                    attrs = Modifier
                        .size(56.px)
                        .backgroundColor(accentColor)
                        .borderRadius(14.px)
                        .toAttrs {
                            style {
                                display(DisplayStyle.Flex)
                                property("align-items", "center")
                                property("justify-content", "center")
                                fontSize(28.px)
                            }
                        }
                ) {
                    Text(icon)
                }
                
                H3(
                    attrs = Modifier
                        .fontSize(22.px)
                        .fontWeight(700)
                        .color(Color.white)
                        .lineHeight(1.3)
                        .flexGrow(1)
                        .toAttrs()
                ) {
                    Text(title)
                }
            }
            
            // Description
            P(
                attrs = Modifier
                    .fontSize(16.px)
                    .fontWeight(400)
                    .color(rgba(255, 255, 255, 0.8))
                    .lineHeight(1.6)
                    .toAttrs()
            ) {
                Text(description)
            }
            
            // Highlights
            Ul(
                attrs = Modifier
                    .toAttrs {
                        style {
                            listStyleType("none")
                            padding(0.px)
                            margin(0.px)
                        }
                    }
            ) {
                highlights.forEach { highlight ->
                    Li(
                        attrs = Modifier
                            .padding(topBottom = 8.px)
                            .toAttrs {
                                style {
                                    display(DisplayStyle.Flex)
                                    property("align-items", "center")
                                    property("gap", "12px")
                                }
                            }
                    ) {
                        // Checkmark
                        Div(
                            attrs = Modifier
                                .size(8.px)
                                .backgroundColor(accentColor)
                                .borderRadius(50.percent)
                                .toAttrs()
                        )
                        
                        SpanText(
                            text = highlight,
                            modifier = Modifier
                                .fontSize(14.px)
                                .fontWeight(500)
                                .color(rgba(255, 255, 255, 0.9))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(48.px)
    ) {
        // Stats title - Using responsive typography
        H3(
            attrs = Modifier
                .fontSize(clamp(24.px, 4.vw, 36.px))  // Responsive section heading using Kobweb clamp
                .fontWeight(700)
                .color(Color.white)
                .textAlign(TextAlign.Center)
                .toAttrs {
                    style {
                        property("text-shadow", "0 2px 10px rgba(0, 0, 0, 0.3)")  // Landing.kt text shadow
                    }
                }
        ) {
            Text("Driving Success Through Innovation")
        }
        
        // Key metrics
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .toAttrs {
                    style {
                        display(DisplayStyle.Grid)
                        property("grid-template-columns", "repeat(auto-fit, minmax(200px, 1fr))")
                        property("gap", "48px")
                        property("text-align", "center")
                    }
                }
        ) {
            StatMetric("80%", "Code Reuse", "Shared business logic across platforms")
            StatMetric("40%", "Time Savings", "Faster development with KMP approach")
            StatMetric("100%", "Native Performance", "Full access to platform features")
            StatMetric("2,500+", "Libraries", "Rich ecosystem for rapid development")
        }
        
        // Trust indicators - Using design system
        Div(
            attrs = Modifier
                .backgroundColor(KhodedDesignSystem.colors.primaryLight)  // Design system primary light
                .borderRadius(KhodedDesignSystem.card.borderRadius)       // Design system border radius
                .padding(KhodedDesignSystem.card.padding)                 // Design system padding
                .maxWidth(800.px)
                .toAttrs()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.px)
            ) {
                P(
                    attrs = Modifier
                        .fontSize(clamp(14.px, 3.vw, 18.px))  // Responsive typography using Kobweb clamp
                        .fontWeight(600)
                        .color(KhodedDesignSystem.colors.primary)  // Design system teal
                        .textAlign(TextAlign.Center)
                        .toAttrs()
                ) {
                    Text("Trusted Technology")
                }
                
                P(
                    attrs = Modifier
                        .fontSize(clamp(14.px, 3.vw, 18.px))  // Responsive typography using Kobweb clamp
                        .color(rgba(255, 255, 255, 0.95))  // Enhanced readability
                        .textAlign(TextAlign.Center)
                        .lineHeight(1.6)
                        .toAttrs {
                            style {
                                property("text-shadow", "0 1px 4px rgba(0, 0, 0, 0.3)")  // Landing.kt text shadow
                            }
                        }
                ) {
                    Text("Kotlin Multiplatform is backed by JetBrains and Google, with adoption by industry leaders like Netflix, McDonald's, and Cash App. Join the 23.8% of developers who've already made the switch.")
                }
            }
        }
    }
}

@Composable
private fun StatMetric(value: String, label: String, description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.px)
    ) {
        SpanText(
            text = value,
            modifier = Modifier
                .fontSize(48.px)
                .fontWeight(800)
                .color(KhodedDesignSystem.colors.primary)  // Design system teal
        )
        
        SpanText(
            text = label,
            modifier = Modifier
                .fontSize(clamp(14.px, 3.vw, 18.px))  // Responsive typography using Kobweb clamp
                .fontWeight(600)
                .color(Color.white)
        )
        
        P(
            attrs = Modifier
                .fontSize(14.px)
                .color(rgba(255, 255, 255, 0.7))
                .textAlign(TextAlign.Center)
                .lineHeight(1.4)
                .toAttrs()
        ) {
            Text(description)
        }
    }
}