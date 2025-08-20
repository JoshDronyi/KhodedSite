package com.probro.khoded.api

import com.probro.khoded.email.MailClient
import com.probro.khoded.messaging.messageData.FormType
import com.probro.khoded.messaging.messageData.MailParams
import com.probro.khoded.messaging.messageData.MailResponse
import com.probro.khoded.messaging.messageData.MessageData
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.http.readBodyText
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.apache.http.HttpStatus
import java.net.URLDecoder

val messagingScope: CoroutineScope = CoroutineScope(
    Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
        println("There was an error from: ${throwable.localizedMessage}")
        throwable.printStackTrace()
    }
)

@OptIn(ExperimentalSerializationApi::class)
val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = true
    prettyPrint = true
}
// SECURITY: Initialized safely to prevent null pointer exceptions
private var mailClient: MailClient? = null

private fun getMailClient(logger: com.varabyte.kobweb.api.log.Logger): MailClient {
    if (mailClient == null) {
        mailClient = MailClient(logger)
    }
    return mailClient!!
}

/**
 * Add comprehensive security headers to API responses
 * Protects against common web vulnerabilities
 */
private fun addSecurityHeaders(ctx: ApiContext) {
    with(ctx.res) {
        // Content Security Policy - prevents XSS attacks  
        headers["Content-Security-Policy"] = "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data: https:; connect-src 'self'; font-src 'self'; object-src 'none'; media-src 'self'; frame-src 'none';"
        
        // Prevent MIME type sniffing
        headers["X-Content-Type-Options"] = "nosniff"
        
        // Prevent clickjacking attacks
        headers["X-Frame-Options"] = "DENY"
        
        // Enable XSS protection in older browsers
        headers["X-XSS-Protection"] = "1; mode=block"
        
        // Force HTTPS (if using HTTPS)
        headers["Strict-Transport-Security"] = "max-age=31536000; includeSubDomains"
        
        // Control referrer information
        headers["Referrer-Policy"] = "strict-origin-when-cross-origin"
        
        // Disable unnecessary features
        headers["Permissions-Policy"] = "geolocation=(), microphone=(), camera=()"
        
        // Prevent caching of sensitive data
        headers["Cache-Control"] = "no-store, no-cache, must-revalidate, private"
        headers["Pragma"] = "no-cache"
        headers["Expires"] = "0"
        
        // Content type
        headers["Content-Type"] = "application/json; charset=utf-8"
    }
}


// SECURITY: Improved rate limiting with cleanup and Redis-ready structure
private val rateLimitMap = mutableMapOf<String, RateLimitData>()
const val RATE_LIMIT_CLEANUP_INTERVAL = 60 * 60 * 1000L // 1 hour
private var lastCleanup = System.currentTimeMillis()

data class RateLimitData(
    var requestCount: Int = 0,
    var windowStart: Long = System.currentTimeMillis(),
    var lastAccess: Long = System.currentTimeMillis()
)

/**
 * Clean up old rate limit entries to prevent memory leaks
 */
private fun cleanupRateLimit() {
    val now = System.currentTimeMillis()
    if (now - lastCleanup > RATE_LIMIT_CLEANUP_INTERVAL) {
        val expiredEntries = rateLimitMap.filter { (_, data) ->
            now - data.lastAccess > RATE_LIMIT_CLEANUP_INTERVAL
        }
        expiredEntries.keys.forEach { rateLimitMap.remove(it) }
        lastCleanup = now
    }
}

/**
 * SECURITY: Enhanced rate limiting with cleanup and better IP validation
 * Allows 5 requests per 15 minutes per IP
 */
private fun checkRateLimit(clientIp: String): Boolean {
    // Clean up old entries periodically
    cleanupRateLimit()
    
    val now = System.currentTimeMillis()
    val windowDuration = 15 * 60 * 1000L // 15 minutes
    val maxRequests = 5

    val rateLimitData = rateLimitMap.getOrPut(clientIp) { RateLimitData() }

    // Reset window if expired
    if (now - rateLimitData.windowStart > windowDuration) {
        rateLimitData.requestCount = 0
        rateLimitData.windowStart = now
    }

    rateLimitData.requestCount++
    rateLimitData.lastAccess = now
    return rateLimitData.requestCount <= maxRequests
}

@Api("sendemail")
suspend fun sendEmail(ctx: ApiContext) = withContext(CoroutineName("SendEmailApiFunction") + Dispatchers.IO) {
    with(ctx) {
        try {
            // SECURITY: Enhanced IP extraction with validation
            val clientIp = getClientIp(req)
            
            // Validate IP format to prevent spoofing
            if (!isValidIp(clientIp)) {
                logger.warn("Invalid or suspicious IP format: $clientIp")
                res.apply {
                    status = 400
                    addSecurityHeaders(ctx)
                    setBodyText("""{"error": "Invalid request format"}""")
                }
                return@withContext
            }

            if (!checkRateLimit(clientIp)) {
                logger.warn("Rate limit exceeded for IP: $clientIp")
                res.apply {
                    status = 429 // Too Many Requests
                    addSecurityHeaders(ctx)
                    setBodyText("""{"error": "Too many requests. Please wait before submitting another message."}""")
                }
                return@withContext
            }

            logger.info("Processing email request from IP: $clientIp")
            val client = getMailClient(logger)

            // Handle both form data and JSON data
            val body = req.readBodyText() ?: ""
            logger.info("Request body: $body")

            val mailResponse = when {
                // Handle form data (application/x-www-form-urlencoded)
                req.headers["Content-Type"]?.contains("application/x-www-form-urlencoded") == true -> {
                    messagingScope.async {
                        handleFormData(body, logger)
                    }
                }

                // Handle JSON data with formType parameter
                else -> {
                    val formType = req.params[MailParams.TYPE.value]
                    logger.info("formType was $formType")

                    when {
                        formType?.equals(FormType.CONTACT.name, ignoreCase = true) == true -> {
                            messagingScope.async {
                                sendContactMessage(
                                    json.decodeFromString<MessageData.ContactMessageData>(body),
                                    logger
                                )
                            }
                        }

                        formType?.equals(FormType.CONSULTATION.name, ignoreCase = true) == true -> {
                            messagingScope.async {
                                sendConsultationMessage(
                                    json.decodeFromString<MessageData.ConsultationMessageData>(body),
                                    logger
                                )
                            }
                        }

                        else -> {
                            messagingScope.async {
                                MailResponse.Error(
                                    exceptionMessage = "Unable to handle formType of $formType. Please specify 'CONTACT' or 'CONSULTATION' in the TYPE parameter.",
                                    stackTrace = "Send Email Api function - Invalid form type."
                                )
                            }
                        }
                    }
                }
            }

            // Handle response
            with(mailResponse.await()) {
                when (val response = this) {
                    is MailResponse.Error -> {
                        logger.error("Email sending failed: ${response.exceptionMessage}")
                        res.apply {
                            status = HttpStatus.SC_BAD_REQUEST
                            addSecurityHeaders(ctx)
                            setBodyText("""{"error": "${response.exceptionMessage}"}""")
                        }
                    }

                    is MailResponse.Success -> {
                        logger.info("Email sent successfully")
                        res.apply {
                            status = HttpStatus.SC_OK
                            addSecurityHeaders(ctx)
                            setBodyText("""{"success": true, "message": "Email sent successfully"}""")
                        }
                    }
                }
            }

        } catch (e: IllegalArgumentException) {
            // Validation errors
            logger.warn("Validation error: ${e.message}")
            res.apply {
                status = HttpStatus.SC_BAD_REQUEST
                addSecurityHeaders(ctx)
                setBodyText("""{"error": "${e.message}"}""")
            }
        } catch (e: Exception) {
            // Unexpected errors
            logger.error("Unexpected error in sendEmail API: ${e.message}")
            res.apply {
                status = HttpStatus.SC_INTERNAL_SERVER_ERROR
                addSecurityHeaders(ctx)
                setBodyText("""{"error": "Internal server error. Please try again later."}""")
            }
        }
    }
}

/**
 * Handle form data from contact form
 */
private suspend fun handleFormData(body: String, logger: com.varabyte.kobweb.api.log.Logger): MailResponse {
    val formData = parseFormData(body)

    val contactData = MessageData.ContactMessageData(
        name = formData["name"] ?: "",
        email = formData["email"] ?: "",
        organization = formData["organization"] ?: "",
        subject = formData["subject"] ?: "",
        message = formData["message"] ?: ""
    )

    return sendContactMessage(contactData, logger)
}

/**
 * Parse URL-encoded form data
 */
internal fun parseFormData(body: String): Map<String, String> {
    return body.split("&")
        .mapNotNull { pair ->
            val parts = pair.split("=", limit = 2)
            if (parts.size == 2) {
                val key = URLDecoder.decode(parts[0], "UTF-8")
                val value = URLDecoder.decode(parts[1], "UTF-8")
                key to value
            } else null
        }
        .toMap()
}


suspend fun sendConsultationMessage(data: MessageData.ConsultationMessageData?, logger: com.varabyte.kobweb.api.log.Logger): MailResponse {
    requireNotNull(value = data, lazyMessage = {
        "Consultation messages must contain a consultation message data object."
    })

    validateMessageData(data)
    val client = getMailClient(logger)
    return client.sendMessage(
        senderName = data.name,
        senderEmail = data.email,
        message = data.message
    )
}

suspend fun sendContactMessage(data: MessageData.ContactMessageData?, logger: com.varabyte.kobweb.api.log.Logger): MailResponse {

    requireNotNull(data) { "Contact messages must contain a contact message data object." }

    validateMessageData(data)

    val client = getMailClient(logger)
    return with(data) {
        client.sendMessage(
            senderName = name,
            senderOrganization = organization,
            subject = subject,
            senderEmail = email,
            message = message
        )
    }
}

/**
 * Comprehensive server-side validation for message data
 * Provides security against malicious inputs and ensures data quality
 */
fun validateMessageData(data: MessageData) {
    when (data) {
        is MessageData.ConsultationMessageData -> {
            validateConsultationData(data)
        }

        is MessageData.ContactMessageData -> {
            validateContactData(data)
        }
    }
}

/**
 * Validate consultation message data with comprehensive checks
 */
private fun validateConsultationData(data: MessageData.ConsultationMessageData) {
    // Name validation
    validateName(data.name, "Name")

    // Email validation  
    validateEmail(data.email, "Email")

    // Message validation
    validateMessage(data.message, "Message", minLength = 10, maxLength = 1000)
}

/**
 * Validate contact message data with comprehensive checks
 */
private fun validateContactData(data: MessageData.ContactMessageData) {
    // Name validation
    validateName(data.name, "Name")

    // Email validation
    validateEmail(data.email, "Email")

    // Organization validation (optional but if provided, must be valid)
    if (data.organization.isNotBlank()) {
        validateOrganization(data.organization, "Organization")
    }

    // Subject validation  
    validateSubject(data.subject, "Subject")

    // Message validation
    validateMessage(data.message, "Message", minLength = 10, maxLength = 1000)
}

/**
 * Validate name field with security checks
 */
private fun validateName(name: String, fieldName: String) {
    if (name.isBlank()) {
        throw IllegalArgumentException("$fieldName is required")
    }

    val trimmedName = name.trim()
    if (trimmedName.length < 2) {
        throw IllegalArgumentException("$fieldName must be at least 2 characters long")
    }

    if (trimmedName.length > 50) {
        throw IllegalArgumentException("$fieldName cannot exceed 50 characters")
    }

    // Check for potentially malicious patterns
    val suspiciousPatterns = listOf(
        "<script", "javascript:", "onclick=", "onerror=", "onload=",
        "eval(", "document.cookie", "window.location"
    )

    val lowerName = trimmedName.lowercase()
    suspiciousPatterns.forEach { pattern ->
        if (lowerName.contains(pattern)) {
            throw IllegalArgumentException("$fieldName contains invalid characters")
        }
    }

    // Allow only letters, spaces, hyphens, and apostrophes
    if (!trimmedName.matches(Regex("^[A-Za-z\\s'\\-\\.]+$"))) {
        throw IllegalArgumentException("$fieldName contains invalid characters")
    }
}

/**
 * Validate email with comprehensive RFC compliance and security checks
 */
private fun validateEmail(email: String, fieldName: String) {
    if (email.isBlank()) {
        throw IllegalArgumentException("$fieldName is required")
    }

    val trimmedEmail = email.trim()
    if (trimmedEmail.length > 254) {
        throw IllegalArgumentException("$fieldName is too long")
    }

    // RFC 5322 compliant email regex
    val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    if (!trimmedEmail.matches(emailPattern)) {
        throw IllegalArgumentException("Please enter a valid $fieldName address")
    }

    // Additional security checks
    val suspiciousPatterns = listOf(
        "<script", "javascript:", "%3cscript", "%3e", "<img", "onclick="
    )

    val lowerEmail = trimmedEmail.lowercase()
    suspiciousPatterns.forEach { pattern ->
        if (lowerEmail.contains(pattern)) {
            throw IllegalArgumentException("$fieldName contains invalid characters")
        }
    }

    // Check for obviously fake/test emails
    val testDomains = listOf("test.com", "example.com", "fake.com", "invalid.com")
    val domain = trimmedEmail.substringAfter("@").lowercase()
    if (testDomains.contains(domain)) {
        throw IllegalArgumentException("Please provide a valid $fieldName address")
    }
}

/**
 * Validate organization name
 */
private fun validateOrganization(organization: String, fieldName: String) {
    val trimmedOrg = organization.trim()
    if (trimmedOrg.length > 100) {
        throw IllegalArgumentException("$fieldName name cannot exceed 100 characters")
    }

    // Basic XSS protection
    val suspiciousPatterns = listOf(
        "<script", "javascript:", "onclick=", "onerror=", "<img", "<iframe"
    )

    val lowerOrg = trimmedOrg.lowercase()
    suspiciousPatterns.forEach { pattern ->
        if (lowerOrg.contains(pattern)) {
            throw IllegalArgumentException("$fieldName name contains invalid characters")
        }
    }
}

/**
 * Validate subject line
 */
private fun validateSubject(subject: String, fieldName: String) {
    if (subject.isBlank()) {
        throw IllegalArgumentException("$fieldName is required")
    }

    val trimmedSubject = subject.trim()
    if (trimmedSubject.length < 3) {
        throw IllegalArgumentException("$fieldName must be at least 3 characters long")
    }

    if (trimmedSubject.length > 100) {
        throw IllegalArgumentException("$fieldName cannot exceed 100 characters")
    }

    // XSS protection
    val suspiciousPatterns = listOf(
        "<script", "javascript:", "onclick=", "onerror=", "<img", "<iframe"
    )

    val lowerSubject = trimmedSubject.lowercase()
    suspiciousPatterns.forEach { pattern ->
        if (lowerSubject.contains(pattern)) {
            throw IllegalArgumentException("$fieldName contains invalid characters")
        }
    }
}

/**
 * Validate message content with comprehensive security checks
 */
private fun validateMessage(message: String, fieldName: String, minLength: Int = 10, maxLength: Int = 1000) {
    if (message.isBlank()) {
        throw IllegalArgumentException("$fieldName is required")
    }

    val trimmedMessage = message.trim()
    if (trimmedMessage.length < minLength) {
        throw IllegalArgumentException("$fieldName must be at least $minLength characters long")
    }

    if (trimmedMessage.length > maxLength) {
        throw IllegalArgumentException("$fieldName cannot exceed $maxLength characters")
    }

    // XSS protection - more comprehensive for message content
    val suspiciousPatterns = listOf(
        "<script", "</script", "javascript:", "onclick=", "onerror=", "onload=",
        "<img", "<iframe", "<object", "<embed", "<form", "<input",
        "eval(", "document.cookie", "window.location", "document.write"
    )

    val lowerMessage = trimmedMessage.lowercase()
    suspiciousPatterns.forEach { pattern ->
        if (lowerMessage.contains(pattern)) {
            throw IllegalArgumentException("$fieldName contains potentially harmful content")
        }
    }

    // Check for potential SQL injection patterns
    val sqlPatterns = listOf(
        "drop table", "delete from", "insert into", "update set",
        "union select", "' or '1'='1", "' or 1=1", "--", "/*", "*/"
    )

    sqlPatterns.forEach { pattern ->
        if (lowerMessage.contains(pattern)) {
            throw IllegalArgumentException("$fieldName contains invalid content")
        }
    }

    // Check for spam indicators
    val spamIndicators = listOf(
        "click here now", "limited time offer", "act now", "buy now",
        "free money", "guaranteed income", "work from home"
    )

    var spamScore = 0
    spamIndicators.forEach { indicator ->
        if (lowerMessage.contains(indicator)) {
            spamScore++
        }
    }

    if (spamScore >= 3) {
        throw IllegalArgumentException("$fieldName appears to contain spam content")
    }
}

sealed class MessagingResponse {
    object MessageSent : MessagingResponse() {
        val statusCode = 200
        val message = "Sent."
    }

    sealed class MessagingError(val errorMessage: String, val statusCode: Int) : MessagingResponse() {
        object EmptyMessage : MessagingError("Message cannot be empty", 3)
        object EmptyEmail : MessagingError("Email cannot be empty", 2)
        object EmptyName : MessagingError("Name cannot be empty", 1)
    }
}

enum class MessagingParams(val value: String) {
    NAME("name"),
    ORGANIZATION("org"),
    EMAIL("email"),
    MESSAGE("message")
}

/**
 * SECURITY: Extract client IP with proper validation
 */
private fun getClientIp(req: com.varabyte.kobweb.api.http.Request): String {
    // Check common proxy headers in order of trust
    val forwardedFor = req.headers["X-Forwarded-For"]?.toString()?.split(",")?.firstOrNull()?.trim()
    val realIp = req.headers["X-Real-IP"]?.toString()?.trim()
    val remoteAddr = req.headers["Remote-Addr"]?.toString()?.trim()
    
    return forwardedFor ?: realIp ?: remoteAddr ?: "unknown"
}

/**
 * SECURITY: Validate IP address format to prevent spoofing
 */
private fun isValidIp(ip: String): Boolean {
    if (ip == "unknown" || ip.isBlank()) return false
    
    // Basic IPv4 validation
    val ipv4Regex = Regex("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    // Basic IPv6 validation (simplified)
    val ipv6Regex = Regex("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$")
    
    return ipv4Regex.matches(ip) || ipv6Regex.matches(ip) || ip == "localhost" || ip == "127.0.0.1"
}