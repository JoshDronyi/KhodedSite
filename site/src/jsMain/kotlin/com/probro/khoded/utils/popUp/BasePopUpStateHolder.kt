package com.probro.khoded.utils.popUp

import com.probro.khoded.messaging.MessagingStage
import com.probro.khoded.models.ButtonState
import com.probro.khoded.pages.aboutSections.Founders
import com.probro.khoded.utils.Pages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

sealed class PopUpStateHolders() {
    abstract class BasePopUpStateHolder<T : PopUpScreenUIModel> : PopUpStateHolders() {
        abstract val popUpState: StateFlow<T>
        abstract fun onCTACLick()
        open fun adjustPopUpText(mailAction: MessagingStage) {}
        open fun adjustPopUpText(founders: Founders) {}
        abstract fun showPopUp()
        abstract fun hidePopUp()
    }

    object MessagingPopUpStateHolder : BasePopUpStateHolder<PopUpScreenUIModel.MessagingPopUpUiModel>() {
        private val _popUpState: MutableStateFlow<PopUpScreenUIModel.MessagingPopUpUiModel> by lazy {
            MutableStateFlow(
                PopUpScreenUIModel.MessagingPopUpUiModel(
                    text = "Default Text",
                    state = ButtonState(
                        buttonText = "Got it.",
                        onButtonClick = ::onCTACLick
                    ),
                    messagingState = MessagingStage.IDLE("No messaging started."),
                    isLoading = false,
                    isVisible = false
                )
            )
        }
        override val popUpState: StateFlow<PopUpScreenUIModel.MessagingPopUpUiModel>
            get() = _popUpState

        override fun onCTACLick() {
            _popUpState.update {
                it.copy(
                    messagingState = MessagingStage.IDLE("No messaging started."),
                    isLoading = false,
                    isVisible = false
                )
            }
        }

        override fun adjustPopUpText(mailAction: MessagingStage) {
            _popUpState.update {
                it.copy(
                    text = mailAction.message,
                    isVisible = mailAction !is MessagingStage.IDLE,
                    messagingState = mailAction
                )
            }
            print("Received mailAction of $mailAction")
            when (mailAction) {
                is MessagingStage.IDLE -> {
                    println("Hiding pop up")
                    hidePopUp()
                }

                is MessagingStage.SENDING -> {
//                TODO(" Display a thank you we are dea;ing with it message to users.")
                }

                is MessagingStage.SENT -> {
//                TODO("Display to the user what the result of sending the message was")
                }

                is MessagingStage.ERROR -> {
//                TODO("Figure out how to display errors.")
                }

                is MessagingStage.RETRY -> {
//                TODO("Figure out a retry strategy and implement it.")
                }

                is MessagingStage.VALIDATING -> {
                    println("Messaging state in popupState holder was validating.")
                    showPopUp()
                }
            }
        }

        override fun showPopUp() {
            _popUpState.update {
                it.copy(
                    isLoading = true,
                    isVisible = true
                )
            }
        }

        override fun hidePopUp() {
            _popUpState.update {
                it.copy(
                    isLoading = false,
                    isVisible = false
                )
            }
        }
    }

    object FounderPopUiStateHolder : BasePopUpStateHolder<PopUpScreenUIModel.FounderHighlightPopUpUIModel>() {
        private val _popUpState: MutableStateFlow<PopUpScreenUIModel.FounderHighlightPopUpUIModel> by lazy {
            MutableStateFlow(
                PopUpScreenUIModel.FounderHighlightPopUpUIModel(
                    text = FounderText(),
                    image = null,
                    state = ButtonState(
                        buttonText = "Ok, I see you fam.",
                        onButtonClick = ::onCTACLick
                    )
                )
            )
        }

        override val popUpState: StateFlow<PopUpScreenUIModel.FounderHighlightPopUpUIModel>
            get() = _popUpState

        override fun onCTACLick() {
            hidePopUp()
        }

        override fun adjustPopUpText(founders: Founders) = with(Pages.Story_Section.OurFounders) {
            if (popUpState.value.isVisible.not()) showPopUp()
            when (founders) {
                Founders.CEO -> _popUpState.update {
                    it.copy(
                        text = FounderText(
                            titleText = estherBio.title,
                            shortDesc = estherBio.shortDesc,
                            desc = estherBio.desc
                        ),
                        //estherBio.fullStory,
                        image = estherBio.image
                    )
                }

                Founders.CTO -> _popUpState.update {
                    it.copy(
                        text = FounderText(
                            titleText = joshBio.title,
                            shortDesc = joshBio.shortDesc,
                            desc = joshBio.desc
                        ),
                        //joshBio.fullStory,
                        image = joshBio.image
                    )
                }
            }
        }

        override fun showPopUp() {
            _popUpState.update {
                it.copy(isVisible = true)
            }
        }

        override fun hidePopUp() {
            _popUpState.update {
                it.copy(isVisible = false)
            }
        }
    }
}