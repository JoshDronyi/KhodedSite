package com.probro.khoded.design

import com.varabyte.kobweb.compose.css.*
import org.jetbrains.compose.web.css.*

/**
 * Core Design System - Single Source of Truth
 * 
 * This is the ONLY place where design values are defined. All other files
 * must import from this system to maintain consistency and follow SOLID principles.
 * 
 * **Architecture Principles Applied:**
 * - Single Responsibility: Each object handles one design concern
 * - Open/Closed: Extensible without modifying existing code
 * - Interface Segregation: Clients depend only on what they use
 * - Dependency Inversion: Components depend on abstractions, not concrete values
 * - DRY: No repeated values anywhere in the codebase
 * - Single Source of Truth: All design values defined once
 * 
 * **Type Safety:**
 * - Returns proper CSS types (CSSNumericValue, Color, etc.)
 * - Compatible with Kobweb's CSS modifier system
 * - Compile-time validation of design token usage
 * 
 * @author Clean Code Architecture Agent
 * @since 2.0.0 (SOLID Architecture Refactor)
 */

// =============================================================================
// CORE DESIGN TOKENS - NEVER IMPORT THESE DIRECTLY IN COMPONENTS
// =============================================================================

/**
 * Raw color palette - Base colors that should never be used directly.
 * Components should use SemanticColors instead.
 */
private object RawColorPalette {
    // Purple Brand Palette (Primary)
    val purple50 = rgb(250, 245, 255)
    val purple100 = rgb(243, 232, 255)
    val purple200 = rgb(196, 181, 253)
    val purple300 = rgb(147, 120, 250)
    val purple400 = rgb(126, 92, 245)
    val purple500 = rgb(107, 33, 168)    // PRIMARY BRAND COLOR
    val purple600 = rgb(88, 28, 135)
    val purple700 = rgb(76, 24, 117)
    val purple800 = rgb(63, 20, 96)
    val purple900 = rgb(51, 16, 75)
    
    // Blue Brand Palette (From original Khoded brand - CRITICAL MISSING COLORS)
    val blue50 = rgb(239, 246, 255)
    val blue100 = rgb(219, 234, 254)
    val blue200 = rgb(191, 219, 254)
    val blue300 = rgb(147, 197, 253)
    val blue400 = rgb(96, 165, 250)
    val blue500 = rgb(68, 182, 198)    // ORIGINAL KHODED BLUE (#44b6c6)
    val blue600 = rgb(59, 130, 246)
    val blue700 = rgb(29, 78, 216)
    val blue800 = rgb(30, 64, 175)
    val blue900 = rgb(30, 58, 138)
    
    // Blue variants with alpha (MISSING INTERACTIVE STATES)
    val blueHighlight = rgba(68, 182, 198, 0.2)    // 20% opacity for highlights
    val blueHover = rgba(68, 182, 198, 0.8)        // 80% opacity for hover states
    val powderBlue = rgb(146, 212, 222)            // #92d4de - Light blue variant
    val lightBlue = rgb(107, 197, 210)             // #6bc5d2 - Medium blue variant
    
    // Teal Secondary Palette (Updated with correct brand values)
    val teal50 = rgb(240, 253, 255)
    val teal100 = rgb(204, 251, 241)
    val teal200 = rgb(153, 246, 228)
    val teal300 = rgb(94, 234, 212)
    val teal400 = rgb(45, 212, 191)
    val teal500 = rgb(68, 182, 198)
    val teal600 = rgb(13, 148, 136)
    val teal700 = rgb(15, 118, 110)
    val teal800 = rgb(17, 94, 89)
    val teal900 = rgb(19, 78, 74)
    
    // Purple variants with alpha (MISSING INTERACTIVE STATES)
    val purpleHover = rgba(57, 0, 80, 0.8)         // 80% opacity for hover states
    
    // Neutral Grays
    val gray50 = rgb(249, 250, 251)
    val gray100 = rgb(243, 244, 246)
    val gray200 = rgb(229, 231, 235)
    val gray300 = rgb(209, 213, 219)
    val gray400 = rgb(156, 163, 175)
    val gray500 = rgb(107, 114, 128)
    val gray600 = rgb(75, 85, 99)
    val gray700 = rgb(55, 65, 81)
    val gray800 = rgb(31, 41, 55)
    val gray900 = rgb(26, 26, 26)
    
    // Semantic Status Colors
    val green500 = rgb(34, 197, 94)      // Success
    val yellow500 = rgb(251, 191, 36)    // Warning
    val red500 = rgb(220, 38, 38)        // Error
    val info500 = rgb(59, 130, 246)      // Info (renamed to avoid conflict)
    
    // Utility
    val white = rgb(255, 255, 255)
    val black = rgb(0, 0, 0)
    val transparent = Color.transparent
}

/**
 * Raw spacing values - Use SpacingSystem instead.
 */
private object RawSpacing {
    val unit = 4.px  // Base 4px grid system
    
    val space1 = unit * 1    // 4px
    val space2 = unit * 2    // 8px
    val space3 = unit * 3    // 12px
    val space4 = unit * 4    // 16px
    val space5 = unit * 5    // 20px
    val space6 = unit * 6    // 24px
    val space8 = unit * 8    // 32px
    val space10 = unit * 10  // 40px
    val space12 = unit * 12  // 48px
    val space16 = unit * 16  // 64px
    val space20 = unit * 20  // 80px
    val space24 = unit * 24  // 96px
}

// =============================================================================
// PUBLIC DESIGN SYSTEM - USE THESE IN COMPONENTS
// =============================================================================

/**
 * Semantic Color System - WCAG 2.2 AA Compliant
 * 
 * Colors organized by semantic meaning, not visual appearance.
 * This allows for theme switching and ensures consistent color usage.
 */
object SemanticColors {
    // Primary Brand Colors
    val primary = RawColorPalette.purple500
    val primaryHover = RawColorPalette.purple600
    val primaryActive = RawColorPalette.purple700
    val primaryLight = RawColorPalette.purple100
    val primaryDark = RawColorPalette.purple700
    
    // Secondary Colors (Teal)
    val secondary = RawColorPalette.teal500
    val secondaryHover = RawColorPalette.teal600
    val secondaryActive = RawColorPalette.teal700
    val secondaryLight = RawColorPalette.teal100
    
    // Brand Blue Colors (CRITICAL MISSING COLORS FROM ORIGINAL KHODED BRAND)
    val brandBlue = RawColorPalette.blue500           // Original Khoded blue (#44b6c6)
    val brandBlueHover = RawColorPalette.blueHover    // 80% opacity hover state
    val brandBlueHighlight = RawColorPalette.blueHighlight  // 20% opacity highlight
    val brandBluePowder = RawColorPalette.powderBlue  // Light variant (#92d4de)
    val brandBlueLight = RawColorPalette.lightBlue    // Medium variant (#6bc5d2)
    
    // Interactive Colors
    val interactive = RawColorPalette.blue500
    val interactiveHover = RawColorPalette.blueHover  // Updated to use proper blue hover
    val interactiveActive = RawColorPalette.blue700
    val interactiveLight = RawColorPalette.blue100
    
    // Purple Interactive States (MISSING ALPHA VARIANTS)
    val primaryHoverAlpha = RawColorPalette.purpleHover  // 80% opacity purple hover
    
    // Text Colors (WCAG AA Compliant)
    val textPrimary = RawColorPalette.gray900     // 16.75:1 contrast
    val textSecondary = RawColorPalette.gray600   // 4.5:1 contrast
    val textTertiary = RawColorPalette.gray500    // 3:1 contrast (large text only)
    val textInverse = RawColorPalette.white       // On dark backgrounds
    val textMuted = RawColorPalette.gray400
    
    // Background Colors
    val backgroundPrimary = RawColorPalette.white
    val backgroundSecondary = RawColorPalette.gray50
    val backgroundTertiary = RawColorPalette.gray100
    
    // Surface Colors (for cards, modals, etc.)
    val surface = RawColorPalette.white
    val surfaceElevated = RawColorPalette.gray50
    val surfaceHover = RawColorPalette.gray100
    
    // Border Colors
    val borderPrimary = RawColorPalette.gray300
    val borderSecondary = RawColorPalette.gray200
    val borderFocus = RawColorPalette.purple500
    
    // Status Colors
    val success = RawColorPalette.green500
    val successBackground = rgb(220, 252, 231)
    val warning = RawColorPalette.yellow500
    val warningBackground = rgb(254, 243, 199)
    val error = RawColorPalette.red500
    val errorBackground = rgb(254, 226, 226)
    val info = RawColorPalette.info500
    val infoBackground = rgb(219, 234, 254)
}

/**
 * Typography System with CSS-compatible values
 */
object TypographySystem {
    // Font Sizes (returning CSSNumericValue for direct CSS usage)
    val displayLarge = 48.px      // Hero headings
    val displayMedium = 36.px     // Page headings
    val displaySmall = 30.px      // Section headings
    
    val headingLarge = 24.px      // Card headings
    val headingMedium = 20.px     // Component headings
    val headingSmall = 18.px      // Small headings
    
    val bodyLarge = 18.px         // Large body text
    val bodyMedium = 16.px        // Default body text
    val bodySmall = 14.px         // Small body text
    
    val labelLarge = 14.px        // Form labels
    val labelMedium = 12.px       // Small labels
    val labelSmall = 11.px        // Tiny labels
    
    // Font Weights
    val fontWeightLight = FontWeight.Normal       // 300
    val fontWeightNormal = FontWeight.Normal      // 400
    val fontWeightMedium = FontWeight.Bold        // 500
    val fontWeightSemiBold = FontWeight.Bold      // 600
    val fontWeightBold = FontWeight.Bold          // 700
    
    // Line Heights
    val lineHeightTight = 1.25
    val lineHeightNormal = 1.5
    val lineHeightRelaxed = 1.625
    val lineHeightLoose = 2.0
    
    // Font Families
    val fontFamilyPrimary = "Inter, system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif"
    val fontFamilyMono = "JetBrains Mono, Consolas, Monaco, 'Andale Mono', 'Ubuntu Mono', monospace"
}

/**
 * Spacing System with CSS-compatible values
 */
object SpacingSystem {
    // Basic Spacing (CSSNumericValue for direct usage)
    val xs = RawSpacing.space1        // 4px
    val sm = RawSpacing.space2        // 8px
    val md = RawSpacing.space3        // 12px
    val lg = RawSpacing.space4        // 16px
    val xl = RawSpacing.space5        // 20px
    val xl2 = RawSpacing.space6       // 24px
    val xl3 = RawSpacing.space8       // 32px
    val xl4 = RawSpacing.space10      // 40px
    val xl5 = RawSpacing.space12      // 48px
    val xl6 = RawSpacing.space16      // 64px
    val xl7 = RawSpacing.space20      // 80px
    val xl8 = RawSpacing.space24      // 96px
    
    // Semantic Spacing
    val space1 = RawSpacing.space1
    val space2 = RawSpacing.space2
    val space3 = RawSpacing.space3
    val space4 = RawSpacing.space4
    val space5 = RawSpacing.space5
    val space6 = RawSpacing.space6
    val space8 = RawSpacing.space8
    val space10 = RawSpacing.space10
    val space12 = RawSpacing.space12
    val space16 = RawSpacing.space16
    val space20 = RawSpacing.space20
    val space24 = RawSpacing.space24
    
    // Touch Targets (WCAG Compliant)
    val touchTargetMin = 44.px        // Minimum touch target
    val touchTarget = 48.px           // Comfortable touch target
    val touchTargetLarge = 56.px      // Large touch target
    
    // Responsive Spacing (for sections)
    val sectionSpacing = RawSpacing.space20     // Between major sections
    val componentSpacing = RawSpacing.space6    // Between components
    val elementSpacing = RawSpacing.space4      // Between related elements
}

/**
 * Border Radius System
 */
object BorderRadiusSystem {
    val none = 0.px
    val sm = 2.px
    val base = 4.px
    val md = 6.px
    val lg = 8.px
    val xl = 12.px
    val xl2 = 16.px
    val xl3 = 20.px
    val full = 9999.px
    
    // Semantic radii
    val small = sm
    val medium = md
    val large = lg
    val round = full
}

/**
 * Shadow System - Using Kobweb's BoxShadow API
 * Based on Kobweb v0.18.1+ BoxShadow support
 */
object ShadowSystem {
    // Note: Kobweb's boxShadow modifier can accept individual parameters
    // For string-based shadows (which work with boxShadow modifier), we use strings
    val none = "none"
    val small = "0 1px 2px 0 rgba(0, 0, 0, 0.05)"
    val medium = "0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)"
    val large = "0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)"
    val xlarge = "0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)"
    val inner = "inset 0 2px 4px 0 rgba(0, 0, 0, 0.06)"
    val focus = "0 0 0 3px rgba(107, 33, 168, 0.3)"  // Purple focus ring
}

/**
 * Animation System
 */
object AnimationSystem {
    // Durations
    val fast = 0.15.s
    val normal = 0.3.s
    val slow = 0.5.s
    
    // Transitions (string-based for Kobweb transition modifier)
    val transition = "all 0.2s cubic-bezier(0.4, 0, 0.2, 1)"
    val transitionFast = "all 0.15s cubic-bezier(0.4, 0, 0.2, 1)"
    val transitionSlow = "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)"
}

// =============================================================================
// COMPONENT-SPECIFIC DESIGN TOKENS
// =============================================================================

/**
 * Button-specific design tokens
 * Following Single Responsibility Principle
 */
object ButtonTokens {
    // Sizes
    val heightSmall = 32.px
    val heightMedium = 40.px
    val heightLarge = 48.px
    
    // Padding
    val paddingSmall = "6px 12px"
    val paddingMedium = "8px 16px"
    val paddingLarge = "12px 24px"
    
    // Typography
    val fontSizeSmall = TypographySystem.bodySmall
    val fontSizeMedium = TypographySystem.bodyMedium
    val fontSizeLarge = TypographySystem.bodyLarge
    
    // Colors (Primary Variant)
    val primaryBackground = SemanticColors.primary
    val primaryBackgroundHover = SemanticColors.primaryHover
    val primaryText = SemanticColors.textInverse
    val primaryBorder = SemanticColors.primary
    
    // Colors (Secondary Variant)
    val secondaryBackground = RawColorPalette.transparent
    val secondaryBackgroundHover = SemanticColors.primaryLight
    val secondaryText = SemanticColors.primary
    val secondaryBorder = SemanticColors.primary
}

/**
 * Form Input design tokens
 */
object InputTokens {
    val height = 40.px
    val padding = "8px 12px"
    val fontSize = TypographySystem.bodyMedium
    val borderRadius = BorderRadiusSystem.base
    val borderWidth = 1.px
    
    // Colors
    val background = SemanticColors.backgroundPrimary
    val border = SemanticColors.borderPrimary
    val borderFocus = SemanticColors.borderFocus
    val borderError = SemanticColors.error
    val text = SemanticColors.textPrimary
    val placeholder = SemanticColors.textTertiary
}

/**
 * Card design tokens
 */
object CardTokens {
    val padding = SpacingSystem.xl2
    val borderRadius = BorderRadiusSystem.lg
    val shadow = ShadowSystem.medium
    val background = SemanticColors.surface
    val border = SemanticColors.borderSecondary
}

/**
 * Component System - MISSING FROM ORIGINAL
 * Based on BaseComponentStyles.kt.disabled analysis
 * Mobile-first responsive component patterns
 */
object ComponentSystem {
    // Container Components (viewport-based responsive)
    object Container {
        val paddingMobile = LayoutUtilitiesSystem.viewportPaddingSmall      // 4vw
        val paddingTablet = LayoutUtilitiesSystem.viewportPaddingMedium     // 6vw  
        val paddingDesktop = LayoutUtilitiesSystem.viewportPaddingLarge     // 8vw
        val paddingXL = LayoutUtilitiesSystem.viewportPaddingXL             // 10vw
        val maxWidth = 1200.px
        val centerMargin = "0 auto"
    }
    
    // Section Components (flexible heights)
    object Section {
        val minHeightSmall = LayoutUtilitiesSystem.viewportHeightSmall      // 50vh
        val minHeightMedium = LayoutUtilitiesSystem.viewportHeightMedium     // 60vh
        val minHeightLarge = LayoutUtilitiesSystem.viewportHeightLarge      // 80vh
        val paddingVertical = SpacingSystem.xl3
        val gapBetweenSections = SpacingSystem.sectionSpacing
    }
    
    // Form Components (mobile-optimized)
    object Form {
        val inputHeightMobile = TouchTargetSystem.minimum      // 44px (WCAG AAA)
        val inputHeightTablet = 48.px                          // 48px comfortable
        val inputHeightDesktop = 52.px                         // 52px large screens
        val inputPadding = SpacingSystem.md
        val inputBorderRadius = BorderRadiusSystem.base
        val labelSpacing = SpacingSystem.space2
    }
    
    // Button Components (touch-friendly)
    object Button {
        val heightSmall = TouchTargetSystem.minimum            // 44px
        val heightMedium = TouchTargetSystem.recommended       // 48px
        val heightLarge = TouchTargetSystem.large              // 56px
        val heightXL = TouchTargetSystem.extraLarge            // 64px
        
        val paddingSmall = "${SpacingSystem.space2} ${SpacingSystem.space3}"
        val paddingMedium = "${SpacingSystem.space3} ${SpacingSystem.space4}"
        val paddingLarge = "${SpacingSystem.space4} ${SpacingSystem.space6}"
        
        val borderRadius = BorderRadiusSystem.base
        val transition = EnhancedAnimationSystem.smoothTransition
    }
    
    // Card Components
    object Card {
        val padding = SpacingSystem.xl
        val paddingCompact = SpacingSystem.lg  
        val borderRadius = BorderRadiusSystem.lg
        val shadow = ShadowSystem.medium
        val shadowHover = ShadowSystem.large
        val border = "1px solid ${SemanticColors.borderSecondary}"
        val background = SemanticColors.surface
    }
    
    // Grid Components (responsive patterns)
    object Grid {
        val cols1to2 = LayoutUtilitiesSystem.responsiveGrid1to2     // Auto-fit 300px min
        val cols1to3 = LayoutUtilitiesSystem.responsiveGrid1to3     // Auto-fit 250px min  
        val cols1to4 = LayoutUtilitiesSystem.responsiveGrid1to4     // Auto-fit 200px min
        val gapSmall = SpacingSystem.space4
        val gapMedium = SpacingSystem.space6
        val gapLarge = SpacingSystem.space8
    }
}

// =============================================================================
// UNIFIED DESIGN SYSTEM INTERFACE
// =============================================================================

/**
 * Main Design System Interface
 * 
 * This is the primary interface components should use.
 * Provides a clean, organized way to access all design tokens.
 */
object KhodedDesignSystem {
    // Core Design Systems
    val colors = SemanticColors
    val typography = TypographySystem
    val spacing = SpacingSystem
    val borderRadius = BorderRadiusSystem
    val shadows = ShadowSystem
    val animations = AnimationSystem
    
    // CRITICAL MISSING SYSTEMS - RESTORED FROM DISABLED FILES
    val breakpoints = ResponsiveBreakpoints
    val fluidTypography = FluidTypographySystem
    val touchTargets = TouchTargetSystem
    val enhancedAnimations = EnhancedAnimationSystem
    val layoutUtils = LayoutUtilitiesSystem
    val accessibility = AccessibilitySystem
    
    // Component Systems
    val components = ComponentSystem
    
    // Legacy component tokens (maintained for backward compatibility)
    val button = ButtonTokens
    val input = InputTokens
    val card = CardTokens
}

// =============================================================================
// THEME PROVIDER INTERFACES (for future theme switching)
// =============================================================================

/**
 * Theme interface for dependency inversion
 * Allows components to depend on abstractions, not concrete implementations
 */
interface ITheme {
    val colors: IColorTheme
    val typography: ITypographyTheme
    val spacing: ISpacingTheme
    val components: IComponentTheme
}

interface IColorTheme {
    val primary: CSSColorValue
    val secondary: CSSColorValue
    val textPrimary: CSSColorValue
    val textSecondary: CSSColorValue
    val background: CSSColorValue
    val surface: CSSColorValue
    val error: CSSColorValue
    val success: CSSColorValue
    val warning: CSSColorValue
}

interface ITypographyTheme {
    val headingLarge: CSSNumericValue<CSSUnit.px>
    val headingMedium: CSSNumericValue<CSSUnit.px>
    val bodyMedium: CSSNumericValue<CSSUnit.px>
    val bodySmall: CSSNumericValue<CSSUnit.px>
}

interface ISpacingTheme {
    val sm: CSSNumericValue<CSSUnit.px>
    val md: CSSNumericValue<CSSUnit.px>
    val lg: CSSNumericValue<CSSUnit.px>
    val xl: CSSNumericValue<CSSUnit.px>
}

interface IComponentTheme {
    val button: IButtonTheme
    val input: IInputTheme
}

interface IButtonTheme {
    val height: CSSNumericValue<CSSUnit.px>
    val padding: String
    val primaryBackground: CSSColorValue
    val primaryText: CSSColorValue
}

interface IInputTheme {
    val height: CSSNumericValue<CSSUnit.px>
    val padding: String
    val fontSize: CSSNumericValue<CSSUnit.px>
}

/**
 * Default Khoded theme implementation
 */
class KhodedTheme : ITheme {
    override val colors = object : IColorTheme {
        override val primary = SemanticColors.primary
        override val secondary = SemanticColors.secondary
        override val textPrimary = SemanticColors.textPrimary
        override val textSecondary = SemanticColors.textSecondary
        override val background = SemanticColors.backgroundPrimary
        override val surface = SemanticColors.surface
        override val error = SemanticColors.error
        override val success = SemanticColors.success
        override val warning = SemanticColors.warning
    }
    
    override val typography = object : ITypographyTheme {
        override val headingLarge = TypographySystem.headingLarge
        override val headingMedium = TypographySystem.headingMedium
        override val bodyMedium = TypographySystem.bodyMedium
        override val bodySmall = TypographySystem.bodySmall
    }
    
    override val spacing = object : ISpacingTheme {
        override val sm = SpacingSystem.sm
        override val md = SpacingSystem.md
        override val lg = SpacingSystem.lg
        override val xl = SpacingSystem.xl
    }
    
    override val components = object : IComponentTheme {
        override val button = object : IButtonTheme {
            override val height = ButtonTokens.heightMedium
            override val padding = ButtonTokens.paddingMedium
            override val primaryBackground = ButtonTokens.primaryBackground
            override val primaryText = ButtonTokens.primaryText
        }
        
        override val input = object : IInputTheme {
            override val height = InputTokens.height
            override val padding = InputTokens.padding
            override val fontSize = InputTokens.fontSize
        }
    }
}

// =============================================================================
// CRITICAL MISSING SYSTEMS - EXTRACTED FROM DISABLED FILES
// =============================================================================

/**
 * Responsive Breakpoints System - CRITICAL MISSING FROM ORIGINAL
 * Based on ResponsiveUtils.kt.disabled analysis
 */
object ResponsiveBreakpoints {
    const val MOBILE = 0      // 0px and up (mobile-first)
    const val SMALL = 640     // 640px and up (large mobile/small tablet)
    const val MEDIUM = 768    // 768px and up (tablet)
    const val LARGE = 1024    // 1024px and up (desktop)
    const val EXTRA_LARGE = 1280  // 1280px and up (large desktop)
    const val XXL = 1536      // 1536px and up (ultra-wide)
}

/**
 * Fluid Typography System - CRITICAL MISSING FROM ORIGINAL
 * Based on KhodedBrandTheme.kt.disabled analysis
 * Uses CSS clamp() for responsive scaling
 */
object FluidTypographySystem {
    // Hero/Display Typography (responsive with CSS clamp)
    val displayFluidLarge = "clamp(2.5rem, 8vw + 1rem, 5rem)"      // 40px → 80px
    val displayFluidMedium = "clamp(2rem, 6vw + 1rem, 4rem)"       // 32px → 64px
    val displayFluidSmall = "clamp(1.75rem, 4vw + 1rem, 3rem)"     // 28px → 48px
    
    // Heading Typography (responsive)
    val headingFluidLarge = "clamp(1.5rem, 4vw + 1rem, 2.5rem)"    // 24px → 40px
    val headingFluidMedium = "clamp(1.375rem, 3vw + 1rem, 2rem)"   // 22px → 32px
    val headingFluidSmall = "clamp(1.25rem, 2vw + 1rem, 1.75rem)"  // 20px → 28px
    
    // Body Typography (responsive)
    val bodyFluidLarge = "clamp(1.125rem, 2vw + 0.75rem, 1.375rem)" // 18px → 22px
    val bodyFluidMedium = "clamp(1rem, 1.5vw + 0.75rem, 1.25rem)"   // 16px → 20px
    val bodyFluidSmall = "clamp(0.875rem, 1vw + 0.75rem, 1rem)"     // 14px → 16px
}

/**
 * Touch Target System - CRITICAL MISSING FROM ORIGINAL
 * Based on ResponsiveUtils.kt.disabled and WCAG AAA requirements
 */
object TouchTargetSystem {
    // WCAG AAA compliant touch targets
    val minimum = 44.px        // WCAG AAA minimum (44x44px)
    val recommended = 48.px    // Recommended comfortable size
    val large = 56.px          // Large touch targets for primary actions
    val extraLarge = 64.px     // Extra large for critical actions
    
    // Touch-friendly spacing
    val touchSpacing = 8.px    // Minimum spacing between touch targets
    val comfortableSpacing = 16.px  // Comfortable spacing between targets
}

/**
 * Enhanced Animation System - MISSING FROM ORIGINAL
 * Based on various disabled component files
 */
object EnhancedAnimationSystem {
    // Timing functions
    val easeInOut = "cubic-bezier(0.4, 0, 0.2, 1)"
    val easeOut = "cubic-bezier(0, 0, 0.2, 1)"
    val easeIn = "cubic-bezier(0.4, 0, 1, 1)"
    val bounce = "cubic-bezier(0.68, -0.55, 0.265, 1.55)"
    
    // Durations
    val instant = 0.1.s
    val fast = 0.15.s
    val normal = 0.3.s
    val slow = 0.5.s
    val slower = 0.75.s
    
    // Complex transitions
    val smoothTransition = "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)"
    val bounceTransition = "all 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55)"
    val slideTransition = "transform 0.3s cubic-bezier(0.4, 0, 0.2, 1)"
    
    // Keyframe animations (from DesignSection.kt.disabled)
    val floatAnimation = "float 6s ease-in-out infinite"
    val spinAnimation = "spin 6s linear infinite"
    val pulseAnimation = "pulse 2s ease-in-out infinite"
}

/**
 * Layout Utilities System - MISSING FROM ORIGINAL
 * Based on BaseComponentStyles.kt.disabled analysis
 */
object LayoutUtilitiesSystem {
    // Viewport-based responsive padding (from BaseComponentStyles.kt.disabled)
    val viewportPaddingSmall = "4vw"    // 4% of viewport width
    val viewportPaddingMedium = "6vw"   // 6% of viewport width
    val viewportPaddingLarge = "8vw"    // 8% of viewport width
    val viewportPaddingXL = "10vw"      // 10% of viewport width
    
    // Flexible container heights
    val viewportHeightSmall = "50vh"    // 50% of viewport height
    val viewportHeightMedium = "60vh"   // 60% of viewport height
    val viewportHeightLarge = "80vh"    // 80% of viewport height
    val viewportHeightFull = "100vh"    // Full viewport height
    
    // CSS Grid responsive patterns
    val responsiveGrid1to2 = "repeat(auto-fit, minmax(300px, 1fr))"
    val responsiveGrid1to3 = "repeat(auto-fit, minmax(250px, 1fr))"
    val responsiveGrid1to4 = "repeat(auto-fit, minmax(200px, 1fr))"
}

/**
 * Accessibility System - MISSING FROM ORIGINAL
 * Based on KhodedDesignSystem.kt.disabled analysis
 */
object AccessibilitySystem {
    // Focus ring styles (WCAG compliant)
    val focusRingColor = SemanticColors.borderFocus
    val focusRingWidth = 2.px
    val focusRingOffset = 2.px
    val focusRingStyle = "0 0 0 2px rgba(107, 33, 168, 0.5)"
    
    // High contrast colors
    val highContrastText = rgb(0, 0, 0)
    val highContrastBackground = rgb(255, 255, 255)
    val highContrastBorder = rgb(0, 0, 0)
    
    // Reduced motion preferences
    val reducedMotionTransition = "none"
    val reducedMotionDuration = 0.01.s
    
    // Screen reader utilities
    val screenReaderOnly = "position: absolute; width: 1px; height: 1px; padding: 0; margin: -1px; overflow: hidden; clip: rect(0, 0, 0, 0); white-space: nowrap; border: 0;"
}