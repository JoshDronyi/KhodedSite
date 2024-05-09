package com.probro.khoded.pages.homeSections

import com.probro.khoded.messaging.MessagingStage
import com.probro.khoded.messaging.MessagingStateHolder
import com.probro.khoded.messaging.messageData.FormType
import com.probro.khoded.messaging.messageData.MailResponse
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.Strings
import com.probro.khoded.utils.Strings.EMAIL_REGEX
import com.probro.khoded.utils.messaging.MailClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object ConsultationStateHolder : MessagingStateHolder<ConsultationFormState>() {

    private val scope = CoroutineScope(Dispatchers.Default + CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    })

    private val _consultationState: MutableStateFlow<ConsultationFormState> = MutableStateFlow(
        ConsultationFormState(
            placeholderData = Pages.Home_Section.ConsultationRequestUIModel(),
            isLoading = false,
            stage = MessagingStage.IDLE()
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
        _consultationState.update {
            it.copy(
                isLoading = true,
                stage = MessagingStage.VALIDATING("Validating your consultation request.")
            )
        }
        try {
            with(formState.value.messageData) {
                require(name.isNotEmpty()) { "Name should not be empty" }
                email.apply {
                    require(isNotEmpty()) { "Email should not be empty" }
                    require(matches(Regex(EMAIL_REGEX)))
                    { "Email ($email) does not fit typical email format. Please fit 'email@example.com'." }
                }
                require(message.isNotEmpty()) { " Cannot send an empty message." }
            }
        } catch (ex: IllegalArgumentException) {
            _consultationState.update {
                it.copy(
                    isLoading = false,
                    stage = MessagingStage.ERROR(ex.message ?: "Unknown validation error.")
                )
            }
        }
    }


    override fun onMessageSend(message: String) {
        validateData()
        println("Sending the message.")
        if (formState.value.isLoading) {
            sendMessage()
        }
    }

    private fun sendMessage() = with(formState.value) {
        println("clientData was $messageData")
        println("getting the mail response")
        _consultationState.update {
            it.copy(
                stage = MessagingStage.SENDING(
                    StringBuilder()
                        .append("Setting your response email to ${messageData.email}\n")
                        .append("")
                        .append("Delivering the message:\n ${messageData.message}")
                        .toString()
                )
            )
        }
        val mailResponse = scope.async {
            MailClient
                .sendEmail(data = messageData, type = FormType.CONSULTATION)
        }
        println("Mail response was $mailResponse")
        scope.launch {
            mailResponse.await().apply {
                when (this) {
                    is MailResponse.Error -> {
                        _consultationState.update {
                            it.copy(
                                isLoading = false,
                                stage = MessagingStage.ERROR(this.exceptionMesaage)
                            )
                        }
                    }

                    is MailResponse.Success -> {
                        _consultationState.update {
                            it.copy(
                                isLoading = false,
                                stage = MessagingStage.SENT(
                                    Strings.consultationThanksMessage
                                )
                            )
                        }
                        storeRequest()
                    }
                }
            }
        }
    }

    private fun storeRequest() {
        // TODO("Save the request in a database.")
    }
}