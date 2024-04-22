package com.probro.khoded.pages.homeSections

import com.probro.khoded.messaging.messageData.MailResponse
import com.probro.khoded.messaging.MessageResult
import com.probro.khoded.messaging.MessagingStage
import com.probro.khoded.messaging.MessagingStateHolder
import com.probro.khoded.messaging.messageData.FormType
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.Strings
import com.probro.khoded.utils.messaging.MailClient
import com.probro.khoded.utils.popUp.PopUpStateHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object ConsultationStateHolder : MessagingStateHolder<ConsultationFormState>() {
    private val _consultationState: MutableStateFlow<ConsultationFormState> = MutableStateFlow(
        ConsultationFormState(
            placeholderData = Pages.Home_Section.ConsultationRequestUIModel(),
            isLoading = false
        )
    )
    val formState: StateFlow<ConsultationFormState>
        get() = _consultationState

    override fun updateName(newName: String) = _consultationState.update {
        it.copy(messageData = it.messageData.apply { name = newName })
    }

    override fun updateEmail(newEmail: String) = _consultationState.update {
        it.copy(messageData = it.messageData.apply { email = newEmail })
    }

    override fun updateMessage(newMsg: String) = _consultationState.update {
        it.copy(messageData = it.messageData.apply { message = newMsg })
    }

    override fun validateData() {
        TODO("Not yet implemented")
    }

    private val scope = CoroutineScope(Dispatchers.Default + CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    })

    override fun onMessageSend(message: String) {
        _consultationState.update { it.copy(isLoading = true) }
        if (PopUpStateHolder.popUpState.value.isShowing.not()) {
            println("Pop up is showing: ${PopUpStateHolder.popUpState.value.isShowing}")
            PopUpStateHolder.showPopUp()
        }
        if (message == Strings.projectPrompt) {
            println("default message string.")
            PopUpStateHolder.adjustPopUpText(MessagingStage.ERROR("Please change the messages default value."))
        } else {
            println("Sending the message.")
            PopUpStateHolder.adjustPopUpText(MessagingStage.SENDING(message))
            scope.launch {
                val response = scope.async { sendMessage() }
                PopUpStateHolder.adjustPopUpText(
                    MessagingStage.RESPONDING(
                        response.await()
                    )
                )
                _consultationState.update {
                    it.copy(isLoading = false, messageResult = MessageResult.Success(response.await()))
                }
            }
        }
    }

    // TODO: EXPAND SSEND MESSAGE FRONT END MESSAGES TO BE FRIENDLIER TO USERS INSTEAD OF DISPLAYING THE EXCEPTION.
    private suspend fun sendMessage(): String = with(formState.value) {
        println("sending data: $messageData")
        println("clientData was $messageData")
        println("Message was ${messageData.message}")
        println("getting the mail response")

        val mailResponse = MailClient
            .sendEmail(data = messageData, type = FormType.CONSULTATION)
        println("Mail response was $mailResponse")

        return when (mailResponse) {
            is MailResponse.Error -> {
                "Exception: ${mailResponse.exceptionMesaage}"
            }

            is MailResponse.Success -> {
                Strings.consultationThanksMessage
            }
        }
    }

    fun storeRequest() {
        // TODO("Save the request in a database.")
    }
}