package com.probro.khoded.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Performance Metrics API
 * Handles collection and aggregation of Core Web Vitals and performance data
 */

@Serializable
data class MetricData(
    val metric: String,
    val value: Double,
    val timestamp: Long,
    val url: String,
    val element: String? = null,
    val rating: String? = null,
    val eventType: String? = null,
    val entryCount: Int? = null,
    val resourceUrl: String? = null,
    val resourceType: String? = null,
    val type: String? = null
)

@Serializable
data class MetricsResponse(
    val status: String,
    val message: String,
    val metricsReceived: Int = 0
)

@Serializable
data class PrometheusMetric(
    val name: String,
    val value: Double,
    val timestamp: Long,
    val labels: Map<String, String> = emptyMap()
)

@Api
fun metrics(ctx: ApiContext) {
    when (ctx.req.method) {
        "POST" -> handleMetricsSubmission(ctx)
        "GET" -> handlePrometheusMetrics(ctx)
        else -> {
            ctx.res.status = 405
            ctx.res.setBodyText("Method not allowed")
        }
    }
}

private fun handleMetricsSubmission(ctx: ApiContext) {
    try {
        val requestBody = ctx.req.body ?: ""
        
        if (requestBody.isBlank()) {
            ctx.res.status = 400
            ctx.res.setBodyText(Json.encodeToString(MetricsResponse(
                status = "error",
                message = "Request body is required"
            )))
            return
        }

        val metricData = Json.decodeFromString<MetricData>(requestBody)
        
        // Validate metric data
        if (metricData.metric.isEmpty() || metricData.value < 0) {
            ctx.res.status = 400
            ctx.res.setBodyText(Json.encodeToString(MetricsResponse(
                status = "error",
                message = "Invalid metric data"
            )))
            return
        }

        // Process and store the metric
        processMetric(metricData)
        
        // Log the metric for monitoring
        val timestamp = Instant.ofEpochMilli(metricData.timestamp)
            .atOffset(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT)
        
        println("METRIC: ${metricData.metric}=${metricData.value} url=${metricData.url} timestamp=$timestamp rating=${metricData.rating ?: "unknown"}")
        
        ctx.res.status = 200
        ctx.res.setBodyText(Json.encodeToString(MetricsResponse(
            status = "success",
            message = "Metric received and processed",
            metricsReceived = 1
        )))
        
    } catch (e: Exception) {
        println("Error processing metrics: ${e.message}")
        ctx.res.status = 500
        ctx.res.setBodyText(Json.encodeToString(MetricsResponse(
            status = "error",
            message = "Failed to process metric: ${e.message}"
        )))
    }
}

private fun handlePrometheusMetrics(ctx: ApiContext) {
    try {
        // Generate Prometheus-formatted metrics
        val metrics = generatePrometheusMetrics()
        
        ctx.res.headers["Content-Type"] = "text/plain; charset=utf-8"
        ctx.res.status = 200
        ctx.res.setBodyText(metrics)
        
    } catch (e: Exception) {
        println("Error generating Prometheus metrics: ${e.message}")
        ctx.res.status = 500
        ctx.res.setBodyText("# Error generating metrics: ${e.message}")
    }
}

private fun processMetric(metricData: MetricData) {
    // In a production environment, you would store this data in a database
    // or send it to a monitoring service like Prometheus, DataDog, etc.
    
    when (metricData.metric) {
        "largest_contentful_paint" -> {
            // Track LCP - should be < 2.5s for good performance
            if (metricData.value > 2500) {
                println("WARNING: Poor LCP detected: ${metricData.value}ms on ${metricData.url}")
            }
        }
        "first_input_delay" -> {
            // Track FID - should be < 100ms for good performance  
            if (metricData.value > 100) {
                println("WARNING: Poor FID detected: ${metricData.value}ms on ${metricData.url}")
            }
        }
        "cumulative_layout_shift" -> {
            // Track CLS - should be < 0.1 for good performance
            if (metricData.value > 0.1) {
                println("WARNING: Poor CLS detected: ${metricData.value} on ${metricData.url}")
            }
        }
        "slow_resource" -> {
            // Track slow loading resources
            println("SLOW RESOURCE: ${metricData.resourceUrl} took ${metricData.value}ms (${metricData.resourceType})")
        }
    }
    
    // Store metric for aggregation (in production, use a proper database)
    storeMetricForAggregation(metricData)
}

private fun storeMetricForAggregation(metricData: MetricData) {
    // In production, store in database/time-series DB like InfluxDB or Prometheus
    // For now, we'll use in-memory storage (this will be lost on restart)
    
    // This is a placeholder for metric storage
    // You would implement proper persistence here
}

private fun generatePrometheusMetrics(): String {
    val currentTime = System.currentTimeMillis()
    
    return buildString {
        appendLine("# HELP khoded_metrics_total Total number of metrics received")
        appendLine("# TYPE khoded_metrics_total counter") 
        appendLine("khoded_metrics_total 0 $currentTime")
        
        appendLine("")
        appendLine("# HELP khoded_core_web_vitals Core Web Vitals metrics")
        appendLine("# TYPE khoded_core_web_vitals gauge")
        appendLine("khoded_core_web_vitals{metric=\"lcp\",rating=\"unknown\"} 0 $currentTime")
        appendLine("khoded_core_web_vitals{metric=\"fid\",rating=\"unknown\"} 0 $currentTime") 
        appendLine("khoded_core_web_vitals{metric=\"cls\",rating=\"unknown\"} 0 $currentTime")
        
        appendLine("")
        appendLine("# HELP khoded_api_health API health status")
        appendLine("# TYPE khoded_api_health gauge")
        appendLine("khoded_api_health{endpoint=\"metrics\"} 1 $currentTime")
    }
}