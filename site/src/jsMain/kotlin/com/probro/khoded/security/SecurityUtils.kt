package com.probro.khoded.security

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date
import kotlin.random.Random

/**
 * Security & Privacy Implementation
 * 
 * Comprehensive security utilities including:
 * - Content Security Policy
 * - Input sanitization and validation
 * - CSRF protection
 * - Rate limiting
 * - Privacy controls and cookie management
 * - Secure data handling
 */

// =============================================================================
// CONTENT SECURITY POLICY
// =============================================================================

object ContentSecurityPolicy {
    fun initialize() {
        val cspDirectives = mapOf(
            "default-src" to listOf("'self'"),
            "script-src" to listOf(
                "'self'",
                "'unsafe-inline'", // Required for Kotlin/JS
                "https://cdn.jsdelivr.net",
                "https://unpkg.com",
                "https://www.googletagmanager.com",
                "https://www.google-analytics.com"
            ),
            "style-src" to listOf(
                "'self'",
                "'unsafe-inline'", // Required for dynamic styles
                "https://fonts.googleapis.com",
                "https://cdn.jsdelivr.net"
            ),
            "font-src" to listOf(
                "'self'",
                "https://fonts.gstatic.com",
                "data:"
            ),
            "img-src" to listOf(
                "'self'",
                "data:",
                "https:",
                "blob:"
            ),
            "connect-src" to listOf(
                "'self'",
                "https://api.khoded.com",
                "https://www.google-analytics.com"
            ),
            "media-src" to listOf("'self'"),
            "object-src" to listOf("'none'"),
            "frame-src" to listOf(
                "'self'",
                "https://www.youtube.com",
                "https://player.vimeo.com"
            ),
            "worker-src" to listOf("'self'", "blob:"),
            "manifest-src" to listOf("'self'"),
            "form-action" to listOf("'self'"),
            "frame-ancestors" to listOf("'none'"),
            "base-uri" to listOf("'self'"),
            "upgrade-insecure-requests" to emptyList()
        )
        
        val cspValue = cspDirectives.entries.joinToString("; ") { (directive, sources) ->
            if (sources.isEmpty()) directive
            else "$directive ${sources.joinToString(" ")}"
        }
        
        // Set CSP via meta tag (backup for when server headers aren't available)
        val metaCSP = document.createElement("meta").apply {
            setAttribute("http-equiv", "Content-Security-Policy")
            setAttribute("content", cspValue)
        }
        document.head?.appendChild(metaCSP)
        
        // Report violations
        setupCSPReporting()
    }
    
    private fun setupCSPReporting() {
        // Listen for CSP violations
        document.addEventListener("securitypolicyviolation") { event ->
            val violation = event.asDynamic()
            val report = mapOf(
                "violatedDirective" to violation.violatedDirective,
                "blockedURI" to violation.blockedURI,
                "originalPolicy" to violation.originalPolicy,
                "sourceFile" to violation.sourceFile,
                "lineNumber" to violation.lineNumber,
                "columnNumber" to violation.columnNumber
            )
            
            console.warn("CSP Violation:", report)
            
            // Send to monitoring service
            reportSecurityViolation("csp_violation", report)
        }
    }
}

// =============================================================================
// INPUT SANITIZATION
// =============================================================================

object InputSanitizer {
    fun sanitizeHtml(input: String): String {
        return input
            .replace(Regex("<script[^>]*>.*?</script>"), "") // Remove script tags
            .replace(Regex("javascript:", flags = setOf(RegexOption.IGNORE_CASE)), "") // Remove javascript: URLs
            .replace(Regex("on\\w+\\s*=", flags = setOf(RegexOption.IGNORE_CASE)), "") // Remove event handlers
            .replace(Regex("<iframe[^>]*>.*?</iframe>"), "") // Remove iframes
            .replace(Regex("<embed[^>]*>"), "") // Remove embed tags
            .replace(Regex("<object[^>]*>.*?</object>"), "") // Remove object tags
    }
    
    fun sanitizeText(input: String): String {
        return input
            .replace(Regex("[<>\"'&]")) { matchResult ->
                when (matchResult.value) {
                    "<" -> "&lt;"
                    ">" -> "&gt;"
                    "\"" -> "&quot;"
                    "'" -> "&#x27;"
                    "&" -> "&amp;"
                    else -> matchResult.value
                }
            }
    }
    
    fun sanitizeEmail(input: String): String {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
        return if (input.matches(emailRegex) && input.length <= 254) {
            input.trim().lowercase()
        } else {
            throw SecurityException("Invalid email format")
        }
    }
    
    fun sanitizePhoneNumber(input: String): String {
        val digitsOnly = input.replace(Regex("[^\\d+\\-\\(\\)\\s]"), "")
        val cleanDigits = digitsOnly.replace(Regex("[^\\d]"), "")
        
        return if (cleanDigits.length in 10..15) {
            digitsOnly.trim()
        } else {
            throw SecurityException("Invalid phone number format")
        }
    }
    
    fun sanitizeUrl(input: String): String {
        val allowedProtocols = listOf("http://", "https://", "mailto:", "tel:")
        val lowercaseInput = input.trim().lowercase()
        
        if (!allowedProtocols.any { lowercaseInput.startsWith(it) }) {
            throw SecurityException("Invalid URL protocol")
        }
        
        // Check for common XSS patterns
        if (lowercaseInput.contains("javascript:") || 
            lowercaseInput.contains("data:") ||
            lowercaseInput.contains("vbscript:")) {
            throw SecurityException("Potentially malicious URL")
        }
        
        return input.trim()
    }
}

// =============================================================================
// CSRF PROTECTION
// =============================================================================

object CSRFProtection {
    private const val TOKEN_STORAGE_KEY = "csrf_token"
    private const val TOKEN_HEADER_NAME = "X-CSRF-Token"
    
    fun generateToken(): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..32).map { charset.random() }.joinToString("")
    }
    
    fun getToken(): String {
        return window.sessionStorage.getItem(TOKEN_STORAGE_KEY) ?: run {
            val newToken = generateToken()
            window.sessionStorage.setItem(TOKEN_STORAGE_KEY, newToken)
            newToken
        }
    }
    
    fun addTokenToHeaders(headers: dynamic): dynamic {
        headers[TOKEN_HEADER_NAME] = getToken()
        return headers
    }
    
    fun addTokenToFormData(formData: FormData): FormData {
        formData.append("_csrf_token", getToken())
        return formData
    }
    
    fun validateToken(token: String): Boolean {
        return token == getToken() && token.isNotBlank()
    }
    
    fun rotateToken() {
        window.sessionStorage.removeItem(TOKEN_STORAGE_KEY)
    }
}

// =============================================================================
// RATE LIMITING
// =============================================================================

class RateLimiter(
    private val maxRequests: Int = 10,
    private val windowMs: Long = 60000, // 1 minute
    private val storageKey: String = "rate_limit"
) {
    @Serializable
    private data class RateLimitData(
        val requests: List<Long> = emptyList(),
        val windowStart: Long = 0
    )
    
    fun isAllowed(identifier: String = "default"): Boolean {
        val key = "${storageKey}_$identifier"
        val now = Date().getTime().toLong()
        
        val currentData = try {
            window.localStorage.getItem(key)?.let { 
                Json.decodeFromString<RateLimitData>(it)
            } ?: RateLimitData()
        } catch (e: Exception) {
            RateLimitData()
        }
        
        // Clean old requests outside the window
        val validRequests = currentData.requests.filter { 
            now - it < windowMs 
        }
        
        // Check if limit is exceeded
        if (validRequests.size >= maxRequests) {
            return false
        }
        
        // Add current request
        val newData = RateLimitData(
            requests = validRequests + now,
            windowStart = if (validRequests.isEmpty()) now else currentData.windowStart
        )
        
        try {
            window.localStorage.setItem(key, Json.encodeToString(newData))
        } catch (e: Exception) {
            // Storage might be full, clean up old entries
            cleanupStorage()
        }
        
        return true
    }
    
    fun getRemainingRequests(identifier: String = "default"): Int {
        val key = "${storageKey}_$identifier"
        val now = Date().getTime().toLong()
        
        val currentData = try {
            window.localStorage.getItem(key)?.let { 
                Json.decodeFromString<RateLimitData>(it)
            } ?: RateLimitData()
        } catch (e: Exception) {
            RateLimitData()
        }
        
        val validRequests = currentData.requests.filter { 
            now - it < windowMs 
        }
        
        return maxOf(0, maxRequests - validRequests.size)
    }
    
    fun getResetTime(identifier: String = "default"): Long {
        val key = "${storageKey}_$identifier"
        val now = Date().getTime().toLong()
        
        val currentData = try {
            window.localStorage.getItem(key)?.let { 
                Json.decodeFromString<RateLimitData>(it)
            } ?: RateLimitData()
        } catch (e: Exception) {
            RateLimitData()
        }
        
        val oldestRequest = currentData.requests.minOrNull()
        return if (oldestRequest != null) {
            oldestRequest + windowMs
        } else {
            now
        }
    }
    
    private fun cleanupStorage() {
        try {
            val keysToRemove = mutableListOf<String>()
            
            for (i in 0 until window.localStorage.length) {
                val key = window.localStorage.key(i)
                if (key?.startsWith(storageKey) == true) {
                    keysToRemove.add(key)
                }
            }
            
            // Remove old rate limit entries
            keysToRemove.take(keysToRemove.size / 2).forEach { key ->
                window.localStorage.removeItem(key)
            }
        } catch (e: Exception) {
            console.warn("Failed to cleanup rate limit storage")
        }
    }
}

// =============================================================================
// PRIVACY CONTROLS
// =============================================================================

@Serializable
data class PrivacyPreferences(
    val analytics: Boolean = false,
    val marketing: Boolean = false,
    val functional: Boolean = true,
    val performance: Boolean = false,
    val lastUpdated: Long = Date().getTime().toLong()
)

class PrivacyManager {
    private val storageKey = "privacy_preferences"
    private val _preferences = mutableStateOf(getStoredPreferences())
    
    val preferences: State<PrivacyPreferences> = _preferences
    
    fun updatePreferences(newPreferences: PrivacyPreferences) {
        val updatedPreferences = newPreferences.copy(lastUpdated = Date().getTime().toLong())
        _preferences.value = updatedPreferences
        
        try {
            window.localStorage.setItem(storageKey, Json.encodeToString(updatedPreferences))
        } catch (e: Exception) {
            console.warn("Failed to store privacy preferences")
        }
        
        // Apply preferences immediately
        applyPrivacyPreferences(updatedPreferences)
    }
    
    fun hasConsent(type: ConsentType): Boolean {
        return when (type) {
            ConsentType.Analytics -> _preferences.value.analytics
            ConsentType.Marketing -> _preferences.value.marketing
            ConsentType.Functional -> _preferences.value.functional
            ConsentType.Performance -> _preferences.value.performance
        }
    }
    
    fun requiresConsent(): Boolean {
        // Check if consent is required (EU/UK visitors, CCPA, etc.)
        return isEUVisitor() || isCCPAApplicable()
    }
    
    private fun getStoredPreferences(): PrivacyPreferences {
        return try {
            window.localStorage.getItem(storageKey)?.let {
                Json.decodeFromString<PrivacyPreferences>(it)
            } ?: PrivacyPreferences()
        } catch (e: Exception) {
            PrivacyPreferences()
        }
    }
    
    private fun applyPrivacyPreferences(prefs: PrivacyPreferences) {
        // Analytics
        if (prefs.analytics) {
            enableAnalytics()
        } else {
            disableAnalytics()
        }
        
        // Marketing
        if (prefs.marketing) {
            enableMarketingCookies()
        } else {
            disableMarketingCookies()
        }
        
        // Performance
        if (prefs.performance) {
            enablePerformanceTracking()
        } else {
            disablePerformanceTracking()
        }
    }
    
    private fun isEUVisitor(): Boolean {
        // Simple timezone-based detection
        val timeZone = try {
            js("Intl.DateTimeFormat().resolvedOptions().timeZone") as String
        } catch (e: Exception) {
            ""
        }
        
        val euTimeZones = listOf(
            "Europe/", "Atlantic/Azores", "Atlantic/Canary", "Atlantic/Faroe", "Atlantic/Madeira"
        )
        
        return euTimeZones.any { timeZone.startsWith(it) }
    }
    
    private fun isCCPAApplicable(): Boolean {
        // Simple check for California users
        // In production, this would use more sophisticated geolocation
        return false // Placeholder
    }
    
    private fun enableAnalytics() {
        try {
            // Enable Google Analytics
            js("""
                if (typeof gtag === 'function') {
                    gtag('consent', 'update', {
                        'analytics_storage': 'granted'
                    });
                }
            """)
        } catch (e: Exception) {
            console.warn("Failed to enable analytics")
        }
    }
    
    private fun disableAnalytics() {
        try {
            js("""
                if (typeof gtag === 'function') {
                    gtag('consent', 'update', {
                        'analytics_storage': 'denied'
                    });
                }
            """)
        } catch (e: Exception) {
            console.warn("Failed to disable analytics")
        }
    }
    
    private fun enableMarketingCookies() {
        try {
            js("""
                if (typeof gtag === 'function') {
                    gtag('consent', 'update', {
                        'ad_storage': 'granted'
                    });
                }
            """)
        } catch (e: Exception) {
            console.warn("Failed to enable marketing cookies")
        }
    }
    
    private fun disableMarketingCookies() {
        try {
            js("""
                if (typeof gtag === 'function') {
                    gtag('consent', 'update', {
                        'ad_storage': 'denied'
                    });
                }
            """)
        } catch (e: Exception) {
            console.warn("Failed to disable marketing cookies")
        }
    }
    
    private fun enablePerformanceTracking() {
        // Enable performance monitoring
    }
    
    private fun disablePerformanceTracking() {
        // Disable performance monitoring
    }
}

enum class ConsentType {
    Analytics, Marketing, Functional, Performance
}

@Composable
fun rememberPrivacyManager(): PrivacyManager {
    return remember { PrivacyManager() }
}

// =============================================================================
// COOKIE MANAGEMENT
// =============================================================================

object CookieManager {
    fun setCookie(
        name: String, 
        value: String, 
        expiryDays: Int = 30,
        secure: Boolean = true,
        httpOnly: Boolean = false,
        sameSite: String = "Strict"
    ) {
        val expires = Date(Date().getTime() + (expiryDays * 24 * 60 * 60 * 1000)).toUTCString()
        
        var cookie = "$name=$value; expires=$expires; path=/"
        
        if (secure && window.location.protocol == "https:") {
            cookie += "; Secure"
        }
        
        if (httpOnly) {
            cookie += "; HttpOnly"
        }
        
        cookie += "; SameSite=$sameSite"
        
        document.cookie = cookie
    }
    
    fun getCookie(name: String): String? {
        val cookies = document.cookie.split(";")
        
        for (cookie in cookies) {
            val parts = cookie.trim().split("=", limit = 2)
            if (parts.size == 2 && parts[0] == name) {
                return parts[1]
            }
        }
        
        return null
    }
    
    fun deleteCookie(name: String) {
        document.cookie = "$name=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
    }
    
    fun deleteAllCookies() {
        val cookies = document.cookie.split(";")
        
        cookies.forEach { cookie ->
            val name = cookie.split("=")[0].trim()
            if (name.isNotEmpty()) {
                deleteCookie(name)
            }
        }
    }
}

// =============================================================================
// SECURE DATA HANDLING
// =============================================================================

object SecureDataHandler {
    fun hashData(data: String): String {
        // Simple hash function - in production, use a proper crypto library
        return data.hashCode().toString()
    }
    
    fun generateSecureId(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val timestamp = Date().getTime().toString()
        val random = (1..16).map { chars.random() }.joinToString("")
        return "${timestamp}-${random}"
    }
    
    fun maskEmail(email: String): String {
        val parts = email.split("@")
        if (parts.size != 2) return email
        
        val localPart = parts[0]
        val domain = parts[1]
        
        val maskedLocal = if (localPart.length > 2) {
            "${localPart.first()}${"*".repeat(localPart.length - 2)}${localPart.last()}"
        } else {
            "*".repeat(localPart.length)
        }
        
        return "$maskedLocal@$domain"
    }
    
    fun maskPhoneNumber(phone: String): String {
        val digits = phone.replace(Regex("[^\\d]"), "")
        return if (digits.length >= 10) {
            val visible = digits.takeLast(4)
            "*".repeat(digits.length - 4) + visible
        } else {
            phone
        }
    }
    
    fun validateDataIntegrity(data: String, expectedHash: String): Boolean {
        return hashData(data) == expectedHash
    }
}

// =============================================================================
// SECURITY MONITORING
// =============================================================================

private fun reportSecurityViolation(type: String, details: Map<String, Any>) {
    try {
        val report = mapOf(
            "type" to type,
            "timestamp" to Date().getTime(),
            "userAgent" to window.navigator.userAgent,
            "url" to window.location.href,
            "details" to details
        )
        
        // Send to monitoring service
        console.warn("Security Violation Reported:", report)
        
        // In production, send to your security monitoring endpoint
        // fetch("/api/security/report", { method: "POST", body: JSON.stringify(report) })
        
    } catch (e: Exception) {
        console.error("Failed to report security violation:", e)
    }
}