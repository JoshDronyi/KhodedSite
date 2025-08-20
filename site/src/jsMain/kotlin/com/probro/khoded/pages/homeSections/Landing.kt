package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.utils.NavigationRoute
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.functions.clamp
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun LandingSectionDisplay(
    onNavigate: (path: NavigationRoute) -> Unit,
    data: Pages.Home_Section.Landing
) = with(data) {
    // Full viewport hero section with proper branding
    Section(
        attrs = Modifier
            .fillMaxWidth()
            .minHeight(100.vh)
            .backgroundColor(rgb(15, 23, 42)) // Deep navy background
            .padding(topBottom = 80.px, leftRight = 24.px)
            .toAttrs {
                id("landing")
                attr("aria-label", "Khoded Hero Section")
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(leftRight = 5.vw),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(60.px)
        ) {
            // Hero Content
            HeroContent(onNavigate)

            // Visual Features Section
            HeroVisualSection()
        }
    }
}

@Composable
private fun HeroContent(onNavigate: (NavigationRoute) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.px)
    ) {
        // Section badge with Khoded logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.px),
            modifier = Modifier
                .backgroundColor(rgba(68, 182, 198, 0.15))
                .padding(10.px, 24.px)
                .borderRadius(50.px)
        ) {
            // Khoded logo icon
            Div(
                attrs = Modifier
                    .size(20.px, 16.px)
                    .backgroundColor(rgb(68, 182, 198))
                    .borderRadius(4.px)
                    .toAttrs {
                        style {
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
                Text("</>")
            }

            SpanText(
                text = "Khoded Specializes in Kotlin Multiplatform",
                modifier = Modifier
                    .fontSize(clamp(12.px, 2.5.vw, 14.px)) // Responsive sizing
                    .fontWeight(600)
                    .color(rgb(6, 182, 212)) // Brighter for better contrast
            )
        }

        // Main hero heading - Enhanced contrast and responsive sizing
        H1(
            attrs = Modifier
                .fontSize(clamp(36.px, 8.vw, 60.px)) // Responsive font size
                .fontWeight(800)
                .color(Color.white) // Pure white for maximum contrast against navy
                .textAlign(TextAlign.Center)
                .lineHeight(1.1)
                .letterSpacing((-1).px)
                .maxWidth(900.px)
                .padding(leftRight = 5.vw)
                .margin(0.px)
                .toAttrs {
                    style {
                        // Text shadow for enhanced readability
                        property("text-shadow", "0 2px 10px rgba(0, 0, 0, 0.3)")
                    }
                }
        ) {
            Text("Build Once, Deploy Everywhere with ")
            Span(
                attrs = Modifier
                    .color(rgb(6, 182, 212)) // Brighter teal for better contrast
                    .toAttrs {
                        style {
                            // Enhanced text shadow for the highlighted text
                            property("text-shadow", "0 2px 10px rgba(6, 182, 212, 0.4)")
                        }
                    }
            ) {
                Text("Kotlin Multiplatform")
            }
        }

        // Subtitle - Enhanced readability
        P(
            attrs = Modifier
                .fontSize(clamp(18.px, 4.vw, 22.px)) // Responsive font size
                .color(rgba(255, 255, 255, 0.95)) // Higher opacity for better readability
                .textAlign(TextAlign.Center)
                .lineHeight(1.6)
                .maxWidth(700.px)
                .padding(leftRight = 5.vw)
                .margin(0.px)
                .toAttrs {
                    style {
                        // Text shadow for better readability
                        property("text-shadow", "0 1px 8px rgba(0, 0, 0, 0.4)")
                    }
                }
        ) {
            Text("80% code reuse. 40% faster development. 100% native performance. Join businesses worldwide who trust Khoded for their Kotlin Multiplatform solutions.")
        }

        // Call to action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.px),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Primary CTA - Enhanced readability
            Button(
                attrs = Modifier
                    .backgroundColor(rgb(6, 182, 212)) // Brighter teal for better contrast
                    .color(Color.white) // Pure white text
                    .border(0.px)
                    .borderRadius(12.px)
                    .padding(18.px, 36.px)
                    .fontSize(clamp(16.px, 3.vw, 18.px)) // Responsive sizing
                    .fontWeight(600)
                    .minHeight(56.px)
                    .toAttrs {
                        style {
                            cursor("pointer")
                            property("transition", "all 0.3s ease")
                            property("text-shadow", "0 1px 4px rgba(0, 0, 0, 0.2)") // Text shadow for readability
                            property("box-shadow", "0 4px 12px rgba(6, 182, 212, 0.3)") // Glow effect
                        }
                        attr("aria-label", "Get started with Khoded")
                        onClick { onNavigate(NavigationRoute.Contact) }
                    }
            ) {
                Text("🚀 GET KHODED")
            }

            // Secondary CTA - Enhanced readability
            Link(
                path = "/services",
                modifier = Modifier
                    .color(rgba(255, 255, 255, 0.98)) // Higher opacity for better readability
                    .fontSize(clamp(16.px, 3.vw, 18.px)) // Responsive sizing
                    .fontWeight(500)
                    .padding(18.px, 24.px)
                    .borderRadius(12.px)
                    .border(2.px, LineStyle.Solid, rgba(255, 255, 255, 0.4)) // More visible border
                    .styleModifier {
                        property("text-shadow", "0 1px 4px rgba(0, 0, 0, 0.3)") // Text shadow for readability
                        property("transition", "all 0.3s ease")
                        property("backdrop-filter", "blur(10px)") // Subtle backdrop filter
                    }
            ) {
                SpanText("👀 View Our Work")
            }
        }
    }
}

/**
 * Hero Visual Features Section
 */
@Composable
private fun HeroVisualSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(1000.px)
            .padding(leftRight = 5.vw),
        horizontalArrangement = Arrangement.spacedBy(32.px, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Top
    ) {
        // Feature cards
        FeatureCard(
            icon = "🎨",
            title = "Design Excellence",
            description = "State-of-the-art designs that captivate and convert"
        )

        FeatureCard(
            icon = "⚡",
            title = "Lightning Fast",
            description = "Optimized performance that loads in milliseconds"
        )

        FeatureCard(
            icon = "🛡️",
            title = "Secure & Reliable",
            description = "Enterprise-grade security and 99.9% uptime"
        )
    }
}

@Composable
private fun FeatureCard(
    icon: String,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .width(280.px)
            .backgroundColor(rgba(255, 255, 255, 0.08)) // Slightly more visible background
            .borderRadius(16.px)
            .padding(32.px)
            .border(1.px, LineStyle.Solid, rgba(255, 255, 255, 0.15)), // More visible border
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.px)
    ) {
        // Icon
        Div(
            attrs = Modifier
                .size(60.px)
                .backgroundColor(rgba(68, 182, 198, 0.2))
                .borderRadius(16.px)
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

        // Title - Enhanced readability
        H3(
            attrs = Modifier
                .fontSize(clamp(18.px, 3.vw, 20.px)) // Responsive sizing
                .fontWeight(600)
                .color(Color.white) // Pure white for maximum contrast
                .textAlign(TextAlign.Center)
                .margin(0.px)
                .toAttrs {
                    style {
                        property("text-shadow", "0 1px 6px rgba(0, 0, 0, 0.3)")
                    }
                }
        ) {
            Text(title)
        }

        // Description - Enhanced readability
        P(
            attrs = Modifier
                .fontSize(clamp(14.px, 2.5.vw, 16.px)) // Responsive sizing
                .color(rgba(255, 255, 255, 0.92)) // Higher opacity for better readability
                .textAlign(TextAlign.Center)
                .lineHeight(1.5)
                .margin(0.px)
                .toAttrs {
                    style {
                        property("text-shadow", "0 1px 4px rgba(0, 0, 0, 0.4)")
                    }
                }
        ) {
            Text(description)
        }
    }
}

// Note: Float animation is now handled by CoreDesignSystem.kt (floatAnimation)