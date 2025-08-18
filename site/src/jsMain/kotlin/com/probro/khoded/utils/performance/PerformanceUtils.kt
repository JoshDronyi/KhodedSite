package com.probro.khoded.utils.performance

import androidx.compose.runtime.*
import kotlinx.coroutines.*

/**
 * Performance Utilities - Simplified Stub Implementation
 * 
 * This is a simplified stub to resolve compilation issues.
 */

@Composable
fun LazyLoadImage(
    src: String,
    alt: String,
    placeholder: String? = null
) {
    // Simplified stub - just show regular image
    org.jetbrains.compose.web.dom.Img(
        src = src,
        alt = alt
    )
}

@Composable
fun <T> LazyLoadComponent(
    content: @Composable () -> T,
    placeholder: @Composable () -> Unit = { LoadingPlaceholder() }
) {
    // Simplified stub - just show content directly
    content()
}

@Composable
private fun LoadingPlaceholder() {
    org.jetbrains.compose.web.dom.Div {
        org.jetbrains.compose.web.dom.Text("Loading...")
    }
}

object PerformanceMonitor {
    fun startTiming(label: String): () -> Unit {
        val startTime = js("Date.now()") as Double
        return {
            val endTime = js("Date.now()") as Double
            console.log("Performance: $label took ${endTime - startTime}ms")
        }
    }
    
    fun measureCoreWebVitals() {
        // Initialize Core Web Vitals tracking based on 2024 best practices
        initializeLargestContentfulPaint()
        initializeFirstInputDelay()
        initializeCumulativeLayoutShift()
        initializeCustomPageMetrics()
    }
    
    private fun initializeLargestContentfulPaint() {
        js("""
            if ('PerformanceObserver' in window) {
                new PerformanceObserver((list) => {
                    for (const entry of list.getEntries()) {
                        // LCP should be < 2.5 seconds for good performance
                        const lcp = entry.startTime;
                        fetch('/api/metrics', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Accept': 'application/json'
                            },
                            body: JSON.stringify({
                                metric: 'largest_contentful_paint',
                                value: lcp,
                                timestamp: Date.now(),
                                url: window.location.pathname,
                                element: entry.element ? entry.element.tagName : null,
                                rating: lcp <= 2500 ? 'good' : lcp <= 4000 ? 'needs-improvement' : 'poor'
                            })
                        }).catch(err => console.warn('Failed to send LCP metric:', err));
                    }
                }).observe({entryTypes: ['largest-contentful-paint']});
            }
        """)
    }
    
    private fun initializeFirstInputDelay() {
        js("""
            if ('PerformanceObserver' in window) {
                new PerformanceObserver((list) => {
                    for (const entry of list.getEntries()) {
                        // FID should be < 100ms for good performance
                        const fid = entry.processingStart - entry.startTime;
                        fetch('/api/metrics', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Accept': 'application/json'
                            },
                            body: JSON.stringify({
                                metric: 'first_input_delay',
                                value: fid,
                                timestamp: Date.now(),
                                url: window.location.pathname,
                                eventType: entry.name,
                                rating: fid <= 100 ? 'good' : fid <= 300 ? 'needs-improvement' : 'poor'
                            })
                        }).catch(err => console.warn('Failed to send FID metric:', err));
                    }
                }).observe({entryTypes: ['first-input']});
            }
        """)
    }
    
    private fun initializeCumulativeLayoutShift() {
        js("""
            if ('PerformanceObserver' in window) {
                let clsValue = 0;
                let clsEntries = [];
                
                new PerformanceObserver((list) => {
                    for (const entry of list.getEntries()) {
                        // Only count layout shifts without recent user input
                        if (!entry.hadRecentInput) {
                            clsValue += entry.value;
                            clsEntries.push(entry);
                        }
                    }
                    
                    // CLS should be < 0.1 for good performance
                    fetch('/api/metrics', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json'
                        },
                        body: JSON.stringify({
                            metric: 'cumulative_layout_shift',
                            value: clsValue,
                            timestamp: Date.now(),
                            url: window.location.pathname,
                            entryCount: clsEntries.length,
                            rating: clsValue <= 0.1 ? 'good' : clsValue <= 0.25 ? 'needs-improvement' : 'poor'
                        })
                    }).catch(err => console.warn('Failed to send CLS metric:', err));
                }).observe({entryTypes: ['layout-shift']});
            }
        """)
    }
    
    private fun initializeCustomPageMetrics() {
        js("""
            // Track page load performance
            window.addEventListener('load', () => {
                const navigation = performance.getEntriesByType('navigation')[0];
                
                if (navigation) {
                    const metrics = {
                        dns_lookup: navigation.domainLookupEnd - navigation.domainLookupStart,
                        tcp_connect: navigation.connectEnd - navigation.connectStart,
                        request_response: navigation.responseEnd - navigation.requestStart,
                        dom_load: navigation.domContentLoadedEventEnd - navigation.domContentLoadedEventStart,
                        page_load: navigation.loadEventEnd - navigation.loadEventStart
                    };
                    
                    Object.entries(metrics).forEach(([metricName, value]) => {
                        fetch('/api/metrics', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Accept': 'application/json'
                            },
                            body: JSON.stringify({
                                metric: metricName,
                                value: value,
                                timestamp: Date.now(),
                                url: window.location.pathname,
                                type: 'navigation'
                            })
                        }).catch(err => console.warn('Failed to send navigation metric:', err));
                    });
                }
            });
            
            // Track resource loading performance
            window.addEventListener('load', () => {
                const resources = performance.getEntriesByType('resource');
                
                resources.forEach(resource => {
                    if (resource.duration > 1000) { // Only track slow resources (>1s)
                        fetch('/api/metrics', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Accept': 'application/json'
                            },
                            body: JSON.stringify({
                                metric: 'slow_resource',
                                value: resource.duration,
                                timestamp: Date.now(),
                                url: window.location.pathname,
                                resourceUrl: resource.name,
                                resourceType: resource.initiatorType
                            })
                        }).catch(err => console.warn('Failed to send resource metric:', err));
                    }
                });
            });
        """)
    }
}