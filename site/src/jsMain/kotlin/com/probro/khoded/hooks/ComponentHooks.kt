package com.probro.khoded.hooks

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * ComponentHooks - Eliminates DRY violations with shared component logic
 * 
 * This file contains reusable hooks that extract common state management
 * patterns from components, following the DRY principle and providing
 * consistent behavior across the application.
 * 
 * Benefits:
 * - Eliminates duplicate state management code
 * - Provides consistent interaction patterns
 * - Makes components more focused and testable
 * - Centralizes common UI logic
 * 
 * @since 2.0.0 (SOLID principles refactor)
 */

/**
 * Hook for managing hover state
 * Eliminates duplicate hover logic across interactive components
 */
@Composable
fun useHover(): Pair<Boolean, Pair<() -> Unit, () -> Unit>> {
    var isHovered by remember { mutableStateOf(false) }
    
    val onMouseEnter = { isHovered = true }
    val onMouseLeave = { isHovered = false }
    
    return isHovered to (onMouseEnter to onMouseLeave)
}

/**
 * Hook for managing focus state
 * Eliminates duplicate focus logic across form components
 */
@Composable
fun useFocus(): Triple<Boolean, () -> Unit, () -> Unit> {
    var isFocused by remember { mutableStateOf(false) }
    
    val onFocus = { isFocused = true }
    val onBlur = { isFocused = false }
    
    return Triple(isFocused, onFocus, onBlur)
}

/**
 * Hook for managing press/active state
 * Eliminates duplicate press logic across button components
 */
@Composable
fun usePress(): Triple<Boolean, () -> Unit, () -> Unit> {
    var isPressed by remember { mutableStateOf(false) }
    
    val onMouseDown = { isPressed = true }
    val onMouseUp = { isPressed = false }
    
    // Ensure press state is reset when mouse leaves
    LaunchedEffect(isPressed) {
        if (isPressed) {
            val handleMouseUp = { _: Event -> isPressed = false }
            document.addEventListener("mouseup", handleMouseUp)
        }
    }
    
    return Triple(isPressed, onMouseDown, onMouseUp)
}

/**
 * Hook for managing loading state with automatic timeout
 * Eliminates duplicate loading logic across async components
 */
@Composable
fun useLoading(timeoutMs: Long = 30000): Triple<Boolean, (Boolean) -> Unit, () -> Unit> {
    var isLoading by remember { mutableStateOf(false) }
    
    val setLoading = { loading: Boolean -> isLoading = loading }
    val stopLoading = { isLoading = false }
    
    // Auto-timeout for loading states
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(timeoutMs)
            isLoading = false
        }
    }
    
    return Triple(isLoading, setLoading, stopLoading)
}

/**
 * Hook for managing toggle state (checkboxes, switches, etc.)
 * Eliminates duplicate toggle logic across form controls
 */
@Composable
fun useToggle(initialValue: Boolean = false): Pair<Boolean, () -> Unit> {
    var value by remember { mutableStateOf(initialValue) }
    val toggle = { value = !value }
    return value to toggle
}

/**
 * Hook for managing input value with validation
 * Eliminates duplicate input state management across form fields
 */
@Composable
fun useInput(
    initialValue: String = "",
    validator: (String) -> Boolean = { true }
): Triple<String, (String) -> Unit, Boolean> {
    var value by remember { mutableStateOf(initialValue) }
    var isValid by remember { mutableStateOf(true) }
    
    val setValue = { newValue: String ->
        value = newValue
        isValid = validator(newValue)
    }
    
    return Triple(value, setValue, isValid)
}

/**
 * Hook for managing form submission state
 * Eliminates duplicate form handling logic
 */
@Composable
fun useFormSubmission(): Triple<Boolean, Boolean, (suspend () -> Unit) -> Unit> {
    var isSubmitting by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
    
    val submitForm = { action: suspend () -> Unit ->
        if (!isSubmitting) {
            isSubmitting = true
            hasError = false
            
            GlobalScope.launch {
                try {
                    action.invoke()
                } catch (e: Exception) {
                    hasError = true
                } finally {
                    isSubmitting = false
                }
            }
        }
    }
    
    return Triple(isSubmitting, hasError, submitForm)
}

/**
 * Hook for managing viewport/window size
 * Eliminates duplicate responsive logic
 */
@Composable
fun useViewport(): Pair<Int, Int> {
    var windowSize by remember { mutableStateOf(window.innerWidth to window.innerHeight) }
    
    LaunchedEffect(Unit) {
        val handleResize = { _: Event ->
            windowSize = window.innerWidth to window.innerHeight
        }
        
        window.addEventListener("resize", handleResize)
    }
    
    return windowSize
}

/**
 * Hook for managing scroll position
 * Eliminates duplicate scroll tracking logic
 */
@Composable
fun useScroll(): Triple<Int, Int, Boolean> {
    var scrollX by remember { mutableStateOf(window.pageXOffset.toInt()) }
    var scrollY by remember { mutableStateOf(window.pageYOffset.toInt()) }
    var isScrolling by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        var scrollTimeout: Int? = null
        
        val handleScroll = { _: Event ->
            scrollX = window.pageXOffset.toInt()
            scrollY = window.pageYOffset.toInt()
            isScrolling = true
            
            // Clear existing timeout
            scrollTimeout?.let { window.clearTimeout(it) }
            
            // Set scrolling to false after scrolling stops
            scrollTimeout = window.setTimeout({
                isScrolling = false
            }, 100)
        }
        
        window.addEventListener("scroll", handleScroll)
    }
    
    return Triple(scrollX, scrollY, isScrolling)
}

/**
 * Hook for managing element visibility (intersection observer)
 * Eliminates duplicate visibility detection logic
 */
@Composable
fun useIntersectionObserver(
    threshold: Double = 0.1
): Pair<Boolean, (HTMLElement?) -> Unit> {
    var isVisible by remember { mutableStateOf(false) }
    var targetElement by remember { mutableStateOf<HTMLElement?>(null) }
    
    LaunchedEffect(targetElement) {
        targetElement?.let { element ->
            // In a real implementation, you'd use IntersectionObserver API
            // For now, we'll use a simplified visibility check
            val checkVisibility = {
                val rect = element.getBoundingClientRect()
                val windowHeight = window.innerHeight
                val windowWidth = window.innerWidth
                
                isVisible = rect.bottom >= 0 && 
                           rect.right >= 0 && 
                           rect.top <= windowHeight && 
                           rect.left <= windowWidth
            }
            
            // Initial check
            checkVisibility()
            
            // Check on scroll
            val handleScroll = { _: Event -> checkVisibility() }
            window.addEventListener("scroll", handleScroll)
        }
    }
    
    val setElement = { element: HTMLElement? -> targetElement = element }
    
    return isVisible to setElement
}

/**
 * Hook for managing debounced values
 * Eliminates duplicate debouncing logic across search inputs
 */
@Composable
fun <T> useDebounce(value: T, delayMs: Long = 300): T {
    var debouncedValue by remember { mutableStateOf(value) }
    
    LaunchedEffect(value) {
        delay(delayMs)
        debouncedValue = value
    }
    
    return debouncedValue
}

/**
 * Hook for managing local storage
 * Eliminates duplicate local storage logic
 */
@Composable
fun useLocalStorage(key: String, defaultValue: String = ""): Pair<String, (String) -> Unit> {
    var storedValue by remember {
        mutableStateOf(
            try {
                window.localStorage.getItem(key) ?: defaultValue
            } catch (e: Exception) {
                defaultValue
            }
        )
    }
    
    val setValue = { value: String ->
        try {
            storedValue = value
            window.localStorage.setItem(key, value)
        } catch (e: Exception) {
            console.log("Failed to save to localStorage: $e")
        }
    }
    
    return storedValue to setValue
}

/**
 * Hook for managing clipboard operations
 * Eliminates duplicate clipboard logic
 */
@Composable
fun useClipboard(): Pair<Boolean, (String) -> Unit> {
    var hasCopied by remember { mutableStateOf(false) }
    
    val copyToClipboard: (String) -> Unit = { text: String ->
        try {
            // In a real implementation, you'd use the Clipboard API
            // For now, we'll use a simplified approach
            js("navigator.clipboard.writeText(text)")
            hasCopied = true
            
            // Reset copied state after 2 seconds
            window.setTimeout({
                hasCopied = false
            }, 2000)
        } catch (e: Exception) {
            console.log("Failed to copy to clipboard: $e")
        }
    }
    
    return hasCopied to copyToClipboard
}

/**
 * Hook for managing keyboard shortcuts
 * Eliminates duplicate keyboard handling logic
 */
@Composable
fun useKeyboard(
    shortcuts: Map<String, () -> Unit>
) {
    LaunchedEffect(shortcuts) {
        val handleKeyPress: (Event) -> Unit = { event: Event ->
            val keyEvent = event.asDynamic()
            val key = keyEvent.key as String
            val ctrlKey = keyEvent.ctrlKey as Boolean
            val altKey = keyEvent.altKey as Boolean
            val shiftKey = keyEvent.shiftKey as Boolean
            
            val shortcutKey = buildString {
                if (ctrlKey) append("ctrl+")
                if (altKey) append("alt+")
                if (shiftKey) append("shift+")
                append(key.lowercase())
            }
            
            shortcuts[shortcutKey]?.invoke()
        }
        
        document.addEventListener("keydown", handleKeyPress)
    }
}

/**
 * Composite hook combining common interaction states
 * Provides all interaction states needed for interactive components
 */
@Composable
fun useInteractionStates(): InteractionStates {
    val (isHovered, hoverHandlers) = useHover()
    val (isFocused, onFocus, onBlur) = useFocus()
    val (isPressed, onMouseDown, onMouseUp) = usePress()
    
    return InteractionStates(
        isHovered = isHovered,
        isFocused = isFocused,
        isPressed = isPressed,
        onMouseEnter = hoverHandlers.first,
        onMouseLeave = hoverHandlers.second,
        onFocus = onFocus,
        onBlur = onBlur,
        onMouseDown = onMouseDown,
        onMouseUp = onMouseUp
    )
}

/**
 * Data class to hold all interaction states
 */
data class InteractionStates(
    val isHovered: Boolean,
    val isFocused: Boolean,
    val isPressed: Boolean,
    val onMouseEnter: () -> Unit,
    val onMouseLeave: () -> Unit,
    val onFocus: () -> Unit,
    val onBlur: () -> Unit,
    val onMouseDown: () -> Unit,
    val onMouseUp: () -> Unit
)