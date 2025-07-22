package com.probro.khoded.utils

import androidx.compose.runtime.*
import com.probro.khoded.styles.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.toPx
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.w3c.dom.events.Event

/**
 * Responsive utilities for consistent mobile-first design across the Khoded website
 */

// Standard responsive breakpoints (mobile-first) - Updated to use design system
object ResponsiveBreakpoints {
    const val MOBILE = 0 // 0px and up
    val SMALL = KhodedBreakpoints.sm.value.toInt() // 640px and up
    val MEDIUM = KhodedBreakpoints.md.value.toInt() // 768px and up
    val LARGE = KhodedBreakpoints.lg.value.toInt() // 1024px and up
    val EXTRA_LARGE = KhodedBreakpoints.xl.value.toInt() // 1280px and up
}

// Responsive spacing utilities (mobile-first) - Updated to use design system
object ResponsiveSpacing {
    @Composable
    fun containerPadding() = ResponsiveModifier.builder()
        .mobile(Modifier.padding(KhodedSpacing.lg))
        .small(Modifier.padding(KhodedSpacing.xl))
        .medium(Modifier.padding(KhodedSpacing.xl2))
        .large(Modifier.padding(KhodedSpacing.xl3))
        .build()

    @Composable
    fun sectionMargin() = ResponsiveModifier.builder()
        .mobile(Modifier.margin(topBottom = KhodedSpacing.lg))
        .small(Modifier.margin(topBottom = KhodedSpacing.xl2))
        .medium(Modifier.margin(topBottom = KhodedSpacing.xl3))
        .large(Modifier.margin(topBottom = KhodedSpacing.xl5))
        .build()

    @Composable
    fun componentGap() = ResponsiveModifier.builder()
        .mobile(Modifier.gap(KhodedSpacing.md))
        .small(Modifier.gap(KhodedSpacing.lg))
        .medium(Modifier.gap(KhodedSpacing.xl))
        .large(Modifier.gap(KhodedSpacing.xl2))
        .build()
}

// Responsive typography utilities - Updated to use design system
object ResponsiveTypography {
    @Composable
    fun headingSize() = ResponsiveModifier.builder()
        .mobile(Modifier.fontSize(KhodedTypography.xl2).lineHeight(KhodedTypography.tight))
        .small(Modifier.fontSize(KhodedTypography.xl3).lineHeight(KhodedTypography.snug))
        .medium(Modifier.fontSize(KhodedTypography.xl4).lineHeight(KhodedTypography.snug))
        .large(Modifier.fontSize(KhodedTypography.xl5).lineHeight(KhodedTypography.tight))
        .build()

    @Composable
    fun bodySize() = ResponsiveModifier.builder()
        .mobile(Modifier.fontSize(KhodedTypography.base).lineHeight(KhodedTypography.normal))
        .small(Modifier.fontSize(17.px).lineHeight(KhodedTypography.relaxed))
        .medium(Modifier.fontSize(KhodedTypography.lg).lineHeight(KhodedTypography.relaxed))
        .large(Modifier.fontSize(KhodedTypography.xl).lineHeight(KhodedTypography.loose))
        .build()

    @Composable
    fun buttonSize() = ResponsiveModifier.builder()
        .mobile(Modifier.fontSize(KhodedTypography.base).padding(KhodedSpacing.lg, KhodedSpacing.md).minHeight(KhodedSpacing.touchTargetMin))
        .small(Modifier.fontSize(17.px).padding(KhodedSpacing.xl, KhodedSpacing.lg).minHeight(50.px))
        .medium(Modifier.fontSize(KhodedTypography.lg).padding(KhodedSpacing.xl, KhodedSpacing.lg).minHeight(52.px))
        .large(Modifier.fontSize(KhodedTypography.lg).padding(KhodedSpacing.xl2, KhodedSpacing.xl).minHeight(54.px))
        .build()
}

// Builder pattern for responsive modifiers
class ResponsiveModifier private constructor() {
    private var mobileModifier: Modifier = Modifier
    private var smallModifier: Modifier = Modifier
    private var mediumModifier: Modifier = Modifier
    private var largeModifier: Modifier = Modifier
    private var extraLargeModifier: Modifier = Modifier

    companion object {
        fun builder() = ResponsiveModifier()
    }

    fun mobile(modifier: Modifier): ResponsiveModifier {
        mobileModifier = modifier
        return this
    }

    fun small(modifier: Modifier): ResponsiveModifier {
        smallModifier = modifier
        return this
    }

    fun medium(modifier: Modifier): ResponsiveModifier {
        mediumModifier = modifier
        return this
    }

    fun large(modifier: Modifier): ResponsiveModifier {
        largeModifier = modifier
        return this
    }

    fun extraLarge(modifier: Modifier): ResponsiveModifier {
        extraLargeModifier = modifier
        return this
    }

    @Composable
    fun build(): Modifier {
        val currentWidth by rememberBreakpointState()
        
        return when {
            currentWidth >= ResponsiveBreakpoints.EXTRA_LARGE -> extraLargeModifier.takeIf { it != Modifier } ?: largeModifier.takeIf { it != Modifier } ?: mediumModifier.takeIf { it != Modifier } ?: smallModifier.takeIf { it != Modifier } ?: mobileModifier
            currentWidth >= ResponsiveBreakpoints.LARGE -> largeModifier.takeIf { it != Modifier } ?: mediumModifier.takeIf { it != Modifier } ?: smallModifier.takeIf { it != Modifier } ?: mobileModifier
            currentWidth >= ResponsiveBreakpoints.MEDIUM -> mediumModifier.takeIf { it != Modifier } ?: smallModifier.takeIf { it != Modifier } ?: mobileModifier
            currentWidth >= ResponsiveBreakpoints.SMALL -> smallModifier.takeIf { it != Modifier } ?: mobileModifier
            else -> mobileModifier
        }
    }
}

// Utility functions for common responsive patterns
@Composable
fun Modifier.responsiveWidth(
    mobile: CSSSizeValue<CSSUnit.percent> = 100.percent,
    small: CSSSizeValue<CSSUnit.percent> = mobile,
    medium: CSSSizeValue<CSSUnit.percent> = small,
    large: CSSSizeValue<CSSUnit.percent> = medium
): Modifier {
    val currentWidth by rememberBreakpointState()
    
    val width = when {
        currentWidth >= ResponsiveBreakpoints.LARGE -> large
        currentWidth >= ResponsiveBreakpoints.MEDIUM -> medium
        currentWidth >= ResponsiveBreakpoints.SMALL -> small
        else -> mobile
    }
    
    return this.fillMaxWidth(width)
}

@Composable
fun Modifier.responsivePadding(
    mobile: CSSLengthValue = 16.px,
    small: CSSLengthValue = 20.px,
    medium: CSSLengthValue = 32.px,
    large: CSSLengthValue = 40.px
): Modifier {
    val currentWidth by rememberBreakpointState()
    
    val padding = when {
        currentWidth >= ResponsiveBreakpoints.LARGE -> large
        currentWidth >= ResponsiveBreakpoints.MEDIUM -> medium
        currentWidth >= ResponsiveBreakpoints.SMALL -> small
        else -> mobile
    }
    
    return this.padding(padding)
}

@Composable
fun Modifier.responsiveMargin(
    mobile: CSSLengthValue = 16.px,
    small: CSSLengthValue = 20.px,
    medium: CSSLengthValue = 24.px,
    large: CSSLengthValue = 32.px
): Modifier {
    val currentWidth by rememberBreakpointState()
    
    val margin = when {
        currentWidth >= ResponsiveBreakpoints.LARGE -> large
        currentWidth >= ResponsiveBreakpoints.MEDIUM -> medium
        currentWidth >= ResponsiveBreakpoints.SMALL -> small
        else -> mobile
    }
    
    return this.margin(topBottom = margin)
}

@Composable
fun Modifier.responsiveFontSize(
    mobile: CSSLengthValue = 16.px,
    small: CSSLengthValue = 17.px,
    medium: CSSLengthValue = 18.px,
    large: CSSLengthValue = 20.px
): Modifier {
    val currentWidth by rememberBreakpointState()
    
    val fontSize = when {
        currentWidth >= ResponsiveBreakpoints.LARGE -> large
        currentWidth >= ResponsiveBreakpoints.MEDIUM -> medium
        currentWidth >= ResponsiveBreakpoints.SMALL -> small
        else -> mobile
    }
    
    return this.fontSize(fontSize)
}

// Enhanced breakpoint state management
@Composable
private fun rememberBreakpointState(): State<Double> {
    val breakpointState = remember { mutableStateOf(window.innerWidth.toDouble()) }

    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = {
            breakpointState.value = window.innerWidth.toDouble()
        }

        window.addEventListener("resize", listener)
        // Also listen for orientation changes on mobile
        window.addEventListener("orientationchange", listener)

        onDispose {
            window.removeEventListener("resize", listener)
            window.removeEventListener("orientationchange", listener)
        }
    }

    return breakpointState
}

// Device detection utilities
@Composable
fun isMobile(): Boolean {
    val width by rememberBreakpointState()
    return width < ResponsiveBreakpoints.MEDIUM
}

@Composable
fun isTablet(): Boolean {
    val width by rememberBreakpointState()
    return width >= ResponsiveBreakpoints.MEDIUM && width < ResponsiveBreakpoints.LARGE
}

@Composable
fun isDesktop(): Boolean {
    val width by rememberBreakpointState()
    return width >= ResponsiveBreakpoints.LARGE
}

// Touch-friendly component utilities - Updated to use design system
object TouchFriendly {
    val MIN_TOUCH_TARGET = KhodedSpacing.touchTargetMin // WCAG AAA minimum touch target
    val RECOMMENDED_TOUCH_TARGET = KhodedSpacing.touchTargetLarge // Recommended size
    
    fun touchTarget() = Modifier
        .minHeight(MIN_TOUCH_TARGET)
        .minWidth(MIN_TOUCH_TARGET)
    
    fun recommendedTouchTarget() = Modifier
        .minHeight(RECOMMENDED_TOUCH_TARGET)
        .minWidth(RECOMMENDED_TOUCH_TARGET)
}

// Container utilities for consistent layouts
@Composable
fun Modifier.responsiveContainer(maxWidth: CSSLengthValue = 1200.px): Modifier {
    return this
        .fillMaxWidth()
        .maxWidth(maxWidth)
        .margin(leftRight = org.jetbrains.compose.web.css.auto)
        .responsivePadding()
}

@Composable
fun Modifier.responsiveSection(): Modifier {
    return this
        .fillMaxWidth()
        .responsiveMargin()
        .responsivePadding()
}

// Grid utilities for responsive layouts
@Composable
fun Modifier.responsiveGrid(
    mobileColumns: Int = 1,
    smallColumns: Int = 2,
    mediumColumns: Int = 3,
    largeColumns: Int = 4
): Modifier {
    val currentWidth by rememberBreakpointState()
    
    val columns = when {
        currentWidth >= ResponsiveBreakpoints.LARGE -> largeColumns
        currentWidth >= ResponsiveBreakpoints.MEDIUM -> mediumColumns
        currentWidth >= ResponsiveBreakpoints.SMALL -> smallColumns
        else -> mobileColumns
    }
    
    return this
        .display(DisplayStyle.Grid)
        .gridTemplateColumns { repeat(columns) { size(1.fr) } }
        .gap(16.px)
}