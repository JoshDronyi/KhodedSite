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
import com.probro.khoded.components.forms.ValidatedContactForm

/**
 * Contact Page - Get in touch with Khoded
 */
@Page
@Composable
fun ContactPage() {
    ErrorBoundary(config = ErrorBoundaryConfig()) {
        WithNavigation { _navigationState ->
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
                            to = rgb(30, 41, 59)       // Lighter navy
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
                            .fontSize(clamp(16.px, 4.vw, 20.px))  // Responsive subtitle
                            .color(Color.white)
                            .textAlign(TextAlign.Center)
                            .maxWidth(600.px)
                            .lineHeight(1.6)
                            .padding(topBottom = 0.px, leftRight = 16.px)  // Mobile padding
                    )
                }

                // Contact Form Section - Responsive layout
                ResponsiveContactSection()
            }
        }
    }
}

/**
 * Responsive Contact Section - Mobile-first design
 * Mobile/Tablet: Form full width, info stacked below
 * Desktop: Side-by-side layout
 */
@Composable
private fun ResponsiveContactSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(KhodedDesignSystem.colors.backgroundDarkSecondary)
            .padding(
                // Mobile-first responsive padding - Increased minimums for better mobile UX
                top = clamp(60.px, 8.vw, 80.px),
                bottom = clamp(60.px, 8.vw, 80.px),
                left = clamp(32.px, 5.vw, 40.px),
                right = clamp(32.px, 5.vw, 40.px)
            ),
        verticalArrangement = Arrangement.spacedBy(clamp(50.px, 6.vw, 60.px)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Desktop: Side-by-side layout using CSS Grid-like approach
        // Mobile/Tablet: Stacked layout
        ResponsiveContactLayout()
    }
}

/**
 * Responsive Contact Layout
 * Uses different layouts based on screen size
 */
@Composable
private fun ResponsiveContactLayout() {
    // For mobile and tablet (< 1024px): Stack vertically
    // For desktop (>= 1024px): Side-by-side
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(1200.px), // Container max width
        verticalArrangement = Arrangement.spacedBy(clamp(40.px, 5.vw, 60.px))
    ) {
        // Mobile/Tablet: Form takes full width
        // Desktop: Form takes left half
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(clamp(32.px, 4.vw, 30.px))
        ) {
            SpanText(
                "Send us a message",
                modifier = Modifier
                    .fontSize(clamp(28.px, 5.vw, 32.px))
                    .fontWeight(700)
                    .color(Color.white)
                    .textAlign(TextAlign.Center)
                    .fillMaxWidth()
            )
            
            ValidatedContactForm(
                modifier = Modifier
                    .fillMaxWidth()
                    // Mobile/Tablet: Full width, Desktop: Constrained with responsive sizing
                    .maxWidth(clamp(100.percent, 45.vw, 600.px)),
                onSubmitSuccess = { message ->
                    println("Contact form submitted successfully: $message")
                }
            )
        }
        
        // Mobile/Tablet: Stacked below form
        // Desktop: Right side info panel
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(clamp(30.px, 5.vw, 40.px))
        ) {
            SpanText(
                "Contact Information",
                modifier = Modifier
                    .fontSize(clamp(24.px, 5.vw, 28.px))
                    .fontWeight(700)
                    .color(Color.white)
                    .textAlign(TextAlign.Center)
            )
            
            // Contact cards in responsive layout
            ContactInfoGrid()
            
            // Free consultation CTA - Mobile optimized
            FreConsultationCTA()
        }
    }
}

/**
 * Contact Information Grid - Responsive layout
 */
@Composable
private fun ContactInfoGrid() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(600.px),
        verticalArrangement = Arrangement.spacedBy(clamp(20.px, 3.vw, 24.px))
    ) {
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
    }
}

/**
 * Free Consultation CTA Component
 */
@Composable
private fun FreConsultationCTA() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(500.px)
            .backgroundColor(rgb(6, 182, 212))
            .borderRadius(12.px)
            .padding(clamp(24.px, 4.vw, 30.px)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.px)
    ) {
        SpanText(
            "Free 30-Min Consultation",
            modifier = Modifier
                .fontSize(clamp(20.px, 4.vw, 22.px))
                .fontWeight(700)
                .color(Color.white)
                .textAlign(TextAlign.Center)
        )
        
        SpanText(
            "Get expert guidance on Kotlin Multiplatform adoption and Connecticut industry compliance.",
            modifier = Modifier
                .fontSize(clamp(15.px, 3.vw, 16.px))
                .color(rgba(255, 255, 255, 0.9))
                .textAlign(TextAlign.Center)
                .lineHeight(1.5)
        )
        
        Column(
            modifier = Modifier
                .padding(18.px, 16.px)  // Larger touch target
                .backgroundColor(Color.white)
                .borderRadius(8.px)
                .cursor(Cursor.Pointer)
                .minHeight(48.px), // WCAG AAA compliant
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SpanText(
                "Schedule Now",
                modifier = Modifier
                    .fontSize(clamp(15.px, 3.vw, 16.px))
                    .fontWeight(600)
                    .color(rgb(6, 182, 212))
            )
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
            .padding(clamp(32.px, 4.vw, 24.px))  // Responsive padding - Increased mobile minimum
            .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 12.px, color = rgba(0, 0, 0, 0.3))
            .minHeight(48.px),  // WCAG AAA compliant touch target
        verticalArrangement = Arrangement.spacedBy(8.px),
        horizontalAlignment = Alignment.Start
    ) {
        SpanText(
            title,
            modifier = Modifier
                .fontSize(clamp(14.px, 3.vw, 16.px))  // Responsive font size
                .fontWeight(600)
                .color(rgb(6, 182, 212))
        )
        
        SpanText(
            value,
            modifier = Modifier
                .fontSize(clamp(16.px, 4.vw, 18.px))  // Responsive font size
                .fontWeight(700)
                .color(Color.white)
                .lineHeight(1.3)
        )
        
        SpanText(
            description,
            modifier = Modifier
                .fontSize(clamp(16.px, 2.5.vw, 14.px))  // Responsive font size - WCAG AA compliant minimum
                .color(rgba(255, 255, 255, 0.8))
                .lineHeight(1.4)
        )
    }
}