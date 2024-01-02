package com.probro.khoded.utils

import com.varabyte.kobweb.browser.api
import kotlinx.browser.window
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject

object MailClient {
    suspend fun sendEmail(data: EmailData) = supervisorScope {
        val jsonData = json.encodeToString(data)
            .encodeToByteArray()

        try {
            println("sending data of : $jsonData")
            val response = window.api.post(
                apiPath = "sendemail",
                body = jsonData
            ).decodeToString()
            println("Success!!!!! $response")
        } catch (ex: Exception) {
            println("Issue sending message: ${ex.message}")
            ex.printStackTrace()
        }
    }

    suspend fun sendIntakeForm(intakeForm: JsonObject) = supervisorScope {
        println("Para para para...... para para para.... cuuuuuhhlick.")
        val jsonData = json.encodeToString(intakeForm).encodeToByteArray()
        println("intakeform converted to byte Array.")
        try {
            val response = window.api.post(
                apiPath = "saveIntakeForm",
                body = jsonData
            ).decodeToString()
            println(response)
        } catch (ex: Exception) {
            println("Issue sending intake form: ${ex.message}")
            ex.printStackTrace()
        }
    }
}

@Serializable
data class EmailData(
    val name: String,
    val email: String,
    val organization: String,
    val message: String
)