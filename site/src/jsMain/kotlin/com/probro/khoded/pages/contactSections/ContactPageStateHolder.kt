package com.probro.khoded.pages.contactSections

import com.probro.khoded.messaging.messageData.MailResponse
import com.probro.khoded.messaging.MessageResult
import com.probro.khoded.messaging.MessagingStage
import com.probro.khoded.messaging.MessagingStateHolder
import com.probro.khoded.messaging.messageData.MessageData
import com.probro.khoded.utils.Pages
import com.probro.khoded.utils.Strings
import com.probro.khoded.utils.Strings.EMAIL_REGEX
import com.probro.khoded.utils.popUp.PopUpStateHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object ContactPageStateHolder : MessagingStateHolder<ContactFormState>() {

    private val messageScope =
        CoroutineScope(Dispatchers.Default + CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            //TODO: Handle messaging error and alert the user.
        })

    private val _contactForm: MutableStateFlow<ContactFormState> = MutableStateFlow(
        ContactFormState(
            clientFilledData = Pages.Contact_Section.MessageUIModel(),
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
        it.copy(clientFilledData = it.clientFilledData.apply {
            fullName = newName
        })
    }

    override fun updateEmail(newEmail: String) = _contactForm.update {
        it.copy(clientFilledData = it.clientFilledData.apply {
            email = newEmail
        })
    }

    override fun updateMessage(newMsg: String) = _contactForm.update {
        it.copy(clientFilledData = it.clientFilledData.apply {
            message = newMsg
        })
    }

    fun updateSubject(newText: String) = _contactForm.update {
        it.copy(clientFilledData = it.clientFilledData.apply {
            messageSubject = newText
        })
    }

    fun updateOgranization(newText: String) = _contactForm.update {
        it.copy(clientFilledData = it.clientFilledData.apply {
            organization = newText
        })
    }

    override fun onMessageSend(message: String) {
        _contactForm.update {
            it.copy(clientFilledData = it.clientFilledData.apply {
                this.message = message
            })
        }
        this.validateData()
        PopUpStateHolder.adjustPopUpText(MessagingStage.SENDING(message))
        val pendingResponse = sendMessage(message)
        messageScope.launch {
            PopUpStateHolder.adjustPopUpText(
                MessagingStage.RESPONDING(
                    pendingResponse.await()
                )
            )
        }
    }

    override fun validateData() {
        try {
            validateName()
            validateEmail()
            validateMessage()
            validateSubject()
            validateOrganization()
        } catch (ex: IllegalArgumentException) {
            TODO("Handle argument exception")
        } catch (ex: IllegalStateException) {
            TODO("Handle state exception")
        }
    }

    private fun validateOrganization() {
        TODO("Not yet implemented")
    }

    private fun validateSubject() {
        TODO("Not yet implemented")
    }

    private fun validateName() = with(_contactForm.value.clientFilledData) {
        if (fullName.isEmpty()) throw IllegalArgumentException("Name cannot be empty.")
        //TODO: FIND OTHER VALIDATIONS TO USE FOR THE NAME.
    }

    private fun validateEmail() = with(_contactForm.value.clientFilledData) {
        email.apply {
            if (isEmpty()) {
                throw IllegalArgumentException("Email cannot be empty.")
            }
            if (matches(Regex(EMAIL_REGEX)).not()) throw IllegalStateException("Invalid Email address format.")
            //TODO: FIND OTHER VALIDATIONS TO USE FOR THE Email.
        }
    }

    private fun validateMessage() = with(_contactForm.value.clientFilledData) {
        if (message.isEmpty()) throw IllegalArgumentException("Message cannot be empty.")
        //TODO: FIND OTHER VALIDATIONS TO USE FOR THE Message.
    }


    private fun sendMessage(message: String): Deferred<String> {
        println("sending message: $message")
        return messageScope.async {
            with(formState.value) {
                println("clientData was $clientFilledData")
                println("Message was $message")
                return@with sendMessage(
                    name = clientFilledData.fullName,
                    email = clientFilledData.email,
                    organization = clientFilledData.organization,
                    subject = clientFilledData.messageSubject,
                    message = message
                )
            }
        }
    }

    private suspend fun sendMessage(
        name: String,
        email: String,
        organization: String,
        subject: String,
        message: String
    ): String {
        val mailResponse: MailResponse = MailResponse.Success(true)
//            MailClient.sendEmail(
//            data = EmailData(
//                name = name,
//                email = email,
//                organization = organization,
//                subject = subject,
//                message = message
//            )
//        )
        return when (mailResponse) {
            is MailResponse.Error -> {
                "Exception: ${mailResponse.exceptionMesaage} \n ${mailResponse.stackTrace}"
            }

            is MailResponse.Success -> {
                Strings.consultationThanksMessage
            }

            else -> {
                ""
            }
        }
    }
}

