package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.models.ButtonState
import com.probro.khoded.design.KhodedDesignSystem
import com.probro.khoded.utils.*
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.compose.css.FontStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Text
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient

@Composable
fun LandingSectionDisplay(
    onNavigate: (path: NavigationRoute) -> Unit,
    data: Pages.Home_Section.Landing
) = with(data) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .minHeight(100.vh)
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottomRight,
                    from = rgb(107, 33, 168),
                    to = rgb(88, 28, 135)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Hero Content
        LandingHeroContent(onNavigate, data)
    }
}

@Composable
private fun LandingHeroContent(
    onNavigate: (path: NavigationRoute) -> Unit,
    data: Pages.Home_Section.Landing
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(1200.px)
            .padding(40.px),
        horizontalArrangement = Arrangement.spacedBy(60.px),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side - Text content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.px),
            horizontalAlignment = Alignment.Start
        ) {
            // Main headline
            SpanText(
                "Psst, a GOOD site tells a brand's story",
                modifier = Modifier
                    .fontSize(48.px)
                    .fontWeight(700)
                    .lineHeight(1.2)
                    .color(Color.white)
            )
            
            // Subtitle
            SpanText(
                "Let Khoded handle all your web app, site redesign, web hosting, and brand + SEO needs in one place.",
                modifier = Modifier
                    .fontSize(18.px)
                    .lineHeight(1.6)
                    .color(rgba(255, 255, 255, 0.9))
                    .maxWidth(500.px)
            )
            
            // CTA Button - FIXED: Added functionality and accessibility
            org.jetbrains.compose.web.dom.Button(
                attrs = {
                    style {
                        padding(16.px, 24.px)
                        backgroundColor(rgb(168, 85, 247))
                        borderRadius(50.px)
                        cursor("pointer")
                        border(0.px)
                        fontSize(16.px)
                        fontWeight(600)
                        color(Color.white)
                        minHeight(44.px)
                    }
                    onClick { 
                        // Navigate to contact/consultation page
                        onNavigate(NavigationRoute.Contact)
                    }
                    attr("aria-label", "Start consultation process - Get Khoded services")
                    attr("type", "button")
                }
            ) {
                Text("GET KHODED")
            }
        }
        
        // Right side - Image placeholder
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(400.px)
                    .backgroundColor(rgba(255, 255, 255, 0.1))
                    .borderRadius(12.px),
                contentAlignment = Alignment.Center
            ) {
                SpanText(
                    "Hero Image",
                    modifier = Modifier
                        .fontSize(24.px)
                        .color(rgba(255, 255, 255, 0.7))
                )
            }
        }
    }
}