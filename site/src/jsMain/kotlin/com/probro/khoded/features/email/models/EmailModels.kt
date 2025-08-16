package com.probro.khoded.features.email.models

import com.probro.khoded.features.consultation.models.ConsultationRequest

/**
 * Email Data Models
 * 
 * Core data structures for the email communication system.
 * Separated from UI components for better maintainability.
 */

data class EmailRequest(
    val from: EmailAddress,
    val to: EmailAddress,
    val subject: String,
    val body: String,
    val template: EmailTemplate = EmailTemplate.GENERAL,
    val attachments: List<EmailAttachment> = emptyList(),
    val isHtml: Boolean = false,
    val priority: EmailPriority = EmailPriority.NORMAL
)

data class EmailAddress(
    val email: String,
    val name: String = ""
) {
    override fun toString(): String = if (name.isNotEmpty()) "$name <$email>" else email
}

data class EmailAttachment(
    val filename: String,
    val content: ByteArray,
    val mimeType: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as EmailAttachment
        if (filename != other.filename) return false
        if (!content.contentEquals(other.content)) return false
        if (mimeType != other.mimeType) return false
        return true
    }

    override fun hashCode(): Int {
        var result = filename.hashCode()
        result = 31 * result + content.contentHashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }
}

enum class EmailTemplate {
    GENERAL,
    CONSULTATION_CONFIRMATION,
    CONSULTATION_FOLLOWUP,
    PROJECT_PROPOSAL,
    INVOICE,
    THANK_YOU
}

enum class EmailPriority {
    LOW, NORMAL, HIGH, URGENT
}

data class EmailResponse(
    val messageId: String,
    val status: String,
    val timestamp: Long = js("Date.now()"),
    val error: String? = null
)

data class GeneralInquiryRequest(
    val name: String,
    val email: String,
    val subject: String,
    val message: String,
    val phone: String? = null,
    val company: String? = null,
    val preferredContactMethod: ContactMethod = ContactMethod.EMAIL,
    val source: String? = null
)

enum class ContactMethod {
    EMAIL, PHONE, SMS, VIDEO_CALL
}

data class EmailCompositionState(
    val to: String = "",
    val subject: String = "",
    val body: String = "",
    val template: EmailTemplate = EmailTemplate.GENERAL,
    val attachments: List<EmailAttachment> = emptyList(),
    val sendStatus: EmailSendStatus = EmailSendStatus.None,
    val validationErrors: Map<String, String> = emptyMap(),
    val isComposing: Boolean = false
)

sealed class EmailSendStatus {
    object None : EmailSendStatus()
    object Sending : EmailSendStatus()
    data class Success(val messageId: String) : EmailSendStatus()
    data class Error(val message: String, val canRetry: Boolean = true) : EmailSendStatus()
}