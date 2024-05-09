package com.probro.khoded.pages.contactSections

import com.probro.khoded.messaging.MessageResult
import com.probro.khoded.messaging.MessagingStage
import com.probro.khoded.messaging.MessagingStateHolder
import com.probro.khoded.messaging.messageData.FormType
import com.probro.khoded.messaging.messageData.MailResponse
import com.probro.khoded.messaging.messageData.MessageData
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.Strings
import com.probro.khoded.utils.Strings.EMAIL_REGEX
import com.probro.khoded.utils.messaging.MailClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object ContactPageStateHolder : MessagingStateHolder<ContactFormState>() {

    private val messageScope =
        CoroutineScope(Dispatchers.Default + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            _contactForm.update {
                it.copy(
                    isLoading = false,
                    stage = MessagingStage.ERROR(
                        msg = "Unfortunately there seems to be an error."
                    )
                )
            }
        })

    private val _contactForm: MutableStateFlow<ContactFormState> = MutableStateFlow(
        ContactFormState(
            placeholderData = Pages.Contact_Section.MessageUIModel(
                fullName = "Full Name",
                email = "Email",
                organization = "Organization",
                messageSubject = "What Do you need Help With",
                message = "Drop Us A Message"
            ),
            stage = MessagingStage.IDLE(),
            isLoading = false,
            messageData = MessageData.ContactMessageData(),
            messageResult = MessageResult.MessagingError("Still in Idle stage.")
        )
    )
    val formState: StateFlow<ContactFormState> get() = _contactForm

    override fun updateName(newName: String) = _contactForm.update {
        it.copy(messageData = it.messageData.apply { name = newName })
    }

    override fun updateEmail(newEmail: String) = _contactForm.update {
        it.copy(messageData = it.messageData.apply { email = newEmail })
    }

    override fun updateMessage(newMsg: String) = _contactForm.update {
        it.copy(messageData = it.messageData.apply { message = newMsg })
    }

    fun updateSubject(newText: String) = _contactForm.update {
        it.copy(messageData = it.messageData.apply { subject = newText })
    }

    fun updateOrganization(newText: String) = _contactForm.update {
        it.copy(messageData = it.messageData.apply { organization = newText })
    }

    override fun onMessageSend(message: String) {
        _contactForm.update {
            it.copy(
                isLoading = true,
                stage = MessagingStage.VALIDATING("Processing the message.")
            )
        }
        validateData()
        if (formState.value.isLoading) {
            sendMessage()
        }
    }

    override fun validateData() {
        try {
            with(formState.value.messageData) {
                require(name.isNotEmpty()) { "Please fill out the name before sending form." }
                email.apply {
                    require(isNotEmpty()) { "Please fill out the email so we can contact you." }
                    require(matches(Regex(EMAIL_REGEX))) { "$email does not fit email format 'email@example.com" }
                }
                require(message.isNotEmpty()) { "Cannot send an empty message." }
            }
        } catch (ex: IllegalArgumentException) {
            _contactForm.update {
                it.copy(
                    isLoading = false,
                    stage = MessagingStage.ERROR(ex.message ?: "Unknown validation error")
                )
            }
        }
    }

    private fun sendMessage() = messageScope.launch {
        with(formState.value) {
            println("messageData was $messageData")
            _contactForm.update {
                it.copy(
                    stage = MessagingStage.SENDING(
                        "Attempting to send message with return contact of ${messageData.email}"
                    )
                )
            }
            val mailResponse: MailResponse = MailClient
                .sendEmail(data = messageData, type = FormType.CONTACT)

            when (mailResponse) {
                is MailResponse.Error -> {
                    println(mailResponse.stackTrace)
                    _contactForm.update {
                        it.copy(
                            isLoading = false,
                            stage = MessagingStage.ERROR("Sorry there was an error: ${mailResponse.exceptionMesaage}")
                        )
                    }
                }

                is MailResponse.Success -> {
                    _contactForm.update {
                        it.copy(
                            isLoading = false,
                            stage = MessagingStage.SENT(Strings.consultationThanksMessage)
                        )
                    }
                }
            }
        }
    }
}