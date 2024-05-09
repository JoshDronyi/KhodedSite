package com.probro.khoded.messaging.messageData

import kotlinx.serialization.Serializable

@Serializable
sealed class MessageData {
    @Serializable
    data class ContactMessageData(
        var email: String = "",
        var name: String = "",
        var message: String = "",
        var organization: String = "",
        var subject: String = ""
    ) : MessageData()

    @Serializable
    data class ConsultationMessageData(
        var email: String = "",
        var name: String = "",
        var message: String = ""
    ) : MessageData()
}

enum class FormType(val value: String) {
    CONTACT("contact"), CONSULTATION("consultation")
}

enum class MailParams(val value: String) {
    TYPE("type")
}