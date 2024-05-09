package com.probro.khoded.utils.messaging

import com.probro.khoded.messaging.messageData.FormType
import com.probro.khoded.messaging.messageData.MailParams
import com.probro.khoded.messaging.messageData.MailResponse
import com.probro.khoded.messaging.messageData.MessageData
import com.probro.khoded.utils.json
import com.varabyte.kobweb.browser.api
import kotlinx.browser.window
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject

object MailClient {

    suspend fun sendEmail(data: MessageData, type: FormType): MailResponse = supervisorScope {
        val jsonData = json.encodeToString(data)
            .encodeToByteArray()
        println("Got jsonData as $jsonData")
        try {
            when (type) {
                FormType.CONTACT -> require(
                    data is MessageData.ContactMessageData
                ) { "Please send a ContactMessageData object if you want to use the type $type" }

                FormType.CONSULTATION -> require(data is MessageData.ConsultationMessageData)
                { "Please send a ConsultationMessageData object if you want to use the type $type" }
            }
            val response = window.api.post(
                apiPath = "sendemail?${MailParams.TYPE.value}=${type.value}",
                body = jsonData
            ).decodeToString()
            println("Success!!!!! $response")
            return@supervisorScope json.decodeFromString<MailResponse.Success>(response)
        } catch (ex: IllegalArgumentException) {
            return@supervisorScope MailResponse.Error.withException(ex).apply {
                println("Validation Error: $exceptionMesaage")
                println(stackTrace)
            }
        } catch (ex: Exception) {
            return@supervisorScope MailResponse.Error.withException(ex).apply {
                println("Issue sending message: $exceptionMesaage")
                println(stackTrace)
            }
        }
    }

    suspend fun sendIntakeForm(intakeForm: JsonObject): MailResponse = supervisorScope {
        println("Para para para...... para para para.... cuuuuuhhlick.")
        val jsonData = json.encodeToString(intakeForm).encodeToByteArray()
        println("intakeform converted to byte Array.")
        try {
            val response = window.api.post(
                apiPath = "saveIntakeForm",
                body = jsonData
            ).decodeToString()
            println(response)
            return@supervisorScope MailResponse.Success(true)
        } catch (ex: Exception) {
            println("Issue sending intake form: ${ex.message}")
            ex.printStackTrace()
            return@supervisorScope MailResponse.Error(
                exceptionMesaage = ex.message ?: "Unknown error.",
                stackTrace = ex.stackTraceToString()
            )
        }
    }
}