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
        console.log("Core Web Vitals measurement not implemented in stub")
    }
}