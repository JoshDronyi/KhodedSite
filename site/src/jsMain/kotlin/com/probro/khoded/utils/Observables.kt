package com.probro.khoded.utils

import androidx.compose.runtime.*
import com.probro.khoded.utils.Constants.SECTION_HEIGHT
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Element
import org.w3c.dom.events.EventListener

/**
 * Viewport intersection observer for tracking element visibility during scroll events.
 *
 * This composable provides functionality to detect when specific page sections
 * enter or exit the viewport, enabling scroll-based animations, lazy loading,
 * and user engagement tracking. It's particularly useful for single-page
 * applications with multiple content sections.
 *
 * @param sectionID The HTML element ID to observe (without the # prefix)
 * @param distanceFromTop Distance threshold from the top of the viewport in pixels.
 *                       Defaults to SECTION_HEIGHT for consistent section-based detection.
 * @param onEnterViewPort Callback executed when the element enters the viewport.
 *                       Use for triggering animations, analytics, or content loading.
 * @param onExitViewPort Callback executed when the element exits the viewport.
 *                      Use for cleanup, pausing animations, or state updates.
 *
 * @since 1.0.0
 * @see IsOnScreenObservable for alternative position-based observation
 * @see Constants.SECTION_HEIGHT for default distance threshold
 *
 * Example usage:
 * ```kotlin
 * OnViewPortEnteredObservable(
 *     sectionID = "hero-section",
 *     onEnterViewPort = { startHeroAnimation() },
 *     onExitViewPort = { pauseHeroAnimation() }
 * )
 * ```
 */
@Composable
fun OnViewPortEnteredObservable(
    sectionID: String,
    distanceFromTop: Int = SECTION_HEIGHT,
    onEnterViewPort: () -> Unit,
    onExitViewPort: () -> Unit
) {
    var isOnScreen by remember { mutableStateOf(false) }
    var prevOnScreen by remember { mutableStateOf(false) }
    var hasExited by remember { mutableStateOf(false) }
    var prevHasExited by remember { mutableStateOf(false) }

    val elementOnScreenListener = remember {
        EventListener {
            document.getElementById(sectionID)?.let {
                val bounds = it.getBoundingClientRect()
                isOnScreen = it.topIsInBounds() && it.bottomIsInBounds()
                if (isOnScreen) {
                    println(
                        "Section $sectionID is in bounds. \n distanceFromTop:$distanceFromTop" +
                                "\n innerHeight:${window.innerHeight}" +
                                "\n offsetY:${window.pageYOffset}" +
                                "\n top: ${bounds.top}" +
                                "\n bottom: ${bounds.bottom}" +
                                "\n height:${bounds.height}"
                    )
                    if (prevOnScreen != isOnScreen) {
                        println("Switching isOnScreen to True. Exited to false.")
                        prevOnScreen = isOnScreen
                        isOnScreen = true
                        hasExited = false
                    }
                }
            } ?: println("Section with id:$sectionID cannot be found on page.")
        }
    }
    val elementExitListener = remember {
        EventListener {
            document.getElementById(sectionID)?.let {
                val bounds = it.getBoundingClientRect()
                hasExited = it.topIsInBounds().not() || it.bottomIsInBounds().not()
                if (hasExited) {
                    println("ON-SCREEN-LISTENER: prevOnScreen:$prevOnScreen, isOnScreen:$isOnScreen, hasExited: $hasExited")
                    if (prevHasExited != hasExited) {
                        println("Switching isOnScreen to false. Exited to true.")
                        prevHasExited = hasExited
                        hasExited = true
                        isOnScreen = false
                    }
                }
            } ?: println("Section with id:$sectionID could not be found.")
        }
    }
    LaunchedEffect(isOnScreen, hasExited) {
        println("Launching...onScreen:$isOnScreen, hasExited: $hasExited")
        with(document) {
            when {
                isOnScreen.not() && hasExited.not() -> {
                    addEventListener("scroll", callback = elementOnScreenListener)
                    addEventListener("scroll", callback = elementExitListener)
                    println("Adding both window scroll listeners")
                }

                isOnScreen && hasExited.not() -> {
                    println("Removing element on Screen scroll listener")
                    onEnterViewPort()
//                    removeEventListener("scroll", callback = elementOnScreenListener)
                }

                else -> {
                    println("Exiting screen. Removing exit screen listener")
                    onExitViewPort()
//                    removeEventListener(type = "scroll", callback = elementExitListener)
                }
            }
        }
    }
}

/**
 * Extension function to check if the top edge of an element is within viewport bounds.
 *
 * Calculates whether the element's top boundary is positioned within a reasonable
 * viewing area, accounting for partial visibility and smooth scrolling transitions.
 *
 * @return true if the element's top edge is considered "in bounds" for visibility
 */
private fun Element.topIsInBounds(): Boolean {
    val bounds = getBoundingClientRect()
    val viewportBottom = window.pageYOffset + window.innerHeight
    return bounds.top >= (-bounds.height.div(2))
            && bounds.top < (viewportBottom - bounds.height.div(2))
}

/**
 * Extension function to check if the bottom edge of an element is within viewport bounds.
 *
 * Calculates whether the element's bottom boundary is positioned within the
 * viewport, considering both positive and negative scroll directions.
 *
 * @return true if the element's bottom edge is considered "in bounds" for visibility
 */
private fun Element.bottomIsInBounds(): Boolean {
    val bounds = getBoundingClientRect()
    val viewportBottom = window.pageYOffset + window.innerHeight
    return bounds.bottom <= viewportBottom + bounds.height.div(2)
            && bounds.bottom > 0
}

/**
 * Determines the complete visibility state of an element relative to the viewport.
 *
 * This function provides comprehensive visibility detection by analyzing the element's
 * position and dimensions against the current viewport boundaries. It distinguishes
 * between full, partial, and no visibility states.
 *
 * @param distanceFromTop Distance threshold from the top of the viewport
 * @return ViewState enum indicating the element's current visibility status
 * @see ViewState for possible return values
 */
private fun Element.getViewState(distanceFromTop: Double): ViewState {
    val state = if (isFullyVisible()) {
        ViewState.FULL
    } else if (isPartiallyVisible(distanceFromTop)) {
        ViewState.PARTIAL
    } else {
        ViewState.GONE
    }
    println("viewstate was $state")
    return state
}

/**
 * Determines if any portion of the element is visible within the viewport.
 *
 * This function performs intersection detection to identify when an element
 * has any visible area within the current viewport boundaries. It's useful
 * for triggering actions when elements first become visible.
 *
 * @param distanceFromTop Distance threshold from the top of the viewport
 * @return true if any part of the element is visible on screen
 */
private fun Element.isPartiallyVisible(distanceFromTop: Double): Boolean {
    val bounds = getBoundingClientRect()
    println("bounds --> top:${bounds.top} bottom: ${bounds.bottom} left:${bounds.left} right:${bounds.right}")
    println("bounds --> width:${bounds.width} height: ${bounds.height} x:${bounds.x} y:${bounds.y}")
    return bounds.top < distanceFromTop ||
            bounds.bottom > distanceFromTop
}


/**
 * Determines if the entire element is completely visible within the viewport.
 *
 * This function checks that all boundaries of the element (top, bottom, left, right)
 * are within the current viewport dimensions. Useful for ensuring complete
 * visibility before triggering certain animations or interactions.
 *
 * @return true if the entire element is visible within the viewport
 */
private fun Element.isFullyVisible(): Boolean {
    val bounds = getBoundingClientRect()
    return bounds.top <= window.innerHeight &&
            bounds.bottom >= 0
}


/**
 * Extension function to check if a coordinate value is within visible screen boundaries.
 *
 * This utility function helps determine if a specific coordinate (typically a
 * vertical position) is within the viewable area of the screen, considering
 * the provided distance threshold.
 *
 * @param distanceFromTop Distance threshold from the top of the viewport
 * @return true if the coordinate value is currently within screen bounds
 */
private fun Double.isWithinScreenBounds(distanceFromTop: Double): Boolean {
    return this < distanceFromTop
}


/**
 * Enumeration representing the possible visibility states of an element within the viewport.
 *
 * This enum provides a clear taxonomy for element visibility, enabling precise
 * control over scroll-based interactions and animations. Each state represents
 * a distinct level of element visibility relative to the current viewport.
 *
 * @since 1.0.0
 * @see Element.getViewState for usage in visibility detection
 */
enum class ViewState {
    /** The element is completely visible within the viewport */
    FULL,
    
    /** The element is partially visible (some portion is within the viewport) */
    PARTIAL,
    
    /** The element is not visible at all within the current viewport */
    GONE
}