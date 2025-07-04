package com.probro.khoded.email

import com.google.api.services.gmail.model.Message
import com.probro.khoded.IntakeFormDTO
import com.probro.khoded.local.KhodedDB
import com.probro.khoded.local.KhodedDB.db
import com.probro.khoded.messaging.messageData.MailResponse
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
        senderOrganization: String = "",
        senderEmail: String,
        subject: String = "",
        message: String
    ): MailResponse = supervisorScope {
        logger.info("Attempting to use the mailer to send email.")
        try {
            logger.info("Using the simple mail implementation")
            val email: Message? = khodedMailer.sendEmail(
                toEmailAddress = "admin@khoded.com",
                messageSubject = "${MailSubjects.CLIENT_CONTACT.value} - $subject",
                bodyText = StringBuilder()
                    .append("From $senderOrganization representative $senderName ($senderEmail)")
                    .appendLine()
                    .append(message)
                    .toString()
            )
            email?.let {
                logger.info("Sent email: $email")
                return@supervisorScope MailResponse.Success(
                    email.labelIds.any {
                        it.equals("sent", ignoreCase = true)
                    }
                )
            } ?: run {
                val unkKnownErrorMsg = "Sorry, unable to send message."
                logger.error(unkKnownErrorMsg)
                return@supervisorScope MailResponse.Error(
                    exceptionMesaage = unkKnownErrorMsg,
                    stackTrace = "UKnown error"
                )
            }
        } catch (ex: IOException) {
            //TODO: HANDLE IO EXCEPTION ACCORDINGLY
            logger.error("IO EXCEPTION: ${ex.message}\n STACKTACE: ${ex.stackTrace}")
            ex.printStackTrace()
            return@supervisorScope MailResponse.Error(
                exceptionMesaage = ex.message ?: ex.localizedMessage ?: "Unknown eror",
                stackTrace = ex.stackTrace.toString()
            )
        } catch (ex: GeneralSecurityException) {
            //TODO: HANDLE SECURITY EXCEPTION ACCORDINGLY
            logger.error("General security exception: ${ex.message}\n STACKTACE: ${ex.stackTrace}")
            ex.printStackTrace()
            return@supervisorScope MailResponse.Error(
                exceptionMesaage = ex.message ?: ex.localizedMessage ?: "Unknown eror",
                stackTrace = ex.stackTrace.toString()
            )
        } catch (ex: Exception) {
            logger.error("There was an unknown error: ${ex.message}")
            ex.printStackTrace()
            return@supervisorScope MailResponse.Error(
                exceptionMesaage = ex.message ?: ex.localizedMessage ?: "Unknown eror",
                stackTrace = ex.stackTrace.toString()
            )
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