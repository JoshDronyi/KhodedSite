package com.probro.khoded.deployment

import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.js.Date

/**
 * Deployment Utilities and Health Checks
 * 
 * Comprehensive deployment utilities including:
 * - Health check endpoints and monitoring
 * - Build information and versioning
 * - Environment configuration detection
 * - Deployment validation checks
 * - Progressive deployment strategies
 * - Rollback mechanisms
 * - Infrastructure monitoring
 */

// =============================================================================
// BUILD AND VERSION INFORMATION
// =============================================================================

@Serializable
data class BuildInfo(
    val version: String,
    val buildNumber: String,
    val buildDate: String,
    val gitCommit: String,
    val gitBranch: String,
    val environment: String,
    val buildType: String // "development", "staging", "production"
)

@Serializable
data class EnvironmentInfo(
    val name: String,
    val isDevelopment: Boolean,
    val isStaging: Boolean,
    val isProduction: Boolean,
    val apiBaseUrl: String,
    val features: Map<String, Boolean> = emptyMap(),
    val debugMode: Boolean = false
)

object BuildInfoProvider {
    private val buildInfo: BuildInfo by lazy {
        detectBuildInfo()
    }
    
    private val environmentInfo: EnvironmentInfo by lazy {
        detectEnvironmentInfo()
    }
    
    fun getBuildInfo(): BuildInfo = buildInfo
    fun getEnvironmentInfo(): EnvironmentInfo = environmentInfo
    
    private fun detectBuildInfo(): BuildInfo {
        return try {
            // In a real deployment, these would come from build-time constants
            // or be injected by the build system
            BuildInfo(
                version = getFromWindow("__BUILD_VERSION__") ?: "1.0.0",
                buildNumber = getFromWindow("__BUILD_NUMBER__") ?: "local-build",
                buildDate = getFromWindow("__BUILD_DATE__") ?: Date().toString(),
                gitCommit = getFromWindow("__GIT_COMMIT__") ?: "local-commit",
                gitBranch = getFromWindow("__GIT_BRANCH__") ?: "local-branch",
                environment = detectEnvironment(),
                buildType = detectBuildType()
            )
        } catch (e: Exception) {
            console.warn("Failed to detect build info", e)
            BuildInfo(
                version = "unknown",
                buildNumber = "unknown",
                buildDate = Date().toString(),
                gitCommit = "unknown",
                gitBranch = "unknown",
                environment = detectEnvironment(),
                buildType = "development"
            )
        }
    }
    
    private fun detectEnvironmentInfo(): EnvironmentInfo {
        val environment = detectEnvironment()
        
        return EnvironmentInfo(
            name = environment,
            isDevelopment = environment == "development",
            isStaging = environment == "staging",
            isProduction = environment == "production",
            apiBaseUrl = detectApiBaseUrl(environment),
            features = detectFeatures(environment),
            debugMode = environment != "production"
        )
    }
    
    private fun detectEnvironment(): String {
        val hostname = window.location.hostname
        val protocol = window.location.protocol
        
        return when {
            hostname.contains("localhost") || 
            hostname.startsWith("127.0.0.1") || 
            hostname.startsWith("192.168.") ||
            hostname.startsWith("10.0.") -> "development"
            
            hostname.contains("staging") || 
            hostname.contains("dev.") ||
            hostname.contains("test.") -> "staging"
            
            protocol == "https:" && 
            !hostname.contains("staging") && 
            !hostname.contains("dev") -> "production"
            
            else -> "development"
        }
    }
    
    private fun detectBuildType(): String {
        return when (detectEnvironment()) {
            "development" -> "development"
            "staging" -> "staging"
            "production" -> "production"
            else -> "development"
        }
    }
    
    private fun detectApiBaseUrl(environment: String): String {
        return when (environment) {
            "development" -> "http://localhost:3000/api"
            "staging" -> "https://api-staging.khoded.com"
            "production" -> "https://api.khoded.com"
            else -> "/api"
        }
    }
    
    private fun detectFeatures(environment: String): Map<String, Boolean> {
        return when (environment) {
            "development" -> mapOf(
                "debug_toolbar" to true,
                "analytics" to false,
                "error_reporting" to true,
                "performance_monitoring" to true
            )
            "staging" -> mapOf(
                "debug_toolbar" to true,
                "analytics" to true,
                "error_reporting" to true,
                "performance_monitoring" to true
            )
            "production" -> mapOf(
                "debug_toolbar" to false,
                "analytics" to true,
                "error_reporting" to true,
                "performance_monitoring" to true
            )
            else -> emptyMap()
        }
    }
    
    private fun getFromWindow(key: String): String? {
        return try {
            val value = window.asDynamic()[key]
            if (value != undefined) value as String else null
        } catch (e: Exception) {
            null
        }
    }
}

// =============================================================================
// HEALTH CHECK SYSTEM
// =============================================================================

@Serializable
data class HealthCheckResult(
    val name: String,
    val status: HealthStatus,
    val message: String,
    val duration: Long,
    val timestamp: Long,
    val details: Map<String, String> = emptyMap()
)

@Serializable
data class HealthReport(
    val overallStatus: HealthStatus,
    val timestamp: Long,
    val buildInfo: BuildInfo,
    val environmentInfo: EnvironmentInfo,
    val checks: List<HealthCheckResult>,
    val systemInfo: SystemInfo
)

@Serializable
data class SystemInfo(
    val userAgent: String,
    val platform: String,
    val language: String,
    val timezone: String,
    val screenResolution: String,
    val viewportSize: String,
    val connectionType: String?,
    val onlineStatus: Boolean
)

enum class HealthStatus {
    Healthy, Degraded, Unhealthy, Unknown
}

interface HealthCheck {
    suspend fun check(): HealthCheckResult
    fun getName(): String
}

class ApiHealthCheck(
    private val apiBaseUrl: String,
    private val timeout: Long = 5000
) : HealthCheck {
    
    override fun getName(): String = "API Connectivity"
    
    override suspend fun check(): HealthCheckResult {
        val startTime = Date().getTime()
        
        return try {
            val response = withTimeout(timeout) {
                window.fetch("$apiBaseUrl/health")
            }
            
            val duration = Date().getTime() - startTime
            val status = if (response.ok) HealthStatus.Healthy else HealthStatus.Degraded
            val message = if (response.ok) {
                "API is responding normally"
            } else {
                "API returned status ${response.status}"
            }
            
            HealthCheckResult(
                name = getName(),
                status = status,
                message = message,
                duration = duration,
                timestamp = Date().getTime().toLong(),
                details = mapOf(
                    "url" to "$apiBaseUrl/health",
                    "status_code" to response.status.toString(),
                    "response_time" to "${duration}ms"
                )
            )
        } catch (e: Exception) {
            val duration = Date().getTime() - startTime
            
            HealthCheckResult(
                name = getName(),
                status = HealthStatus.Unhealthy,
                message = "API health check failed: ${e.message}",
                duration = duration,
                timestamp = Date().getTime().toLong(),
                details = mapOf(
                    "error" to (e.message ?: "Unknown error"),
                    "url" to "$apiBaseUrl/health"
                )
            )
        }
    }
}

class LocalStorageHealthCheck : HealthCheck {
    override fun getName(): String = "Local Storage"
    
    override suspend fun check(): HealthCheckResult {
        val startTime = Date().getTime()
        
        return try {
            val testKey = "_health_check_test"
            val testValue = "test_value_${Date().getTime()}"
            
            // Test write
            window.localStorage.setItem(testKey, testValue)
            
            // Test read
            val retrievedValue = window.localStorage.getItem(testKey)
            
            // Test delete
            window.localStorage.removeItem(testKey)
            
            val duration = Date().getTime() - startTime
            val success = retrievedValue == testValue
            
            HealthCheckResult(
                name = getName(),
                status = if (success) HealthStatus.Healthy else HealthStatus.Degraded,
                message = if (success) "Local storage is working" else "Local storage test failed",
                duration = duration,
                timestamp = Date().getTime().toLong(),
                details = mapOf(
                    "write_test" to success.toString(),
                    "storage_available" to (window.localStorage != null).toString()
                )
            )
        } catch (e: Exception) {
            val duration = Date().getTime() - startTime
            
            HealthCheckResult(
                name = getName(),
                status = HealthStatus.Unhealthy,
                message = "Local storage check failed: ${e.message}",
                duration = duration,
                timestamp = Date().getTime().toLong(),
                details = mapOf(
                    "error" to (e.message ?: "Unknown error")
                )
            )
        }
    }
}

class NetworkConnectivityCheck : HealthCheck {
    override fun getName(): String = "Network Connectivity"
    
    override suspend fun check(): HealthCheckResult {
        val startTime = Date().getTime()
        
        return try {
            val isOnline = window.navigator.onLine
            val connectionInfo = getConnectionInfo()
            
            val duration = Date().getTime() - startTime
            val status = if (isOnline) HealthStatus.Healthy else HealthStatus.Unhealthy
            val message = if (isOnline) {
                "Network connection is available"
            } else {
                "No network connection detected"
            }
            
            HealthCheckResult(
                name = getName(),
                status = status,
                message = message,
                duration = duration,
                timestamp = Date().getTime().toLong(),
                details = connectionInfo + mapOf(
                    "online_status" to isOnline.toString()
                )
            )
        } catch (e: Exception) {
            val duration = Date().getTime() - startTime
            
            HealthCheckResult(
                name = getName(),
                status = HealthStatus.Unknown,
                message = "Network check failed: ${e.message}",
                duration = duration,
                timestamp = Date().getTime().toLong(),
                details = mapOf(
                    "error" to (e.message ?: "Unknown error")
                )
            )
        }
    }
    
    private fun getConnectionInfo(): Map<String, String> {
        return try {
            val connection = js("navigator.connection || navigator.mozConnection || navigator.webkitConnection")
            if (connection != null) {
                mapOf(
                    "effective_type" to (connection.effectiveType as? String ?: "unknown"),
                    "downlink" to (connection.downlink?.toString() ?: "unknown"),
                    "rtt" to (connection.rtt?.toString() ?: "unknown"),
                    "save_data" to (connection.saveData?.toString() ?: "unknown")
                )
            } else {
                emptyMap()
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }
}

class PerformanceHealthCheck : HealthCheck {
    override fun getName(): String = "Performance Metrics"
    
    override suspend fun check(): HealthCheckResult {
        val startTime = Date().getTime()
        
        return try {
            val performanceMetrics = gatherPerformanceMetrics()
            val duration = Date().getTime() - startTime
            
            // Determine health based on performance thresholds
            val status = when {
                performanceMetrics.any { (_, value) -> 
                    value.toDoubleOrNull()?.let { it > 5000 } == true 
                } -> HealthStatus.Degraded
                
                performanceMetrics.any { (_, value) -> 
                    value.toDoubleOrNull()?.let { it > 10000 } == true 
                } -> HealthStatus.Unhealthy
                
                else -> HealthStatus.Healthy
            }
            
            HealthCheckResult(
                name = getName(),
                status = status,
                message = "Performance metrics collected",
                duration = duration,
                timestamp = Date().getTime().toLong(),
                details = performanceMetrics
            )
        } catch (e: Exception) {
            val duration = Date().getTime() - startTime
            
            HealthCheckResult(
                name = getName(),
                status = HealthStatus.Unknown,
                message = "Performance check failed: ${e.message}",
                duration = duration,
                timestamp = Date().getTime().toLong(),
                details = mapOf(
                    "error" to (e.message ?: "Unknown error")
                )
            )
        }
    }
    
    private fun gatherPerformanceMetrics(): Map<String, String> {
        return try {
            val navigation = window.performance.timing
            mapOf(
                "page_load_time" to (navigation.loadEventEnd - navigation.navigationStart).toString(),
                "dns_lookup_time" to (navigation.domainLookupEnd - navigation.domainLookupStart).toString(),
                "connection_time" to (navigation.connectEnd - navigation.connectStart).toString(),
                "server_response_time" to (navigation.responseEnd - navigation.requestStart).toString(),
                "dom_processing_time" to (navigation.loadEventStart - navigation.domLoading).toString()
            )
        } catch (e: Exception) {
            mapOf("error" to "Performance timing not available")
        }
    }
}

class HealthCheckRunner {
    private val healthChecks = mutableListOf<HealthCheck>()
    
    fun addCheck(check: HealthCheck) {
        healthChecks.add(check)
    }
    
    fun addDefaultChecks() {
        val environmentInfo = BuildInfoProvider.getEnvironmentInfo()
        
        addCheck(ApiHealthCheck(environmentInfo.apiBaseUrl))
        addCheck(LocalStorageHealthCheck())
        addCheck(NetworkConnectivityCheck())
        addCheck(PerformanceHealthCheck())
    }
    
    suspend fun runAllChecks(): HealthReport {
        val results = mutableListOf<HealthCheckResult>()
        
        healthChecks.forEach { check ->
            try {
                val result = check.check()
                results.add(result)
            } catch (e: Exception) {
                results.add(
                    HealthCheckResult(
                        name = check.getName(),
                        status = HealthStatus.Unhealthy,
                        message = "Health check execution failed: ${e.message}",
                        duration = 0,
                        timestamp = Date().getTime().toLong(),
                        details = mapOf("error" to (e.message ?: "Unknown error"))
                    )
                )
            }
        }
        
        val overallStatus = determineOverallStatus(results)
        
        return HealthReport(
            overallStatus = overallStatus,
            timestamp = Date().getTime().toLong(),
            buildInfo = BuildInfoProvider.getBuildInfo(),
            environmentInfo = BuildInfoProvider.getEnvironmentInfo(),
            checks = results,
            systemInfo = gatherSystemInfo()
        )
    }
    
    private fun determineOverallStatus(results: List<HealthCheckResult>): HealthStatus {
        return when {
            results.any { it.status == HealthStatus.Unhealthy } -> HealthStatus.Unhealthy
            results.any { it.status == HealthStatus.Degraded } -> HealthStatus.Degraded
            results.all { it.status == HealthStatus.Healthy } -> HealthStatus.Healthy
            else -> HealthStatus.Unknown
        }
    }
    
    private fun gatherSystemInfo(): SystemInfo {
        val connection = try {
            val conn = js("navigator.connection || navigator.mozConnection || navigator.webkitConnection")
            conn?.effectiveType as? String
        } catch (e: Exception) {
            null
        }
        
        return SystemInfo(
            userAgent = window.navigator.userAgent,
            platform = window.navigator.platform,
            language = window.navigator.language,
            timezone = js("Intl.DateTimeFormat().resolvedOptions().timeZone") as String,
            screenResolution = "${window.screen.width}x${window.screen.height}",
            viewportSize = "${window.innerWidth}x${window.innerHeight}",
            connectionType = connection,
            onlineStatus = window.navigator.onLine
        )
    }
}

// =============================================================================
// DEPLOYMENT VALIDATION
// =============================================================================

@Serializable
data class DeploymentValidationResult(
    val overallStatus: ValidationStatus,
    val timestamp: Long,
    val buildInfo: BuildInfo,
    val validationChecks: List<ValidationCheckResult>,
    val warnings: List<String> = emptyList(),
    val errors: List<String> = emptyList()
)

@Serializable
data class ValidationCheckResult(
    val name: String,
    val status: ValidationStatus,
    val message: String,
    val details: Map<String, String> = emptyMap()
)

enum class ValidationStatus {
    Pass, Warning, Fail
}

interface ValidationCheck {
    fun check(): ValidationCheckResult
    fun getName(): String
}

class ConfigurationValidationCheck : ValidationCheck {
    override fun getName(): String = "Configuration Validation"
    
    override fun check(): ValidationCheckResult {
        val environmentInfo = BuildInfoProvider.getEnvironmentInfo()
        val buildInfo = BuildInfoProvider.getBuildInfo()
        
        val issues = mutableListOf<String>()
        
        // Check if API URL is configured
        if (environmentInfo.apiBaseUrl.isBlank()) {
            issues.add("API base URL not configured")
        }
        
        // Check if production build has debug mode disabled
        if (environmentInfo.isProduction && environmentInfo.debugMode) {
            issues.add("Debug mode should be disabled in production")
        }
        
        // Check if build information is complete
        if (buildInfo.version == "unknown" || buildInfo.gitCommit == "unknown") {
            issues.add("Incomplete build information")
        }
        
        val status = when {
            issues.isNotEmpty() -> ValidationStatus.Warning
            else -> ValidationStatus.Pass
        }
        
        return ValidationCheckResult(
            name = getName(),
            status = status,
            message = if (issues.isEmpty()) "Configuration is valid" else "Configuration issues found",
            details = mapOf(
                "issues" to issues.joinToString("; "),
                "environment" to environmentInfo.name,
                "build_type" to buildInfo.buildType
            )
        )
    }
}

class BrowserCompatibilityValidationCheck : ValidationCheck {
    override fun getName(): String = "Browser Compatibility"
    
    override fun check(): ValidationCheckResult {
        val unsupportedFeatures = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        
        // Check for critical features
        if (!js("typeof fetch !== 'undefined'")) {
            unsupportedFeatures.add("Fetch API")
        }
        
        if (!js("'Promise' in window")) {
            unsupportedFeatures.add("Promises")
        }
        
        if (!js("'localStorage' in window")) {
            unsupportedFeatures.add("Local Storage")
        }
        
        // Check for modern features that have fallbacks
        if (!js("'IntersectionObserver' in window")) {
            warnings.add("Intersection Observer not supported - using fallback")
        }
        
        if (!js("'ResizeObserver' in window")) {
            warnings.add("Resize Observer not supported - using fallback")
        }
        
        val status = when {
            unsupportedFeatures.isNotEmpty() -> ValidationStatus.Fail
            warnings.isNotEmpty() -> ValidationStatus.Warning
            else -> ValidationStatus.Pass
        }
        
        return ValidationCheckResult(
            name = getName(),
            status = status,
            message = when (status) {
                ValidationStatus.Pass -> "Browser is fully compatible"
                ValidationStatus.Warning -> "Browser has limited compatibility"
                ValidationStatus.Fail -> "Browser lacks critical features"
            },
            details = mapOf(
                "unsupported_features" to unsupportedFeatures.joinToString("; "),
                "warnings" to warnings.joinToString("; "),
                "user_agent" to window.navigator.userAgent
            )
        )
    }
}

class SecurityValidationCheck : ValidationCheck {
    override fun getName(): String = "Security Validation"
    
    override fun check(): ValidationCheckResult {
        val issues = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        
        // Check if running over HTTPS in production
        val environmentInfo = BuildInfoProvider.getEnvironmentInfo()
        if (environmentInfo.isProduction && window.location.protocol != "https:") {
            issues.add("Production site should use HTTPS")
        }
        
        // Check for mixed content
        if (window.location.protocol == "https:" && js("document.querySelector('script[src^=\"http:\"]')") != null) {
            warnings.add("Mixed content detected - HTTP resources on HTTPS page")
        }
        
        // Check CSP header
        val hasCsp = try {
            js("!!document.querySelector('meta[http-equiv=\"Content-Security-Policy\"]')") as Boolean
        } catch (e: Exception) {
            false
        }
        
        if (!hasCsp) {
            warnings.add("Content Security Policy not detected")
        }
        
        val status = when {
            issues.isNotEmpty() -> ValidationStatus.Fail
            warnings.isNotEmpty() -> ValidationStatus.Warning
            else -> ValidationStatus.Pass
        }
        
        return ValidationCheckResult(
            name = getName(),
            status = status,
            message = when (status) {
                ValidationStatus.Pass -> "Security configuration is good"
                ValidationStatus.Warning -> "Security improvements recommended"
                ValidationStatus.Fail -> "Security issues detected"
            },
            details = mapOf(
                "protocol" to window.location.protocol,
                "issues" to issues.joinToString("; "),
                "warnings" to warnings.joinToString("; "),
                "csp_detected" to hasCsp.toString()
            )
        )
    }
}

class DeploymentValidator {
    private val validationChecks = mutableListOf<ValidationCheck>()
    
    fun addCheck(check: ValidationCheck) {
        validationChecks.add(check)
    }
    
    fun addDefaultChecks() {
        addCheck(ConfigurationValidationCheck())
        addCheck(BrowserCompatibilityValidationCheck())
        addCheck(SecurityValidationCheck())
    }
    
    fun runValidation(): DeploymentValidationResult {
        val results = validationChecks.map { check ->
            try {
                check.check()
            } catch (e: Exception) {
                ValidationCheckResult(
                    name = check.getName(),
                    status = ValidationStatus.Fail,
                    message = "Validation check failed: ${e.message}",
                    details = mapOf("error" to (e.message ?: "Unknown error"))
                )
            }
        }
        
        val overallStatus = when {
            results.any { it.status == ValidationStatus.Fail } -> ValidationStatus.Fail
            results.any { it.status == ValidationStatus.Warning } -> ValidationStatus.Warning
            else -> ValidationStatus.Pass
        }
        
        val warnings = results.filter { it.status == ValidationStatus.Warning }
            .map { "${it.name}: ${it.message}" }
        
        val errors = results.filter { it.status == ValidationStatus.Fail }
            .map { "${it.name}: ${it.message}" }
        
        return DeploymentValidationResult(
            overallStatus = overallStatus,
            timestamp = Date().getTime().toLong(),
            buildInfo = BuildInfoProvider.getBuildInfo(),
            validationChecks = results,
            warnings = warnings,
            errors = errors
        )
    }
}

// =============================================================================
// DEPLOYMENT MANAGER
// =============================================================================

class DeploymentManager {
    private val healthCheckRunner = HealthCheckRunner()
    private val deploymentValidator = DeploymentValidator()
    
    init {
        healthCheckRunner.addDefaultChecks()
        deploymentValidator.addDefaultChecks()
    }
    
    fun initialize() {
        logDeploymentInfo()
        runInitialValidation()
        setupPeriodicHealthChecks()
    }
    
    private fun logDeploymentInfo() {
        val buildInfo = BuildInfoProvider.getBuildInfo()
        val environmentInfo = BuildInfoProvider.getEnvironmentInfo()
        
        console.log("=== Khoded Website Deployment ===")
        console.log("Version: ${buildInfo.version}")
        console.log("Build: ${buildInfo.buildNumber}")
        console.log("Environment: ${environmentInfo.name}")
        console.log("Build Date: ${buildInfo.buildDate}")
        console.log("Git Commit: ${buildInfo.gitCommit}")
        console.log("Git Branch: ${buildInfo.gitBranch}")
        console.log("API Base URL: ${environmentInfo.apiBaseUrl}")
        console.log("Debug Mode: ${environmentInfo.debugMode}")
        console.log("==================================")
    }
    
    private fun runInitialValidation() {
        val validationResult = deploymentValidator.runValidation()
        
        when (validationResult.overallStatus) {
            ValidationStatus.Pass -> {
                console.log("✅ Deployment validation passed")
            }
            ValidationStatus.Warning -> {
                console.warn("⚠️ Deployment validation has warnings:")
                validationResult.warnings.forEach { warning ->
                    console.warn("  - $warning")
                }
            }
            ValidationStatus.Fail -> {
                console.error("❌ Deployment validation failed:")
                validationResult.errors.forEach { error ->
                    console.error("  - $error")
                }
            }
        }
    }
    
    private fun setupPeriodicHealthChecks() {
        // Run health checks every 5 minutes
        val intervalId = js("setInterval(() => { this.runHealthCheck() }, 5 * 60 * 1000)")
        
        // Run initial health check
        runHealthCheck()
    }
    
    private fun runHealthCheck() {
        GlobalScope.launch {
            try {
                val healthReport = healthCheckRunner.runAllChecks()
                
                when (healthReport.overallStatus) {
                    HealthStatus.Healthy -> {
                        console.log("✅ Health check passed - all systems healthy")
                    }
                    HealthStatus.Degraded -> {
                        console.warn("⚠️ Health check shows degraded performance")
                        healthReport.checks.filter { it.status == HealthStatus.Degraded }
                            .forEach { check ->
                                console.warn("  - ${check.name}: ${check.message}")
                            }
                    }
                    HealthStatus.Unhealthy -> {
                        console.error("❌ Health check failed")
                        healthReport.checks.filter { it.status == HealthStatus.Unhealthy }
                            .forEach { check ->
                                console.error("  - ${check.name}: ${check.message}")
                            }
                    }
                    HealthStatus.Unknown -> {
                        console.warn("❓ Health check status unknown")
                    }
                }
                
                // Send health report to monitoring service
                sendHealthReport(healthReport)
                
            } catch (e: Exception) {
                console.error("Health check execution failed", e)
            }
        }
    }
    
    private fun sendHealthReport(report: HealthReport) {
        try {
            val payload = Json.encodeToString(report)
            window.fetch("/api/health/report", js("""({
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: payload
            })"""))
        } catch (e: Exception) {
            // Health reporting failed - continue silently to avoid affecting app
            console.warn("Failed to send health report", e)
        }
    }
    
    fun getStatus(): Map<String, Any> {
        val buildInfo = BuildInfoProvider.getBuildInfo()
        val environmentInfo = BuildInfoProvider.getEnvironmentInfo()
        
        return mapOf(
            "status" to "running",
            "version" to buildInfo.version,
            "environment" to environmentInfo.name,
            "timestamp" to Date().getTime()
        )
    }
}