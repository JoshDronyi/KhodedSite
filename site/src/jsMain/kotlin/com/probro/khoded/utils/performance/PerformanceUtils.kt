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
                new PerformanceObserver(function(list) {
                    var entries = list.getEntries();
                    for (var i = 0; i < entries.length; i++) {
                        var entry = entries[i];
                        var lcp = entry.startTime;
                        var metricData = {
                            metric: 'largest_contentful_paint',
                            value: lcp,
                            timestamp: Date.now(),
                            url: window.location.pathname,
                            element: entry.element ? entry.element.tagName : null,
                            rating: lcp <= 2500 ? 'good' : lcp <= 4000 ? 'needs-improvement' : 'poor'
                        };
                        
                        fetch('/api/metrics', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Accept': 'application/json'
                            },
                            body: JSON.stringify(metricData)
                        }).catch(function(err) {
                            console.warn('Failed to send LCP metric:', err);
                        });
                    }
                }).observe({entryTypes: ['largest-contentful-paint']});
            }
        """)
    }
    
    private fun initializeFirstInputDelay() {
        js("""
            if ('PerformanceObserver' in window) {
                new PerformanceObserver(function(list) {
                    var entries = list.getEntries();
                    for (var i = 0; i < entries.length; i++) {
                        var entry = entries[i];
                        var fid = entry.processingStart - entry.startTime;
                        var metricData = {
                            metric: 'first_input_delay',
                            value: fid,
                            timestamp: Date.now(),
                            url: window.location.pathname,
                            eventType: entry.name,
                            rating: fid <= 100 ? 'good' : fid <= 300 ? 'needs-improvement' : 'poor'
                        };
                        
                        fetch('/api/metrics', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Accept': 'application/json'
                            },
                            body: JSON.stringify(metricData)
                        }).catch(function(err) {
                            console.warn('Failed to send FID metric:', err);
                        });
                    }
                }).observe({entryTypes: ['first-input']});
            }
        """)
    }
    
    private fun initializeCumulativeLayoutShift() {
        js("""
            if ('PerformanceObserver' in window) {
                var clsValue = 0;
                var clsEntries = [];
                
                new PerformanceObserver(function(list) {
                    var entries = list.getEntries();
                    for (var i = 0; i < entries.length; i++) {
                        var entry = entries[i];
                        if (!entry.hadRecentInput) {
                            clsValue += entry.value;
                            clsEntries.push(entry);
                        }
                    }
                    
                    var metricData = {
                        metric: 'cumulative_layout_shift',
                        value: clsValue,
                        timestamp: Date.now(),
                        url: window.location.pathname,
                        entryCount: clsEntries.length,
                        rating: clsValue <= 0.1 ? 'good' : clsValue <= 0.25 ? 'needs-improvement' : 'poor'
                    };
                    
                    fetch('/api/metrics', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json'
                        },
                        body: JSON.stringify(metricData)
                    }).catch(function(err) {
                        console.warn('Failed to send CLS metric:', err);
                    });
                }).observe({entryTypes: ['layout-shift']});
            }
        """)
    }
    
    private fun initializeCustomPageMetrics() {
        js("""
            window.addEventListener('load', function() {
                var navigationEntries = performance.getEntriesByType('navigation');
                if (navigationEntries.length > 0) {
                    var navigation = navigationEntries[0];
                    var metrics = {
                        dns_lookup: navigation.domainLookupEnd - navigation.domainLookupStart,
                        tcp_connect: navigation.connectEnd - navigation.connectStart,
                        request_response: navigation.responseEnd - navigation.requestStart,
                        dom_load: navigation.domContentLoadedEventEnd - navigation.domContentLoadedEventStart,
                        page_load: navigation.loadEventEnd - navigation.loadEventStart
                    };
                    
                    var metricNames = Object.keys(metrics);
                    for (var i = 0; i < metricNames.length; i++) {
                        var metricName = metricNames[i];
                        var value = metrics[metricName];
                        var metricData = {
                            metric: metricName,
                            value: value,
                            timestamp: Date.now(),
                            url: window.location.pathname,
                            type: 'navigation'
                        };
                        
                        fetch('/api/metrics', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Accept': 'application/json'
                            },
                            body: JSON.stringify(metricData)
                        }).catch(function(err) {
                            console.warn('Failed to send navigation metric:', err);
                        });
                    }
                }
            });
            
            window.addEventListener('load', function() {
                var resources = performance.getEntriesByType('resource');
                
                for (var i = 0; i < resources.length; i++) {
                    var resource = resources[i];
                    if (resource.duration > 1000) {
                        var metricData = {
                            metric: 'slow_resource',
                            value: resource.duration,
                            timestamp: Date.now(),
                            url: window.location.pathname,
                            resourceUrl: resource.name,
                            resourceType: resource.initiatorType
                        };
                        
                        fetch('/api/metrics', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'Accept': 'application/json'
                            },
                            body: JSON.stringify(metricData)
                        }).catch(function(err) {
                            console.warn('Failed to send resource metric:', err);
                        });
                    }
                }
            });
        """)
    }
}