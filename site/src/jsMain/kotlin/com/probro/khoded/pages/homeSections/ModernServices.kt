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
 * Modern Services Section
 * Showcases the three main Khoded offerings:
 * 1. Code-Base Transformations
 * 2. Ready-Made Solutions  
 * 3. Custom Development
 */
@Composable
fun ModernServicesSection() {
    Section(
        attrs = Modifier
            .fillMaxWidth()
            .backgroundColor(KhodedDesignSystem.colors.backgroundDarkSecondary) // Consistent dark theme
            .padding(topBottom = 120.px, leftRight = 24.px)
            .toAttrs {
                id("services")
                attr("aria-label", "Our Services")
            }
    ) {
        Container(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(1200.px),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Section header
            ServicesHeader()
            
            Div(attrs = { style { height(80.px) } })
            
            // Services grid
            ServicesGrid()
            
            Div(attrs = { style { height(60.px) } })
            
            // Bottom CTA
            ServicesCTA()
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
private fun ServicesHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.px)
    ) {
        // Section badge with logo - Using design system
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.px),
            modifier = Modifier
                .backgroundColor(KhodedDesignSystem.colors.primaryLight)  // Design system primary light
                .padding(8.px, 20.px)
                .borderRadius(50.px)
        ) {
            // Mini logo - Using design system
            org.jetbrains.compose.web.dom.Div(
                attrs = {
                    style {
                        width(20.px)
                        height(16.px)
                        backgroundColor(KhodedDesignSystem.colors.primary)  // Design system teal
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
                org.jetbrains.compose.web.dom.Text("</>")
            }
            
            SpanText(
                text = "What We Offer",
                modifier = Modifier
                    .fontSize(14.px)
                    .fontWeight(600)
                    .color(rgb(6, 182, 212))
            )
        }
        
        // Main heading
        H2(
            attrs = Modifier
                .fontSize(48.px)
                .fontWeight(800)
                .color(rgb(15, 23, 42)) // Dark navy
                .textAlign(TextAlign.Center)
                .lineHeight(1.2)
                .letterSpacing((-0.5).px)
                .maxWidth(700.px)
                .toAttrs()
        ) {
            Text("Discover our main three offerings")
        }
        
        // Description
        P(
            attrs = Modifier
                .fontSize(18.px)
                .fontWeight(400)
                .color(rgb(100, 116, 139)) // Gray
                .textAlign(TextAlign.Center)
                .lineHeight(1.6)
                .maxWidth(600.px)
                .toAttrs()
        ) {
            Text("Comprehensive solutions designed to transform your development process and accelerate your business growth.")
        }
    }
}

@Composable
private fun ServicesGrid() {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .toAttrs {
                style {
                    display(DisplayStyle.Grid)
                    property("grid-template-columns", "repeat(auto-fit, minmax(350px, 1fr))")
                    property("gap", "32px")
                    property("align-items", "stretch")
                }
            }
    ) {
        // Service 1: Code-Base Transformations
        ServiceCard(
            number = "1",
            title = "Code-Base Transformations",
            description = "We create customized development strategies that meet your project's unique requirements, ensuring successful outcomes.",
            subtitle = "(transfer existing code to multiplatform)",
            color = rgb(6, 182, 212), // Teal
            features = listOf(
                "Legacy system modernization",
                "Cross-platform migration", 
                "Performance optimization",
                "80% code reuse efficiency"
            )
        )
        
        // Service 2: Ready-Made Solutions
        ServiceCard(
            number = "2", 
            title = "Ready-Made Solutions",
            description = "Our team offers ongoing support throughout every phase of development, ensuring your project stays on track and on time.",
            subtitle = "(plug + play dashboards/ tools)",
            color = rgb(139, 92, 246), // Purple
            features = listOf(
                "Pre-built dashboard templates",
                "Business intelligence tools",
                "Analytics platforms", 
                "Instant deployment"
            )
        )
        
        // Service 3: Custom Development
        ServiceCard(
            number = "3",
            title = "Custom Development", 
            description = "Leverage cutting-edge technologies and innovative approaches that drive your development projects forward and deliver exceptional results.",
            subtitle = "(tools + apps + MVPs)",
            color = rgb(34, 197, 94), // Green
            features = listOf(
                "Native mobile applications",
                "Scalable web platforms",
                "MVP prototyping",
                "Enterprise architecture"
            )
        )
    }
}

@Composable
private fun ServiceCard(
    number: String,
    title: String,
    description: String,
    subtitle: String,
    color: CSSColorValue,
    features: List<String>
) {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .backgroundColor(Color.white)
            .borderRadius(20.px)
            .padding(32.px)
            .boxShadow(
                offsetX = 0.px, 
                offsetY = 8.px, 
                blurRadius = 25.px, 
                color = rgba(0, 0, 0, 0.08)
            )
            .toAttrs {
                style {
                    border(1.px, LineStyle.Solid, rgba(0, 0, 0, 0.05))
                    property("transition", "all 0.3s ease")
                }
                // TODO: Add hover effects when API is available
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.px)
        ) {
            // Header with number and title
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.px)
            ) {
                // Number badge
                Div(
                    attrs = Modifier
                        .size(48.px)
                        .backgroundColor(color)
                        .borderRadius(12.px)
                        .toAttrs {
                            style {
                                display(DisplayStyle.Flex)
                                property("align-items", "center")
                                property("justify-content", "center")
                                fontSize(20.px)
                                fontWeight(700)
                                color(Color.white)
                            }
                        }
                ) {
                    Text(number)
                }
                
                // Title section
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.px)
                ) {
                    H3(
                        attrs = Modifier
                            .fontSize(24.px)
                            .fontWeight(700)
                            .color(rgb(15, 23, 42))
                            .lineHeight(1.3)
                            .toAttrs()
                    ) {
                        Text(title)
                    }
                    
                    P(
                        attrs = Modifier
                            .fontSize(14.px)
                            .fontWeight(500)
                            .color(color)
                            .fontStyle(FontStyle.Italic)
                            .toAttrs()
                    ) {
                        Text(subtitle)
                    }
                }
            }
            
            // Description
            P(
                attrs = Modifier
                    .fontSize(16.px)
                    .fontWeight(400)
                    .color(rgb(100, 116, 139))
                    .lineHeight(1.6)
                    .toAttrs()
            ) {
                Text(description)
            }
            
            // Features list
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
                features.forEach { feature ->
                    Li(
                        attrs = Modifier
                            .padding(topBottom = 6.px)
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
                                .size(6.px)
                                .backgroundColor(color)
                                .borderRadius(50.percent)
                                .toAttrs()
                        )
                        
                        SpanText(
                            text = feature,
                            modifier = Modifier
                                .fontSize(14.px)
                                .fontWeight(500)
                                .color(rgb(71, 85, 105))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServicesCTA() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.px)
    ) {
        H3(
            attrs = Modifier
                .fontSize(28.px)
                .fontWeight(700)
                .color(rgb(15, 23, 42))
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text("Ready to transform your development process?")
        }
        
        P(
            attrs = Modifier
                .fontSize(16.px)
                .color(rgb(100, 116, 139))
                .textAlign(TextAlign.Center)
                .maxWidth(500.px)
                .toAttrs()
        ) {
            Text("Let's discuss how our proven expertise can accelerate your next project.")
        }
        
        org.jetbrains.compose.web.dom.Button(
            attrs = {
                style {
                    backgroundColor(rgb(6, 182, 212))
                    color(Color.white)
                    border(0.px)
                    borderRadius(12.px)
                    padding(16.px, 32.px)
                    fontSize(16.px)
                    fontWeight(600)
                    cursor(Cursor.Pointer)
                    minHeight(52.px)
                    property("transition", "all 0.3s ease")
                }
                // TODO: Add hover effects when API is available
                attr("aria-label", "Get free consultation")
            }
        ) {
            Text("Get Free 30-Min Consultation")
        }
    }
}