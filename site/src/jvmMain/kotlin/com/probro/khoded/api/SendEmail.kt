package com.probro.khoded.api

import com.probro.khoded.EmailData
import com.probro.khoded.MailResponse
import com.probro.khoded.email.MailClient
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.http.Response
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
lateinit var client: MailClient


@Api
suspend fun sendEmail(ctx: ApiContext) = messagingScope.launch {
    ctx.logger.info("Got to send email.")
    client = MailClient(ctx.logger)
    val body = ctx.req.body?.decodeToString()
    ctx.logger.info("body was ${body}")
    val jsonBody: EmailData = json.decodeFromString<EmailData>(body ?: "")
    ctx.logger.info("Params were $body with a value of $jsonBody")
    jsonBody.apply {
        if (name == null || email == null || message == null) {
            ctx.logger.info("Something was null")
            name ?: sendError(ctx.res, MessagingResponse.MessagingError.EmptyName)
            email ?: sendError(ctx.res, MessagingResponse.MessagingError.EmptyEmail)
            message ?: sendError(ctx.res, MessagingResponse.MessagingError.EmptyMessage)
        } else {
            ctx.logger.info(
                "Sending to mailing client."
            )
            val mailResponse = client.sendMessage(
                senderName = name,
                senderOrganization = organization ?: "",
                subject = subject ?: "",
                senderEmail = email,
                message = message
            )
            when (mailResponse) {
                is MailResponse.Error -> {
                    with(ctx) {
                        logger.apply {
                            info("Issue trying to build and send email: ${mailResponse.exceptionMesaage}")
                            info("Sending response stacktrace: ${mailResponse.stackTrace}")
                        }
                        res.apply {
                            status = 500
                            setBodyText(json.encodeToString(mailResponse))
                        }
                    }

                }

                is MailResponse.Success -> {
                    with(ctx) {
                        logger.info("Sending success response")
                        res.apply {
                            status = 200
                            setBodyText(json.encodeToString(mailResponse))
                        }
                    }
                }
            }
        }
    }
}

fun sendError(response: Response, error: MessagingResponse.MessagingError) {
    response.apply {
        status = error.statusCode
        setBodyText(error.errorMessage)
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