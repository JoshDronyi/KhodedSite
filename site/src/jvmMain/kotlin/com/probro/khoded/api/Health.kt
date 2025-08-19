package com.probro.khoded.api

import com.probro.khoded.local.KhodedDB
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant

@Serializable
data class HealthResponse(
    val status: String,
    val timestamp: String,
    val environment: String,
    val checks: Map<String, CheckResult>
)

@Serializable
data class CheckResult(
    val status: String,
    val message: String? = null,
    val responseTime: Long? = null
)

@Api
fun health(ctx: ApiContext) {
    val startTime = System.currentTimeMillis()
    
    try {
        val checks = mutableMapOf<String, CheckResult>()
        
        // Database connectivity check
        val dbStartTime = System.currentTimeMillis()
        try {
            runBlocking {
                newSuspendedTransaction(db = KhodedDB.db) {
                    // Simple query to test database connectivity
                    exec("SELECT 1")
                }
            }
            val dbResponseTime = System.currentTimeMillis() - dbStartTime
            checks["database"] = CheckResult(
                status = "UP",
                message = "Database connection successful",
                responseTime = dbResponseTime
            )
        } catch (e: Exception) {
            val dbResponseTime = System.currentTimeMillis() - dbStartTime
            checks["database"] = CheckResult(
                status = "DOWN",
                message = "Database connection failed: ${e.message}",
                responseTime = dbResponseTime
            )
        }
        
        // Gmail service account check
        val gmailStartTime = System.currentTimeMillis()
        try {
            val gmailServiceAccountJson = System.getenv("GMAIL_SERVICE_ACCOUNT_JSON")
            val gmailResponseTime = System.currentTimeMillis() - gmailStartTime
            
            if (gmailServiceAccountJson != null && gmailServiceAccountJson.isNotBlank()) {
                checks["gmail_service_account"] = CheckResult(
                    status = "UP",
                    message = "Gmail service account configured",
                    responseTime = gmailResponseTime
                )
            } else {
                checks["gmail_service_account"] = CheckResult(
                    status = "DOWN",
                    message = "Gmail service account not configured",
                    responseTime = gmailResponseTime
                )
            }
        } catch (e: Exception) {
            val gmailResponseTime = System.currentTimeMillis() - gmailStartTime
            checks["gmail_service_account"] = CheckResult(
                status = "DOWN",
                message = "Gmail service account check failed: ${e.message}",
                responseTime = gmailResponseTime
            )
        }
        
        // Environment check
        val environment = System.getenv("APP_ENVIRONMENT") ?: "development"
        checks["environment"] = CheckResult(
            status = "UP",
            message = "Running in $environment mode"
        )
        
        // Memory check
        val runtime = Runtime.getRuntime()
        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
        val maxMemory = runtime.maxMemory() / 1024 / 1024
        val memoryUsagePercent = (usedMemory * 100) / maxMemory
        
        checks["memory"] = CheckResult(
            status = if (memoryUsagePercent < 90) "UP" else "WARNING",
            message = "Memory usage: ${usedMemory}MB / ${maxMemory}MB (${memoryUsagePercent}%)"
        )
        
        // Overall status
        val hasFailures = checks.values.any { it.status == "DOWN" }
        val overallStatus = if (hasFailures) "DOWN" else "UP"
        
        val healthResponse = HealthResponse(
            status = overallStatus,
            timestamp = Instant.now().toString(),
            environment = environment,
            checks = checks
        )
        
        val responseJson = Json.encodeToString(healthResponse)
        
        ctx.res.setBodyText(responseJson)
        ctx.res.setHeader("Content-Type", "application/json")
        
        // Set appropriate HTTP status
        if (hasFailures) {
            ctx.res.status = 503 // Service Unavailable
        } else {
            ctx.res.status = 200 // OK
        }
        
    } catch (e: Exception) {
        // Fallback error response
        val errorResponse = HealthResponse(
            status = "DOWN",
            timestamp = Instant.now().toString(),
            environment = System.getenv("APP_ENVIRONMENT") ?: "unknown",
            checks = mapOf(
                "application" to CheckResult(
                    status = "DOWN",
                    message = "Health check failed: ${e.message}"
                )
            )
        )
        
        val errorJson = Json.encodeToString(errorResponse)
        ctx.res.setBodyText(errorJson)
        ctx.res.setHeader("Content-Type", "application/json")
        ctx.res.status = 503
    }
}

// Simple health endpoint for load balancers
@Api
fun healthz(ctx: ApiContext) {
    try {
        // Quick database check
        runBlocking {
            newSuspendedTransaction(db = KhodedDB.db) {
                exec("SELECT 1")
            }
        }
        ctx.res.setBodyText("OK")
        ctx.res.status = 200
    } catch (e: Exception) {
        ctx.res.setBodyText("ERROR")
        ctx.res.status = 503
    }
}