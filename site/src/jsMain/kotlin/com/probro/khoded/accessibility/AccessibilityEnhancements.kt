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
        setupFocusIndicators()
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
    
    private fun setupFocusIndicators() {
        js("""
            // Enhanced focus indicators for accessibility compliance
            const style = document.createElement('style');
            style.textContent = `
                .enhanced-focus:focus {
                    outline: 3px solid #4A90E2 !important;
                    outline-offset: 2px !important;
                    box-shadow: 0 0 0 5px rgba(74, 144, 226, 0.3) !important;
                    border-radius: 4px !important;
                }
                
                .enhanced-focus:focus:not(:focus-visible) {
                    outline: none !important;
                    box-shadow: none !important;
                }
                
                .enhanced-focus:focus-visible {
                    outline: 3px solid #4A90E2 !important;
                    outline-offset: 2px !important;
                    box-shadow: 0 0 0 5px rgba(74, 144, 226, 0.3) !important;
                }
                
                .skip-link {
                    position: absolute;
                    top: -40px;
                    left: 6px;
                    background: #000;
                    color: #fff;
                    padding: 8px;
                    text-decoration: none;
                    border-radius: 4px;
                    z-index: 1000;
                }
                
                .skip-link:focus {
                    top: 6px;
                }
            `;
            document.head.appendChild(style);
            
            // Add enhanced focus class to all interactive elements
            const interactiveElements = document.querySelectorAll(
                'button, input, select, textarea, a[href], [tabindex]:not([tabindex="-1"])'
            );
            
            interactiveElements.forEach(element => {
                element.classList.add('enhanced-focus');
            });
        """)
    }
}

// Production-ready accessibility utilities
object AccessibilityUtils {
    fun createSkipLinks() {
        js("""
            const skipLinks = document.createElement('div');
            skipLinks.innerHTML = `
                <a href="#main-content" class="skip-link">Skip to main content</a>
                <a href="#navigation" class="skip-link">Skip to navigation</a>
            `;
            document.body.insertBefore(skipLinks, document.body.firstChild);
        """)
    }
    
    fun enhanceKeyboardNavigation() {
        js("""
            // Trap focus in modals and dialogs
            document.addEventListener('keydown', function(e) {
                if (e.key === 'Tab') {
                    const modal = document.querySelector('.modal:not([hidden])');
                    if (modal) {
                        trapFocus(modal, e);
                    }
                }
                
                // ESC key handling for modals
                if (e.key === 'Escape') {
                    const modal = document.querySelector('.modal:not([hidden])');
                    if (modal) {
                        closeModal(modal);
                    }
                }
            });
            
            function trapFocus(container, event) {
                const focusableElements = container.querySelectorAll(
                    'button, input, select, textarea, a[href], [tabindex]:not([tabindex="-1"])'
                );
                
                const firstElement = focusableElements[0];
                const lastElement = focusableElements[focusableElements.length - 1];
                
                if (event.shiftKey) {
                    if (document.activeElement === firstElement) {
                        event.preventDefault();
                        lastElement.focus();
                    }
                } else {
                    if (document.activeElement === lastElement) {
                        event.preventDefault();
                        firstElement.focus();
                    }
                }
            }
            
            function closeModal(modal) {
                modal.setAttribute('hidden', '');
                // Restore focus to trigger element if available
                const trigger = modal.dataset.triggerElement;
                if (trigger) {
                    document.getElementById(trigger)?.focus();
                }
            }
        """)
    }
    
    fun announcePageChange(pageTitle: String) {
        js("""
            const announcement = `Page changed to: ${pageTitle}`;
            const liveRegion = document.getElementById('page-change-announcer') || createPageChangeAnnouncer();
            liveRegion.textContent = announcement;
            
            function createPageChangeAnnouncer() {
                const announcer = document.createElement('div');
                announcer.id = 'page-change-announcer';
                announcer.setAttribute('aria-live', 'polite');
                announcer.setAttribute('aria-atomic', 'true');
                announcer.className = 'sr-only';
                announcer.style.cssText = 'position: absolute; left: -10000px; width: 1px; height: 1px; overflow: hidden;';
                document.body.appendChild(announcer);
                return announcer;
            }
        """.replace("${pageTitle}", pageTitle))
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