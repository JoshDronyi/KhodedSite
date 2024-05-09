package com.probro.khoded.messaging

import com.probro.khoded.messaging.messageData.MessageData

interface MessagingState {
    val isLoading: Boolean
    val messageResult: MessageResult
    val stage: MessagingStage
    val messageData: MessageData
}

sealed class MessagingStage(val message: String) {
    data class IDLE(val msg: String = "No message is being sent.") : MessagingStage(msg)
    data class VALIDATING(val msg: String = "No message is being sent.") : MessagingStage(msg)
    data class SENDING(val msg: String = "Please wait while we send your message") : MessagingStage(msg)
    data class SENT(val msg: String = "Mamma saiiid.......") : MessagingStage(msg)
    data class ERROR(val msg: String = "Oh nooooo!!!!") : MessagingStage(msg)
    data class RETRY(val msg: String = "Ok, trying again.") : MessagingStage(msg)
}


sealed class MessageResult {
    data class MessagingError(val error: String) : MessageResult()
    data class Success(val message: String) : MessageResult()
}