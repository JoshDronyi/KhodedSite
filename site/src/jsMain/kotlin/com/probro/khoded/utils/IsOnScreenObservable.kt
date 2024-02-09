package com.probro.khoded.utils

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.EventListener
import kotlin.math.absoluteValue

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
    println("InnerHeight: ${window.innerHeight}")

    val scrollListener = EventListener { event ->
        val section = document.getElementById(sectionID)
        val bounds = section?.getBoundingClientRect()

        val topDistance: Int = getScreenTop().absoluteValue.toInt()
            .minus(bounds?.top?.absoluteValue?.toInt() ?: 0)
        val bottomDistance: Int = getScreenBottom().absoluteValue.toInt()
            .minus(bounds?.bottom?.absoluteValue?.toInt() ?: 0)

        if (event.type.equals("scroll", ignoreCase = true)) {
            sectionPosition = bounds?.let {
                isOnScreen(
                    sectionID = sectionID,
                    topDistance = topDistance,
                    bottomDistance = bottomDistance
                )
            }
        } else {
            println("Not a scroll event. EventType: ${event.type}")
        }
    }

    LaunchedEffect(sectionPosition) {
        sectionPosition?.let { onSectionPositionChange(it) }
        with(document) {
            when (sectionPosition) {
                SectionPosition.ABOVE, SectionPosition.BELOW,
                SectionPosition.ON_SCREEN,
                null ->
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

private fun isOnScreen(sectionID: String, topDistance: Int, bottomDistance: Int): SectionPosition {
    var screenMid = (getScreenTop() + getScreenBottom()) / 2

    val boundsMid = (topDistance + bottomDistance) / 2
    println("$sectionID: ScreenMid: $screenMid, boundsMid: $boundsMid")

    return when {
        boundsMid < getScreenTop() -> SectionPosition.ABOVE
        boundsMid >= getScreenTop() && boundsMid < getScreenBottom() ||
                boundsMid <= getScreenBottom() && boundsMid < getScreenTop() -> SectionPosition.ON_SCREEN

        boundsMid > getScreenBottom() -> SectionPosition.BELOW
        else -> SectionPosition.IDLE
    }
}


private fun getScreenTop(): Double {
    return window.screenY + window.pageYOffset
}

private fun getScreenBottom(): Double {
    return getScreenTop() + window.innerHeight
}