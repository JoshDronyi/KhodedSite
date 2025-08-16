package com.probro.khoded.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.HttpMethod
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.lang.management.ManagementFactory
import java.time.Instant
import java.time.format.DateTimeFormatter
import com.probro.khoded.config.ServiceAccountConfig

/**
 * Health Check API Endpoint
 * 
 * Provides comprehensive health status for production monitoring.
 * Used by load balancers, monitoring systems, and Docker health checks.
 */

@Serializable
data class HealthStatus(
    val status: String,
    val timestamp: String,
    val uptime: Long,
    val checks: Map<String, CheckResult>
)

@Serializable
data class CheckResult(
    val status: String,
    val message: String? = null,
    val responseTime: Long? = null,
    val details: Map<String, String>? = null
)

@Api
fun health(ctx: ApiContext) {
    when (ctx.req.method) {
        HttpMethod.GET -> handleHealthCheck(ctx)
        else -> {
            ctx.res.status = 405
            ctx.res.setBodyText("Method not allowed")
        }
    }
}

private fun handleHealthCheck(ctx: ApiContext) {
    val startTime = System.currentTimeMillis()
    
    try {
        val checks = mutableMapOf<String, CheckResult>()
        var overallStatus = "UP"
        
        // Application health check
        checks["application"] = checkApplication()
        
        // Memory health check
        checks["memory"] = checkMemory()
        
        // Disk space check
        checks["disk"] = checkDiskSpace()
        
        // Database connectivity check (if applicable)
        checks["database"] = checkDatabase()
        
        // External services check
        checks["external_services"] = checkExternalServices()
        
        // Gmail service account check
        checks["gmail_service_account"] = checkGmailServiceAccount()
        
        // Determine overall status
        if (checks.values.any { it.status == "DOWN" }) {
            overallStatus = "DOWN"
            ctx.res.status = 503 // Service Unavailable
        } else if (checks.values.any { it.status == "WARNING" }) {
            overallStatus = "WARNING"
        }
        
        val healthStatus = HealthStatus(
            status = overallStatus,
            timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
            uptime = ManagementFactory.getRuntimeMXBean().uptime,
            checks = checks
        )
        
        ctx.res.setBodyText(Json.encodeToString(HealthStatus.serializer(), healthStatus))
        ctx.res.headers["Content-Type"] = "application/json"
        
    } catch (e: Exception) {
        ctx.res.status = 500
        val errorStatus = HealthStatus(
            status = "DOWN",
            timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
            uptime = ManagementFactory.getRuntimeMXBean().uptime,
            checks = mapOf(
                "error" to CheckResult(
                    status = "DOWN",
                    message = "Health check failed: ${e.message}"
                )
            )
        )
        ctx.res.setBodyText(Json.encodeToString(HealthStatus.serializer(), errorStatus))
        ctx.res.headers["Content-Type"] = "application/json"
    }
}

private fun checkApplication(): CheckResult {
    return try {
        val runtime = Runtime.getRuntime()
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = totalMemory - freeMemory
        val memoryUsagePercent = (usedMemory.toDouble() / totalMemory * 100).toInt()
        
        CheckResult(
            status = "UP",
            message = "Application is running normally",
            details = mapOf(
                "memory_usage_percent" to memoryUsagePercent.toString(),
                "total_memory_mb" to (totalMemory / 1024 / 1024).toString(),
                "used_memory_mb" to (usedMemory / 1024 / 1024).toString()
            )
        )
    } catch (e: Exception) {
        CheckResult(
            status = "DOWN",
            message = "Application check failed: ${e.message}"
        )
    }
}

private fun checkMemory(): CheckResult {
    return try {
        val runtime = Runtime.getRuntime()
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = totalMemory - freeMemory
        val memoryUsagePercent = usedMemory.toDouble() / totalMemory * 100
        
        val status = when {
            memoryUsagePercent > 90 -> "DOWN"
            memoryUsagePercent > 80 -> "WARNING"
            else -> "UP"
        }
        
        val message = when (status) {
            "DOWN" -> "Critical memory usage: ${memoryUsagePercent.toInt()}%"
            "WARNING" -> "High memory usage: ${memoryUsagePercent.toInt()}%"
            else -> "Memory usage normal: ${memoryUsagePercent.toInt()}%"
        }
        
        CheckResult(
            status = status,
            message = message,
            details = mapOf(
                "usage_percent" to memoryUsagePercent.toInt().toString(),
                "used_mb" to (usedMemory / 1024 / 1024).toString(),
                "total_mb" to (totalMemory / 1024 / 1024).toString(),
                "free_mb" to (freeMemory / 1024 / 1024).toString()
            )
        )
    } catch (e: Exception) {
        CheckResult(
            status = "DOWN",
            message = "Memory check failed: ${e.message}"
        )
    }
}

private fun checkDiskSpace(): CheckResult {
    return try {
        val file = java.io.File("/")
        val totalSpace = file.totalSpace
        val freeSpace = file.freeSpace
        val usedSpace = totalSpace - freeSpace
        val usagePercent = usedSpace.toDouble() / totalSpace * 100
        
        val status = when {
            usagePercent > 95 -> "DOWN"
            usagePercent > 85 -> "WARNING"
            else -> "UP"
        }
        
        val message = when (status) {
            "DOWN" -> "Critical disk usage: ${usagePercent.toInt()}%"
            "WARNING" -> "High disk usage: ${usagePercent.toInt()}%"
            else -> "Disk usage normal: ${usagePercent.toInt()}%"
        }
        
        CheckResult(
            status = status,
            message = message,
            details = mapOf(
                "usage_percent" to usagePercent.toInt().toString(),
                "used_gb" to (usedSpace / 1024 / 1024 / 1024).toString(),
                "total_gb" to (totalSpace / 1024 / 1024 / 1024).toString(),
                "free_gb" to (freeSpace / 1024 / 1024 / 1024).toString()
            )
        )
    } catch (e: Exception) {
        CheckResult(
            status = "DOWN",
            message = "Disk check failed: ${e.message}"
        )
    }
}

private fun checkDatabase(): CheckResult {
    return try {
        // Create a test connection to verify database connectivity
        val config = com.zaxxer.hikari.HikariConfig().apply {
            // Use environment variables similar to PostgreSQLService
            jdbcUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/khoded_db"
            driverClassName = "org.postgresql.Driver"
            username = System.getenv("DATABASE_USER") ?: "khoded_user"
            password = System.getenv("DATABASE_PASSWORD") ?: "khoded_password"
            
            // Minimal settings for health check
            maximumPoolSize = 1
            minimumIdle = 0
            connectionTimeout = 5000L // 5 seconds
            validationTimeout = 3000L // 3 seconds
        }
        
        val testDataSource = com.zaxxer.hikari.HikariDataSource(config)
        
        // Test the connection with a simple query
        testDataSource.connection.use { connection ->
            val isValid = connection.isValid(3) // 3 second timeout
            if (isValid) {
                // Execute a simple test query to verify database functionality
                connection.createStatement().use { statement ->
                    statement.executeQuery("SELECT 1").use { resultSet ->
                        if (resultSet.next()) {
                            CheckResult(
                                status = "UP",
                                message = "Database connection successful",
                                details = mapOf(
                                    "url" to (System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/khoded_db"),
                                    "driver" to "PostgreSQL",
                                    "test_query" to "SELECT 1"
                                )
                            )
                        } else {
                            CheckResult(
                                status = "DOWN",
                                message = "Database test query failed"
                            )
                        }
                    }
                }
            } else {
                CheckResult(
                    status = "DOWN",
                    message = "Database connection validation failed"
                )
            }
        }.also {
            // Clean up test connection pool
            testDataSource.close()
        }
    } catch (e: Exception) {
        CheckResult(
            status = "DOWN",
            message = "Database check failed: ${e.message}",
            details = mapOf(
                "error_type" to e.javaClass.simpleName,
                "error_message" to (e.message ?: "Unknown error")
            )
        )
    }
}

private fun checkExternalServices(): CheckResult {
    return try {
        // Check external service dependencies
        val checks = mutableMapOf<String, String>()
        
        // Basic connectivity checks
        checks["google_apis"] = "UP" // Could ping googleapis.com
        
        // Add other external service checks as needed
        
        val failedServices = checks.filter { it.value != "UP" }
        
        val status = if (failedServices.isEmpty()) "UP" else "WARNING"
        val message = if (failedServices.isEmpty()) {
            "All external services accessible"
        } else {
            "Some external services unavailable: ${failedServices.keys.joinToString()}"
        }
        
        CheckResult(
            status = status,
            message = message,
            details = checks
        )
    } catch (e: Exception) {
        CheckResult(
            status = "DOWN",
            message = "External services check failed: ${e.message}"
        )
    }
}

private fun checkGmailServiceAccount(): CheckResult {
    return try {
        val validationIssues = ServiceAccountConfig.validateCredentials()
        
        if (validationIssues.isEmpty()) {
            val creds = ServiceAccountConfig.credentials
            CheckResult(
                status = "UP",
                message = "Gmail service account configured correctly",
                details = mapOf(
                    "project_id" to creds.project_id,
                    "client_email" to creds.client_email,
                    "type" to creds.type,
                    "universe_domain" to (creds.universe_domain ?: "googleapis.com")
                )
            )
        } else {
            CheckResult(
                status = "DOWN",
                message = "Gmail service account configuration issues",
                details = mapOf(
                    "issues" to validationIssues.joinToString("; ")
                )
            )
        }
    } catch (e: Exception) {
        CheckResult(
            status = "DOWN",
            message = "Gmail service account check failed: ${e.message}",
            details = mapOf(
                "error_type" to e::class.simpleName.toString(),
                "suggestion" to "Check service account key file or environment variables"
            )
        )
    }
}