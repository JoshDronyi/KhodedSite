package com.probro.khoded.testing

import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.w3c.dom.*
import kotlin.js.Date

/**
 * Cross-Browser Testing Utilities
 * 
 * Comprehensive cross-browser testing framework with:
 * - Browser detection and capability testing
 * - Feature compatibility testing
 * - Performance testing across browsers
 * - CSS and layout testing
 * - JavaScript API compatibility
 * - Automated browser testing scenarios
 */

// =============================================================================
// BROWSER DETECTION AND CAPABILITIES
// =============================================================================

data class BrowserInfo(
    val name: String,
    val version: String,
    val engine: String,
    val platform: String,
    val mobile: Boolean,
    val userAgent: String
)

data class BrowserCapabilities(
    val css: CSSCapabilities,
    val javascript: JSCapabilities,
    val html: HTMLCapabilities,
    val webAPIs: WebAPICapabilities,
    val performance: PerformanceCapabilities
)

data class CSSCapabilities(
    val grid: Boolean,
    val flexbox: Boolean,
    val customProperties: Boolean,
    val animations: Boolean,
    val transforms: Boolean,
    val filters: Boolean,
    val clipPath: Boolean,
    val backdropFilter: Boolean
)

data class JSCapabilities(
    val es6Modules: Boolean,
    val asyncAwait: Boolean,
    val arrow: Boolean,
    val classes: Boolean,
    val destructuring: Boolean,
    val templateLiterals: Boolean,
    val promises: Boolean,
    val fetch: Boolean
)

data class HTMLCapabilities(
    val html5: Boolean,
    val semanticElements: Boolean,
    val customElements: Boolean,
    val shadowDOM: Boolean,
    val webComponents: Boolean
)

data class WebAPICapabilities(
    val serviceWorker: Boolean,
    val webWorkers: Boolean,
    val localStorage: Boolean,
    val sessionStorage: Boolean,
    val indexedDB: Boolean,
    val webGL: Boolean,
    val canvas: Boolean,
    val geolocation: Boolean,
    val notifications: Boolean,
    val fullscreen: Boolean,
    val intersectionObserver: Boolean,
    val mutationObserver: Boolean
)

data class PerformanceCapabilities(
    val performanceAPI: Boolean,
    val navigationTiming: Boolean,
    val resourceTiming: Boolean,
    val userTiming: Boolean,
    val paintTiming: Boolean
)

object BrowserDetector {
    fun detectBrowser(): BrowserInfo {
        val userAgent = window.navigator.userAgent
        val platform = window.navigator.platform
        
        return when {
            userAgent.contains("Chrome") && !userAgent.contains("Edge") -> {
                BrowserInfo(
                    name = "Chrome",
                    version = extractVersion(userAgent, "Chrome/"),
                    engine = "Blink",
                    platform = platform,
                    mobile = isMobile(userAgent),
                    userAgent = userAgent
                )
            }
            userAgent.contains("Firefox") -> {
                BrowserInfo(
                    name = "Firefox",
                    version = extractVersion(userAgent, "Firefox/"),
                    engine = "Gecko", 
                    platform = platform,
                    mobile = isMobile(userAgent),
                    userAgent = userAgent
                )
            }
            userAgent.contains("Safari") && !userAgent.contains("Chrome") -> {
                BrowserInfo(
                    name = "Safari",
                    version = extractVersion(userAgent, "Version/"),
                    engine = "WebKit",
                    platform = platform,
                    mobile = isMobile(userAgent),
                    userAgent = userAgent
                )
            }
            userAgent.contains("Edge") -> {
                BrowserInfo(
                    name = "Edge",
                    version = extractVersion(userAgent, "Edge/"),
                    engine = "EdgeHTML",
                    platform = platform,
                    mobile = isMobile(userAgent),
                    userAgent = userAgent
                )
            }
            else -> {
                BrowserInfo(
                    name = "Unknown",
                    version = "Unknown",
                    engine = "Unknown",
                    platform = platform,
                    mobile = isMobile(userAgent),
                    userAgent = userAgent
                )
            }
        }
    }
    
    fun detectCapabilities(): BrowserCapabilities {
        return BrowserCapabilities(
            css = detectCSSCapabilities(),
            javascript = detectJSCapabilities(),
            html = detectHTMLCapabilities(),
            webAPIs = detectWebAPICapabilities(),
            performance = detectPerformanceCapabilities()
        )
    }
    
    private fun extractVersion(userAgent: String, prefix: String): String {
        val startIndex = userAgent.indexOf(prefix)
        if (startIndex == -1) return "Unknown"
        
        val versionStart = startIndex + prefix.length
        val versionEnd = userAgent.indexOf(' ', versionStart).let { 
            if (it == -1) userAgent.length else it 
        }
        
        return userAgent.substring(versionStart, versionEnd)
    }
    
    private fun isMobile(userAgent: String): Boolean {
        val mobileKeywords = listOf("Mobile", "Android", "iPhone", "iPad", "Windows Phone")
        return mobileKeywords.any { userAgent.contains(it) }
    }
    
    private fun detectCSSCapabilities(): CSSCapabilities {
        return CSSCapabilities(
            grid = supportsCSSFeature("display", "grid"),
            flexbox = supportsCSSFeature("display", "flex"),
            customProperties = supportsCSSCustomProperties(),
            animations = supportsCSSFeature("animation-name", "test"),
            transforms = supportsCSSFeature("transform", "translateX(0)"),
            filters = supportsCSSFeature("filter", "blur(1px)"),
            clipPath = supportsCSSFeature("clip-path", "circle()"),
            backdropFilter = supportsCSSFeature("backdrop-filter", "blur(1px)")
        )
    }
    
    private fun detectJSCapabilities(): JSCapabilities {
        return JSCapabilities(
            es6Modules = supportsES6Modules(),
            asyncAwait = supportsAsyncAwait(),
            arrow = supportsArrowFunctions(),
            classes = supportsClasses(),
            destructuring = supportsDestructuring(),
            templateLiterals = supportsTemplateLiterals(),
            promises = supportsPromises(),
            fetch = supportsFetch()
        )
    }
    
    private fun detectHTMLCapabilities(): HTMLCapabilities {
        return HTMLCapabilities(
            html5 = supportsHTML5(),
            semanticElements = supportsSemanticElements(),
            customElements = supportsCustomElements(),
            shadowDOM = supportsShadowDOM(),
            webComponents = supportsWebComponents()
        )
    }
    
    private fun detectWebAPICapabilities(): WebAPICapabilities {
        return WebAPICapabilities(
            serviceWorker = supportsServiceWorker(),
            webWorkers = supportsWebWorkers(),
            localStorage = supportsLocalStorage(),
            sessionStorage = supportsSessionStorage(),
            indexedDB = supportsIndexedDB(),
            webGL = supportsWebGL(),
            canvas = supportsCanvas(),
            geolocation = supportsGeolocation(),
            notifications = supportsNotifications(),
            fullscreen = supportsFullscreen(),
            intersectionObserver = supportsIntersectionObserver(),
            mutationObserver = supportsMutationObserver()
        )
    }
    
    private fun detectPerformanceCapabilities(): PerformanceCapabilities {
        return PerformanceCapabilities(
            performanceAPI = supportsPerformanceAPI(),
            navigationTiming = supportsNavigationTiming(),
            resourceTiming = supportsResourceTiming(),
            userTiming = supportsUserTiming(),
            paintTiming = supportsPaintTiming()
        )
    }
    
    // CSS capability detection helpers
    private fun supportsCSSFeature(property: String, value: String): Boolean {
        return try {
            val testElement = kotlinx.browser.document.createElement("div")
            testElement.setAttribute("style", "$property: $value")
            val computedStyle = window.getComputedStyle(testElement)
            computedStyle.getPropertyValue(property) == value
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsCSSCustomProperties(): Boolean {
        return try {
            val testElement = kotlinx.browser.document.createElement("div")
            testElement.setAttribute("style", "--test-var: 1; color: var(--test-var)")
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // JavaScript capability detection helpers
    private fun supportsES6Modules(): Boolean {
        return try {
            js("typeof import === 'function'") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsAsyncAwait(): Boolean {
        return try {
            js("(async function() {})") != null
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsArrowFunctions(): Boolean {
        return try {
            js("(() => {})") != null
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsClasses(): Boolean {
        return try {
            js("class TestClass {}") != null
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsDestructuring(): Boolean {
        return try {
            js("const [a] = [1]") != null
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsTemplateLiterals(): Boolean {
        return try {
            js("`template`") != null
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsPromises(): Boolean {
        return try {
            js("typeof Promise !== 'undefined'") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsFetch(): Boolean {
        return try {
            js("typeof fetch !== 'undefined'") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    // HTML capability detection helpers
    private fun supportsHTML5(): Boolean {
        return try {
            js("!!document.createElement('canvas').getContext") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsSemanticElements(): Boolean {
        return try {
            val section = kotlinx.browser.document.createElement("section")
            section.tagName == "SECTION"
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsCustomElements(): Boolean {
        return try {
            js("typeof customElements !== 'undefined'") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsShadowDOM(): Boolean {
        return try {
            js("!!Element.prototype.attachShadow") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsWebComponents(): Boolean {
        return supportsCustomElements() && supportsShadowDOM()
    }
    
    // Web API capability detection helpers
    private fun supportsServiceWorker(): Boolean {
        return try {
            js("'serviceWorker' in navigator") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsWebWorkers(): Boolean {
        return try {
            js("typeof Worker !== 'undefined'") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsLocalStorage(): Boolean {
        return try {
            js("typeof Storage !== 'undefined' && 'localStorage' in window") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsSessionStorage(): Boolean {
        return try {
            js("typeof Storage !== 'undefined' && 'sessionStorage' in window") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsIndexedDB(): Boolean {
        return try {
            js("'indexedDB' in window") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsWebGL(): Boolean {
        return try {
            val canvas = kotlinx.browser.document.createElement("canvas") as HTMLCanvasElement
            canvas.getContext("webgl") != null || canvas.getContext("experimental-webgl") != null
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsCanvas(): Boolean {
        return try {
            js("!!document.createElement('canvas').getContext") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsGeolocation(): Boolean {
        return try {
            js("'geolocation' in navigator") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsNotifications(): Boolean {
        return try {
            js("'Notification' in window") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsFullscreen(): Boolean {
        return try {
            js("document.fullscreenEnabled || document.webkitFullscreenEnabled || document.mozFullScreenEnabled") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsIntersectionObserver(): Boolean {
        return try {
            js("'IntersectionObserver' in window") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsMutationObserver(): Boolean {
        return try {
            js("'MutationObserver' in window") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    // Performance API capability detection helpers
    private fun supportsPerformanceAPI(): Boolean {
        return try {
            js("'performance' in window") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsNavigationTiming(): Boolean {
        return try {
            js("'performance' in window && 'timing' in performance") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsResourceTiming(): Boolean {
        return try {
            js("'performance' in window && 'getEntriesByType' in performance") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsUserTiming(): Boolean {
        return try {
            js("'performance' in window && 'mark' in performance") as Boolean
        } catch (e: Exception) {
            false
        }
    }
    
    private fun supportsPaintTiming(): Boolean {
        return try {
            js("'PerformancePaintTiming' in window") as Boolean
        } catch (e: Exception) {
            false
        }
    }
}

// =============================================================================
// CROSS-BROWSER TESTING FRAMEWORK
// =============================================================================

class CrossBrowserTestSuite {
    private val browserInfo = BrowserDetector.detectBrowser()
    private val capabilities = BrowserDetector.detectCapabilities()
    private val results = mutableListOf<CrossBrowserTestResult>()
    
    suspend fun runCompatibilityTests(): CrossBrowserTestReport {
        val startTime = Date().getTime()
        
        // Run feature compatibility tests
        testCSSCompatibility()
        testJavaScriptCompatibility()
        testWebAPICompatibility()
        testPerformanceCompatibility()
        testLayoutCompatibility()
        
        val endTime = Date().getTime()
        
        return CrossBrowserTestReport(
            browserInfo = browserInfo,
            capabilities = capabilities,
            totalTests = results.size,
            passedTests = results.count { it.passed },
            failedTests = results.count { !it.passed },
            duration = endTime - startTime,
            results = results.toList()
        )
    }
    
    private fun testCSSCompatibility() {
        // Grid Layout Test
        addTestResult(
            "CSS Grid Layout",
            capabilities.css.grid,
            if (capabilities.css.grid) "Full support" else "Not supported - will fallback to flexbox"
        )
        
        // Flexbox Test
        addTestResult(
            "CSS Flexbox",
            capabilities.css.flexbox,
            if (capabilities.css.flexbox) "Full support" else "Not supported - will fallback to float layouts"
        )
        
        // Custom Properties Test
        addTestResult(
            "CSS Custom Properties",
            capabilities.css.customProperties,
            if (capabilities.css.customProperties) "Full support" else "Not supported - will use static values"
        )
        
        // Animations Test
        addTestResult(
            "CSS Animations",
            capabilities.css.animations,
            if (capabilities.css.animations) "Full support" else "Not supported - will show static content"
        )
        
        // Modern CSS Features
        addTestResult(
            "CSS Transforms",
            capabilities.css.transforms,
            if (capabilities.css.transforms) "Full support" else "Limited transform support"
        )
        
        addTestResult(
            "CSS Filters",
            capabilities.css.filters,
            if (capabilities.css.filters) "Full support" else "Filter effects not available"
        )
    }
    
    private fun testJavaScriptCompatibility() {
        // ES6+ Features
        addTestResult(
            "ES6 Modules",
            capabilities.javascript.es6Modules,
            if (capabilities.javascript.es6Modules) "Native module support" else "Will use bundled modules"
        )
        
        addTestResult(
            "Async/Await",
            capabilities.javascript.asyncAwait,
            if (capabilities.javascript.asyncAwait) "Native async/await support" else "Will use Promise chains"
        )
        
        addTestResult(
            "Arrow Functions",
            capabilities.javascript.arrow,
            if (capabilities.javascript.arrow) "Arrow function support" else "Will use function expressions"
        )
        
        addTestResult(
            "ES6 Classes",
            capabilities.javascript.classes,
            if (capabilities.javascript.classes) "Native class support" else "Will use constructor functions"
        )
        
        addTestResult(
            "Fetch API",
            capabilities.javascript.fetch,
            if (capabilities.javascript.fetch) "Native fetch support" else "Will use XMLHttpRequest"
        )
    }
    
    private fun testWebAPICompatibility() {
        // Storage APIs
        addTestResult(
            "Local Storage",
            capabilities.webAPIs.localStorage,
            if (capabilities.webAPIs.localStorage) "Full storage support" else "Storage not available"
        )
        
        addTestResult(
            "Session Storage",
            capabilities.webAPIs.sessionStorage,
            if (capabilities.webAPIs.sessionStorage) "Session storage available" else "No session persistence"
        )
        
        // Modern APIs
        addTestResult(
            "Intersection Observer",
            capabilities.webAPIs.intersectionObserver,
            if (capabilities.webAPIs.intersectionObserver) "Efficient scroll detection" else "Will use scroll events"
        )
        
        addTestResult(
            "Service Workers",
            capabilities.webAPIs.serviceWorker,
            if (capabilities.webAPIs.serviceWorker) "Offline support available" else "No offline capabilities"
        )
        
        addTestResult(
            "Web Workers",
            capabilities.webAPIs.webWorkers,
            if (capabilities.webAPIs.webWorkers) "Background processing available" else "Main thread processing only"
        )
        
        // Media APIs
        addTestResult(
            "Canvas API",
            capabilities.webAPIs.canvas,
            if (capabilities.webAPIs.canvas) "Canvas graphics available" else "No canvas graphics"
        )
        
        addTestResult(
            "WebGL",
            capabilities.webAPIs.webGL,
            if (capabilities.webAPIs.webGL) "Hardware-accelerated graphics" else "Software rendering only"
        )
    }
    
    private fun testPerformanceCompatibility() {
        addTestResult(
            "Performance API",
            capabilities.performance.performanceAPI,
            if (capabilities.performance.performanceAPI) "Performance monitoring available" else "Limited performance insights"
        )
        
        addTestResult(
            "User Timing",
            capabilities.performance.userTiming,
            if (capabilities.performance.userTiming) "Custom performance marks available" else "Basic timing only"
        )
        
        addTestResult(
            "Navigation Timing",
            capabilities.performance.navigationTiming,
            if (capabilities.performance.navigationTiming) "Page load metrics available" else "No navigation metrics"
        )
    }
    
    private fun testLayoutCompatibility() {
        // Test viewport handling
        val viewportTest = testViewportUnits()
        addTestResult(
            "Viewport Units (vh/vw)",
            viewportTest,
            if (viewportTest) "Responsive viewport units work" else "Fixed units only"
        )
        
        // Test responsive images
        val responsiveImagesTest = testResponsiveImages()
        addTestResult(
            "Responsive Images",
            responsiveImagesTest,
            if (responsiveImagesTest) "srcset and sizes attributes work" else "Single image resolution"
        )
        
        // Test media queries
        val mediaQueriesTest = testMediaQueries()
        addTestResult(
            "CSS Media Queries",
            mediaQueriesTest,
            if (mediaQueriesTest) "Responsive design fully supported" else "Limited responsive features"
        )
    }
    
    private fun testViewportUnits(): Boolean {
        return try {
            val testElement = kotlinx.browser.document.createElement("div")
            testElement.setAttribute("style", "height: 100vh")
            val computedStyle = window.getComputedStyle(testElement)
            computedStyle.height != "auto"
        } catch (e: Exception) {
            false
        }
    }
    
    private fun testResponsiveImages(): Boolean {
        return try {
            val img = kotlinx.browser.document.createElement("img")
            "srcset" in img
        } catch (e: Exception) {
            false
        }
    }
    
    private fun testMediaQueries(): Boolean {
        return try {
            window.matchMedia("(min-width: 768px)").matches != null
        } catch (e: Exception) {
            false
        }
    }
    
    private fun addTestResult(testName: String, passed: Boolean, message: String) {
        results.add(
            CrossBrowserTestResult(
                testName = testName,
                passed = passed,
                message = message,
                browserName = browserInfo.name,
                browserVersion = browserInfo.version
            )
        )
    }
}

data class CrossBrowserTestResult(
    val testName: String,
    val passed: Boolean,
    val message: String,
    val browserName: String,
    val browserVersion: String
)

data class CrossBrowserTestReport(
    val browserInfo: BrowserInfo,
    val capabilities: BrowserCapabilities,
    val totalTests: Int,
    val passedTests: Int,
    val failedTests: Int,
    val duration: Long,
    val results: List<CrossBrowserTestResult>
) {
    val compatibilityScore: Double
        get() = if (totalTests > 0) (passedTests.toDouble() / totalTests) * 100 else 0.0
}

// =============================================================================
// POLYFILL DETECTION AND MANAGEMENT
// =============================================================================

object PolyfillManager {
    private val requiredPolyfills = mutableListOf<PolyfillInfo>()
    
    data class PolyfillInfo(
        val name: String,
        val feature: String,
        val testFunction: () -> Boolean,
        val polyfillUrl: String,
        val priority: PolyfillPriority = PolyfillPriority.Medium
    )
    
    enum class PolyfillPriority { Critical, High, Medium, Low }
    
    fun addPolyfill(polyfill: PolyfillInfo) {
        requiredPolyfills.add(polyfill)
    }
    
    fun checkPolyfillNeeds(): List<PolyfillInfo> {
        return requiredPolyfills.filter { !it.testFunction() }
    }
    
    suspend fun loadRequiredPolyfills() {
        val neededPolyfills = checkPolyfillNeeds()
            .sortedByDescending { it.priority.ordinal }
        
        neededPolyfills.forEach { polyfill ->
            try {
                loadPolyfill(polyfill)
                console.log("Loaded polyfill: ${polyfill.name}")
            } catch (e: Exception) {
                console.warn("Failed to load polyfill: ${polyfill.name}", e)
            }
        }
    }
    
    private suspend fun loadPolyfill(polyfill: PolyfillInfo) {
        val script = kotlinx.browser.document.createElement("script") as HTMLScriptElement
        script.src = polyfill.polyfillUrl
        script.async = true
        
        return suspendCancellableCoroutine { continuation ->
            script.onload = {
                continuation.resume(Unit)
            }
            script.onerror = {
                continuation.resumeWithException(Exception("Failed to load polyfill: ${polyfill.name}"))
            }
            kotlinx.browser.document.head?.appendChild(script)
        }
    }
    
    init {
        // Add common polyfills
        addPolyfill(
            PolyfillInfo(
                name = "Fetch API",
                feature = "fetch",
                testFunction = { js("typeof fetch !== 'undefined'") as Boolean },
                polyfillUrl = "https://cdn.jsdelivr.net/npm/whatwg-fetch@3/dist/fetch.umd.js",
                priority = PolyfillPriority.Critical
            )
        )
        
        addPolyfill(
            PolyfillInfo(
                name = "Intersection Observer",
                feature = "IntersectionObserver",
                testFunction = { js("'IntersectionObserver' in window") as Boolean },
                polyfillUrl = "https://cdn.jsdelivr.net/npm/intersection-observer@0.12.0/intersection-observer.js",
                priority = PolyfillPriority.High
            )
        )
        
        addPolyfill(
            PolyfillInfo(
                name = "ResizeObserver",
                feature = "ResizeObserver",
                testFunction = { js("'ResizeObserver' in window") as Boolean },
                polyfillUrl = "https://cdn.jsdelivr.net/npm/resize-observer-polyfill@1.5.1/dist/ResizeObserver.js",
                priority = PolyfillPriority.Medium
            )
        )
    }
}

// External interfaces
external interface HTMLScriptElement : Element {
    var src: String
    var async: Boolean
    var onload: ((Event) -> Unit)?
    var onerror: ((Event) -> Unit)?
}