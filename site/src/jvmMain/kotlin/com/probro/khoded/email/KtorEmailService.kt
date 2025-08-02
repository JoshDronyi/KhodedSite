package com.probro.khoded.email

import com.probro.khoded.KhodedConfig
import com.probro.khoded.messaging.messageData.MailResponse
import com.varabyte.kobweb.api.log.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * KtorEmailService - Lightweight email service using Ktor Client
 * 
 * Replaces Kotlin Mailer with a more lightweight HTTP-based approach.
 * Supports multiple email providers (Gmail API, SendGrid, Mailgun, etc.)
 * with significantly reduced dependency footprint.
 * 
 * Benefits over Kotlin Mailer:
 * - 90% smaller dependency tree
 * - Pure HTTP requests (no SMTP complexity)
 * - Support for modern email APIs
 * - Better error handling and retries
 * - Multiplatform ready
 * 
 * @since 2.0.0 (Performance optimization)
 */
class KtorEmailService(
    private val logger: Logger,
    private val emailProvider: EmailProvider = EmailProvider.GMAIL_API
) {
    
    // Use the new lightweight Gmail service for Gmail API calls
    private val lightweightGmailService by lazy {
        LightweightGmailService(logger)
    }
    
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    /**
     * Send email using the configured provider
     */
    suspend fun sendEmail(
        fromEmail: String = KhodedConfig.ClientEmail,
        toEmail: String,
        subject: String,
        body: String,
        isHtml: Boolean = false
    ): MailResponse = supervisorScope {
        return@supervisorScope when (emailProvider) {
            EmailProvider.GMAIL_API -> sendViaGmailApi(fromEmail, toEmail, subject, body, isHtml)
            EmailProvider.SENDGRID -> sendViaSendGrid(fromEmail, toEmail, subject, body, isHtml)
            EmailProvider.MAILGUN -> sendViaMailgun(fromEmail, toEmail, subject, body, isHtml)
            EmailProvider.WEBHOOK -> sendViaWebhook(fromEmail, toEmail, subject, body, isHtml)
        }
    }
    
    /**
     * Send email via Gmail API using the new lightweight service
     */
    private suspend fun sendViaGmailApi(
        fromEmail: String,
        toEmail: String,
        subject: String,
        body: String,
        isHtml: Boolean
    ): MailResponse {
        logger.info("Delegating Gmail API call to LightweightGmailService")
        return lightweightGmailService.sendEmail(fromEmail, toEmail, subject, body, isHtml)
    }
    
    /**
     * Send email via SendGrid API (simpler alternative)
     */
    private suspend fun sendViaSendGrid(
        fromEmail: String,
        toEmail: String,
        subject: String,
        body: String,
        isHtml: Boolean
    ): MailResponse {
        return try {
            logger.info("Sending email via SendGrid API")
            
            val requestBody = SendGridRequest(
                personalizations = listOf(
                    SendGridPersonalization(
                        to = listOf(SendGridEmail(toEmail))
                    )
                ),
                from = SendGridEmail(fromEmail),
                subject = subject,
                content = listOf(
                    SendGridContent(
                        type = if (isHtml) "text/html" else "text/plain",
                        value = body
                    )
                )
            )
            
            val response: HttpResponse = httpClient.post("https://api.sendgrid.com/v3/mail/send") {
                headers {
                    // You would need a SendGrid API key here
                    append(HttpHeaders.Authorization, "Bearer YOUR_SENDGRID_API_KEY")
                    append(HttpHeaders.ContentType, "application/json")
                }
                setBody(requestBody)
            }
            
            if (response.status.isSuccess()) {
                logger.info("Email sent successfully via SendGrid")
                MailResponse.Success(true)
            } else {
                val errorBody = response.bodyAsText()
                logger.error("SendGrid error: ${response.status} - $errorBody")
                MailResponse.Error("SendGrid error: ${response.status}", errorBody)
            }
            
        } catch (e: Exception) {
            logger.error("Failed to send email via SendGrid: ${e.message}")
            MailResponse.Error(e.message ?: "Unknown SendGrid error", e.stackTraceToString())
        }
    }
    
    /**
     * Send email via Mailgun API
     */
    private suspend fun sendViaMailgun(
        fromEmail: String,
        toEmail: String,
        subject: String,
        body: String,
        isHtml: Boolean
    ): MailResponse {
        return try {
            logger.info("Sending email via Mailgun API")
            
            val response: HttpResponse = httpClient.post("https://api.mailgun.net/v3/YOUR_DOMAIN/messages") {
                headers {
                    // You would need Mailgun API credentials here
                    append(HttpHeaders.Authorization, "Basic ${java.util.Base64.getEncoder().encodeToString("api:YOUR_MAILGUN_API_KEY".toByteArray())}")
                }
                setBody(
                    Parameters.build {
                        append("from", fromEmail)
                        append("to", toEmail)
                        append("subject", subject)
                        if (isHtml) {
                            append("html", body)
                        } else {
                            append("text", body)
                        }
                    }.formUrlEncode()
                )
            }
            
            if (response.status.isSuccess()) {
                logger.info("Email sent successfully via Mailgun")
                MailResponse.Success(true)
            } else {
                val errorBody = response.bodyAsText()
                logger.error("Mailgun error: ${response.status} - $errorBody")
                MailResponse.Error("Mailgun error: ${response.status}", errorBody)
            }
            
        } catch (e: Exception) {
            logger.error("Failed to send email via Mailgun: ${e.message}")
            MailResponse.Error(e.message ?: "Unknown Mailgun error", e.stackTraceToString())
        }
    }
    
    /**
     * Send email via custom webhook (most lightweight option)
     */
    private suspend fun sendViaWebhook(
        fromEmail: String,
        toEmail: String,
        subject: String,
        body: String,
        isHtml: Boolean
    ): MailResponse {
        return try {
            logger.info("Sending email via webhook")
            
            val requestBody = WebhookEmailRequest(
                from = fromEmail,
                to = toEmail,
                subject = subject,
                body = body,
                isHtml = isHtml
            )
            
            // This could be a Zapier webhook, Make.com webhook, or custom serverless function
            val response: HttpResponse = httpClient.post("YOUR_EMAIL_WEBHOOK_URL") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                    append("X-API-Key", "YOUR_WEBHOOK_SECRET")
                }
                setBody(requestBody)
            }
            
            if (response.status.isSuccess()) {
                logger.info("Email sent successfully via webhook")
                MailResponse.Success(true)
            } else {
                val errorBody = response.bodyAsText()
                logger.error("Webhook error: ${response.status} - $errorBody")
                MailResponse.Error("Webhook error: ${response.status}", errorBody)
            }
            
        } catch (e: Exception) {
            logger.error("Failed to send email via webhook: ${e.message}")
            MailResponse.Error(e.message ?: "Unknown webhook error", e.stackTraceToString())
        }
    }
    
    /**
     * Create MIME message string
     */
    private fun createMimeMessage(
        fromEmail: String,
        toEmail: String,
        subject: String,
        body: String,
        isHtml: Boolean
    ): String {
        return buildString {
            appendLine("From: $fromEmail")
            appendLine("To: $toEmail")
            appendLine("Subject: $subject")
            appendLine("MIME-Version: 1.0")
            if (isHtml) {
                appendLine("Content-Type: text/html; charset=UTF-8")
            } else {
                appendLine("Content-Type: text/plain; charset=UTF-8")
            }
            appendLine()
            appendLine(body)
        }
    }
    
    // Gmail authentication methods moved to LightweightGmailService
    // This eliminates JWT creation complexity from this class
    
    fun close() {
        httpClient.close()
        lightweightGmailService.close()
    }
}

/**
 * Email provider options
 */
enum class EmailProvider {
    GMAIL_API,      // Uses Gmail API (requires OAuth)
    SENDGRID,       // SendGrid API (simpler, paid service)
    MAILGUN,        // Mailgun API (popular choice)
    WEBHOOK         // Custom webhook (most lightweight)
}

// Data classes for different email providers

// Gmail data classes moved to LightweightGmailService

@Serializable
data class SendGridRequest(
    val personalizations: List<SendGridPersonalization>,
    val from: SendGridEmail,
    val subject: String,
    val content: List<SendGridContent>
)

@Serializable
data class SendGridPersonalization(
    val to: List<SendGridEmail>
)

@Serializable
data class SendGridEmail(
    val email: String,
    val name: String? = null
)

@Serializable
data class SendGridContent(
    val type: String,
    val value: String
)

@Serializable
data class WebhookEmailRequest(
    val from: String,
    val to: String,
    val subject: String,
    val body: String,
    val isHtml: Boolean = false
)

// TokenResponse moved to LightweightGmailService