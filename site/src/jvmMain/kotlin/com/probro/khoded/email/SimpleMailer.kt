package com.probro.khoded.email

import at.quickme.kotlinmailer.data.SMTPLoginInfo
import at.quickme.kotlinmailer.delivery.MailerManager
import at.quickme.kotlinmailer.delivery.mailerBuilder
import com.probro.khoded.KhodedConfig
import com.varabyte.kobweb.api.log.Logger
import jakarta.mail.internet.InternetAddress
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.email.EmailBuilder

class SimpleMailer(val logger: Logger) {
    private val mailer: Mailer by lazy {
        getSimpleMailer()
    }

    private fun getSimpleMailer(): Mailer = try {
        logger.info("building mailer, getting smpt login info")
        val loginInfo = SMTPLoginInfo(
            host = "smtp.gmail.com",
            port = 465,
            username = "admin@khoded.com",
            password = "businessjawnski215"
        )
        logger.info("Got login info $loginInfo, loading to mailer")
        val temp = mailerBuilder(loginInfo)
        logger.info("Got the temp mailer $temp")
        temp
    } catch (ex: Exception) {
        logger.info("Error occurred with message: ${ex.localizedMessage}")
        ex.printStackTrace()
        throw ex
    }


    init {
        logger.info("Setting default mailer")
        MailerManager.defaultMailer = mailer
    }

    private fun sendSimpleMail(
        subject: String,
        message: String
    ) {
        logger.info("Sending simple mail.")
        mailer.sendMail(
            EmailBuilder.startingBlank()
                .from(KhodedConfig.ClientEmail)
                .to(InternetAddress("admin@khoded.com"))
                .withSubject(subject)
                .withPlainText(message)
                .buildEmail()
        )
    }

}