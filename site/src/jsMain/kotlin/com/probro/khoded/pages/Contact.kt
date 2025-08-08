package com.probro.khoded.pages

import androidx.compose.runtime.Composable
import com.probro.khoded.components.ErrorBoundary
import com.probro.khoded.components.ErrorBoundaryConfig
import com.probro.khoded.design.KhodedDesignSystem
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
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.css.functions.clamp
import com.varabyte.kobweb.compose.ui.graphics.Colors

/**
 * Contact Page - Get in touch with Khoded
 */
@Page
@Composable
fun ContactPage() {
    ErrorBoundary(config = ErrorBoundaryConfig()) {
        WithNavigation { navigationState ->
            Column(
                modifier = Modifier.fillMaxWidth()
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
                    H1(
                        attrs = {
                            style {
                                property("font-size", KhodedDesignSystem.typography.sectionLarge)  // Responsive clamp string
                                fontWeight(700)
                                color(Color.white)
                                textAlign("center")
                                marginBottom(20.px)
                                property("text-shadow", "0 2px 10px rgba(0, 0, 0, 0.3)")  // Enhanced readability
                            }
                        }
                    ) {
                        Text("Get In Touch")
                    }
                    
                    SpanText(
                        "Ready to achieve 80% code reuse with Kotlin Multiplatform? Let's discuss your Connecticut business needs.",
                        modifier = Modifier
                            .fontSize(20.px)
                            .color(Color.white)
                            .textAlign(TextAlign.Center)
                            .maxWidth(600.px)
                    )
                }

                // Contact Form Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .backgroundColor(KhodedDesignSystem.colors.backgroundDarkSecondary)
                        .padding(80.px, 40.px),
                    horizontalArrangement = Arrangement.spacedBy(60.px),
                    verticalAlignment = Alignment.Top
                ) {
                    // Left side - Contact form
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(30.px)
                    ) {
                        SpanText(
                            "Send us a message",
                            modifier = Modifier
                                .fontSize(28.px)
                                .fontWeight(700)
                                .color(Color.white)
                        )
                        
                        // Contact form placeholder
                        SpanText(
                            text = "Contact form coming soon. Please email us at admin@khoded.com",
                            modifier = Modifier
                                .fontSize(16.px)
                                .color(rgba(255, 255, 255, 0.8))
                        )
                    }
                    
                    // Right side - Contact info
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(40.px)
                    ) {
                        SpanText(
                            "Contact Information",
                            modifier = Modifier
                                .fontSize(28.px)
                                .fontWeight(700)
                                .color(Color.white)
                        )
                        
                        ContactInfoCard(
                            title = "Email",
                            value = "hello@khoded.com",
                            description = "Send us an email anytime"
                        )
                        
                        ContactInfoCard(
                            title = "Phone",
                            value = "+1 (555) 123-4567",
                            description = "Call us during business hours"
                        )
                        
                        ContactInfoCard(
                            title = "Location",
                            value = "Connecticut, USA",
                            description = "Serving Connecticut's Finance, Healthcare & Manufacturing"
                        )
                        
                        // Free consultation CTA
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .backgroundColor(rgb(6, 182, 212))
                                .borderRadius(12.px)
                                .padding(30.px),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(15.px)
                        ) {
                            SpanText(
                                "Free 30-Min Consultation",
                                modifier = Modifier
                                    .fontSize(20.px)
                                    .fontWeight(700)
                                    .color(Color.white)
                                    .textAlign(TextAlign.Center)
                            )
                            
                            SpanText(
                                "Get expert guidance on Kotlin Multiplatform adoption and Connecticut industry compliance.",
                                modifier = Modifier
                                    .fontSize(14.px)
                                    .color(rgba(255, 255, 255, 0.9))
                                    .textAlign(TextAlign.Center)
                            )
                            
                            Column(
                                modifier = Modifier
                                    .padding(12.px, 20.px)
                                    .backgroundColor(Color.white)
                                    .borderRadius(8.px)
                                    .cursor(Cursor.Pointer),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SpanText(
                                    "Schedule Now",
                                    modifier = Modifier
                                        .fontSize(14.px)
                                        .fontWeight(600)
                                        .color(rgb(6, 182, 212))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ContactInfoCard(
    title: String,
    value: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(rgba(255, 255, 255, 0.08))
            .borderRadius(12.px)
            .padding(24.px)
            .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 12.px, color = rgba(0, 0, 0, 0.3)),
        verticalArrangement = Arrangement.spacedBy(8.px)
    ) {
        SpanText(
            title,
            modifier = Modifier
                .fontSize(16.px)
                .fontWeight(600)
                .color(rgb(6, 182, 212))
        )
        
        SpanText(
            value,
            modifier = Modifier
                .fontSize(18.px)
                .fontWeight(700)
                .color(Color.white)
        )
        
        SpanText(
            description,
            modifier = Modifier
                .fontSize(14.px)
                .color(rgba(255, 255, 255, 0.8))
        )
    }
}