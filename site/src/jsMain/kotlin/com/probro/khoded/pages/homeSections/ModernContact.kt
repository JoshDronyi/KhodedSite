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
 * Modern Contact/CTA Section
 * Final section encouraging users to get in touch
 * Uses Khoded brand colors and professional tone
 */
@Composable
fun ModernContactSection() {
    Section(
        attrs = Modifier
            .fillMaxWidth()
            .backgroundColor(rgb(15, 23, 42)) // Deep navy background
            .padding(topBottom = 120.px, leftRight = 24.px)
            .toAttrs {
                id("contact")
                attr("aria-label", "Contact Khoded")
            }
    ) {
        Container(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(1000.px),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main contact content
            ContactContent()
            
            Div(attrs = { style { height(60.px) } })
            
            // Contact methods
            ContactMethods()
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
private fun ContactContent() {
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
                org.jetbrains.compose.web.dom.Text("</>")
            }
            
            SpanText(
                text = "Ready to Start?",
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
            Text("Transform your business with Kotlin Multiplatform specialists serving worldwide")
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
            Text("Join ambitious businesses across the Americas, Africa, and beyond who are discovering the power of Kotlin Multiplatform. Get 80% code reuse, 40% faster delivery, and 100% native performance.")
        }
    }
}

@Composable
private fun ContactMethods() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.px)
    ) {
        // Primary CTA button
        org.jetbrains.compose.web.dom.Button(
            attrs = {
                style {
                    backgroundColor(rgb(6, 182, 212)) // Teal
                    color(Color.white)
                    border(0.px)
                    borderRadius(12.px)
                    padding(20.px, 40.px)
                    fontSize(18.px)
                    fontWeight(600)
                    cursor(Cursor.Pointer)
                    minHeight(60.px)
                    minWidth(280.px)
                    property("transition", "all 0.3s ease")
                }
                // TODO: Add hover effects when API is available
                attr("aria-label", "Schedule free consultation with Khoded")
            }
        ) {
            Text("Get Free 30-Min Consultation")
        }
        
        // Contact alternatives
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .toAttrs {
                    style {
                        display(DisplayStyle.Grid)
                        property("grid-template-columns", "repeat(auto-fit, minmax(250px, 1fr))")
                        property("gap", "32px")
                        property("text-align", "center")
                    }
                }
        ) {
            ContactMethod(
                icon = "📧",
                title = "Email Us",
                description = "hello@khoded.com",
                subtitle = "Quick response within 4 hours"
            )
            
            ContactMethod(
                icon = "📱",
                title = "Text Support",
                description = "Direct line to our team",
                subtitle = "Personal support guarantee"
            )
        }
        
        // Trust badge
        Div(
            attrs = Modifier
                .backgroundColor(rgba(6, 182, 212, 0.1))
                .borderRadius(16.px)
                .padding(24.px)
                .maxWidth(600.px)
                .toAttrs()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.px)
            ) {
                SpanText(
                    text = "🏆 Connecticut's #1 Kotlin Multiplatform Agency",
                    modifier = Modifier
                        .fontSize(16.px)
                        .fontWeight(600)
                        .color(rgb(6, 182, 212))
                        .textAlign(TextAlign.Center)
                )
                
                P(
                    attrs = Modifier
                        .fontSize(14.px)
                        .color(rgba(255, 255, 255, 0.8))
                        .textAlign(TextAlign.Center)
                        .lineHeight(1.5)
                        .toAttrs()
                ) {
                    Text("Specialized expertise in Finance, Healthcare, and Manufacturing industries")
                }
            }
        }
    }
}

@Composable
private fun ContactMethod(
    icon: String,
    title: String,
    description: String,
    subtitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.px)
    ) {
        // Icon
        Div(
            attrs = Modifier
                .size(56.px)
                .backgroundColor(rgba(6, 182, 212, 0.15))
                .borderRadius(14.px)
                .toAttrs {
                    style {
                        display(DisplayStyle.Flex)
                        property("align-items", "center")
                        property("justify-content", "center")
                        fontSize(24.px)
                    }
                }
        ) {
            Text(icon)
        }
        
        // Title
        H3(
            attrs = Modifier
                .fontSize(18.px)
                .fontWeight(600)
                .color(Color.white)
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text(title)
        }
        
        // Description
        P(
            attrs = Modifier
                .fontSize(16.px)
                .fontWeight(500)
                .color(rgb(6, 182, 212))
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text(description)
        }
        
        // Subtitle
        P(
            attrs = Modifier
                .fontSize(14.px)
                .color(rgba(255, 255, 255, 0.7))
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text(subtitle)
        }
    }
}