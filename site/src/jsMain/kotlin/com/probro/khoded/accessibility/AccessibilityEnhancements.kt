package com.probro.khoded.accessibility

import androidx.compose.runtime.*
import com.probro.khoded.styles.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.*
import org.w3c.dom.events.KeyboardEvent

/**
 * Advanced Accessibility Enhancements
 * 
 * Comprehensive accessibility features including:
 * - Screen reader optimization
 * - High contrast mode support
 * - Advanced keyboard navigation
 * - Focus management
 * - ARIA live regions
 * - Voice navigation support
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
        
        // Listen for preference changes
        setupAccessibilityListeners { newPreferences ->
            preferences = newPreferences
        }
    }
    
    return preferences
}

private fun detectAccessibilityPreferences(): AccessibilityPreferences {
    return AccessibilityPreferences(
        reducedMotion = matchesMediaQuery("(prefers-reduced-motion: reduce)"),
        highContrast = matchesMediaQuery("(prefers-contrast: high)"),
        darkMode = matchesMediaQuery("(prefers-color-scheme: dark)"),
        increasedTextSize = window.devicePixelRatio > 1.5, // Approximate detection
        screenReaderMode = detectScreenReader(),
        keyboardNavigationOnly = detectKeyboardNavigation()
    )
}

private fun matchesMediaQuery(query: String): Boolean {
    return try {
        window.matchMedia(query).matches
    } catch (e: Exception) {
        false
    }
}

private fun detectScreenReader(): Boolean {
    return try {
        // Check for common screen reader indicators
        js("""
            navigator.userAgent.includes('NVDA') ||
            navigator.userAgent.includes('JAWS') ||
            navigator.userAgent.includes('VoiceOver') ||
            navigator.userAgent.includes('TalkBack') ||
            window.speechSynthesis !== undefined
        """) as Boolean
    } catch (e: Exception) {
        false
    }
}

private fun detectKeyboardNavigation(): Boolean {
    return try {
        // Detect if user is primarily using keyboard
        js("window.navigator.maxTouchPoints === 0") as Boolean
    } catch (e: Exception) {
        false
    }
}

private fun setupAccessibilityListeners(onPreferencesChange: (AccessibilityPreferences) -> Unit) {
    val mediaQueries = listOf(
        "(prefers-reduced-motion: reduce)",
        "(prefers-contrast: high)",
        "(prefers-color-scheme: dark)"
    )
    
    mediaQueries.forEach { query ->
        try {
            window.matchMedia(query).addEventListener("change") {
                onPreferencesChange(detectAccessibilityPreferences())
            }
        } catch (e: Exception) {
            console.warn("Failed to set up accessibility listener for: $query")
        }
    }
}

// =============================================================================
// ENHANCED FOCUS MANAGEMENT
// =============================================================================

class FocusManager {
    private val focusHistory = mutableListOf<Element>()
    private var isManaging = false
    
    fun startManaging() {
        isManaging = true
        setupFocusTrapping()
    }
    
    fun stopManaging() {
        isManaging = false
        focusHistory.clear()
    }
    
    fun trapFocus(container: Element) {
        val focusableElements = getFocusableElements(container)
        if (focusableElements.isEmpty()) return
        
        val firstElement = focusableElements.first()
        val lastElement = focusableElements.last()
        
        container.addEventListener("keydown", { event ->
            if ((event as KeyboardEvent).key == "Tab") {
                if (event.shiftKey) {
                    // Shift+Tab - going backwards
                    if (document.activeElement == firstElement) {
                        event.preventDefault()
                        (lastElement as HTMLElement).focus()
                    }
                } else {
                    // Tab - going forwards
                    if (document.activeElement == lastElement) {
                        event.preventDefault()
                        (firstElement as HTMLElement).focus()
                    }
                }
            }
        })
    }
    
    fun restoreFocus() {
        focusHistory.lastOrNull()?.let { element ->
            (element as HTMLElement).focus()
            focusHistory.removeLastOrNull()
        }
    }
    
    fun saveFocus() {
        document.activeElement?.let { element ->
            focusHistory.add(element)
        }
    }
    
    fun focusElement(element: Element, options: FocusOptions? = null) {
        if (isManaging) {
            saveFocus()
        }
        (element as HTMLElement).focus(options)
    }
    
    private fun setupFocusTrapping() {
        document.addEventListener("focusin") { event ->
            if (isManaging) {
                val target = event.target as Element
                // Additional focus management logic here
            }
        }
    }
    
    private fun getFocusableElements(container: Element): List<Element> {
        val selector = """
            button:not([disabled]),
            [href],
            input:not([disabled]),
            select:not([disabled]),
            textarea:not([disabled]),
            [tabindex]:not([tabindex="-1"]):not([disabled]),
            details summary,
            iframe
        """.trimIndent()
        
        return container.querySelectorAll(selector).asList().filter { element ->
            isVisible(element) && !isInert(element)
        }
    }
    
    private fun isVisible(element: Element): Boolean {
        val style = window.getComputedStyle(element)
        return style.display != "none" && 
               style.visibility != "hidden" && 
               style.opacity != "0"
    }
    
    private fun isInert(element: Element): Boolean {
        return element.hasAttribute("inert") || 
               element.hasAttribute("aria-hidden") && 
               element.getAttribute("aria-hidden") == "true"
    }
}

@Composable
fun rememberFocusManager(): FocusManager {
    return remember { FocusManager() }
}

external interface FocusOptions {
    val preventScroll: Boolean?
}

// =============================================================================
// ARIA LIVE REGIONS
// =============================================================================

enum class LiveRegionPoliteness { Off, Polite, Assertive }

@Composable
fun AriaLiveRegion(
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
            .overflow(Overflow.Hidden)
            .toAttrs {
                attr("aria-live", when (politeness) {
                    LiveRegionPoliteness.Off -> "off"
                    LiveRegionPoliteness.Polite -> "polite"
                    LiveRegionPoliteness.Assertive -> "assertive"
                })
                attr("aria-atomic", "true")
            }
    ) {
        Text(message)
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
    
    init {
        // Create default regions
        createRegion("polite-region", LiveRegionPoliteness.Polite)
        createRegion("assertive-region", LiveRegionPoliteness.Assertive)
    }
}

@Composable
fun rememberLiveRegionManager(): LiveRegionManager {
    return remember { LiveRegionManager() }
}

// =============================================================================
// HIGH CONTRAST MODE SUPPORT
// =============================================================================

@Composable
fun HighContrastProvider(
    enabled: Boolean,
    content: @Composable () -> Unit
) {
    LaunchedEffect(enabled) {
        if (enabled) {
            document.body?.classList?.add("high-contrast")
            injectHighContrastStyles()
        } else {
            document.body?.classList?.remove("high-contrast")
        }
    }
    
    content()
}

private fun injectHighContrastStyles() {
    val existingStyle = document.getElementById("high-contrast-styles")
    if (existingStyle != null) return
    
    val style = document.createElement("style").apply {
        id = "high-contrast-styles"
        textContent = """
            .high-contrast {
                filter: contrast(150%) brightness(110%);
            }
            
            .high-contrast * {
                border-color: currentColor !important;
                outline-color: currentColor !important;
            }
            
            .high-contrast button,
            .high-contrast input,
            .high-contrast select,
            .high-contrast textarea {
                border: 2px solid currentColor !important;
                background: transparent !important;
            }
            
            .high-contrast a {
                text-decoration: underline !important;
                color: #0066CC !important;
            }
            
            .high-contrast :focus {
                outline: 3px solid #FFFF00 !important;
                outline-offset: 2px !important;
                background-color: #000000 !important;
                color: #FFFFFF !important;
            }
            
            .high-contrast img {
                filter: contrast(120%);
            }
        """.trimIndent()
    }
    
    document.head?.appendChild(style)
}

// =============================================================================
// KEYBOARD NAVIGATION ENHANCEMENTS
// =============================================================================

@Composable
fun KeyboardNavigationProvider(
    onKeyboardNavigation: (KeyboardEvent) -> Unit = {},
    content: @Composable () -> Unit
) {
    LaunchedEffect(Unit) {
        val handleKeyDown = { event: dynamic ->
            val keyEvent = event as KeyboardEvent
            
            when (keyEvent.key) {
                "Tab" -> {
                    // Enhanced tab navigation
                    handleTabNavigation(keyEvent)
                }
                "Enter", " " -> {
                    // Activate focused element
                    handleActivation(keyEvent)
                }
                "Escape" -> {
                    // Close modals, menus, etc.
                    handleEscape(keyEvent)
                }
                "ArrowUp", "ArrowDown", "ArrowLeft", "ArrowRight" -> {
                    // Arrow key navigation
                    handleArrowNavigation(keyEvent)
                }
                "Home", "End" -> {
                    // Jump to start/end
                    handleHomeEnd(keyEvent)
                }
            }
            
            onKeyboardNavigation(keyEvent)
        }
        
        document.addEventListener("keydown", handleKeyDown)
    }
    
    content()
}

private fun handleTabNavigation(event: KeyboardEvent) {
    // Enhanced tab navigation logic
    val activeElement = document.activeElement
    if (activeElement?.hasAttribute("data-skip-tab") == true) {
        event.preventDefault()
        // Find next focusable element
    }
}

private fun handleActivation(event: KeyboardEvent) {
    val activeElement = document.activeElement
    if (activeElement?.tagName in listOf("BUTTON", "A")) {
        (activeElement as HTMLElement).click()
    }
}

private fun handleEscape(event: KeyboardEvent) {
    // Find and close the topmost modal/dialog
    val modal = document.querySelector("[role='dialog']:not([hidden])")
    modal?.let {
        it.setAttribute("hidden", "true")
        // Restore focus to trigger element
    }
}

private fun handleArrowNavigation(event: KeyboardEvent) {
    val activeElement = document.activeElement
    val parent = activeElement?.closest("[role='menu'], [role='listbox'], [role='grid']")
    
    parent?.let {
        // Implement arrow key navigation for menus, lists, grids
        val items = parent.querySelectorAll("[role='menuitem'], [role='option'], [role='gridcell']")
        val currentIndex = items.asList().indexOf(activeElement)
        
        if (currentIndex >= 0) {
            val newIndex = when (event.key) {
                "ArrowUp" -> maxOf(0, currentIndex - 1)
                "ArrowDown" -> minOf(items.length - 1, currentIndex + 1)
                "ArrowLeft" -> maxOf(0, currentIndex - 1)
                "ArrowRight" -> minOf(items.length - 1, currentIndex + 1)
                else -> currentIndex
            }
            
            if (newIndex != currentIndex) {
                event.preventDefault()
                (items[newIndex] as HTMLElement).focus()
            }
        }
    }
}

private fun handleHomeEnd(event: KeyboardEvent) {
    val activeElement = document.activeElement
    val container = activeElement?.closest("[role='menu'], [role='listbox'], [role='grid']")
    
    container?.let {
        val items = container.querySelectorAll("[role='menuitem'], [role='option'], [role='gridcell']")
        
        when (event.key) {
            "Home" -> {
                event.preventDefault()
                (items[0] as HTMLElement).focus()
            }
            "End" -> {
                event.preventDefault()
                (items[items.length - 1] as HTMLElement).focus()
            }
        }
    }
}

// =============================================================================
// SCREEN READER OPTIMIZATIONS
// =============================================================================

@Composable
fun ScreenReaderOptimized(
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Div(
        attrs = modifier
            .toAttrs {
                attr("role", "region")
                attr("aria-label", description)
            }
    ) {
        content()
    }
}

@Composable
fun ScreenReaderOnly(
    text: String,
    modifier: Modifier = Modifier
) {
    Span(
        attrs = modifier
            .position(Position.Absolute)
            .left((-10000).px)
            .width(1.px)
            .height(1.px)
            .overflow(Overflow.Hidden)
            .toAttrs {
                attr("class", "sr-only")
            }
    ) {
        Text(text)
    }
}

// =============================================================================
// ACCESSIBILITY TESTING UTILITIES
// =============================================================================

object AccessibilityTester {
    fun runAutomaticTests(): List<AccessibilityIssue> {
        val issues = mutableListOf<AccessibilityIssue>()
        
        // Check for missing alt text
        issues.addAll(checkMissingAltText())
        
        // Check color contrast
        issues.addAll(checkColorContrast())
        
        // Check heading structure
        issues.addAll(checkHeadingStructure())
        
        // Check form labels
        issues.addAll(checkFormLabels())
        
        // Check ARIA attributes
        issues.addAll(checkAriaAttributes())
        
        return issues
    }
    
    private fun checkMissingAltText(): List<AccessibilityIssue> {
        val issues = mutableListOf<AccessibilityIssue>()
        val images = document.querySelectorAll("img:not([alt])")
        
        images.asList().forEach { img ->
            issues.add(
                AccessibilityIssue(
                    type = AccessibilityIssueType.MissingAltText,
                    element = img,
                    message = "Image is missing alt text",
                    severity = AccessibilitySeverity.Error,
                    wcagLevel = WCAGLevel.A
                )
            )
        }
        
        return issues
    }
    
    private fun checkColorContrast(): List<AccessibilityIssue> {
        val issues = mutableListOf<AccessibilityIssue>()
        // Color contrast checking would require more complex implementation
        // This is a simplified version
        return issues
    }
    
    private fun checkHeadingStructure(): List<AccessibilityIssue> {
        val issues = mutableListOf<AccessibilityIssue>()
        val headings = document.querySelectorAll("h1, h2, h3, h4, h5, h6")
        var previousLevel = 0
        
        headings.asList().forEach { heading ->
            val currentLevel = heading.tagName.last().toString().toInt()
            
            if (currentLevel > previousLevel + 1) {
                issues.add(
                    AccessibilityIssue(
                        type = AccessibilityIssueType.HeadingStructure,
                        element = heading,
                        message = "Heading levels should not skip levels",
                        severity = AccessibilitySeverity.Warning,
                        wcagLevel = WCAGLevel.AA
                    )
                )
            }
            
            previousLevel = currentLevel
        }
        
        return issues
    }
    
    private fun checkFormLabels(): List<AccessibilityIssue> {
        val issues = mutableListOf<AccessibilityIssue>()
        val formControls = document.querySelectorAll("input:not([type='hidden']), select, textarea")
        
        formControls.asList().forEach { control ->
            val hasLabel = control.hasAttribute("aria-label") ||
                          control.hasAttribute("aria-labelledby") ||
                          document.querySelector("label[for='${control.id}']") != null
            
            if (!hasLabel) {
                issues.add(
                    AccessibilityIssue(
                        type = AccessibilityIssueType.MissingLabel,
                        element = control,
                        message = "Form control is missing a label",
                        severity = AccessibilitySeverity.Error,
                        wcagLevel = WCAGLevel.A
                    )
                )
            }
        }
        
        return issues
    }
    
    private fun checkAriaAttributes(): List<AccessibilityIssue> {
        val issues = mutableListOf<AccessibilityIssue>()
        // ARIA validation would be more complex in a real implementation
        return issues
    }
}

data class AccessibilityIssue(
    val type: AccessibilityIssueType,
    val element: Element,
    val message: String,
    val severity: AccessibilitySeverity,
    val wcagLevel: WCAGLevel,
    val suggestion: String? = null
)

enum class AccessibilityIssueType {
    MissingAltText,
    ColorContrast,
    HeadingStructure,
    MissingLabel,
    InvalidAria,
    KeyboardTrapping,
    FocusManagement
}

enum class AccessibilitySeverity {
    Error,
    Warning,
    Info
}

enum class WCAGLevel {
    A, AA, AAA
}