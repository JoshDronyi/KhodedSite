package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.design.KhodedDesignSystem
import com.probro.khoded.utils.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.ColorInterpolationMethod
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

/**
 * Modern Landing Hero Section
 * Based on Khoded brand identity: Professional, innovative, tech-forward
 * Colors: Deep navy (#0f172a), vibrant teal (#06b6d4), white
 * Tagline: "Perform natively, scale effortlessly"
 */
@Composable
fun ModernLandingHero(
    onNavigate: (NavigationRoute) -> Unit
) {
    // Hero section with gradient background
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .minHeight(100.vh)
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottomRight,
                    from = rgb(15, 23, 42),    // Deep navy #0f172a
                    to = rgb(30, 41, 59),       // Lighter navy #1e293b
                    interpolation = ColorInterpolationMethod.ProphotoRgb
                )
            )
            .position(Position.Relative),
        contentAlignment = Alignment.Center
    ) {
        // Background geometric elements
        GeometricBackground()
        
        // Main hero content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(leftRight = 24.px, topBottom = 80.px)
                .zIndex(10),
            verticalArrangement = Arrangement.Center
        ) {
            // Centered content container
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            // Logo and brand
            BrandHeader()
            
            Div(attrs = { style { height(60.px) } })
            
            // Main headline
            MainHeadline()
            
            Div(attrs = { style { height(32.px) } })
            
            // Subheadline and description
            SubHeadline()
            
            Div(attrs = { style { height(48.px) } })
            
            // CTA buttons
            CTAButtons(onNavigate)
            
            Div(attrs = { style { height(80.px) } })
            
            // Trust indicators
            TrustIndicators()
            }
        }
    }
}

@Composable
private fun GeometricBackground() {
    // Decorative geometric shapes
    Box(
        modifier = Modifier
            .fillMaxSize()
            .position(Position.Absolute)
            .top(0.px)
            .left(0.px)
            .overflow(Overflow.Hidden)
    ) {
        // Large geometric shape - top right
        Div(
            attrs = Modifier
                .size(400.px)
                .position(Position.Absolute)
                .top((-100).px)
                .right((-100).px)
                .borderRadius(50.percent)
                .backgroundColor(rgba(6, 182, 212, 0.1)) // Teal with opacity
                .toAttrs()
        )
        
        // Medium geometric shape - bottom left
        Div(
            attrs = Modifier
                .size(300.px)
                .position(Position.Absolute)
                .bottom((-50).px)
                .left((-50).px)
                .borderRadius(50.percent)
                .backgroundColor(rgba(6, 182, 212, 0.05))
                .toAttrs()
        )
        
        // Small accent dots
        repeat(3) { index ->
            Div(
                attrs = Modifier
                    .size(12.px)
                    .position(Position.Absolute)
                    .top((200 + index * 150).px)
                    .right((100 + index * 80).px)
                    .borderRadius(50.percent)
                    .backgroundColor(rgb(6, 182, 212))
                    .toAttrs()
            )
        }
    }
}

@Composable
private fun BrandHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.px)
    ) {
        // Khoded developer logo
        Div(
            attrs = Modifier
                .width(80.px)
                .height(64.px)
                .backgroundColor(rgb(6, 182, 212)) // Teal
                .borderRadius(12.px)
                .toAttrs {
                    style {
                        display(DisplayStyle.Flex)
                        property("align-items", "center")
                        property("justify-content", "center")
                        fontFamily("'Fira Code', 'Consolas', 'Monaco', monospace")
                        fontSize(24.px)
                        fontWeight(700)
                        color(Color.white)
                    }
                }
        ) {
            Text("</>")
        }
        
        // Brand name
        SpanText(
            text = "Khoded",
            modifier = Modifier
                .fontSize(28.px)
                .fontWeight(600)
                .color(Color.white)
                .letterSpacing((-0.5).px)
        )
    }
}

@Composable
private fun MainHeadline() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.px)
    ) {
        // Main headline
        SpanText(
            text = "The Ultimate Development Experience",
            modifier = Modifier
                .fontSize(64.px)
                .fontWeight(800)
                .color(Color.white)
                .textAlign(TextAlign.Center)
                .lineHeight(1.1)
                .letterSpacing((-1).px)
                .maxWidth(800.px)
        )
        
        // Accent word with teal highlight
        SpanText(
            text = "Smart businesses choose Kotlin Multiplatform to build iOS & Android apps simultaneously with 80% shared code, slashing time-to-market by months and development costs by up to 60%.",
            modifier = Modifier
                .fontSize(24.px)
                .fontWeight(400)
                .color(rgba(255, 255, 255, 0.8))
                .textAlign(TextAlign.Center)
                .lineHeight(1.5)
                .maxWidth(800.px)
        )
    }
}

@Composable
private fun SubHeadline() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.px)
    ) {
        // Tagline
        SpanText(
            text = "One codebase, every platform, guaranteed results",
            modifier = Modifier
                .fontSize(20.px)
                .fontWeight(500)
                .color(rgb(6, 182, 212)) // Teal accent
                .textAlign(TextAlign.Center)
                .letterSpacing(0.5.px)
        )
    }
}

@Composable
private fun CTAButtons(onNavigate: (NavigationRoute) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.px),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.flexWrap(FlexWrap.Wrap)
    ) {
        // Primary CTA
        org.jetbrains.compose.web.dom.Button(
            attrs = {
                style {
                    backgroundColor(rgb(6, 182, 212)) // Teal
                    color(Color.white)
                    border(0.px)
                    borderRadius(12.px)
                    padding(16.px, 32.px)
                    fontSize(18.px)
                    fontWeight(600)
                    cursor(Cursor.Pointer)
                    minHeight(56.px)
                    minWidth(200.px)
                    property("transition", "all 0.3s ease")
                }
                onClick { onNavigate(NavigationRoute.Contact) }
                // Note: hover effects will be added when Compose API stabilizes
                attr("aria-label", "Start your project with Khoded")
            }
        ) {
            Text("Get In Touch")
        }
        
        // Secondary CTA
        org.jetbrains.compose.web.dom.Button(
            attrs = {
                style {
                    backgroundColor(Color.transparent)
                    color(Color.white)
                    border(2.px, LineStyle.Solid, rgba(255, 255, 255, 0.3))
                    borderRadius(12.px)
                    padding(16.px, 32.px)
                    fontSize(18.px)
                    fontWeight(500)
                    cursor(Cursor.Pointer)
                    minHeight(56.px)
                    minWidth(180.px)
                    property("transition", "all 0.3s ease")
                }
                onClick { onNavigate(NavigationRoute.About) }
                // Note: hover effects will be added when Compose API stabilizes
                attr("aria-label", "Learn more about Khoded")
            }
        ) {
            Text("See Case Studies")
        }
    }
}

@Composable
private fun TrustIndicators() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.px)
    ) {
        SpanText(
            text = "Trusted by forward-thinking businesses",
            modifier = Modifier
                .fontSize(14.px)
                .fontWeight(500)
                .color(rgba(255, 255, 255, 0.6))
                .textAlign(TextAlign.Center)
        )
        
        // Key stats row
        Row(
            horizontalArrangement = Arrangement.spacedBy(48.px),  
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.flexWrap(FlexWrap.Wrap)
        ) {
            StatItem("80%", "Code Reuse")
            StatItem("40%", "Faster Development")
            StatItem("100%", "Native Performance")
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.px)
    ) {
        SpanText(
            text = value,
            modifier = Modifier
                .fontSize(24.px)
                .fontWeight(700)
                .color(rgb(6, 182, 212)) // Teal
        )
        SpanText(
            text = label,
            modifier = Modifier
                .fontSize(12.px)
                .fontWeight(500)
                .color(rgba(255, 255, 255, 0.7))
                .textAlign(TextAlign.Center)
        )
    }
}