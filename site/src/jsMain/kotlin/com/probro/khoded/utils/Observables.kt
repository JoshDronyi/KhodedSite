package com.probro.khoded.utils

import androidx.compose.runtime.*
import com.probro.khoded.utils.Constants.SECTION_HEIGHT
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Element
import org.w3c.dom.events.EventListener

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

private fun Element.topIsInBounds(): Boolean {
    val bounds = getBoundingClientRect()
    val viewportBottom = window.pageYOffset + window.innerHeight
    return bounds.top >= (-bounds.height.div(2))
            && bounds.top < (viewportBottom - bounds.height.div(2))
}

private fun Element.bottomIsInBounds(): Boolean {
    val bounds = getBoundingClientRect()
    val viewportBottom = window.pageYOffset + window.innerHeight
    return bounds.bottom <= viewportBottom + bounds.height.div(2)
            && bounds.bottom > 0
}

/**
 * Checks to see if an element is visible on the screen,
 *
 * @return ViewState -> An enum representing if an element is fully visible, partially visible or not visible at all.
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
 * Checks to see if any of the corners are visible on the screen.
 *
 * @return a boolean representing if the element can be seen at all.
 */
private fun Element.isPartiallyVisible(distanceFromTop: Double): Boolean {
    val bounds = getBoundingClientRect()
    println("bounds --> top:${bounds.top} bottom: ${bounds.bottom} left:${bounds.left} right:${bounds.right}")
    println("bounds --> width:${bounds.width} height: ${bounds.height} x:${bounds.x} y:${bounds.y}")
    return bounds.top < distanceFromTop ||
            bounds.bottom > distanceFromTop
}


/**
 * Checks to see if all four corners of an element are on screen.
 *
 * @return true if all corners are on screen.
 */
private fun Element.isFullyVisible(): Boolean {
    val bounds = getBoundingClientRect()
    return bounds.top <= window.innerHeight &&
            bounds.bottom >= 0
}


/**
 * Checks to see if a double value is within the visible screen boundaries.
 *  @returns true if the double is currently shown on screen.
 */
private fun Double.isWithinScreenBounds(distanceFromTop: Double): Boolean {
    return this < distanceFromTop
}


/**
 * View state: an enum class representing the possible visibility states for a
 *
 * @constructor Create empty View state
 */
enum class ViewState {
    FULL, PARTIAL, GONE
}