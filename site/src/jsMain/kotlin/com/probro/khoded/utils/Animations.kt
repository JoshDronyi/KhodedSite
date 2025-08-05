package com.probro.khoded.utils

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.silk.style.animation.Keyframes
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

/**
 * Animation keyframes for smooth UI transitions and visual effects.
 *
 * This file provides pre-defined animation keyframes for common UI interactions
 * and visual feedback. These animations enhance user experience by providing
 * smooth transitions and visual continuity throughout the application.
 *
 * All animations use CSS properties that can be hardware-accelerated for
 * optimal performance across devices and browsers.
 *
 * @since 1.0.0
 * @see com.probro.khoded.styles.animations for additional animation utilities
 */

/**
 * Fall-in animation keyframes for element entrance effects.
 *
 * Creates a smooth transition where elements appear from above the viewport
 * while fading in from transparent to opaque. This animation is commonly used
 * for section reveals, content loading, and scroll-triggered animations.
 *
 * Animation properties:
 * - Starting state: Invisible (0% opacity) and positioned 150px above final position
 * - Ending state: Fully visible (100% opacity) at final position (top: 0)
 * - Uses transform-friendly properties for hardware acceleration
 *
 * Usage example:
 * ```kotlin
 * modifier.animation(
 *     fallInAnimation.toAnimation(
 *         duration = 0.8.s,
 *         timingFunction = AnimationTimingFunction.EaseOut
 *     )
 * )
 * ```
 */
val fallInAnimation = Keyframes {
    from {
        Modifier
            .opacity(0.percent)
            .top((-150).px)
    }
    to {
        Modifier
            .opacity(100.percent)
            .top(0.px)

    }
}

/**
 * Fly-up animation keyframes for element exit effects.
 *
 * Creates a smooth transition where elements disappear by moving upward
 * while fading out from opaque to transparent. This animation provides
 * the reverse effect of fallInAnimation and is commonly used for element
 * removal, content hiding, and transition effects.
 *
 * Animation properties:
 * - Starting state: Fully visible (100% opacity) at current position
 * - Ending state: Invisible (0% opacity) and positioned 150px above final position
 * - Mirrors the fallInAnimation for consistent visual behavior
 *
 * Usage example:
 * ```kotlin
 * modifier.animation(
 *     flyUpAnimation.toAnimation(
 *         duration = 0.6.s,
 *         timingFunction = AnimationTimingFunction.EaseIn
 *     )
 * )
 * ```
 */
val flyUpAnimation = Keyframes {
    from {
        Modifier
            .opacity(100.percent)
            .top(0.px)
    }
    to {
        Modifier
            .opacity(0.percent)
            .top((-150).px)
    }
}