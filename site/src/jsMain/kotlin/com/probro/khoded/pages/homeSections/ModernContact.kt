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
 * Modern Contact/CTA Section
 * Final section encouraging users to get in touch
 * Uses Khoded brand colors and professional tone
 */
@Composable
fun ModernContactSection() {
    KhodedSection(
        id = "contact",
        ariaLabel = "Contact Khoded",
        backgroundColor = KhodedDesignSystem.colors.backgroundDarkSecondary
    ) {
        // Main contact content
        ContactContent()
        
        VerticalSpacer(60.px)
        
        // Contact methods
        ContactMethods()
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
            // Mini logo - Using unified component
            KhodedLogo(size = LogoSize.Medium)
            
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
            Text("Ready to Cut Your App Development Costs by 60%?")
        }
        
        // Description
        P(
            attrs = Modifier
                .fontSize(20.px)
                .fontWeight(400)
                .color(rgba(255, 255, 255, 0.8))
                .textAlign(TextAlign.Center)
                .lineHeight(1.6)
                .maxWidth(800.px)
                .toAttrs()
        ) {
            Text("Join smart businesses who chose Kotlin Multiplatform and saved thousands while reaching every customer. Book your free consultation to see exactly how much you'll save.")
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
                    backgroundColor(KhodedDesignSystem.colors.primary) // Design system teal
                    color(Color.white)
                    border(0.px)
                    borderRadius(12.px)
                    padding(20.px, 40.px)
                    property("font-size", KhodedDesignSystem.typography.bodyFluidLarge)  // Responsive typography via property
                    fontWeight(600)
                    cursor(Cursor.Pointer)
                    minHeight(60.px)
                    minWidth(280.px)
                    property("transition", "all 0.3s ease")
                    property("text-shadow", "0 1px 4px rgba(0, 0, 0, 0.2)")  // Enhanced readability
                    property("box-shadow", "0 4px 12px rgba(6, 182, 212, 0.3)")  // Modern glow effect
                }
                onClick {
                    // Navigate to contact page where the actual form is located
                    kotlinx.browser.window.location.href = "/contact"
                }
                attr("aria-label", "Schedule free consultation with Khoded")
            }
        ) {
            Text("Get Free 30-Min Consultation")
        }
    }
}

