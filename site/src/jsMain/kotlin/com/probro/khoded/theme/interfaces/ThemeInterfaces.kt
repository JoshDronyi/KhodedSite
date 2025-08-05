package com.probro.khoded.theme.interfaces

import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.CSSColorValue

/**
 * Theme Interfaces - Implements Interface Segregation Principle (ISP)
 * 
 * Instead of one large theme interface, we create small, focused interfaces
 * that clients can implement only what they need. This prevents forced
 * implementation of unused methods and reduces coupling.
 * 
 * Benefits of ISP implementation:
 * - Components only depend on the theme aspects they actually use
 * - Easy to implement partial theme support
 * - Clear separation of concerns for different theme aspects
 * - Reduces the impact of theme changes on components
 * 
 * @since 2.0.0 (SOLID principles refactor)
 */

/**
 * Color theming interface - only for components that need colors
 */
interface IColorTheme {
    val primary: CSSColorValue
    val secondary: CSSColorValue
    val background: CSSColorValue
    val surface: CSSColorValue
    val error: CSSColorValue
    val warning: CSSColorValue
    val success: CSSColorValue
    val textPrimary: CSSColorValue
    val textSecondary: CSSColorValue
    val textMuted: CSSColorValue
}

/**
 * Typography theming interface - only for components that use text
 */
interface ITypographyTheme {
    val fontFamilyPrimary: String
    val fontFamilySecondary: String
    val fontSizeXSmall: String
    val fontSizeSmall: String
    val fontSizeMedium: String
    val fontSizeLarge: String
    val fontSizeXLarge: String
    val fontWeightNormal: Int
    val fontWeightMedium: Int
    val fontWeightBold: Int
    val lineHeightTight: Double
    val lineHeightNormal: Double
    val lineHeightLoose: Double
}

/**
 * Spacing theming interface - only for components that need spacing
 */
interface ISpacingTheme {
    val spaceXSmall: String
    val spaceSmall: String
    val spaceMedium: String
    val spaceLarge: String
    val spaceXLarge: String
    val spaceXXLarge: String
}

/**
 * Border and radius theming interface - for components with borders
 */
interface IBorderTheme {
    val borderWidthThin: String
    val borderWidthMedium: String
    val borderWidthThick: String
    val borderRadiusSmall: String
    val borderRadiusMedium: String
    val borderRadiusLarge: String
    val borderRadiusFull: String
}

/**
 * Shadow theming interface - only for components that use shadows
 */
interface IShadowTheme {
    val shadowSmall: String
    val shadowMedium: String
    val shadowLarge: String
    val shadowXLarge: String
}

/**
 * Animation theming interface - for components with animations
 */
interface IAnimationTheme {
    val transitionFast: String
    val transitionMedium: String
    val transitionSlow: String
    val easingStandard: String
    val easingAccelerate: String
    val easingDecelerate: String
}

/**
 * Z-index theming interface - for components that need layering
 */
interface IZIndexTheme {
    val zIndexDropdown: Int
    val zIndexModal: Int
    val zIndexPopover: Int
    val zIndexTooltip: Int
    val zIndexNotification: Int
}

/**
 * Breakpoint theming interface - for responsive components
 */
interface IBreakpointTheme {
    val breakpointXSmall: String
    val breakpointSmall: String
    val breakpointMedium: String
    val breakpointLarge: String
    val breakpointXLarge: String
}

/**
 * Form-specific theming interface - only for form components
 */
interface IFormTheme {
    val inputHeight: String
    val inputPadding: String
    val inputBorderColor: CSSColorValue
    val inputBorderColorFocus: CSSColorValue
    val inputBorderColorError: CSSColorValue
    val inputBackgroundColor: CSSColorValue
    val inputTextColor: CSSColorValue
    val inputPlaceholderColor: CSSColorValue
    val labelColor: CSSColorValue
    val errorTextColor: CSSColorValue
    val helpTextColor: CSSColorValue
}

/**
 * Button-specific theming interface - only for button components
 */
interface IButtonTheme {
    val buttonHeightSmall: String
    val buttonHeightMedium: String
    val buttonHeightLarge: String
    val buttonPaddingSmall: String
    val buttonPaddingMedium: String
    val buttonPaddingLarge: String
    val buttonBorderRadius: String
}

/**
 * Navigation-specific theming interface - only for navigation components
 */
interface INavigationTheme {
    val navBarHeight: String
    val navBarBackground: CSSColorValue
    val navBarBorder: CSSColorValue
    val navLinkColor: CSSColorValue
    val navLinkColorActive: CSSColorValue
    val navLinkColorHover: CSSColorValue
    val mobileMenuBackground: CSSColorValue
    val mobileMenuShadow: String
}

/**
 * Composite interfaces for components that need multiple theme aspects
 * These combine smaller interfaces following ISP principles
 */

/**
 * Complete theme interface - only implement if you truly need ALL aspects
 */
interface ICompleteTheme : 
    IColorTheme, 
    ITypographyTheme, 
    ISpacingTheme, 
    IBorderTheme, 
    IShadowTheme, 
    IAnimationTheme, 
    IZIndexTheme, 
    IBreakpointTheme,
    IFormTheme,
    IButtonTheme,
    INavigationTheme

/**
 * Basic theme interface - for simple components needing colors and spacing
 */
interface IBasicTheme : IColorTheme, ISpacingTheme

/**
 * Interactive theme interface - for interactive components needing colors, spacing, and borders
 */
interface IInteractiveTheme : IColorTheme, ISpacingTheme, IBorderTheme, IAnimationTheme

/**
 * Text theme interface - for text-heavy components
 */
interface ITextTheme : IColorTheme, ITypographyTheme, ISpacingTheme

/**
 * Example of ISP benefit: A simple badge component only needs colors
 */
interface IBadgeTheme : IColorTheme {
    // Badge component only depends on colors, not typography, spacing, etc.
    // This makes it lightweight and focused
}

/**
 * Example: A complex data table needs many aspects but can be selective
 */
interface IDataTableTheme : IColorTheme, ITypographyTheme, ISpacingTheme, IBorderTheme, IShadowTheme {
    // Data table component specifies exactly what it needs
    // It doesn't need animation or z-index themes
}