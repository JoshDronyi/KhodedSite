package com.probro.khoded.utils

import androidx.compose.runtime.*
import com.probro.khoded.design.KhodedDesignSystem
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*

/**
 * Modern Navigation System for Khoded Website
 * Simplified implementation using pure Kobweb components
 */

// Navigation route types
sealed class NavigationRoute(val path: String) {
    object Home : NavigationRoute("/")
    object About : NavigationRoute("/about")
    object Services : NavigationRoute("/services")
    object Contact : NavigationRoute("/contact")
}

// Simple navigation state
class NavigationState {
    // Minimal state for now - can be expanded later
}

@Composable
fun rememberNavigationState(): NavigationState {
    return remember { NavigationState() }
}

@Composable
fun WithNavigation(
    content: @Composable (navigationState: NavigationState) -> Unit
) {
    val navigationState = rememberNavigationState()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationHeader()
        content(navigationState)
    }
}

@Composable
fun NavigationHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(rgb(15, 23, 42)) // Deep navy background
            .padding(20.px, 16.px)
            .boxShadow(offsetX = 0.px, offsetY = 2.px, blurRadius = 8.px, color = rgba(0, 0, 0, 0.2)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo with specialized Khoded branding
        Link(
            path = "/",
            modifier = Modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.px)
            ) {
                // Khoded logo icon
                org.jetbrains.compose.web.dom.Div(
                    attrs = {
                        style {
                            width(40.px)
                            height(32.px)
                            backgroundColor(rgb(6, 182, 212)) // Teal
                            borderRadius(8.px)
                            display(DisplayStyle.Flex)
                            property("align-items", "center")
                            property("justify-content", "center")
                            fontSize(16.px)
                            fontWeight(700)
                            color(Color.white)
                            fontFamily("'Fira Code', 'Consolas', 'Monaco', monospace")
                        }
                    }
                ) {
                    org.jetbrains.compose.web.dom.Text("</>")
                }
                
                SpanText(
                    "KHODED",
                    modifier = Modifier
                        .fontSize(24.px)
                        .fontWeight(700)
                        .color(Color.white)
                )
            }
        }
        
        // Navigation Links
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.px),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationLink("About", "/about")
            NavigationLink("Services", "/services")
            NavigationLink("Contact", "/contact")
        }
    }
}

@Composable
private fun NavigationLink(text: String, href: String) {
    Link(
        path = href,
        modifier = Modifier
    ) {
        SpanText(
            text,
            modifier = Modifier
                .fontSize(16.px)
                .fontWeight(500)
                .color(rgba(255, 255, 255, 0.9)) // Light white for contrast against navy
                .padding(12.px, 16.px)
                .borderRadius(8.px)
                // TODO: Add hover effect to change to teal when API allows
        )
    }
}