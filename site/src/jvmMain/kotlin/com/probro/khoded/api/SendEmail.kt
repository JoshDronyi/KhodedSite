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
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.http.HttpStatus

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


@Api("sendemail")
suspend fun sendEmail(ctx: ApiContext) = withContext(CoroutineName("SendEmailApiFunction") + Dispatchers.IO) {
    with(ctx) {
        val formType = logger.let {
            it.info("Got to send email. ${ctx.req.readBodyText()}")
            it.info("Params were ${ctx.req.params}")
            client = MailClient(it)
            ctx.req.params[MailParams.TYPE.value]
        }
        val body = req.body?.decodeToString()
        logger.info("formType was $formType,\n body was $body")
        val mailResponse = when {
            formType?.equals(FormType.CONTACT.name, ignoreCase = true) == true -> {
                messagingScope.async {
                    sendContactMessage(
                        json.decodeFromString<MessageData.ContactMessageData>(body ?: "")
                    )
                }
            }

            formType?.equals(FormType.CONSULTATION.name, ignoreCase = true) == true -> {
                messagingScope.async {
                    sendConsultationMessage(
                        json.decodeFromString<MessageData.ConsultationMessageData>(body ?: "")
                    )
                }
            }

            else -> {
                messagingScope.async {
                    MailResponse.Error(
                        exceptionMesaage = "Unable to handle formType of $formType",
                        stackTrace = "Send Email Api function."
                    )
                }
            }
        }
        with(mailResponse.await()) {
            println("mailResponse for $body is $this")
            when (val response = this) {
                is MailResponse.Error -> {
                    logger.apply {
                        info("Issue trying to build and send email: ${response.exceptionMesaage}")
                        info("Stacktrace: ${response.stackTrace}")
                    }
                    res.apply {
                        status = HttpStatus.SC_INTERNAL_SERVER_ERROR
                        setBodyText(json.encodeToString(response))
                    }
                }

                is MailResponse.Success -> {
                    logger.info("Sending success response")
                    res.apply {
                        status = HttpStatus.SC_OK
                        setBodyText(json.encodeToString(response))
                    }
                }
            }
        }
    }
}


suspend fun sendConsultationMessage(data: MessageData.ConsultationMessageData?): MailResponse {
    requireNotNull(value = data, lazyMessage = {
        "Consultation messages must contain a consultation message data object."
    })

    validateMessageData(data)
    return client.sendMessage(
        senderName = data.name,
        senderEmail = data.email,
        message = data.message
    )
}

suspend fun sendContactMessage(data: MessageData.ContactMessageData?): MailResponse {

    requireNotNull(data) { "Contact messages must contain a contact message data object." }

    validateMessageData(data)

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

fun validateMessageData(data: MessageData) {
    when (data) {
        is MessageData.ConsultationMessageData -> {
            println("Must do more Consultation validation")
        }

        is MessageData.ContactMessageData -> {
            println("Must do more Contact validation")
        }
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