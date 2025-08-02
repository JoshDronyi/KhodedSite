package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.design.KhodedDesignSystem
import com.probro.khoded.utils.*
import com.varabyte.kobweb.compose.css.*
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
    Section(
        attrs = Modifier
            .fillMaxWidth()
            .backgroundColor(rgb(15, 23, 42)) // Dark navy background
            .padding(topBottom = 120.px, leftRight = 24.px)
            .toAttrs {
                id("about")
                attr("aria-label", "About Khoded")
            }
    ) {
        Container(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(1200.px),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Section header
            AboutHeader()
            
            Div(attrs = { style { height(80.px) } })
            
            // Key advantages grid
            AdvantagesGrid()
            
            Div(attrs = { style { height(80.px) } })
            
            // Stats and credibility section
            StatsSection()
        }
    }
}

@Composable
private fun Container(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        content()
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
            // Mini logo
            org.jetbrains.compose.web.dom.Div(
                attrs = {
                    style {
                        width(20.px)
                        height(16.px)
                        backgroundColor(rgb(6, 182, 212))
                        borderRadius(4.px)
                        display(DisplayStyle.Flex)
                        property("align-items", "center")
                        property("justify-content", "center")
                        fontSize(10.px)
                        fontWeight(700)
                        color(Color.white)
                        fontFamily("'Fira Code', 'Consolas', 'Monaco', monospace")
                    }
                }
            ) {
                org.jetbrains.compose.web.dom.Text("</&gt;")
            }
            
            SpanText(
                text = "Our Competitive Advantage",
                modifier = Modifier
                    .fontSize(14.px)
                    .fontWeight(600)
                    .color(rgb(6, 182, 212))
            )
        }
        
        // Main heading
        H2(
            attrs = Modifier
                .fontSize(52.px)
                .fontWeight(800)
                .color(Color.white)
                .textAlign(TextAlign.Center)
                .lineHeight(1.2)
                .letterSpacing((-0.5).px)
                .maxWidth(800.px)
                .toAttrs()
        ) {
            Text("Why Connecticut businesses choose Khoded")
        }
        
        // Description
        P(
            attrs = Modifier
                .fontSize(20.px)
                .fontWeight(400)
                .color(rgba(255, 255, 255, 0.8))
                .textAlign(TextAlign.Center)
                .lineHeight(1.6)
                .maxWidth(700.px)
                .toAttrs()
        ) {
            Text("We're early specialists in Kotlin Multiplatform — the technology that's revolutionizing cross-platform development with 80% code reuse and native performance.")
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
        // Technical Differentiator
        AdvantageCard(
            icon = "⚡",
            title = "Kotlin Shared Codebase",
            description = "Our lean team delivers the same quality as larger agencies, but with 40% reduced development time and cost advantages through efficient code sharing.",
            highlights = listOf(
                "80% code reuse across platforms",
                "Native performance guaranteed", 
                "40% faster development",
                "Cost-effective lean team"
            ),
            accentColor = rgb(6, 182, 212)
        )
        
        // Market Position
        AdvantageCard(
            icon = "🎯",
            title = "Early Market Specialization",
            description = "While others are still learning, we're already experts. With 2,500+ available libraries and backing from JetBrains & Google, we're ahead of the curve.",
            highlights = listOf(
                "Early KMP specialization",
                "2,500+ libraries available",
                "JetBrains & Google backing",
                "Adopted by Netflix, McDonald's"
            ),
            accentColor = rgb(139, 92, 246)
        )
        
        // Local Advantage
        AdvantageCard(
            icon = "🏆",
            title = "Connecticut-Focused Expertise",
            description = "We understand the unique needs of Connecticut's key industries — Finance, Healthcare, and Manufacturing — with personalized support via text & email.",
            highlights = listOf(
                "Finance industry experience",
                "Healthcare compliance ready",
                "Manufacturing solutions",
                "Personal support guarantee"
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
            .backgroundColor(rgba(255, 255, 255, 0.05))
            .borderRadius(20.px)
            .padding(32.px)
            .toAttrs {
                style {
                    border(1.px, LineStyle.Solid, rgba(255, 255, 255, 0.1))
                    property("backdrop-filter", "blur(10px)")
                    property("transition", "all 0.3s ease")
                }
                // TODO: Add hover effects when API is available
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
        // Stats title
        H3(
            attrs = Modifier
                .fontSize(32.px)
                .fontWeight(700)
                .color(Color.white)
                .textAlign(TextAlign.Center)
                .toAttrs()
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
        
        // Trust indicators
        Div(
            attrs = Modifier
                .backgroundColor(rgba(6, 182, 212, 0.1))
                .borderRadius(16.px)
                .padding(32.px)
                .maxWidth(800.px)
                .toAttrs()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.px)
            ) {
                P(
                    attrs = Modifier
                        .fontSize(18.px)
                        .fontWeight(600)
                        .color(rgb(6, 182, 212))
                        .textAlign(TextAlign.Center)
                        .toAttrs()
                ) {
                    Text("Trusted Technology")
                }
                
                P(
                    attrs = Modifier
                        .fontSize(16.px)
                        .color(rgba(255, 255, 255, 0.9))
                        .textAlign(TextAlign.Center)
                        .lineHeight(1.6)
                        .toAttrs()
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
                .color(rgb(6, 182, 212))
        )
        
        SpanText(
            text = label,
            modifier = Modifier
                .fontSize(18.px)
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