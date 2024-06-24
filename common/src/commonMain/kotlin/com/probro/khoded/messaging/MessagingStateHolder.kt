package com.probro.khoded.messaging

abstract class MessagingStateHolder<T : MessagingState> {
    //    abstract val formState: StateFlow<T>
//    abstract fun updateMessagingState(state: T)
    abstract fun updateName(newName: String)
    abstract fun updateEmail(newEmail: String)
    abstract fun updateMessage(newMsg: String)
    abstract fun validateData()
    abstract fun onMessageSend(message: String)
}

