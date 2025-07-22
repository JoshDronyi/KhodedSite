package com.probro.khoded.utils.performance

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import org.w3c.dom.*
import org.w3c.dom.events.Event
import kotlin.js.Date

/**
 * Performance Optimization Utilities
 * 
 * Implements Core Web Vitals optimization and performance monitoring:
 * - Lazy loading for images and components
 * - Intersection Observer for scroll-triggered loading
 * - Performance metrics collection
 * - Resource hints and preloading
 * - Critical resource prioritization
 */

// =============================================================================
// LAZY LOADING UTILITIES
// =============================================================================

@Composable
fun LazyLoadImage(
    src: String,
    alt: String,
    placeholder: String? = null,
    modifier: Modifier = Modifier,
    className: String = "",
    onLoad: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    intersectionThreshold: Double = 0.1
) {
    var isVisible by remember { mutableStateOf(false) }
    var imageRef by remember { mutableStateOf<Element?>(null) }
    var hasLoaded by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
    
    LaunchedEffect(imageRef) {
        imageRef?.let { element ->
            val observer = IntersectionObserver({ entries, _ ->
                entries.forEach { entry ->
                    if (entry.isIntersecting) {
                        isVisible = true
                        observer.disconnect()
                    }
                }
            }, IntersectionObserverInit(threshold = arrayOf(intersectionThreshold)))
            
            observer.observe(element)
        }
    }
    
    org.jetbrains.compose.web.dom.Img(
        src = if (isVisible) src else (placeholder ?: "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1 1'%3E%3C/svg%3E"),
        alt = alt,
        attrs = modifier
            .className(className)
            .apply {
                if (!hasLoaded && isVisible) {
                    className("loading")
                }
                if (hasError) {
                    className("error")
                }
            }
            .toAttrs {
                ref { element ->
                    imageRef = element
                    onDispose { }
                }
                
                if (isVisible) {
                    onLoad { 
                        hasLoaded = true
                        onLoad?.invoke()
                    }
                    onError { 
                        hasError = true
                        onError?.invoke()
                    }
                }
                
                // Add loading attribute for native lazy loading support
                attr("loading", "lazy")
                attr("decoding", "async")
            }
    )
}

@Composable
fun <T> LazyLoadComponent(
    content: @Composable () -> T,
    placeholder: @Composable () -> Unit = { LoadingPlaceholder() },
    intersectionThreshold: Double = 0.1
) {
    var isVisible by remember { mutableStateOf(false) }
    var containerRef by remember { mutableStateOf<Element?>(null) }
    
    LaunchedEffect(containerRef) {
        containerRef?.let { element ->
            val observer = IntersectionObserver({ entries, _ ->
                entries.forEach { entry ->
                    if (entry.isIntersecting) {
                        isVisible = true
                        observer.disconnect()
                    }
                }
            }, IntersectionObserverInit(threshold = arrayOf(intersectionThreshold)))
            
            observer.observe(element)
        }
    }
    
    org.jetbrains.compose.web.dom.Div(
        attrs = {
            ref { element ->
                containerRef = element
                onDispose { }
            }
        }
    ) {
        if (isVisible) {
            content()
        } else {
            placeholder()
        }
    }
}

@Composable
private fun LoadingPlaceholder() {
    org.jetbrains.compose.web.dom.Div(
        attrs = {
            style {
                property("background", "linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%)")
                property("background-size", "200% 100%")
                property("animation", "loading 1.5s infinite")
                property("border-radius", "8px")
                property("height", "200px")
                property("width", "100%")
            }
        }
    )
}

// =============================================================================
// PERFORMANCE MONITORING
// =============================================================================

object PerformanceMonitor {
    private val metrics = mutableMapOf<String, Double>()
    
    fun startTiming(label: String): () -> Unit {
        val startTime = Date().getTime()
        return {
            val endTime = Date().getTime()
            metrics[label] = endTime - startTime
            console.log("Performance: $label took ${endTime - startTime}ms")
        }
    }
    
    fun measureCoreWebVitals() {
        // Largest Contentful Paint (LCP)
        try {
            val observer = PerformanceObserver { list, _ ->
                val entries = list.getEntries()
                entries.forEach { entry ->
                    if (entry.entryType == "largest-contentful-paint") {
                        val lcp = entry.startTime
                        console.log("LCP: ${lcp}ms")
                        metrics["LCP"] = lcp
                        
                        // Report to analytics if available
                        reportMetric("LCP", lcp)
                    }
                }
            }
            observer.observe(PerformanceObserverInit(entryTypes = arrayOf("largest-contentful-paint")))
        } catch (e: Throwable) {
            console.warn("LCP measurement not supported")
        }
        
        // First Input Delay (FID) and Cumulative Layout Shift (CLS)
        try {
            val observer = PerformanceObserver { list, _ ->
                val entries = list.getEntries()
                entries.forEach { entry ->
                    when (entry.entryType) {
                        "first-input" -> {
                            val fid = entry.processingStart - entry.startTime
                            console.log("FID: ${fid}ms")
                            metrics["FID"] = fid
                            reportMetric("FID", fid)
                        }
                        "layout-shift" -> {
                            val cls = entry.value
                            console.log("Layout shift: $cls")
                            metrics["CLS"] = (metrics["CLS"] ?: 0.0) + cls
                            reportMetric("CLS", metrics["CLS"] ?: 0.0)
                        }
                    }
                }
            }
            observer.observe(PerformanceObserverInit(entryTypes = arrayOf("first-input", "layout-shift")))
        } catch (e: Throwable) {
            console.warn("FID/CLS measurement not supported")
        }
    }
    
    private fun reportMetric(name: String, value: Double) {
        // Report to analytics service
        try {
            js("gtag('event', 'web_vitals', { metric_name: name, metric_value: value })")
        } catch (e: Throwable) {
            // Fallback or custom analytics reporting
            console.log("Metric reported: $name = $value")
        }
    }
    
    fun getMetrics(): Map<String, Double> = metrics.toMap()
    
    fun clearMetrics() {
        metrics.clear()
    }
}

// =============================================================================
// RESOURCE HINTS AND PRELOADING
// =============================================================================

object ResourceOptimizer {
    fun preloadCriticalResources() {
        // Preload critical CSS
        addResourceHint("preload", "/styles/critical.css", "style")
        
        // Preload fonts
        addResourceHint("preload", "https://fonts.gstatic.com/s/inter/v12/UcCO3FwrK3iLTeHuS_fvQtMwCp50KnMw2boKoduKmMEVuLyfAZ9hiA.woff2", "font", mapOf(
            "crossorigin" to ""
        ))
        
        // Preload hero images
        addResourceHint("preload", "/images/hero-bg.webp", "image")
        
        // Prefetch likely next pages
        prefetchResource("/about")
        prefetchResource("/services")
        prefetchResource("/contact")
    }
    
    fun addResourceHint(rel: String, href: String, asType: String? = null, attributes: Map<String, String> = emptyMap()) {
        val link = document.createElement("link")
        link.setAttribute("rel", rel)
        link.setAttribute("href", href)
        asType?.let { link.setAttribute("as", it) }
        
        attributes.forEach { (key, value) ->
            link.setAttribute(key, value)
        }
        
        document.head?.appendChild(link)
    }
    
    fun prefetchResource(href: String) {
        addResourceHint("prefetch", href)
    }
    
    fun preconnectToDomain(domain: String, crossOrigin: Boolean = false) {
        val attributes = if (crossOrigin) mapOf("crossorigin" to "") else emptyMap()
        addResourceHint("preconnect", domain, attributes = attributes)
    }
    
    fun dnsPrefetch(domain: String) {
        addResourceHint("dns-prefetch", domain)
    }
}

// =============================================================================
// CRITICAL RESOURCE PRIORITIZATION
// =============================================================================

object CriticalResourceManager {
    fun optimizeResourcePriority() {
        // Add critical CSS inline
        inlineCriticalCSS()
        
        // Defer non-critical JavaScript
        deferNonCriticalJS()
        
        // Optimize image loading
        optimizeImageLoading()
    }
    
    private fun inlineCriticalCSS() {
        val criticalCSS = """
            /* Critical above-the-fold styles */
            body { 
                font-family: Inter, system-ui, sans-serif;
                margin: 0;
                padding: 0;
                line-height: 1.6;
            }
            
            .header {
                position: fixed;
                top: 0;
                width: 100%;
                z-index: 1000;
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
            }
            
            .hero {
                min-height: 80vh;
                display: flex;
                align-items: center;
                justify-content: center;
                padding-top: 80px;
            }
            
            /* Loading states */
            .loading {
                background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
                background-size: 200% 100%;
                animation: loading 1.5s infinite;
            }
            
            @keyframes loading {
                0% { background-position: 200% 0; }
                100% { background-position: -200% 0; }
            }
            
            /* Focus styles for accessibility */
            :focus-visible {
                outline: 2px solid #0066CC;
                outline-offset: 2px;
            }
        """.trimIndent()
        
        val style = document.createElement("style")
        style.textContent = criticalCSS
        document.head?.insertBefore(style, document.head?.firstChild)
    }
    
    private fun deferNonCriticalJS() {
        // Mark non-critical scripts as defer or async
        document.querySelectorAll("script:not([critical])").asList().forEach { script ->
            if (script is HTMLScriptElement) {
                script.defer = true
            }
        }
    }
    
    private fun optimizeImageLoading() {
        // Add loading="lazy" to all images below the fold
        document.querySelectorAll("img:not([loading])").asList().forEach { img ->
            if (img is HTMLImageElement) {
                val rect = img.getBoundingClientRect()
                if (rect.top > window.innerHeight) {
                    img.loading = "lazy"
                }
                img.decoding = "async"
            }
        }
    }
}

// =============================================================================
// INTERSECTION OBSERVER UTILITIES
// =============================================================================

@Composable
fun useIntersectionObserver(
    threshold: Double = 0.1,
    rootMargin: String = "0px"
): Pair<(Element?) -> Unit, Boolean> {
    var isIntersecting by remember { mutableStateOf(false) }
    var targetElement by remember { mutableStateOf<Element?>(null) }
    
    LaunchedEffect(targetElement) {
        targetElement?.let { element ->
            val observer = IntersectionObserver({ entries, _ ->
                entries.forEach { entry ->
                    isIntersecting = entry.isIntersecting
                }
            }, IntersectionObserverInit(
                threshold = arrayOf(threshold),
                rootMargin = rootMargin
            ))
            
            observer.observe(element)
            
            // Cleanup
            onDispose {
                observer.disconnect()
            }
        }
    }
    
    val setRef: (Element?) -> Unit = { element ->
        targetElement = element
    }
    
    return Pair(setRef, isIntersecting)
}

// =============================================================================
// BUNDLE OPTIMIZATION UTILITIES
// =============================================================================

object BundleOptimizer {
    fun initializeCodeSplitting() {
        // Dynamic imports for route-based code splitting
        console.log("Code splitting initialized")
        
        // Prefetch route modules based on user interaction
        setupRoutePrefetching()
    }
    
    private fun setupRoutePrefetching() {
        // Prefetch routes on hover or focus
        document.addEventListener("mouseover", { event ->
            val target = event.target as? HTMLAnchorElement
            target?.href?.let { href ->
                if (isInternalLink(href)) {
                    ResourceOptimizer.prefetchResource(href)
                }
            }
        })
        
        document.addEventListener("focus", { event ->
            val target = event.target as? HTMLAnchorElement
            target?.href?.let { href ->
                if (isInternalLink(href)) {
                    ResourceOptimizer.prefetchResource(href)
                }
            }
        })
    }
    
    private fun isInternalLink(href: String): Boolean {
        return href.startsWith(window.location.origin) || href.startsWith("/")
    }
}

// =============================================================================
// JAVASCRIPT EXTENSIONS FOR PERFORMANCE
// =============================================================================

external interface IntersectionObserverInit {
    val threshold: Array<Double>?
    val rootMargin: String?
    val root: Element?
}

external interface IntersectionObserverEntry {
    val isIntersecting: Boolean
    val intersectionRatio: Double
    val target: Element
}

external class IntersectionObserver(
    callback: (Array<IntersectionObserverEntry>, IntersectionObserver) -> Unit,
    options: IntersectionObserverInit? = definedExternally
) {
    fun observe(target: Element)
    fun unobserve(target: Element)
    fun disconnect()
}

external interface PerformanceObserverInit {
    val entryTypes: Array<String>
}

external interface PerformanceEntry {
    val entryType: String
    val startTime: Double
    val name: String
    val duration: Double
    val value: Double
    val processingStart: Double
}

external class PerformanceObserver(
    callback: (PerformanceObserverEntryList, PerformanceObserver) -> Unit
) {
    fun observe(options: PerformanceObserverInit)
    fun disconnect()
}

external interface PerformanceObserverEntryList {
    fun getEntries(): Array<PerformanceEntry>
}