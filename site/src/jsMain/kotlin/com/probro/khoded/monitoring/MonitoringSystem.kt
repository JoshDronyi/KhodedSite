package com.probro.khoded.monitoring

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import kotlinx.serialization.*

/**
 * Monitoring System - Simplified Stub Implementation
 * 
 * This is a simplified stub to resolve compilation issues.
 */

@Serializable
data class MetricData(
    val name: String,
    val value: Double,
    val timestamp: String = js("new Date().toISOString()").toString(),
    val tags: Map<String, String> = emptyMap()
)

@Serializable
data class ErrorReport(
    val message: String,
    val stackTrace: String? = null,
    val timestamp: String = js("new Date().toISOString()").toString(),
    val userId: String? = null
)

object MonitoringSystem {
    
    private val metrics = mutableListOf<MetricData>()
    private val errors = mutableListOf<ErrorReport>()
    
    fun trackMetric(name: String, value: Double, tags: Map<String, String> = emptyMap()) {
        val metric = MetricData(name, value, tags = tags)
        metrics.add(metric)
        console.log("Metric tracked: $name = $value")
    }
    
    fun trackError(error: Throwable, userId: String? = null) {
        val report = ErrorReport(
            message = error.message ?: "Unknown error",
            stackTrace = error.stackTraceToString(),
            userId = userId
        )
        errors.add(report)
        console.error("Error tracked: ${error.message}")
    }
    
    fun trackPageView(path: String) {
        trackMetric("page_view", 1.0, mapOf("path" to path))
    }
    
    fun trackUserAction(action: String, userId: String? = null) {
        val tags = mutableMapOf("action" to action)
        userId?.let { tags["user_id"] = it }
        trackMetric("user_action", 1.0, tags)
    }
    
    suspend fun sendMetrics() = withContext(Dispatchers.Default) {
        // Simplified stub - just log metrics
        console.log("Sending ${metrics.size} metrics to monitoring service")
        metrics.clear()
    }
    
    suspend fun sendErrors() = withContext(Dispatchers.Default) {
        // Simplified stub - just log errors  
        console.log("Sending ${errors.size} errors to monitoring service")
        errors.clear()
    }
    
    fun getMetrics(): List<MetricData> = metrics.toList()
    fun getErrors(): List<ErrorReport> = errors.toList()
}