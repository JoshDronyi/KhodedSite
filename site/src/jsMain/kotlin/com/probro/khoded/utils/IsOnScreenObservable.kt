package com.probro.khoded.utils

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.EventListener

enum class SectionPosition {
    ABOVE, ON_SCREEN, BELOW, IDLE
}

@Composable
fun IsOnScreenObservable(
    sectionID: String,
    onSectionPositionChange: (position: SectionPosition) -> Unit
) {
    var sectionPosition: SectionPosition? by remember { mutableStateOf(null) }
    var isListening by remember { mutableStateOf(false) }
    val halfwayPoint = (window.screenY + window.innerHeight) / 2
    val screenBottom = window.screenY + window.innerHeight

    val scrollListener = EventListener { event ->
        val section = document.getElementById(sectionID)
        val bounds = section?.getBoundingClientRect()
        if (event.type.equals("scroll", ignoreCase = true)) {
            val theTop = bounds?.top?.toInt() ?: 0
            val sectionHalfway: Int = bounds?.bottom?.div(2)?.toInt() ?: 0
            sectionPosition = when {
                theTop < window.screenY -> SectionPosition.ABOVE
                theTop >= window.screenY && theTop < screenBottom -> SectionPosition.ON_SCREEN
                sectionHalfway >= halfwayPoint -> SectionPosition.BELOW
                else -> SectionPosition.IDLE
            }
        } else {
            println("Not a scroll event. EventType: ${event.type}")
        }
    }

    LaunchedEffect(sectionPosition) {
        sectionPosition?.let { onSectionPositionChange(it) }
        with(document) {
            when (sectionPosition) {
                SectionPosition.ABOVE, SectionPosition.BELOW, SectionPosition.ON_SCREEN, null ->
                    if (isListening.not()) {
                        addEventListener("scroll", scrollListener)
                        isListening = !isListening
                    }

                SectionPosition.IDLE -> {
                    removeEventListener("scroll", scrollListener)
                    isListening = !isListening
                }
            }
        }
    }
}