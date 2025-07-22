package com.probro.khoded.components.interactions

import androidx.compose.runtime.*
import com.probro.khoded.styles.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import kotlinx.browser.window
import kotlinx.coroutines.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*
import org.w3c.dom.events.*

/**
 * Advanced Interaction Patterns
 * 
 * Sophisticated UI interactions including:
 * - Micro-interactions with haptic feedback
 * - Touch gestures and swipe detection
 * - Progressive enhancement patterns
 * - Context-aware interactions
 * - Smooth state transitions
 */

// =============================================================================
// MICRO-INTERACTION COMPONENTS
// =============================================================================

enum class InteractionState { Idle, Hover, Active, Loading, Success, Error, Disabled }

@Composable
fun MicroInteractionButton(
    text: String,
    onClick: suspend () -> Unit,
    modifier: Modifier = Modifier,
    variant: KhodedButtonVariant = KhodedButtonVariant.Primary,
    size: KhodedButtonSize = KhodedButtonSize.Medium,
    icon: String? = null,
    disabled: Boolean = false,
    hapticFeedback: Boolean = true
) {
    var interactionState by remember { mutableStateOf(InteractionState.Idle) }
    var isPressed by remember { mutableStateOf(false) }
    var rippleX by remember { mutableStateOf(0.0) }
    var rippleY by remember { mutableStateOf(0.0) }
    var showRipple by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    
    val handleClick = { event: MouseEvent ->
        if (!disabled && interactionState != InteractionState.Loading) {
            // Haptic feedback for supported devices
            if (hapticFeedback) {
                triggerHapticFeedback()
            }
            
            // Ripple effect
            val rect = (event.target as org.w3c.dom.Element).getBoundingClientRect()
            rippleX = event.clientX - rect.left
            rippleY = event.clientY - rect.top
            showRipple = true
            
            scope.launch {
                interactionState = InteractionState.Loading
                try {
                    onClick()
                    interactionState = InteractionState.Success
                    delay(1000) // Show success state
                } catch (e: Exception) {
                    interactionState = InteractionState.Error
                    delay(2000) // Show error state longer
                } finally {
                    interactionState = InteractionState.Idle
                    showRipple = false
                }
            }
        }
    }
    
    Button(
        attrs = modifier
            .position(Position.Relative)
            .overflow(Overflow.Hidden)
            .backgroundColor(getButtonColor(variant, interactionState, disabled))
            .color(getButtonTextColor(variant, interactionState, disabled))
            .padding(getButtonPadding(size))
            .borderRadius(KhodedRadius.md)
            .minHeight(KhodedSpacing.touchTargetMin)
            .cursor(if (disabled) Cursor.NotAllowed else Cursor.Pointer)
            .opacity(if (disabled) 0.6 else 1.0)
            .transition(CSSTransition("all", KhodedAnimations.normal))
            .transform {
                when {
                    isPressed && !disabled -> scale(0.96)
                    interactionState == InteractionState.Hover && !disabled -> scale(1.02)
                    else -> scale(1.0)
                }
            }
            .boxShadow(getButtonShadow(interactionState, disabled))
            .toAttrs {
                onMouseDown { isPressed = true }
                onMouseUp { isPressed = false }
                onMouseLeave { isPressed = false }
                onMouseEnter { 
                    if (!disabled) interactionState = InteractionState.Hover 
                }
                onMouseLeave { 
                    if (interactionState == InteractionState.Hover) {
                        interactionState = InteractionState.Idle 
                    }
                }
                onClick { event -> handleClick(event) }
                disabled(disabled)
                
                // Touch events for mobile
                onTouchStart { isPressed = true }
                onTouchEnd { isPressed = false }
            }
    ) {
        // Ripple effect
        if (showRipple) {
            Div(
                attrs = Modifier
                    .position(Position.Absolute)
                    .width(0.px)
                    .height(0.px)
                    .left(rippleX.px)
                    .top(rippleY.px)
                    .backgroundColor(Color.white.copy(alpha = 77)) // 30% opacity
                    .borderRadius(50.percent)
                    .animation(
                        Animation(
                            name = "ripple",
                            duration = 0.6.s,
                            timingFunction = AnimationTimingFunction.EaseOut
                        )
                    )
                    .toAttrs()
            )
        }
        
        // Button content
        Row(
            modifier = Modifier
                .alignItems(AlignItems.Center)
                .gap(KhodedSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with state-based changes
            icon?.let { iconText ->
                Span(
                    attrs = Modifier
                        .fontSize(getIconSize(size))
                        .transition(CSSTransition("transform", KhodedAnimations.fast))
                        .transform {
                            when (interactionState) {
                                InteractionState.Loading -> rotate(360.deg)
                                InteractionState.Success -> scale(1.2)
                                else -> scale(1.0)
                            }
                        }
                        .toAttrs()
                ) {
                    Text(getStateIcon(interactionState, iconText))
                }
            }
            
            // Text with typing animation for loading
            Span(
                attrs = Modifier
                    .fontSize(getButtonFontSize(size))
                    .fontWeight(KhodedTypography.medium)
                    .toAttrs()
            ) {
                Text(getButtonText(interactionState, text))
            }
        }
    }
}

private fun getButtonColor(variant: KhodedButtonVariant, state: InteractionState, disabled: Boolean): Color {
    if (disabled) return KhodedColors.Gray300
    
    return when (state) {
        InteractionState.Loading -> KhodedColors.Gray500
        InteractionState.Success -> KhodedColors.Success
        InteractionState.Error -> KhodedColors.Error
        InteractionState.Hover -> when (variant) {
            KhodedButtonVariant.Primary -> KhodedColors.Purple600
            KhodedButtonVariant.Secondary -> KhodedColors.Teal600
            else -> KhodedColors.Purple500
        }
        else -> when (variant) {
            KhodedButtonVariant.Primary -> KhodedColors.Purple500
            KhodedButtonVariant.Secondary -> KhodedColors.Teal500
            else -> KhodedColors.Purple500
        }
    }
}

private fun getStateIcon(state: InteractionState, defaultIcon: String): String {
    return when (state) {
        InteractionState.Loading -> "⏳"
        InteractionState.Success -> "✓"
        InteractionState.Error -> "⚠"
        else -> defaultIcon
    }
}

private fun getButtonText(state: InteractionState, defaultText: String): String {
    return when (state) {
        InteractionState.Loading -> "Processing..."
        InteractionState.Success -> "Success!"
        InteractionState.Error -> "Try Again"
        else -> defaultText
    }
}

// =============================================================================
// GESTURE DETECTION
// =============================================================================

data class SwipeGesture(
    val direction: SwipeDirection,
    val distance: Double,
    val velocity: Double,
    val duration: Long
)

enum class SwipeDirection { Left, Right, Up, Down, None }

@Composable
fun SwipeDetector(
    onSwipe: (SwipeGesture) -> Unit,
    threshold: Double = 50.0,
    velocityThreshold: Double = 0.3,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var touchStart by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var touchStartTime by remember { mutableStateOf(0L) }
    var isTracking by remember { mutableStateOf(false) }
    
    Div(
        attrs = modifier
            .toAttrs {
                onTouchStart { event ->
                    val touch = event.touches[0]!!
                    touchStart = Pair(touch.clientX, touch.clientY)
                    touchStartTime = kotlinx.browser.window.Date().getTime().toLong()
                    isTracking = true
                }
                
                onTouchMove { event ->
                    if (isTracking) {
                        event.preventDefault() // Prevent scrolling during swipe
                    }
                }
                
                onTouchEnd { event ->
                    if (isTracking && touchStart != null) {
                        val touch = event.changedTouches[0]!!
                        val (startX, startY) = touchStart!!
                        val endX = touch.clientX
                        val endY = touch.clientY
                        val endTime = kotlinx.browser.window.Date().getTime().toLong()
                        
                        val deltaX = endX - startX
                        val deltaY = endY - startY
                        val distance = kotlin.math.sqrt(deltaX * deltaX + deltaY * deltaY)
                        val duration = endTime - touchStartTime
                        val velocity = distance / duration
                        
                        if (distance > threshold && velocity > velocityThreshold) {
                            val direction = when {
                                kotlin.math.abs(deltaX) > kotlin.math.abs(deltaY) -> {
                                    if (deltaX > 0) SwipeDirection.Right else SwipeDirection.Left
                                }
                                deltaY > 0 -> SwipeDirection.Down
                                else -> SwipeDirection.Up
                            }
                            
                            onSwipe(SwipeGesture(direction, distance, velocity, duration))
                        }
                        
                        touchStart = null
                        isTracking = false
                    }
                }
                
                onTouchCancel {
                    touchStart = null
                    isTracking = false
                }
            }
    ) {
        content()
    }
}

// =============================================================================
// PROGRESSIVE ENHANCEMENT
// =============================================================================

@Composable
fun EnhancedWithFallback(
    enhanced: @Composable () -> Unit,
    fallback: @Composable () -> Unit,
    condition: () -> Boolean = { checkJavaScriptSupport() }
) {
    if (condition()) {
        enhanced()
    } else {
        fallback()
    }
}

private fun checkJavaScriptSupport(): Boolean {
    return try {
        js("typeof window !== 'undefined' && typeof document !== 'undefined'") as Boolean
    } catch (e: Exception) {
        false
    }
}

@Composable
fun ProgressiveImage(
    src: String,
    alt: String,
    placeholder: String? = null,
    blurHash: String? = null,
    modifier: Modifier = Modifier
) {
    var imageLoaded by remember { mutableStateOf(false) }
    var imageError by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier.position(Position.Relative)
    ) {
        // Blur hash or placeholder
        if (!imageLoaded && !imageError) {
            if (blurHash != null) {
                BlurHashPlaceholder(blurHash, modifier = Modifier.fillMaxSize())
            } else if (placeholder != null) {
                Img(
                    src = placeholder,
                    alt = "$alt placeholder",
                    attrs = Modifier
                        .fillMaxSize()
                        .filter { blur(5.px) }
                        .transition(CSSTransition("filter", KhodedAnimations.normal))
                        .toAttrs()
                )
            } else {
                ColorPlaceholder(modifier = Modifier.fillMaxSize())
            }
        }
        
        // Main image
        Img(
            src = src,
            alt = alt,
            attrs = Modifier
                .fillMaxSize()
                .opacity(if (imageLoaded) 1.0 else 0.0)
                .transition(CSSTransition("opacity", KhodedAnimations.slow))
                .toAttrs {
                    onLoad { imageLoaded = true }
                    onError { imageError = true }
                }
        )
        
        // Error state
        if (imageError) {
            ErrorPlaceholder(
                alt = alt,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun BlurHashPlaceholder(hash: String, modifier: Modifier = Modifier) {
    // Simplified blur hash implementation
    Canvas(
        attrs = modifier
            .backgroundColor(KhodedColors.Gray200)
            .toAttrs {
                attr("data-blurhash", hash)
            }
    )
}

@Composable
private fun ColorPlaceholder(modifier: Modifier = Modifier) {
    Div(
        attrs = modifier
            .backgroundColor(KhodedColors.Gray200)
            .toAttrs()
    )
}

@Composable
private fun ErrorPlaceholder(alt: String, modifier: Modifier = Modifier) {
    Div(
        attrs = modifier
            .backgroundColor(KhodedColors.Gray100)
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.Center)
            .color(KhodedColors.TextSecondary)
            .fontSize(KhodedTypography.sm)
            .toAttrs()
    ) {
        Text("⚠ Failed to load: $alt")
    }
}

// =============================================================================
// CONTEXT-AWARE INTERACTIONS
// =============================================================================

data class UserContext(
    val isTouch: Boolean,
    val screenSize: ScreenSize,
    val connectionType: String,
    val prefersReducedMotion: Boolean,
    val theme: String
)

@Composable
fun rememberUserContext(): UserContext {
    var context by remember { 
        mutableStateOf(
            UserContext(
                isTouch = false,
                screenSize = ScreenSize.MD,
                connectionType = "4g",
                prefersReducedMotion = false,
                theme = "light"
            )
        ) 
    }
    
    LaunchedEffect(Unit) {
        context = context.copy(
            isTouch = checkTouchSupport(),
            screenSize = getCurrentScreenSize(),
            connectionType = getConnectionType(),
            prefersReducedMotion = checkReducedMotionPreference(),
            theme = getPreferredTheme()
        )
    }
    
    return context
}

private fun checkTouchSupport(): Boolean {
    return try {
        js("'ontouchstart' in window || navigator.maxTouchPoints > 0") as Boolean
    } catch (e: Exception) {
        false
    }
}

private fun getCurrentScreenSize(): ScreenSize {
    val width = window.innerWidth
    return when {
        width >= 1536 -> ScreenSize.XXL
        width >= 1280 -> ScreenSize.XL
        width >= 1024 -> ScreenSize.LG
        width >= 768 -> ScreenSize.MD
        width >= 640 -> ScreenSize.SM
        else -> ScreenSize.XS
    }
}

private fun getConnectionType(): String {
    return try {
        js("navigator.connection?.effectiveType || '4g'") as String
    } catch (e: Exception) {
        "4g"
    }
}

private fun checkReducedMotionPreference(): Boolean {
    return try {
        js("window.matchMedia('(prefers-reduced-motion: reduce)').matches") as Boolean
    } catch (e: Exception) {
        false
    }
}

private fun getPreferredTheme(): String {
    return try {
        js("window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'") as String
    } catch (e: Exception) {
        "light"
    }
}

@Composable
fun ContextAwareComponent(
    context: UserContext,
    touchContent: @Composable () -> Unit,
    mouseContent: @Composable () -> Unit
) {
    if (context.isTouch) {
        touchContent()
    } else {
        mouseContent()
    }
}

// =============================================================================
// SMOOTH STATE TRANSITIONS
// =============================================================================

@Composable
fun <T> AnimatedTransition(
    targetState: T,
    transitionDuration: CSSSizeValue<CSSUnit.s> = KhodedAnimations.normal,
    content: @Composable (T) -> Unit
) {
    var currentState by remember { mutableStateOf(targetState) }
    var isTransitioning by remember { mutableStateOf(false) }
    
    LaunchedEffect(targetState) {
        if (targetState != currentState) {
            isTransitioning = true
            delay(transitionDuration.value.toLong() * 1000)
            currentState = targetState
            isTransitioning = false
        }
    }
    
    Box(
        modifier = Modifier
            .opacity(if (isTransitioning) 0.0 else 1.0)
            .transition(CSSTransition("opacity", transitionDuration))
    ) {
        content(currentState)
    }
}

// =============================================================================
// UTILITY FUNCTIONS
// =============================================================================

private fun triggerHapticFeedback(type: String = "medium") {
    try {
        js("navigator.vibrate && navigator.vibrate(type === 'light' ? 10 : type === 'medium' ? 20 : 30)")
    } catch (e: Exception) {
        // Haptic feedback not supported
    }
}

private fun getButtonPadding(size: KhodedButtonSize): Modifier {
    return when (size) {
        KhodedButtonSize.Small -> Modifier.padding(vertical = KhodedSpacing.sm, horizontal = KhodedSpacing.md)
        KhodedButtonSize.Medium -> Modifier.padding(vertical = KhodedSpacing.md, horizontal = KhodedSpacing.lg)
        KhodedButtonSize.Large -> Modifier.padding(vertical = KhodedSpacing.lg, horizontal = KhodedSpacing.xl)
    }
}

private fun getButtonFontSize(size: KhodedButtonSize): CSSSizeValue<CSSUnit.px> {
    return when (size) {
        KhodedButtonSize.Small -> KhodedTypography.sm
        KhodedButtonSize.Medium -> KhodedTypography.base
        KhodedButtonSize.Large -> KhodedTypography.lg
    }
}

private fun getIconSize(size: KhodedButtonSize): CSSSizeValue<CSSUnit.px> {
    return when (size) {
        KhodedButtonSize.Small -> 16.px
        KhodedButtonSize.Medium -> 20.px
        KhodedButtonSize.Large -> 24.px
    }
}

private fun getButtonTextColor(variant: KhodedButtonVariant, state: InteractionState, disabled: Boolean): Color {
    if (disabled) return KhodedColors.TextMuted
    
    return when (state) {
        InteractionState.Success, InteractionState.Error, InteractionState.Loading -> KhodedColors.TextInverse
        else -> when (variant) {
            KhodedButtonVariant.Ghost -> KhodedColors.Purple500
            else -> KhodedColors.TextInverse
        }
    }
}

private fun getButtonShadow(state: InteractionState, disabled: Boolean): String {
    if (disabled) return "none"
    
    return when (state) {
        InteractionState.Hover -> KhodedShadows.lg
        InteractionState.Active -> KhodedShadows.sm
        InteractionState.Success -> "0 0 20px rgba(34, 197, 94, 0.3)"
        InteractionState.Error -> "0 0 20px rgba(220, 38, 38, 0.3)"
        else -> KhodedShadows.base
    }
}