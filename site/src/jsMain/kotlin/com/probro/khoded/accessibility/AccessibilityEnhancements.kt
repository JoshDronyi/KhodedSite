package com.probro.khoded.accessibility

import androidx.compose.runtime.*
import com.probro.khoded.styles.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.*

/**
 * Accessibility Enhancements - Simplified version for compilation compatibility
 * 
 * This is a simplified version of the accessibility enhancements to ensure
 * the project builds successfully. Full accessibility features can be
 * implemented incrementally.
 */

// =============================================================================
// ACCESSIBILITY CONTEXT AND PREFERENCES
// =============================================================================

data class AccessibilityPreferences(
    val reducedMotion: Boolean = false,
    val highContrast: Boolean = false,
    val darkMode: Boolean = false,
    val increasedTextSize: Boolean = false,
    val screenReaderMode: Boolean = false,
    val keyboardNavigationOnly: Boolean = false,
    val focusIndicatorEnhanced: Boolean = false
)

@Composable
fun rememberAccessibilityPreferences(): AccessibilityPreferences {
    var preferences by remember { 
        mutableStateOf(AccessibilityPreferences()) 
    }
    
    LaunchedEffect(Unit) {
        preferences = detectAccessibilityPreferences()
    }
    
    return preferences
}

private fun detectAccessibilityPreferences(): AccessibilityPreferences {
    return try {
        AccessibilityPreferences(
            reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches,
            highContrast = window.matchMedia("(prefers-contrast: high)").matches,
            darkMode = window.matchMedia("(prefers-color-scheme: dark)").matches
        )
    } catch (e: Exception) {
        AccessibilityPreferences()
    }
}

// =============================================================================
// LIVE REGIONS FOR SCREEN READERS
// =============================================================================

enum class LiveRegionPoliteness {
    Off, Polite, Assertive
}

@Composable
fun LiveRegion(
    message: String,
    politeness: LiveRegionPoliteness = LiveRegionPoliteness.Polite,
    modifier: Modifier = Modifier
) {
    Div(
        attrs = modifier
            .position(Position.Absolute)
            .left((-10000).px)
            .width(1.px)
            .height(1.px)
            .attrsModifier { style { property("overflow", "hidden") } }
            .toAttrs {
                attr("aria-live", when (politeness) {
                    LiveRegionPoliteness.Off -> "off"
                    LiveRegionPoliteness.Polite -> "polite"
                    LiveRegionPoliteness.Assertive -> "assertive"
                })
                attr("aria-atomic", "true")
            }
    ) {
        org.jetbrains.compose.web.dom.Text(message)
    }
}

class LiveRegionManager {
    private val regions = mutableMapOf<String, Element>()
    
    fun createRegion(id: String, politeness: LiveRegionPoliteness = LiveRegionPoliteness.Polite) {
        val region = document.createElement("div").apply {
            setAttribute("id", id)
            setAttribute("aria-live", when (politeness) {
                LiveRegionPoliteness.Off -> "off"
                LiveRegionPoliteness.Polite -> "polite"  
                LiveRegionPoliteness.Assertive -> "assertive"
            })
            setAttribute("aria-atomic", "true")
            setAttribute("class", "sr-only")
        }
        
        document.body?.appendChild(region)
        regions[id] = region
    }
    
    fun announce(regionId: String, message: String) {
        regions[regionId]?.textContent = message
    }
    
    fun announcePolite(message: String) {
        announce("polite-region", message)
    }
    
    fun announceAssertive(message: String) {
        announce("assertive-region", message)
    }
    
    fun clear(regionId: String) {
        regions[regionId]?.textContent = ""
    }
    
    fun clearAll() {
        regions.values.forEach { it.textContent = "" }
    }
}

// =============================================================================
// FOCUS MANAGEMENT
// =============================================================================

class FocusManager {
    private val focusHistory = mutableListOf<Element>()
    private var isManaging = false
    
    fun startManaging() {
        isManaging = true
    }
    
    fun stopManaging() {
        isManaging = false
        focusHistory.clear()
    }
    
    fun focusElement(element: Element) {
        if (isManaging) {
            document.activeElement?.let { focusHistory.add(it) }
        }
        (element as HTMLElement).focus()
    }
    
    fun restoreFocus() {
        if (focusHistory.isNotEmpty()) {
            val lastFocused = focusHistory.removeLastOrNull()
            (lastFocused as? HTMLElement)?.focus()
        }
    }
}

// =============================================================================
// UTILITY FUNCTIONS
// =============================================================================

@Composable
fun ScreenReaderOnly(
    text: String,
    modifier: Modifier = Modifier
) {
    Div(
        attrs = modifier
            .position(Position.Absolute)
            .left((-10000).px)
            .width(1.px)
            .height(1.px)
            .attrsModifier { style { property("overflow", "hidden") } }
            .toAttrs {
                attr("class", "sr-only")
            }
    ) {
        org.jetbrains.compose.web.dom.Text(text)
    }
}