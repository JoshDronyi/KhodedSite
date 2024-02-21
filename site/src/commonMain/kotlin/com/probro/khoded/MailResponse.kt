package com.probro.khoded

import kotlinx.serialization.Serializable

sealed class MailResponse {
    @Serializable
    data class Success(
        val response: String
    ) : MailResponse()

    @Serializable
    data class Error(
        val exceptionMesaage: String,
        val stackTrace: String
    ) : MailResponse()
}