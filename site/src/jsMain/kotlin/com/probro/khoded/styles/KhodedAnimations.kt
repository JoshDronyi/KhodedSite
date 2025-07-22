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

val slideInRight = Keyframes {
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
    animation: Animation,
    reducedAnimation: Animation? = null
): Modifier = this
    .animation(animation)
    .styleModifier {
        media("(prefers-reduced-motion: reduce)") {
            animation(reducedAnimation ?: Animation(
                name = "none",
                duration = 0.s
            ))
        }
    }

/**
 * Standard entrance animation with motion preference respect
 */
fun Modifier.entranceAnimation(
    keyframes: Keyframes = fadeIn,
    duration: CSSSizeValue<CSSUnit.s> = KhodedAnimations.normal,
    delay: CSSSizeValue<CSSUnit.s> = 0.s,
    timingFunction: AnimationTimingFunction = AnimationTimingFunction.EaseOut
): Modifier = respectMotionPreference(
    Animation(
        name = keyframes,
        duration = duration,
        delay = delay,
        timingFunction = timingFunction,
        fillMode = AnimationFillMode.Both
    )
)

/**
 * Standard hover animation
 */
fun Modifier.hoverAnimation(
    scaleAmount: Double = 1.02,
    duration: CSSSizeValue<CSSUnit.s> = KhodedAnimations.fast
): Modifier = this
    .transition(CSSTransition("transform", duration, AnimationTimingFunction.EaseOut))
    .hover {
        transform { scale(scaleAmount) }
    }

/**
 * Focus animation for accessibility
 */
fun Modifier.focusAnimation(): Modifier = this
    .transition(CSSTransition("all", KhodedAnimations.fast))
    .focus {
        outline("2px solid ${KhodedColors.Focus}")
        outlineOffset(2.px)
        transform { scale(1.02) }
    }

/**
 * Button press animation
 */
fun Modifier.pressAnimation(): Modifier = this
    .transition(CSSTransition("transform", KhodedAnimations.fast))
    .active {
        transform { scale(0.98) }
    }

/**
 * Stagger animation utility for lists
 */
fun Modifier.staggerDelay(index: Int, baseDelay: CSSSizeValue<CSSUnit.s> = 0.1.s): Modifier = this
    .styleModifier {
        property("animation-delay", "${baseDelay.value * index}s")
    }

/**
 * Loading spinner with accessibility
 */
fun Modifier.loadingSpinner(
    size: CSSSizeValue<CSSUnit.px> = 24.px,
    color: Color = KhodedColors.Purple500
): Modifier = this
    .size(size)
    .border(2.px, LineStyle.Solid, KhodedColors.Gray200)
    .borderTop(2.px, LineStyle.Solid, color)
    .borderRadius(50.percent)
    .respectMotionPreference(
        Animation(
            name = spin,
            duration = 1.s,
            iterationCount = AnimationIterationCount.Infinite,
            timingFunction = AnimationTimingFunction.Linear
        ),
        Animation(name = "none", duration = 0.s)
    )
    .attrsModifier {
        attr("role", "status")
        attr("aria-label", "Loading")
    }

/**
 * Smooth scroll behavior
 */
fun Modifier.smoothScroll(): Modifier = this
    .styleModifier {
        property("scroll-behavior", "smooth")
    }

/**
 * Parallax effect utility
 */
fun Modifier.parallax(
    speed: Double = 0.5
): Modifier = this
    .styleModifier {
        property("transform", "translateY(calc(var(--scroll-position, 0) * ${speed}))")
        property("will-change", "transform")
    }

// =============================================================================
// PERFORMANCE OPTIMIZATIONS
// =============================================================================

/**
 * Optimize animations for performance
 */
fun Modifier.optimizeAnimation(): Modifier = this
    .styleModifier {
        property("will-change", "transform, opacity")
        property("backface-visibility", "hidden")
        property("perspective", "1000px")
    }

/**
 * GPU acceleration for smooth animations
 */
fun Modifier.gpuAccelerated(): Modifier = this
    .styleModifier {
        property("transform", "translateZ(0)")
        property("will-change", "transform")
    }

// =============================================================================
// ANIMATION PRESETS
// =============================================================================

object AnimationPresets {
    /**
     * Card hover animation
     */
    fun cardHover() = Modifier
        .transition(CSSTransition("all", KhodedAnimations.normal))
        .hover {
            transform { translateY((-4).px) scale(1.02) }
            boxShadow("0 10px 25px rgba(0, 0, 0, 0.15)")
        }
    
    /**
     * Button animation preset
     */
    fun button() = Modifier
        .transition(CSSTransition("all", KhodedAnimations.fast))
        .hover {
            transform { translateY((-1).px) }
        }
        .active {
            transform { scale(0.98) }
        }
        .focusAnimation()
    
    /**
     * Link animation preset
     */
    fun link() = Modifier
        .transition(CSSTransition("color", KhodedAnimations.fast))
        .hover {
            color(KhodedColors.Purple600)
        }
        .focus {
            outline("2px solid ${KhodedColors.Focus}")
            outlineOffset(2.px)
        }
    
    /**
     * Modal animation preset
     */
    fun modal() = Modifier
        .entranceAnimation(scaleIn, KhodedAnimations.normal)
        .optimizeAnimation()
    
    /**
     * Page transition preset
     */
    fun pageTransition() = Modifier
        .entranceAnimation(slideInUp, KhodedAnimations.slow)
        .optimizeAnimation()
}