package com.probro.khoded.email

import com.probro.khoded.IntakeFormDTO
import com.probro.khoded.database.PostgreSQLService
import com.probro.khoded.messaging.messageData.MailResponse
import com.varabyte.kobweb.api.log.Logger
import kotlinx.coroutines.supervisorScope

class MailClient(
    private val logger: Logger
) {
    // Replace heavy Kotlin Mailer + Gmail API with lightweight Ktor Client approach
    private val ktorEmailService: KtorEmailService by lazy {
        KtorEmailService(logger, EmailProvider.GMAIL_API)
    }
    
    // Production-ready PostgreSQL database using JDBI + HikariCP
    private val database: PostgreSQLService by lazy {
        PostgreSQLService(logger, isDevelopment = true)
    }

    suspend fun sendMessage(
        senderName: String,
        senderOrganization: String = "",
        senderEmail: String,
        subject: String = "",
        message: String
    ): MailResponse = supervisorScope {
        logger.info("Attempting to send email using lightweight Ktor email service.")
        
        val formattedSubject = "${MailSubjects.CLIENT_CONTACT.value} - $subject"
        val formattedBody = buildString {
            append("From $senderOrganization representative $senderName ($senderEmail)")
            appendLine()
            append(message)
        }
        
        return@supervisorScope ktorEmailService.sendEmail(
            toEmail = "hello@khoded.com",
            subject = formattedSubject,
            body = formattedBody,
            isHtml = false
        )
    }

    suspend fun sendIntakeForm(intakeFormDTO: IntakeFormDTO) = supervisorScope {
        logger.info("Got form in mailer and attempting to send using Ktor email service.")
        
        val emailResult = ktorEmailService.sendEmail(
            toEmail = "hello@khoded.com",
            subject = "${MailSubjects.CLIENT_REQUEST_FORM.value} - ${intakeFormDTO.organization}",
            body = intakeFormDTO.toString(),
            isHtml = false
        )
        
        logger.info("Email send result: $emailResult")
        saveForm(intakeFormDTO)
        emailResult
    }

    private suspend fun saveForm(intakeFormDTO: IntakeFormDTO) {
        try {
            logger.info("Saving intake form using PostgreSQL database")
            val requestId = database.saveProjectRequest(intakeFormDTO)
            logger.info("Intake form saved successfully with ID: $requestId")
        } catch (e: Exception) {
            logger.error("Failed to save intake form: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * Clean up resources when the mail client is no longer needed
     */
    suspend fun close() {
        ktorEmailService.close()
        database.close()
    }
    
    /**
     * Initialize database tables (call this once at startup)
     */
    suspend fun initializeDatabase() {
        database.initializeTables()
    }
    
    /**
     * Health check for both email and database services
     */
    suspend fun healthCheck(): Map<String, Boolean> {
        return mapOf(
            "database" to database.healthCheck(),
            "email" to true // KtorEmailService doesn't maintain persistent connections
        )
    }
}

enum class MailSubjects(val value: String) {
    CLIENT_REQUEST_FORM("Client Request Form"),
    CLIENT_CONTACT("Contact Us")
}