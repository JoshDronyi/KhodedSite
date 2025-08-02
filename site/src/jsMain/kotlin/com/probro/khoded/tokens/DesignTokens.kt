package com.probro.khoded.tokens

import org.jetbrains.compose.web.css.Color

/**
 * DesignTokens - Centralized design system tokens
 * 
 * Establishes a single source of truth for all design values, eliminating 
 * hardcoded design values throughout the codebase. This supports:
 * - Consistent visual design across components
 * - Easy theme switching and customization
 * - Centralized maintenance of design values
 * - Type-safe access to design constants
 * 
 * Benefits:
 * - DRY principle: No repeated design values
 * - Single source of truth for design system
 * - Easy global design changes
 * - Better design consistency
 * - Theme-able design system
 * 
 * @since 2.0.0 (SOLID principles refactor)
 */

/**
 * Color tokens organized by semantic meaning
 */
object ColorTokens {
    
    // Primary brand colors
    object Primary {
        val c50 = Color("#f0f9ff")
        val c100 = Color("#e0f2fe")
        val c200 = Color("#bae6fd")
        val c300 = Color("#7dd3fc")
        val c400 = Color("#38bdf8")
        val c500 = Color("#0ea5e9")
        val c600 = Color("#0284c7")
        val c700 = Color("#0369a1")
        val c800 = Color("#075985")
        val c900 = Color("#0c4a6e")
        val c950 = Color("#082f49")
    }
    
    // Secondary brand colors
    object Secondary {
        val c50 = Color("#f8fafc")
        val c100 = Color("#f1f5f9")
        val c200 = Color("#e2e8f0")
        val c300 = Color("#cbd5e1")
        val c400 = Color("#94a3b8")
        val c500 = Color("#64748b")
        val c600 = Color("#475569")
        val c700 = Color("#334155")
        val c800 = Color("#1e293b")
        val c900 = Color("#0f172a")
        val c950 = Color("#020617")
    }
    
    // Neutral grays
    object Neutral {
        val c50 = Color("#f9fafb")
        val c100 = Color("#f3f4f6")
        val c200 = Color("#e5e7eb")
        val c300 = Color("#d1d5db")
        val c400 = Color("#9ca3af")
        val c500 = Color("#6b7280")
        val c600 = Color("#4b5563")
        val c700 = Color("#374151")
        val c800 = Color("#1f2937")
        val c900 = Color("#111827")
        val c950 = Color("#030712")
    }
    
    // Semantic colors
    object Success {
        val c50 = Color("#f0fdf4")
        val c100 = Color("#dcfce7")
        val c200 = Color("#bbf7d0")
        val c300 = Color("#86efac")
        val c400 = Color("#4ade80")
        val c500 = Color("#22c55e")
        val c600 = Color("#16a34a")
        val c700 = Color("#15803d")
        val c800 = Color("#166534")
        val c900 = Color("#14532d")
    }
    
    object Warning {
        val c50 = Color("#fffbeb")
        val c100 = Color("#fef3c7")
        val c200 = Color("#fde68a")
        val c300 = Color("#fcd34d")
        val c400 = Color("#fbbf24")
        val c500 = Color("#f59e0b")
        val c600 = Color("#d97706")
        val c700 = Color("#b45309")
        val c800 = Color("#92400e")
        val c900 = Color("#78350f")
    }
    
    object Error {
        val c50 = Color("#fef2f2")
        val c100 = Color("#fee2e2")
        val c200 = Color("#fecaca")
        val c300 = Color("#fca5a5")
        val c400 = Color("#f87171")
        val c500 = Color("#ef4444")
        val c600 = Color("#dc2626")
        val c700 = Color("#b91c1c")
        val c800 = Color("#991b1b")
        val c900 = Color("#7f1d1d")
    }
    
    // Utility colors
    object Utility {
        val white = Color("#ffffff")
        val black = Color("#000000")
        val transparent = Color("transparent")
        val current = Color("currentColor")
    }
}

/**
 * Typography tokens for consistent text styling
 */
object TypographyTokens {
    
    // Font families
    object FontFamily {
        val primary = "Inter, system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
        val secondary = "JetBrains Mono, 'Fira Code', 'Monaco', Consolas, monospace"
        val display = "Cal Sans, Inter, system-ui, sans-serif"
    }
    
    // Font sizes
    object FontSize {
        val xs = "12px"
        val sm = "14px"
        val base = "16px"
        val lg = "18px"
        val xl = "20px"
        val xl2 = "24px"
        val xl3 = "30px"
        val xl4 = "36px"
        val xl5 = "48px"
        val xl6 = "60px"
        val xl7 = "72px"
        val xl8 = "96px"
        val xl9 = "128px"
    }
    
    // Font weights
    object FontWeight {
        val thin = 100
        val extraLight = 200
        val light = 300
        val normal = 400
        val medium = 500
        val semiBold = 600
        val bold = 700
        val extraBold = 800
        val black = 900
    }
    
    // Line heights
    object LineHeight {
        val none = 1.0
        val tight = 1.25
        val snug = 1.375
        val normal = 1.5
        val relaxed = 1.625
        val loose = 2.0
    }
    
    // Letter spacing
    object LetterSpacing {
        val tighter = "-0.05em"
        val tight = "-0.025em"
        val normal = "0em"
        val wide = "0.025em"
        val wider = "0.05em"
        val widest = "0.1em"
    }
}

/**
 * Spacing tokens for consistent layout
 */
object SpacingTokens {
    val px = "1px"
    val s0 = "0px"
    val s0_5 = "2px"
    val s1 = "4px"
    val s1_5 = "6px"
    val s2 = "8px"
    val s2_5 = "10px"
    val s3 = "12px"
    val s3_5 = "14px"
    val s4 = "16px"
    val s5 = "20px"
    val s6 = "24px"
    val s7 = "28px"
    val s8 = "32px"
    val s9 = "36px"
    val s10 = "40px"
    val s11 = "44px"
    val s12 = "48px"
    val s14 = "56px"
    val s16 = "64px"
    val s20 = "80px"
    val s24 = "96px"
    val s28 = "112px"
    val s32 = "128px"
    val s36 = "144px"
    val s40 = "160px"
    val s44 = "176px"
    val s48 = "192px"
    val s52 = "208px"
    val s56 = "224px"
    val s60 = "240px"
    val s64 = "256px"
    val s72 = "288px"
    val s80 = "320px"
    val s96 = "384px"
}

/**
 * Border radius tokens for consistent curves
 */
object BorderRadiusTokens {
    val none = "0px"
    val xs = "2px"
    val sm = "4px"
    val base = "6px"
    val md = "8px"
    val lg = "12px"
    val xl = "16px"
    val xl2 = "20px"
    val xl3 = "24px"
    val full = "9999px"
}

/**
 * Shadow tokens for consistent elevation
 */
object ShadowTokens {
    val xs = "0 1px 2px 0 rgba(0, 0, 0, 0.05)"
    val sm = "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px -1px rgba(0, 0, 0, 0.1)"
    val base = "0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -2px rgba(0, 0, 0, 0.1)"
    val md = "0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -4px rgba(0, 0, 0, 0.1)"
    val lg = "0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1)"
    val xl = "0 25px 50px -12px rgba(0, 0, 0, 0.25)"
    val xl2 = "0 50px 100px -20px rgba(0, 0, 0, 0.25)"
    val inner = "inset 0 2px 4px 0 rgba(0, 0, 0, 0.05)"
}

/**
 * Z-index tokens for consistent layering
 */
object ZIndexTokens {
    val auto = "auto"
    val base = 0
    val docked = 10
    val dropdown = 1000
    val sticky = 1020
    val banner = 1030
    val overlay = 1040
    val modal = 1050
    val popover = 1060
    val skipLink = 1070
    val toast = 1080
    val tooltip = 1090
}

/**
 * Breakpoint tokens for responsive design
 */
object BreakpointTokens {
    val xs = "475px"
    val sm = "640px"
    val md = "768px"
    val lg = "1024px"
    val xl = "1280px"
    val xl2 = "1536px"
}

/**
 * Animation tokens for consistent motion
 */
object AnimationTokens {
    
    object Duration {
        val instant = "0ms"
        val fast = "150ms"
        val normal = "300ms"
        val slow = "500ms"
        val slower = "750ms"
        val slowest = "1000ms"
    }
    
    object Easing {
        val linear = "linear"
        val ease = "ease"
        val easeIn = "ease-in"
        val easeOut = "ease-out"
        val easeInOut = "ease-in-out"
        val bounce = "cubic-bezier(0.68, -0.55, 0.265, 1.55)"
        val smooth = "cubic-bezier(0.4, 0, 0.2, 1)"
    }
}

/**
 * Component-specific tokens
 */
object ComponentTokens {
    
    object Button {
        val height = mapOf(
            "sm" to "32px",
            "md" to "40px",
            "lg" to "48px",
            "xl" to "56px"
        )
        
        val padding = mapOf(
            "sm" to "6px 12px",
            "md" to "8px 16px",
            "lg" to "12px 24px",
            "xl" to "16px 32px"
        )
        
        val fontSize = mapOf(
            "sm" to TypographyTokens.FontSize.sm,
            "md" to TypographyTokens.FontSize.base,
            "lg" to TypographyTokens.FontSize.lg,
            "xl" to TypographyTokens.FontSize.xl
        )
    }
    
    object Input {
        val height = "40px"
        val padding = "8px 12px"
        val fontSize = TypographyTokens.FontSize.base
        val borderRadius = BorderRadiusTokens.base
        val borderWidth = "1px"
    }
    
    object Card {
        val padding = SpacingTokens.s6
        val borderRadius = BorderRadiusTokens.lg
        val shadow = ShadowTokens.sm
    }
    
    object Modal {
        val zIndex = ZIndexTokens.modal
        val backdropBlur = "4px"
        val borderRadius = BorderRadiusTokens.xl
        val shadow = ShadowTokens.xl2
    }
}

/**
 * Utility object to access design tokens by category
 */
object DesignTokens {
    val colors = ColorTokens
    val typography = TypographyTokens
    val spacing = SpacingTokens
    val borderRadius = BorderRadiusTokens
    val shadows = ShadowTokens
    val zIndex = ZIndexTokens
    val breakpoints = BreakpointTokens
    val animations = AnimationTokens
    val components = ComponentTokens
}

/**
 * Extension functions for easy token access
 */
fun String.asColor() = Color(this)
fun Int.asPx() = "${this}px"
fun Double.asRem() = "${this}rem"
fun Double.asEm() = "${this}em"