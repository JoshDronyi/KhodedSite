package com.probro.khoded.utils

import com.probro.khoded.EmailData
import com.probro.khoded.MailResponse
import com.varabyte.kobweb.browser.api
import kotlinx.browser.window
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject

object MailClient {

    suspend fun sendEmail(data: EmailData): MailResponse = supervisorScope {
        val jsonData = json.encodeToString(data)
            .encodeToByteArray()

        try {
            val response = window.api.post(
                apiPath = "sendemail",
                body = jsonData
            ).decodeToString()
            println("Success!!!!! $response")
            return@supervisorScope MailResponse.Success(response)
        } catch (ex: Exception) {
            println("Issue sending message: ${ex.message}")
            ex.printStackTrace()
            return@supervisorScope MailResponse.Error(
                exceptionMesaage = ex.message ?: "Unknown error.",
                stackTrace = ex.stackTraceToString()
            )
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
            return@supervisorScope MailResponse.Success(response)
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