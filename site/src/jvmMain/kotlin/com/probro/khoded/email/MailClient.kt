package com.probro.khoded.email

import com.google.api.services.gmail.model.Message
import com.probro.khoded.IntakeFormDTO
import com.probro.khoded.local.KhodedDB
import com.probro.khoded.local.KhodedDB.db
import com.varabyte.kobweb.api.log.Logger
import kotlinx.coroutines.supervisorScope
import org.jetbrains.exposed.sql.transactions.TransactionManager.Companion.closeAndUnregister
import org.jetbrains.exposed.sql.transactions.transactionManager
import java.io.IOException
import java.security.GeneralSecurityException

class MailClient(
    private val logger: Logger
) {
    private val khodedMailer: GmailQuickstart.KhodedMailer by lazy {
        GmailQuickstart.KhodedMailer(logger)
    }

    suspend fun sendMessage(
        senderName: String,
        senderOrganization: String,
        senderEmail: String,
        message: String,
        onSuccess: (message: String) -> Unit
    ) = supervisorScope {
        logger.info("Attempting to use the mailer to send email.")
        try {
            logger.info("Using the simple mail implementation")
            val email: Message? = khodedMailer.sendEmail(
                toEmailAddress = "admin@khoded.com",
                messageSubject = "${MailSubjects.CLIENT_CONTACT.value} - " +
                        "$senderName ($senderEmail) from $senderOrganization",
                bodyText = message
            )
            email?.let {
                onSuccess("Sent email: $email")
            } ?: logger.error("Sorry, unable to send message.")
        } catch (ex: IOException) {
            //TODO: HANDLE IO EXCEPTION ACCORDINGLY
            logger.error("IO EXCEPTION: ${ex.message}\n STACKTACE: ${ex.stackTrace}")
            ex.printStackTrace()
        } catch (ex: GeneralSecurityException) {
            //TODO: HANDLE SECURITY EXCEPTION ACCORDINGLY
            logger.error("General security exception: ${ex.message}\n STACKTACE: ${ex.stackTrace}")
            ex.printStackTrace()
        } catch (ex: Exception) {
            logger.error("There was an unknown error: ${ex.message}")
            ex.printStackTrace()
        }
    }

    suspend fun sendIntakeForm(intakeFormDTO: IntakeFormDTO) = supervisorScope {
        logger.info("Got form in mailer and attempting to send.")
        val email = khodedMailer.sendEmail(
            toEmailAddress = "admin@khoded.com",
            messageSubject = "${MailSubjects.CLIENT_REQUEST_FORM.value} - ${intakeFormDTO.organization}",
            bodyText = intakeFormDTO.toString()
        )
        logger.info("Sent email $email")
        saveForm(intakeFormDTO)
    }

    private suspend fun saveForm(intakeFormDTO: IntakeFormDTO) {
        db.transactionManager.apply {
            val request = KhodedDB.saveProjectRequest(intakeFormDTO, logger)
            println(request.await().toString())
            closeAndUnregister(db)
        }
    }
}

enum class MailSubjects(val value: String) {
    CLIENT_REQUEST_FORM("Client Request Form"),
    CLIENT_CONTACT("Contact Us")
}