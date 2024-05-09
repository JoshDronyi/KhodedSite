package com.probro.khoded.utils.popUp

import com.probro.khoded.messaging.MessagingStage
import com.probro.khoded.models.ButtonState


sealed class PopUpScreenUIModel(
    val promptText: String,
    val buttonState: ButtonState,
    val isShowing: Boolean
) {
    data class MessagingPopUpUiModel(
        var text: String,
        val state: ButtonState,
        var messagingState: MessagingStage = MessagingStage.IDLE(),
        var isLoading: Boolean = false,
        var isVisible: Boolean = false
    ) : PopUpScreenUIModel(
        promptText = text,
        buttonState = state,
        isShowing = isVisible
    )

    data class FounderHighlightPopUpUIModel(
        var text: FounderText,
        val image: String? = null,
        val state: ButtonState,
        var isVisible: Boolean = false
    ) : PopUpScreenUIModel(
        promptText = text.desc,
        buttonState = state,
        isShowing = isVisible
    )
}

data class FounderText(
    val titleText: String = "",
    val shortDesc: String = "",
    val desc: String = ""
)