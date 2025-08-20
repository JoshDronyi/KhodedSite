package com.probro.khoded.utils

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.EventListener
import kotlin.math.absoluteValue

/**
 * Enumeration representing the relative position of a section within the viewport.
 *
 * This enum provides precise positioning information for sections relative to
 * the current viewport, enabling sophisticated scroll-based interactions,
 * navigation highlighting, and content management strategies.
 *
 * @since 1.0.0
 * @see IsOnScreenObservable for position detection implementation
 */
enum class SectionPosition {
    /** Section is positioned above the current viewport (user needs to scroll up) */
    ABOVE,
    
    /** Section is currently visible within the viewport */
    ON_SCREEN,
    
    /** Section is positioned below the current viewport (user needs to scroll down) */
    BELOW,
    
    /** Section position is idle/unchanged (no active monitoring needed) */
    IDLE
}

/**
 * Advanced viewport position observer for precise section position tracking.
 *
 * This composable provides more granular position information compared to
 * OnViewPortEnteredObservable, offering continuous position updates as the
 * user scrolls. It's particularly useful for navigation highlighting,
 * progress indicators, and complex scroll-based UI updates.
 *
 * The observer calculates the relative position of a section against the
 * current viewport boundaries and provides real-time updates via the callback.
 *
 * @param sectionID The HTML element ID to observe (without the # prefix)
 * @param onSectionPositionChange Callback invoked when the section's position
 *                               changes relative to the viewport. Receives the
 *                               new SectionPosition as a parameter.
 *
 * @since 1.0.0
 * @see SectionPosition for possible position values
 * @see OnViewPortEnteredObservable for simpler enter/exit detection
 *
 * Example usage:
 * ```kotlin
 * IsOnScreenObservable(
 *     sectionID = "services-section"
 * ) { position ->
 *     when (position) {
 *         SectionPosition.ON_SCREEN -> highlightNavItem("services")
 *         SectionPosition.ABOVE -> updateScrollProgress(0.8f)
 *         SectionPosition.BELOW -> updateScrollProgress(0.2f)
 *         SectionPosition.IDLE -> { /* no action needed */ }
 *     }
 * }
 * ```
 */
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

/**
 * Determines the relative position of a section within the viewport.
 *
 * This function analyzes the calculated distances from the viewport boundaries
 * to determine where the section is positioned relative to the current view.
 * It uses the midpoint calculation to provide accurate positioning information.
 *
 * @param sectionID The HTML element ID being analyzed (used for debugging)
 * @param topDistance Calculated distance from the viewport top
 * @param bottomDistance Calculated distance from the viewport bottom
 * @return SectionPosition indicating where the section is relative to viewport
 */
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


/**
 * Calculates the absolute Y coordinate of the viewport's top edge.
 *
 * Combines the screen position with the current scroll offset to provide
 * the absolute position of the viewport's top boundary within the document.
 *
 * @return The absolute Y coordinate of the viewport top
 */
private fun getScreenTop(): Double {
    return window.screenY + window.pageYOffset
}

/**
 * Calculates the absolute Y coordinate of the viewport's bottom edge.
 *
 * Uses the top position and viewport height to determine where the
 * bottom of the current viewport is positioned within the document.
 *
 * @return The absolute Y coordinate of the viewport bottom
 */
private fun getScreenBottom(): Double {
    return getScreenTop() + window.innerHeight
}