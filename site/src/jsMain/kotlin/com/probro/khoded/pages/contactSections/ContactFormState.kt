package com.probro.khoded.pages.contactSections

import com.probro.khoded.messaging.MessageResult
import com.probro.khoded.messaging.MessagingStage
import com.probro.khoded.messaging.MessagingState
import com.probro.khoded.messaging.messageData.MessageData
import com.probro.khoded.utils.Pages

data class ContactFormState(
    val clientFilledData: Pages.Contact_Section.MessageUIModel,
    val placeholderData: Pages.Contact_Section.MessageUIModel,
    override val stage: MessagingStage = MessagingStage.IDLE(),
    override val isLoading: Boolean = false,
    override val messageData: MessageData.ContactMessageData = MessageData.ContactMessageData(),
    override val messageResult: MessageResult
) : MessagingState