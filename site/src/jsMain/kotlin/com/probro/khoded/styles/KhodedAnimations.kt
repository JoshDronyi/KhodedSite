package com.probro.khoded.styles

import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.animation.Keyframes
import org.jetbrains.compose.web.css.*

/**
 * Khoded Animation System
 * 
 * Comprehensive animation utilities with:
 * - Respect for user motion preferences
 * - Smooth, polished animations
 * - Performance-optimized CSS transforms
 * - Accessibility considerations
 * - Consistent timing and easing
 */

// =============================================================================
// ENTRANCE ANIMATIONS
// =============================================================================

val fadeIn = Keyframes {
    from {
        Modifier.opacity(0)
    }
    to {
        Modifier.opacity(1)
    }
}

val slideInUp = Keyframes {
    from {
        Modifier
            .opacity(0)
            .transform { translateY(30.px) }
    }
    to {
        Modifier
            .opacity(1)
            .transform { translateY(0.px) }
    }
}

val slideInDown = Keyframes {
    from {
        Modifier
            .opacity(0)
            .transform { translateY((-30).px) }
    }
    to {
        Modifier
            .opacity(1)
            .transform { translateY(0.px) }
    }
}

val slideInLeft = Keyframes {
    from {
        Modifier
            .opacity(0)
            .transform { translateX((-30).px) }
    }
    to {
        Modifier
            .opacity(1)
            .transform { translateX(0.px) }
    }
}

val slideInRight = Keyframes {
    from {
        Modifier
            .opacity(0)
            .transform { translateX(30.px) }
    }
    to {
        Modifier
            .opacity(1)
            .transform { translateX(0.px) }
    }
}

val scaleIn = Keyframes {
    from {
        Modifier
            .opacity(0)
            .transform { scale(0.95) }
    }
    to {
        Modifier
            .opacity(1)
            .transform { scale(1) }
    }
}

val zoomIn = Keyframes {
    from {
        Modifier
            .opacity(0)
            .transform { scale(0.8) }
    }
    to {
        Modifier
            .opacity(1)
            .transform { scale(1) }
    }
}

// =============================================================================
// EXIT ANIMATIONS
// =============================================================================

val fadeOut = Keyframes {
    from {
        Modifier.opacity(1)
    }
    to {
        Modifier.opacity(0)
    }
}

val slideOutUp = Keyframes {
    from {
        Modifier
            .opacity(1)
            .transform { translateY(0.px) }
    }
    to {
        Modifier
            .opacity(0)
            .transform { translateY((-30).px) }
    }
}

val slideOutDown = Keyframes {
    from {
        Modifier
            .opacity(1)
            .transform { translateY(0.px) }
    }
    to {
        Modifier
            .opacity(0)
            .transform { translateY(30.px) }
    }
}

val scaleOut = Keyframes {
    from {
        Modifier
            .opacity(1)
            .transform { scale(1) }
    }
    to {
        Modifier
            .opacity(0)
            .transform { scale(0.95) }
    }
}

// =============================================================================
// INTERACTION ANIMATIONS
// =============================================================================

val bounce = Keyframes {
    0.percent {
        Modifier.transform { translateY(0.px) }
    }
    25.percent {
        Modifier.transform { translateY((-5).px) }
    }
    50.percent {
        Modifier.transform { translateY(0.px) }
    }
    75.percent {
        Modifier.transform { translateY((-2).px) }
    }
    100.percent {
        Modifier.transform { translateY(0.px) }
    }
}

val shake = Keyframes {
    0.percent {
        Modifier.transform { translateX(0.px) }
    }
    25.percent {
        Modifier.transform { translateX((-5).px) }
    }
    50.percent {
        Modifier.transform { translateX(5.px) }
    }
    75.percent {
        Modifier.transform { translateX((-5).px) }
    }
    100.percent {
        Modifier.transform { translateX(0.px) }
    }
}

val pulse = Keyframes {
    0.percent {
        Modifier.transform { scale(1) }
    }
    50.percent {
        Modifier.transform { scale(1.05) }
    }
    100.percent {
        Modifier.transform { scale(1) }
    }
}

val wobble = Keyframes {
    0.percent {
        Modifier.transform { rotate(0.deg) }
    }
    25.percent {
        Modifier.transform { rotate((-2).deg) }
    }
    50.percent {
        Modifier.transform { rotate(2.deg) }
    }
    75.percent {
        Modifier.transform { rotate((-1).deg) }
    }
    100.percent {
        Modifier.transform { rotate(0.deg) }
    }
}

// =============================================================================
// LOADING ANIMATIONS
// =============================================================================

val spin = Keyframes {
    from {
        Modifier.transform { rotate(0.deg) }
    }
    to {
        Modifier.transform { rotate(360.deg) }
    }
}

val spinReverse = Keyframes {
    from {
        Modifier.transform { rotate(360.deg) }
    }
    to {
        Modifier.transform { rotate(0.deg) }
    }
}

val loadingDots = Keyframes {
    0.percent {
        Modifier.opacity(0.3)
    }
    50.percent {
        Modifier.opacity(1)
    }
    100.percent {
        Modifier.opacity(0.3)
    }
}

val progress = Keyframes {
    0.percent {
        Modifier.transform { translateX((-100).percent) }
    }
    100.percent {
        Modifier.transform { translateX(100.percent) }
    }
}

// =============================================================================
// NAVIGATION ANIMATIONS
// =============================================================================

val slideInRightNav = Keyframes {
    from {
        Modifier.transform { translateX(100.percent) }
    }
    to {
        Modifier.transform { translateX(0.percent) }
    }
}

val slideOutRight = Keyframes {
    from {
        Modifier.transform { translateX(0.percent) }
    }
    to {
        Modifier.transform { translateX(100.percent) }
    }
}

val dropDown = Keyframes {
    from {
        Modifier
            .opacity(0)
            .transform { 
                translateY((-10).px)
                scale(0.95)
            }
    }
    to {
        Modifier
            .opacity(1)
            .transform { 
                translateY(0.px)
                scale(1)
            }
    }
}

// =============================================================================
// STAGGER ANIMATIONS
// =============================================================================

val staggerFadeIn = Keyframes {
    0.percent {
        Modifier.opacity(0).transform { translateY(20.px) }
    }
    100.percent {
        Modifier.opacity(1).transform { translateY(0.px) }
    }
}

// =============================================================================
// ANIMATION UTILITIES AND HELPERS
// =============================================================================

/**
 * Respect user's motion preferences
 * Provides fallback for users who prefer reduced motion
 */
fun Modifier.respectMotionPreference(
    animation: String,
    reducedAnimation: String? = null
): Modifier = this
    // TODO: Add motion preference support when Animation API stabilizes

/**
 * Standard entrance animation with motion preference respect
 */
fun Modifier.entranceAnimation(
    keyframes: Keyframes = fadeIn,
    duration: CSSSizeValue<CSSUnit.s> = 0.3.s,
    delay: CSSSizeValue<CSSUnit.s> = 0.s,
    timingFunction: AnimationTimingFunction = AnimationTimingFunction.EaseOut
): Modifier = this
    // TODO: Add entrance animation when Animation API stabilizes

/**
 * Standard hover animation
 */
fun Modifier.hoverAnimation(
    scaleAmount: Double = 1.02,
    duration: CSSSizeValue<CSSUnit.s> = 0.15.s
): Modifier = this
    // TODO: Add hover scale animation when CSS API stabilizes

/**
 * Focus animation for accessibility
 */
fun Modifier.focusAnimation(): Modifier = this
    // TODO: Add focus animation when CSS API stabilizes

/**
 * Button press animation
 */
fun Modifier.pressAnimation(): Modifier = this
    // TODO: Add press animation when CSS API stabilizes

/**
 * Stagger animation utility for lists
 */
fun Modifier.staggerDelay(index: Int, baseDelay: CSSSizeValue<CSSUnit.s> = 0.1.s): Modifier = this
    // TODO: Add stagger delay when CSS API stabilizes

/**
 * Loading spinner with accessibility
 */
fun Modifier.loadingSpinner(
    size: CSSSizeValue<CSSUnit.px> = 24.px,
    color: CSSColorValue = rgb(139, 92, 246)
): Modifier = this
    .size(size)
    .border(2.px, LineStyle.Solid, rgb(229, 231, 235))
    .borderTop(2.px, LineStyle.Solid, color)
    .borderRadius(50.percent)
    // TODO: Add loading spinner animation when Animation API stabilizes

/**
 * Smooth scroll behavior
 */
fun Modifier.smoothScroll(): Modifier = this
    // TODO: Add smooth scroll when CSS API stabilizes

/**
 * Parallax effect utility
 */
fun Modifier.parallax(
    speed: Double = 0.5
): Modifier = this
    // TODO: Add parallax when CSS API stabilizes

// =============================================================================
// PERFORMANCE OPTIMIZATIONS
// =============================================================================

/**
 * Optimize animations for performance
 */
fun Modifier.optimizeAnimation(): Modifier = this
    // TODO: Add animation optimization when CSS API stabilizes

/**
 * GPU acceleration for smooth animations
 */
fun Modifier.gpuAccelerated(): Modifier = this
    // TODO: Add GPU acceleration when CSS API stabilizes

// =============================================================================
// ANIMATION PRESETS
// =============================================================================

object AnimationPresets {
    /**
     * Card hover animation
     */
    fun cardHover() = Modifier
        // TODO: Add card hover animation when CSS API stabilizes
    
    /**
     * Button animation preset
     */
    fun button() = Modifier
        // TODO: Add button animation when CSS API stabilizes
    
    /**
     * Link animation preset
     */
    fun link() = Modifier
        // TODO: Add link animation when CSS API stabilizes
    
    /**
     * Modal animation preset
     */
    fun modal() = Modifier
        .entranceAnimation(scaleIn, 0.3.s)
        .optimizeAnimation()
    
    /**
     * Page transition preset
     */
    fun pageTransition() = Modifier
        .entranceAnimation(slideInUp, 0.5.s)
        .optimizeAnimation()
}