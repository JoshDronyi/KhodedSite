package com.probro.khoded.pages.homeSections

import com.probro.khoded.messaging.MessageResult
import com.probro.khoded.messaging.MessagingStage
import com.probro.khoded.messaging.MessagingState
import com.probro.khoded.messaging.messageData.MessageData
import com.probro.khoded.utils.Pages

data class ConsultationFormState(
    val placeholderData: Pages.Home_Section.ConsultationRequestUIModel,
    override val stage: MessagingStage,
    override val isLoading: Boolean = false,
    override val messageResult: MessageResult = MessageResult.MessagingError("Havent started yet."),
    override val messageData: MessageData.ConsultationMessageData = MessageData.ConsultationMessageData()
) : MessagingState
