package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.design.KhodedDesignSystem
import com.probro.khoded.utils.*
import com.probro.khoded.components.layout.KhodedSection
import com.probro.khoded.components.layout.VerticalSpacer
import com.probro.khoded.components.branding.KhodedLogo
import com.probro.khoded.components.branding.LogoSize
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
    KhodedSection(
        id = "services",
        ariaLabel = "Our Services",
        backgroundColor = KhodedDesignSystem.colors.backgroundDark
    ) {
        // Section header
        ServicesHeader()
        
        VerticalSpacer(80.px)
        
        // Services grid
        ServicesGrid()
        
        VerticalSpacer(60.px)
        
        // Bottom CTA
        ServicesCTA()
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
            // Mini logo - Using unified component
            KhodedLogo(size = LogoSize.Medium)
            
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
                .color(Color.white) // Updated for dark theme consistency
                .textAlign(TextAlign.Center)
                .lineHeight(1.2)
                .letterSpacing((-0.5).px)
                .maxWidth(700.px)
                .toAttrs()
        ) {
            Text("Three Ways We Save Your Business Time & Money")
        }
        
        // Description
        P(
            attrs = Modifier
                .fontSize(18.px)
                .fontWeight(400)
                .color(rgba(255, 255, 255, 0.8)) // Softer white for dark theme
                .textAlign(TextAlign.Center)
                .lineHeight(1.6)
                .maxWidth(700.px)
                .toAttrs()
        ) {
            Text("Choose the approach that fits your business needs: modernize existing apps, launch quickly with proven solutions, or build completely custom experiences that give you competitive advantage.")
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
            title = "App Modernization & Migration",
            description = "Transform your existing iOS and Android apps into a single multiplatform solution. Cut maintenance costs by 60% while expanding to new platforms faster than ever.",
            subtitle = "Save $50K-$200K annually on development costs",
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
            title = "Rapid Business Solutions",
            description = "Launch your mobile presence in weeks, not months. Pre-built, customizable solutions designed for small businesses who need professional apps without enterprise budgets.",
            subtitle = "Get to market 3-6 months faster than traditional development",
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
            title = "Custom Competitive Advantage", 
            description = "Build unique features that differentiate your business. Custom Kotlin Multiplatform solutions that scale with your growth and adapt to market changes.",
            subtitle = "Turn your vision into market-leading applications",
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
            .backgroundColor(rgba(255, 255, 255, 0.08)) // Soft semi-transparent background to prevent seizure triggers
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
                            .color(Color.white) // Updated for new semi-transparent background
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
                    .color(rgba(255, 255, 255, 0.8)) // Softer white for better readability
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
                                .color(rgba(255, 255, 255, 0.9)) // Updated for dark theme consistency
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
                .color(Color.white) // Updated for dark theme
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text("Ready to transform your development process?")
        }
        
        P(
            attrs = Modifier
                .fontSize(16.px)
                .color(rgba(255, 255, 255, 0.8)) // Consistent with other descriptions
                .textAlign(TextAlign.Center)
                .maxWidth(700.px)
                .toAttrs()
        ) {
            Text("Let's discuss how our proven expertise can accelerate your next project.")
        }
        
        org.jetbrains.compose.web.dom.Button(
            attrs = {
                style {
                    backgroundColor(KhodedDesignSystem.colors.primary)  // Design system color
                    color(Color.white)
                    border(0.px)
                    borderRadius(12.px)
                    padding(16.px, 32.px)
                    property("font-size", KhodedDesignSystem.typography.bodyFluidMedium)  // Responsive typography via property
                    fontWeight(600)
                    cursor(Cursor.Pointer)
                    minHeight(52.px)
                    property("transition", "all 0.3s ease")
                    property("text-shadow", "0 1px 4px rgba(0, 0, 0, 0.2)")  // Enhanced readability
                    property("box-shadow", "0 4px 12px rgba(6, 182, 212, 0.3)")  // Modern glow effect
                }
                onClick { 
                    // Navigate to contact page for consultation booking
                    kotlinx.browser.window.location.href = "/contact"
                }
                attr("aria-label", "Get free consultation")
            }
        ) {
            Text("Get Free 30-Min Consultation")
        }
    }
}