package com.probro.khoded.monitoring

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.performance.*
import kotlin.js.Date
import kotlin.random.Random

/**
 * Deployment & Monitoring Infrastructure
 * 
 * Comprehensive monitoring and deployment utilities including:
 * - Real-time performance monitoring
 * - Error tracking and reporting
 * - User analytics and behavior tracking
 * - Core Web Vitals measurement
 * - A/B testing framework
 * - Feature flags and progressive deployment
 * - Health checks and uptime monitoring
 * - Automated deployment metrics
 */

// =============================================================================
// PERFORMANCE MONITORING
// =============================================================================

@Serializable
data class PerformanceMetric(
    val name: String,
    val value: Double,
    val timestamp: Long,
    val url: String,
    val userAgent: String,
    val sessionId: String
)

@Serializable
data class CoreWebVitals(
    val lcp: Double?, // Largest Contentful Paint
    val fid: Double?, // First Input Delay
    val cls: Double?, // Cumulative Layout Shift
    val fcp: Double?, // First Contentful Paint
    val ttfb: Double?, // Time to First Byte
    val timestamp: Long,
    val url: String,
    val sessionId: String
)

class PerformanceMonitor {
    private val sessionId = generateSessionId()
    private val metrics = mutableListOf<PerformanceMetric>()
    private var coreWebVitals: CoreWebVitals? = null
    
    fun start() {
        setupPerformanceObservers()
        measureCoreWebVitals()
        setupNavigationTiming()
        setupResourceTiming()
    }
    
    private fun setupPerformanceObservers() {
        try {
            // Performance Observer for paint timing
            val paintObserver = js("new PerformanceObserver((list) => { this.handlePaintEntries(list.getEntries()) })")
            paintObserver.observe(js("{ entryTypes: ['paint'] }"))
            
            // Performance Observer for layout shift
            val layoutShiftObserver = js("new PerformanceObserver((list) => { this.handleLayoutShiftEntries(list.getEntries()) })")
            layoutShiftObserver.observe(js("{ entryTypes: ['layout-shift'] }"))
            
            // Performance Observer for largest contentful paint
            val lcpObserver = js("new PerformanceObserver((list) => { this.handleLCPEntries(list.getEntries()) })")
            lcpObserver.observe(js("{ entryTypes: ['largest-contentful-paint'] }"))
            
        } catch (e: Exception) {
            console.warn("Performance observers not supported", e)
        }
    }
    
    @JsName("handlePaintEntries")
    fun handlePaintEntries(entries: Array<dynamic>) {
        entries.forEach { entry ->
            when (entry.name as String) {
                "first-contentful-paint" -> {
                    recordMetric("FCP", entry.startTime as Double)
                }
                "first-paint" -> {
                    recordMetric("FP", entry.startTime as Double)
                }
            }
        }
    }
    
    @JsName("handleLayoutShiftEntries")
    fun handleLayoutShiftEntries(entries: Array<dynamic>) {
        var clsScore = 0.0
        entries.forEach { entry ->
            if (!(entry.hadRecentInput as Boolean)) {
                clsScore += entry.value as Double
            }
        }
        recordMetric("CLS", clsScore)
    }
    
    @JsName("handleLCPEntries") 
    fun handleLCPEntries(entries: Array<dynamic>) {
        entries.forEach { entry ->
            recordMetric("LCP", entry.startTime as Double)
        }
    }
    
    private fun measureCoreWebVitals() {
        val startTime = Date().getTime()
        
        // Measure Time to First Byte
        window.addEventListener("load") {
            val ttfb = try {
                val timing = window.performance.timing
                timing.responseStart - timing.requestStart
            } catch (e: Exception) {
                null
            }
            
            if (ttfb != null) {
                recordMetric("TTFB", ttfb.toDouble())
            }
        }
        
        // Setup First Input Delay measurement
        setupFIDMeasurement()
    }
    
    private fun setupFIDMeasurement() {
        var firstInputDelay: Double? = null
        var isFirstInput = true
        
        val inputTypes = arrayOf("click", "touchstart", "keydown")
        
        inputTypes.forEach { eventType ->
            document.addEventListener(eventType) { event ->
                if (isFirstInput) {
                    isFirstInput = false
                    val eventTimeStamp = (event as Event).timeStamp
                    val processingStart = window.performance.now()
                    
                    // Schedule processing to next frame to measure delay
                    window.requestAnimationFrame {
                        val processingTime = window.performance.now() - processingStart
                        recordMetric("FID", processingTime)
                    }
                }
            }
        }
    }
    
    private fun setupNavigationTiming() {
        window.addEventListener("load") {
            try {
                val timing = window.performance.timing
                
                // DNS Lookup Time
                val dnsTime = timing.domainLookupEnd - timing.domainLookupStart
                recordMetric("DNS_Time", dnsTime.toDouble())
                
                // Connection Time
                val connectionTime = timing.connectEnd - timing.connectStart
                recordMetric("Connection_Time", connectionTime.toDouble())
                
                // Server Response Time
                val serverResponseTime = timing.responseEnd - timing.requestStart
                recordMetric("Server_Response_Time", serverResponseTime.toDouble())
                
                // DOM Processing Time
                val domProcessingTime = timing.loadEventStart - timing.domLoading
                recordMetric("DOM_Processing_Time", domProcessingTime.toDouble())
                
                // Total Page Load Time
                val totalLoadTime = timing.loadEventEnd - timing.navigationStart
                recordMetric("Total_Load_Time", totalLoadTime.toDouble())
                
            } catch (e: Exception) {
                console.warn("Navigation timing not available", e)
            }
        }
    }
    
    private fun setupResourceTiming() {
        try {
            val resourceObserver = js("new PerformanceObserver((list) => { this.handleResourceEntries(list.getEntries()) })")
            resourceObserver.observe(js("{ entryTypes: ['resource'] }"))
        } catch (e: Exception) {
            console.warn("Resource timing observer not supported", e)
        }
    }
    
    @JsName("handleResourceEntries")
    fun handleResourceEntries(entries: Array<dynamic>) {
        entries.forEach { entry ->
            val resourceName = entry.name as String
            val duration = entry.duration as Double
            val resourceType = (entry.initiatorType as? String) ?: "unknown"
            
            recordMetric("Resource_Load_${resourceType}", duration)
            
            // Track slow resources
            if (duration > 1000) { // Resources taking more than 1 second
                recordMetric("Slow_Resource", duration)
                console.warn("Slow resource detected: $resourceName took ${duration}ms")
            }
        }
    }
    
    private fun recordMetric(name: String, value: Double) {
        val metric = PerformanceMetric(
            name = name,
            value = value,
            timestamp = Date().getTime().toLong(),
            url = window.location.href,
            userAgent = window.navigator.userAgent,
            sessionId = sessionId
        )
        
        metrics.add(metric)
        
        // Send to analytics if configured
        sendMetricToAnalytics(metric)
        
        console.log("Performance metric recorded: $name = $value")
    }
    
    private fun sendMetricToAnalytics(metric: PerformanceMetric) {
        try {
            // Send to Google Analytics if available
            js("""
                if (typeof gtag === 'function') {
                    gtag('event', 'performance_metric', {
                        'metric_name': metric.name,
                        'metric_value': metric.value,
                        'custom_map': {
                            'dimension1': metric.sessionId,
                            'dimension2': metric.url
                        }
                    });
                }
            """)
        } catch (e: Exception) {
            // Analytics not available, continue silently
        }
    }
    
    fun getMetrics(): List<PerformanceMetric> = metrics.toList()
    
    fun getAverageMetric(name: String): Double? {
        val filtered = metrics.filter { it.name == name }
        return if (filtered.isEmpty()) null else filtered.map { it.value }.average()
    }
    
    private fun generateSessionId(): String {
        return "session-${Date().getTime()}-${Random.nextInt(1000, 9999)}"
    }
}

// =============================================================================
// ERROR TRACKING
// =============================================================================

@Serializable
data class ErrorEvent(
    val message: String,
    val source: String,
    val lineno: Int,
    val colno: Int,
    val error: String?,
    val timestamp: Long,
    val url: String,
    val userAgent: String,
    val sessionId: String,
    val userId: String? = null,
    val breadcrumbs: List<Breadcrumb> = emptyList()
)

@Serializable 
data class Breadcrumb(
    val timestamp: Long,
    val category: String,
    val message: String,
    val level: String = "info",
    val data: Map<String, String> = emptyMap()
)

class ErrorTracker {
    private val sessionId = generateSessionId()
    private val breadcrumbs = mutableListOf<Breadcrumb>()
    private val maxBreadcrumbs = 50
    private var userId: String? = null
    
    fun start() {
        setupErrorHandlers()
        setupUnhandledPromiseRejection()
        setupConsoleInterception()
    }
    
    fun setUserId(id: String) {
        userId = id
    }
    
    fun addBreadcrumb(
        category: String,
        message: String,
        level: String = "info",
        data: Map<String, String> = emptyMap()
    ) {
        val breadcrumb = Breadcrumb(
            timestamp = Date().getTime().toLong(),
            category = category,
            message = message,
            level = level,
            data = data
        )
        
        breadcrumbs.add(breadcrumb)
        
        // Keep only recent breadcrumbs
        if (breadcrumbs.size > maxBreadcrumbs) {
            breadcrumbs.removeAt(0)
        }
    }
    
    private fun setupErrorHandlers() {
        window.onerror = { message, source, lineno, colno, error ->
            val errorEvent = ErrorEvent(
                message = message.toString(),
                source = source?.toString() ?: "",
                lineno = lineno?.toInt() ?: 0,
                colno = colno?.toInt() ?: 0,
                error = error?.toString(),
                timestamp = Date().getTime().toLong(),
                url = window.location.href,
                userAgent = window.navigator.userAgent,
                sessionId = sessionId,
                userId = userId,
                breadcrumbs = breadcrumbs.toList()
            )
            
            reportError(errorEvent)
            undefined
        }
    }
    
    private fun setupUnhandledPromiseRejection() {
        window.addEventListener("unhandledrejection") { event ->
            val rejection = event.asDynamic()
            val errorEvent = ErrorEvent(
                message = "Unhandled Promise Rejection",
                source = window.location.href,
                lineno = 0,
                colno = 0,
                error = rejection.reason?.toString(),
                timestamp = Date().getTime().toLong(),
                url = window.location.href,
                userAgent = window.navigator.userAgent,
                sessionId = sessionId,
                userId = userId,
                breadcrumbs = breadcrumbs.toList()
            )
            
            reportError(errorEvent)
        }
    }
    
    private fun setupConsoleInterception() {
        val originalConsoleError = console.asDynamic().error
        
        console.asDynamic().error = { message: dynamic ->
            addBreadcrumb(
                category = "console",
                message = message.toString(),
                level = "error"
            )
            originalConsoleError(message)
        }
        
        val originalConsoleWarn = console.asDynamic().warn
        console.asDynamic().warn = { message: dynamic ->
            addBreadcrumb(
                category = "console", 
                message = message.toString(),
                level = "warning"
            )
            originalConsoleWarn(message)
        }
    }
    
    fun reportError(errorEvent: ErrorEvent) {
        console.error("Error tracked:", errorEvent)
        
        // Send to error reporting service
        sendToErrorReportingService(errorEvent)
        
        // Add breadcrumb for this error
        addBreadcrumb(
            category = "error",
            message = errorEvent.message,
            level = "error",
            data = mapOf(
                "source" to errorEvent.source,
                "line" to errorEvent.lineno.toString(),
                "column" to errorEvent.colno.toString()
            )
        )
    }
    
    private fun sendToErrorReportingService(errorEvent: ErrorEvent) {
        try {
            // Send to your error reporting service (e.g., Sentry, LogRocket, etc.)
            val payload = Json.encodeToString(errorEvent)
            
            window.fetch("/api/errors", js("""({
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: payload
            })"""))
            
        } catch (e: Exception) {
            console.warn("Failed to send error report", e)
        }
    }
    
    private fun generateSessionId(): String {
        return "error-session-${Date().getTime()}-${Random.nextInt(1000, 9999)}"
    }
}

// =============================================================================
// USER ANALYTICS
// =============================================================================

@Serializable
data class UserEvent(
    val event: String,
    val properties: Map<String, String> = emptyMap(),
    val timestamp: Long,
    val sessionId: String,
    val userId: String? = null,
    val url: String,
    val referrer: String
)

@Serializable
data class UserSession(
    val sessionId: String,
    val userId: String? = null,
    val startTime: Long,
    val endTime: Long? = null,
    val pageViews: List<PageView> = emptyList(),
    val events: List<UserEvent> = emptyList(),
    val userAgent: String,
    val initialReferrer: String,
    val deviceInfo: DeviceInfo
)

@Serializable
data class PageView(
    val url: String,
    val title: String,
    val timestamp: Long,
    val timeOnPage: Long? = null,
    val exitPage: Boolean = false
)

@Serializable
data class DeviceInfo(
    val userAgent: String,
    val screenWidth: Int,
    val screenHeight: Int,
    val viewportWidth: Int,
    val viewportHeight: Int,
    val devicePixelRatio: Double,
    val touchSupport: Boolean,
    val language: String,
    val timezone: String
)

class UserAnalytics {
    private val sessionId = generateSessionId()
    private var userId: String? = null
    private val session = UserSession(
        sessionId = sessionId,
        startTime = Date().getTime().toLong(),
        userAgent = window.navigator.userAgent,
        initialReferrer = document.referrer,
        deviceInfo = captureDeviceInfo()
    )
    private val events = mutableListOf<UserEvent>()
    private val pageViews = mutableListOf<PageView>()
    private var currentPageView: PageView? = null
    
    fun start() {
        setupPageViewTracking()
        setupUserInteractionTracking()
        setupSessionTracking()
        trackInitialPageView()
    }
    
    fun setUserId(id: String) {
        userId = id
        trackEvent("user_identified", mapOf("user_id" to id))
    }
    
    fun trackEvent(eventName: String, properties: Map<String, String> = emptyMap()) {
        val event = UserEvent(
            event = eventName,
            properties = properties,
            timestamp = Date().getTime().toLong(),
            sessionId = sessionId,
            userId = userId,
            url = window.location.href,
            referrer = document.referrer
        )
        
        events.add(event)
        sendEventToAnalytics(event)
        
        console.log("User event tracked: $eventName", properties)
    }
    
    fun trackPageView(url: String = window.location.href, title: String = document.title) {
        // End previous page view
        currentPageView?.let { prev ->
            val timeOnPage = Date().getTime().toLong() - prev.timestamp
            val updatedPageView = prev.copy(timeOnPage = timeOnPage)
            pageViews[pageViews.size - 1] = updatedPageView
        }
        
        // Start new page view
        val pageView = PageView(
            url = url,
            title = title,
            timestamp = Date().getTime().toLong()
        )
        
        pageViews.add(pageView)
        currentPageView = pageView
        
        trackEvent("page_view", mapOf(
            "page_url" to url,
            "page_title" to title
        ))
    }
    
    private fun setupPageViewTracking() {
        // Track page changes in SPA
        var currentUrl = window.location.href
        
        setInterval({
            val newUrl = window.location.href
            if (newUrl != currentUrl) {
                currentUrl = newUrl
                trackPageView(newUrl, document.title)
            }
        }, 500)
        
        // Track page unload
        window.addEventListener("beforeunload") {
            currentPageView?.let { pageView ->
                val timeOnPage = Date().getTime().toLong() - pageView.timestamp
                val finalPageView = pageView.copy(
                    timeOnPage = timeOnPage,
                    exitPage = true
                )
                pageViews[pageViews.size - 1] = finalPageView
                endSession()
            }
        }
    }
    
    private fun setupUserInteractionTracking() {
        // Track clicks on important elements
        document.addEventListener("click") { event ->
            val target = event.target as? Element
            if (target != null) {
                val tagName = target.tagName.lowercase()
                val elementId = target.id
                val className = target.className
                
                when {
                    tagName == "a" -> trackEvent("link_click", mapOf(
                        "link_url" to (target.getAttribute("href") ?: ""),
                        "link_text" to (target.textContent ?: "")
                    ))
                    tagName == "button" -> trackEvent("button_click", mapOf(
                        "button_id" to elementId,
                        "button_text" to (target.textContent ?: "")
                    ))
                    className.contains("cta") -> trackEvent("cta_click", mapOf(
                        "cta_id" to elementId,
                        "cta_class" to className
                    ))
                }
            }
        }
        
        // Track form submissions
        document.addEventListener("submit") { event ->
            val form = event.target as? HTMLFormElement
            form?.let {
                trackEvent("form_submit", mapOf(
                    "form_id" to (it.id),
                    "form_action" to (it.action)
                ))
            }
        }
        
        // Track scroll depth
        setupScrollTracking()
    }
    
    private fun setupScrollTracking() {
        var maxScroll = 0
        val scrollMilestones = listOf(25, 50, 75, 90, 100)
        val trackedMilestones = mutableSetOf<Int>()
        
        window.addEventListener("scroll") {
            val scrollTop = window.pageYOffset
            val documentHeight = document.documentElement.scrollHeight
            val windowHeight = window.innerHeight
            val scrollPercent = ((scrollTop / (documentHeight - windowHeight)) * 100).toInt()
            
            if (scrollPercent > maxScroll) {
                maxScroll = scrollPercent
                
                scrollMilestones.forEach { milestone ->
                    if (scrollPercent >= milestone && !trackedMilestones.contains(milestone)) {
                        trackedMilestones.add(milestone)
                        trackEvent("scroll_depth", mapOf(
                            "depth_percent" to milestone.toString()
                        ))
                    }
                }
            }
        }
    }
    
    private fun setupSessionTracking() {
        // Send session data periodically
        setInterval({
            sendSessionData()
        }, 30000) // Every 30 seconds
        
        // Track user engagement
        var lastActivity = Date().getTime()
        val activityEvents = arrayOf("click", "scroll", "keypress", "mousemove")
        
        activityEvents.forEach { eventType ->
            document.addEventListener(eventType) {
                lastActivity = Date().getTime()
            }
        }
        
        // Check for inactivity
        setInterval({
            val now = Date().getTime()
            val inactiveTime = now - lastActivity
            
            if (inactiveTime > 300000) { // 5 minutes of inactivity
                trackEvent("session_inactive", mapOf(
                    "inactive_duration" to inactiveTime.toString()
                ))
            }
        }, 60000) // Check every minute
    }
    
    private fun trackInitialPageView() {
        trackPageView()
    }
    
    private fun captureDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            userAgent = window.navigator.userAgent,
            screenWidth = window.screen.width,
            screenHeight = window.screen.height,
            viewportWidth = window.innerWidth,
            viewportHeight = window.innerHeight,
            devicePixelRatio = window.devicePixelRatio,
            touchSupport = js("'ontouchstart' in window") as Boolean,
            language = window.navigator.language,
            timezone = js("Intl.DateTimeFormat().resolvedOptions().timeZone") as String
        )
    }
    
    private fun sendEventToAnalytics(event: UserEvent) {
        try {
            // Send to Google Analytics
            js("""
                if (typeof gtag === 'function') {
                    gtag('event', event.event, {
                        'event_category': 'user_interaction',
                        'event_label': event.url,
                        'custom_map': event.properties
                    });
                }
            """)
            
            // Send to your analytics endpoint
            val payload = Json.encodeToString(event)
            window.fetch("/api/analytics/events", js("""({
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: payload
            })"""))
            
        } catch (e: Exception) {
            console.warn("Failed to send analytics event", e)
        }
    }
    
    private fun sendSessionData() {
        try {
            val sessionData = session.copy(
                endTime = Date().getTime().toLong(),
                pageViews = pageViews.toList(),
                events = events.toList(),
                userId = userId
            )
            
            val payload = Json.encodeToString(sessionData)
            window.fetch("/api/analytics/sessions", js("""({
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: payload
            })"""))
            
        } catch (e: Exception) {
            console.warn("Failed to send session data", e)
        }
    }
    
    private fun endSession() {
        sendSessionData()
        trackEvent("session_end")
    }
    
    private fun generateSessionId(): String {
        return "analytics-${Date().getTime()}-${Random.nextInt(10000, 99999)}"
    }
}

// =============================================================================
// A/B TESTING FRAMEWORK
// =============================================================================

@Serializable
data class ABTest(
    val testId: String,
    val name: String,
    val description: String,
    val variants: List<ABVariant>,
    val trafficAllocation: Double = 1.0, // Percentage of users to include
    val startDate: Long,
    val endDate: Long?,
    val active: Boolean = true
)

@Serializable
data class ABVariant(
    val variantId: String,
    val name: String,
    val description: String,
    val weight: Double, // Percentage split
    val config: Map<String, String> = emptyMap()
)

@Serializable
data class ABTestAssignment(
    val testId: String,
    val variantId: String,
    val userId: String?,
    val sessionId: String,
    val timestamp: Long
)

class ABTestingFramework {
    private val assignments = mutableMapOf<String, ABTestAssignment>()
    private val activeTests = mutableMapOf<String, ABTest>()
    private val sessionId = generateSessionId()
    private var userId: String? = null
    
    fun setUserId(id: String) {
        userId = id
    }
    
    fun addTest(test: ABTest) {
        activeTests[test.testId] = test
    }
    
    fun getVariant(testId: String): String? {
        val test = activeTests[testId] ?: return null
        
        // Check if user is already assigned
        assignments[testId]?.let { return it.variantId }
        
        // Check if test is active and within date range
        val now = Date().getTime().toLong()
        if (!test.active || now < test.startDate || (test.endDate != null && now > test.endDate)) {
            return null
        }
        
        // Check traffic allocation
        if (Random.nextDouble() > test.trafficAllocation) {
            return null
        }
        
        // Assign variant based on weights
        val randomValue = Random.nextDouble()
        var cumulativeWeight = 0.0
        
        for (variant in test.variants) {
            cumulativeWeight += variant.weight
            if (randomValue <= cumulativeWeight) {
                val assignment = ABTestAssignment(
                    testId = testId,
                    variantId = variant.variantId,
                    userId = userId,
                    sessionId = sessionId,
                    timestamp = now
                )
                
                assignments[testId] = assignment
                trackABTestAssignment(assignment)
                return variant.variantId
            }
        }
        
        // Fallback to control (first variant)
        return test.variants.firstOrNull()?.variantId
    }
    
    fun getVariantConfig(testId: String): Map<String, String>? {
        val variantId = getVariant(testId) ?: return null
        val test = activeTests[testId] ?: return null
        return test.variants.find { it.variantId == variantId }?.config
    }
    
    fun trackConversion(testId: String, conversionEvent: String, value: Double = 0.0) {
        val assignment = assignments[testId] ?: return
        
        try {
            val conversionData = mapOf(
                "test_id" to testId,
                "variant_id" to assignment.variantId,
                "conversion_event" to conversionEvent,
                "conversion_value" to value.toString(),
                "user_id" to (userId ?: ""),
                "session_id" to sessionId
            )
            
            // Track conversion event
            val payload = Json.encodeToString(conversionData)
            window.fetch("/api/ab-testing/conversions", js("""({
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: payload
            })"""))
            
            console.log("A/B test conversion tracked: $testId -> $conversionEvent")
            
        } catch (e: Exception) {
            console.warn("Failed to track A/B test conversion", e)
        }
    }
    
    private fun trackABTestAssignment(assignment: ABTestAssignment) {
        try {
            val payload = Json.encodeToString(assignment)
            window.fetch("/api/ab-testing/assignments", js("""({
                method: 'POST', 
                headers: { 'Content-Type': 'application/json' },
                body: payload
            })"""))
            
            console.log("A/B test assignment tracked: ${assignment.testId} -> ${assignment.variantId}")
            
        } catch (e: Exception) {
            console.warn("Failed to track A/B test assignment", e)
        }
    }
    
    private fun generateSessionId(): String {
        return "ab-${Date().getTime()}-${Random.nextInt(1000, 9999)}"
    }
}

// =============================================================================
// FEATURE FLAGS
// =============================================================================

@Serializable
data class FeatureFlag(
    val flagId: String,
    val name: String,
    val description: String,
    val enabled: Boolean,
    val rolloutPercentage: Double = 100.0,
    val conditions: List<FeatureCondition> = emptyList(),
    val variants: Map<String, Boolean> = emptyMap()
)

@Serializable
data class FeatureCondition(
    val type: String, // "user_id", "session_id", "user_agent", "geo", etc.
    val operator: String, // "equals", "contains", "starts_with", "in", etc.
    val value: String
)

class FeatureFlagManager {
    private val flags = mutableMapOf<String, FeatureFlag>()
    private var userId: String? = null
    private val sessionId = generateSessionId()
    
    fun setUserId(id: String) {
        userId = id
    }
    
    fun addFlag(flag: FeatureFlag) {
        flags[flag.flagId] = flag
    }
    
    fun loadFlags() {
        try {
            window.fetch("/api/feature-flags")
                .then { response -> response.json() }
                .then { data ->
                    val flagsData = data as Array<dynamic>
                    flagsData.forEach { flagData ->
                        val flag = Json.decodeFromString<FeatureFlag>(
                            JSON.stringify(flagData)
                        )
                        flags[flag.flagId] = flag
                    }
                }
        } catch (e: Exception) {
            console.warn("Failed to load feature flags", e)
        }
    }
    
    fun isEnabled(flagId: String): Boolean {
        val flag = flags[flagId] ?: return false
        
        // Check if flag is globally disabled
        if (!flag.enabled) return false
        
        // Check conditions
        if (!checkConditions(flag.conditions)) return false
        
        // Check rollout percentage
        val userHash = generateUserHash(userId ?: sessionId)
        val rolloutThreshold = flag.rolloutPercentage / 100.0
        
        return (userHash % 1.0) <= rolloutThreshold
    }
    
    fun getVariant(flagId: String, variantName: String): Boolean {
        if (!isEnabled(flagId)) return false
        
        val flag = flags[flagId] ?: return false
        return flag.variants[variantName] ?: false
    }
    
    private fun checkConditions(conditions: List<FeatureCondition>): Boolean {
        return conditions.all { condition ->
            when (condition.type) {
                "user_id" -> checkStringCondition(userId ?: "", condition)
                "session_id" -> checkStringCondition(sessionId, condition)
                "user_agent" -> checkStringCondition(window.navigator.userAgent, condition)
                "url" -> checkStringCondition(window.location.href, condition)
                else -> true // Unknown condition types default to true
            }
        }
    }
    
    private fun checkStringCondition(value: String, condition: FeatureCondition): Boolean {
        return when (condition.operator) {
            "equals" -> value == condition.value
            "contains" -> value.contains(condition.value)
            "starts_with" -> value.startsWith(condition.value)
            "ends_with" -> value.endsWith(condition.value)
            "in" -> condition.value.split(",").contains(value)
            "not_equals" -> value != condition.value
            else -> true
        }
    }
    
    private fun generateUserHash(input: String): Double {
        // Simple hash function to ensure consistent flag assignments
        var hash = 0.0
        for (i in input.indices) {
            val char = input[i].toInt()
            hash = ((hash * 31) + char) % 1000000
        }
        return hash / 1000000.0
    }
    
    private fun generateSessionId(): String {
        return "ff-${Date().getTime()}-${Random.nextInt(1000, 9999)}"
    }
}

// =============================================================================
// MONITORING SYSTEM INTEGRATION
// =============================================================================

class MonitoringSystem {
    private val performanceMonitor = PerformanceMonitor()
    private val errorTracker = ErrorTracker()
    private val userAnalytics = UserAnalytics()
    private val abTesting = ABTestingFramework()
    private val featureFlags = FeatureFlagManager()
    
    fun initialize() {
        performanceMonitor.start()
        errorTracker.start()
        userAnalytics.start()
        featureFlags.loadFlags()
        
        console.log("Monitoring system initialized")
    }
    
    fun setUserId(userId: String) {
        errorTracker.setUserId(userId)
        userAnalytics.setUserId(userId)
        abTesting.setUserId(userId)
        featureFlags.setUserId(userId)
    }
    
    fun trackEvent(event: String, properties: Map<String, String> = emptyMap()) {
        userAnalytics.trackEvent(event, properties)
    }
    
    fun trackPageView(url: String? = null, title: String? = null) {
        userAnalytics.trackPageView(
            url ?: window.location.href,
            title ?: document.title
        )
    }
    
    fun getABVariant(testId: String): String? {
        return abTesting.getVariant(testId)
    }
    
    fun trackConversion(testId: String, event: String, value: Double = 0.0) {
        abTesting.trackConversion(testId, event, value)
    }
    
    fun isFeatureEnabled(flagId: String): Boolean {
        return featureFlags.isEnabled(flagId)
    }
    
    fun getFeatureVariant(flagId: String, variantName: String): Boolean {
        return featureFlags.getVariant(flagId, variantName)
    }
}

@Composable
fun rememberMonitoringSystem(): MonitoringSystem {
    return remember { MonitoringSystem() }
}

// External JavaScript functions for integration
external fun setInterval(callback: () -> Unit, delay: Int): Int
external fun clearInterval(intervalId: Int)