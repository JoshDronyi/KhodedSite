package com.probro.khoded.messaging.messageData

import kotlinx.serialization.Serializable

@Serializable
sealed class MailResponse {
    @Serializable
    data class Success(
        val messageSent: Boolean
    ) : MailResponse()

    @Serializable
    data class Error(
        val exceptionMesaage: String,
        val stackTrace: String
    ) : MailResponse() {
        companion object {
            fun withException(ex: Exception): MailResponse.Error {
                return MailResponse.Error(ex.message ?: "UnKnown Error.", ex.stackTraceToString())
            }
        }
    }


}