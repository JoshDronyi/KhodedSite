package com.probro.khoded.utils.popUp

import com.probro.khoded.components.composables.PopUpScreenUIModel
import com.probro.khoded.models.ButtonState
import com.probro.khoded.messaging.MessagingStage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object PopUpStateHolder {
    private val _popUpState: MutableStateFlow<PopUpScreenUIModel> = MutableStateFlow(
        PopUpScreenUIModel(
            promptText = "",
            isShowing = false,
            buttonState = ButtonState(
                buttonText = "Ok",
                onButtonClick = PopUpStateHolder::onCTACLick
            )
        )
    )
    val popUpState: StateFlow<PopUpScreenUIModel> get() = _popUpState

    private fun onCTACLick() {
        _popUpState.update {
            it.copy(
                isShowing = false
            )
        }
    }


    fun adjustPopUpText(mailAction: MessagingStage) {
        adjustPopUpText(mailAction.message)
        when (mailAction) {
            is MessagingStage.IDLE -> {
//                 TODO("The pop up can now disappear")
            }

            is MessagingStage.SENDING -> {
//                TODO(" Display a thank you we are dea;ing with it message to users.")
            }

            is MessagingStage.RESPONDING -> {
//                TODO("Display to the user what the result of sending the message was")
            }

            is MessagingStage.ERROR -> {

//                TODO("Figure out how to display errors.")
            }

            is MessagingStage.RETRY -> {
//                TODO("Figure out a retry strategy and implement it.")
            }
        }
    }

    private fun adjustPopUpText(newText: String) {
        _popUpState.update {
            it.copy(
                promptText = newText
            )
        }
//        println("TOdo")
    }

    fun showPopUp() {
        _popUpState.update {
            it.copy(isShowing = true)
        }
    }

    fun hidePopUp() {
        _popUpState.update {
            it.copy(isShowing = false)
        }
    }

}