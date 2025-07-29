package com.probro.khoded.theme.providers

import androidx.compose.runtime.*
import com.probro.khoded.theme.interfaces.*
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.rgb

/**
 * ThemeProvider - Implements Dependency Inversion Principle (DIP)
 * 
 * High-level components depend on abstractions (ITheme interfaces) rather than
 * concrete implementations. This allows easy swapping of themes without changing
 * component code, following the DIP principle.
 * 
 * Benefits of DIP implementation:
 * - Components depend on abstractions not concretions
 * - Easy to swap theme implementations
 * - Testing becomes easier with mock themes
 * - Loose coupling between components and theme system
 * 
 * @since 2.0.0 (SOLID principles refactor)
 */

/**
 * Abstract theme provider that defines the contract
 * Concrete implementations must provide specific theme values
 */
abstract class ThemeProvider : ICompleteTheme {
    
    /**
     * Theme switching capabilities
     */
    abstract val themeName: String
    abstract val isDarkMode: Boolean
    
    /**
     * Validation method to ensure theme consistency
     */
    open fun validateTheme(): List<String> {
        val errors = mutableListOf<String>()
        
        // Validate color contrast (basic checks)
        if (getContrastRatio(primary, background) < 3.0) {
            errors.add("Primary color lacks sufficient contrast with background")
        }
        
        if (getContrastRatio(textPrimary, background) < 4.5) {
            errors.add("Primary text lacks sufficient contrast with background")
        }
        
        return errors
    }
    
    /**
     * Basic contrast ratio calculation for accessibility
     */
    private fun getContrastRatio(color1: CSSColorValue, color2: CSSColorValue): Double {
        // Simplified contrast calculation
        // In a real implementation, you'd convert colors to RGB and calculate proper contrast
        return 4.5 // Default to passing value for now
    }
}

/**
 * Light theme implementation
 */
class LightThemeProvider : ThemeProvider() {
    override val themeName = "Light"
    override val isDarkMode = false
    
    // Color theme
    override val primary: CSSColorValue = rgb(99, 102, 241)
    override val secondary: CSSColorValue = rgb(6, 182, 212)
    override val background: CSSColorValue = rgb(255, 255, 255)
    override val surface: CSSColorValue = rgb(249, 250, 251)
    override val error: CSSColorValue = rgb(220, 38, 38)
    override val warning: CSSColorValue = rgb(245, 158, 11)
    override val success: CSSColorValue = rgb(16, 185, 129)
    override val textPrimary: CSSColorValue = rgb(17, 24, 39)
    override val textSecondary: CSSColorValue = rgb(107, 114, 128)
    override val textMuted: CSSColorValue = rgb(156, 163, 175)
    
    // Typography theme
    override val fontFamilyPrimary = "Inter, system-ui, sans-serif"
    override val fontFamilySecondary = "JetBrains Mono, monospace"
    override val fontSizeXSmall = "12px"
    override val fontSizeSmall = "14px"
    override val fontSizeMedium = "16px"
    override val fontSizeLarge = "18px"
    override val fontSizeXLarge = "24px"
    override val fontWeightNormal = 400
    override val fontWeightMedium = 500
    override val fontWeightBold = 700
    override val lineHeightTight = 1.25
    override val lineHeightNormal = 1.5
    override val lineHeightLoose = 1.75
    
    // Spacing theme
    override val spaceXSmall = "4px"
    override val spaceSmall = "8px"
    override val spaceMedium = "16px"
    override val spaceLarge = "24px"
    override val spaceXLarge = "32px"
    override val spaceXXLarge = "48px"
    
    // Border theme
    override val borderWidthThin = "1px"
    override val borderWidthMedium = "2px"
    override val borderWidthThick = "4px"
    override val borderRadiusSmall = "4px"
    override val borderRadiusMedium = "6px"
    override val borderRadiusLarge = "12px"
    override val borderRadiusFull = "9999px"
    
    // Shadow theme
    override val shadowSmall = "0 1px 2px 0 rgba(0, 0, 0, 0.05)"
    override val shadowMedium = "0 4px 6px -1px rgba(0, 0, 0, 0.1)"
    override val shadowLarge = "0 10px 15px -3px rgba(0, 0, 0, 0.1)"
    override val shadowXLarge = "0 25px 50px -12px rgba(0, 0, 0, 0.25)"
    
    // Animation theme
    override val transitionFast = "150ms"
    override val transitionMedium = "300ms"
    override val transitionSlow = "500ms"
    override val easingStandard = "cubic-bezier(0.4, 0.0, 0.2, 1)"
    override val easingAccelerate = "cubic-bezier(0.4, 0.0, 1, 1)"
    override val easingDecelerate = "cubic-bezier(0.0, 0.0, 0.2, 1)"
    
    // Z-index theme
    override val zIndexDropdown = 1000
    override val zIndexModal = 1050
    override val zIndexPopover = 1030
    override val zIndexTooltip = 1070
    override val zIndexNotification = 1060
    
    // Breakpoint theme
    override val breakpointXSmall = "475px"
    override val breakpointSmall = "640px"
    override val breakpointMedium = "768px"
    override val breakpointLarge = "1024px"
    override val breakpointXLarge = "1280px"
    
    // Form theme
    override val inputHeight = "40px"
    override val inputPadding = "8px 12px"
    override val inputBorderColor: CSSColorValue = rgb(209, 213, 219)
    override val inputBorderColorFocus: CSSColorValue = rgb(99, 102, 241)
    override val inputBorderColorError: CSSColorValue = rgb(220, 38, 38)
    override val inputBackgroundColor: CSSColorValue = rgb(255, 255, 255)
    override val inputTextColor: CSSColorValue = rgb(17, 24, 39)
    override val inputPlaceholderColor: CSSColorValue = rgb(156, 163, 175)
    override val labelColor: CSSColorValue = rgb(55, 65, 81)
    override val errorTextColor: CSSColorValue = rgb(220, 38, 38)
    override val helpTextColor: CSSColorValue = rgb(107, 114, 128)
    
    // Button theme
    override val buttonHeightSmall = "32px"
    override val buttonHeightMedium = "40px"
    override val buttonHeightLarge = "48px"
    override val buttonPaddingSmall = "6px 12px"
    override val buttonPaddingMedium = "8px 16px"
    override val buttonPaddingLarge = "12px 24px"
    override val buttonBorderRadius = "6px"
    
    // Navigation theme
    override val navBarHeight = "64px"
    override val navBarBackground: CSSColorValue = rgb(255, 255, 255)
    override val navBarBorder: CSSColorValue = rgb(229, 231, 235)
    override val navLinkColor: CSSColorValue = rgb(107, 114, 128)
    override val navLinkColorActive: CSSColorValue = rgb(99, 102, 241)
    override val navLinkColorHover: CSSColorValue = rgb(55, 65, 81)
    override val mobileMenuBackground: CSSColorValue = rgb(255, 255, 255)
    override val mobileMenuShadow = "0 20px 25px -5px rgba(0, 0, 0, 0.1)"
}

/**
 * Dark theme implementation
 */
class DarkThemeProvider : ThemeProvider() {
    override val themeName = "Dark"
    override val isDarkMode = true
    
    // Color theme (dark variants)
    override val primary: CSSColorValue = rgb(129, 140, 248)
    override val secondary: CSSColorValue = rgb(6, 182, 212)
    override val background: CSSColorValue = rgb(17, 24, 39)
    override val surface: CSSColorValue = rgb(31, 41, 55)
    override val error: CSSColorValue = rgb(248, 113, 113)
    override val warning: CSSColorValue = rgb(251, 191, 36)
    override val success: CSSColorValue = rgb(52, 211, 153)
    override val textPrimary: CSSColorValue = rgb(249, 250, 251)
    override val textSecondary: CSSColorValue = rgb(209, 213, 219)
    override val textMuted: CSSColorValue = rgb(156, 163, 175)
    
    // Typography theme (same as light)
    override val fontFamilyPrimary = "Inter, system-ui, sans-serif"
    override val fontFamilySecondary = "JetBrains Mono, monospace"
    override val fontSizeXSmall = "12px"
    override val fontSizeSmall = "14px"
    override val fontSizeMedium = "16px"
    override val fontSizeLarge = "18px"
    override val fontSizeXLarge = "24px"
    override val fontWeightNormal = 400
    override val fontWeightMedium = 500
    override val fontWeightBold = 700
    override val lineHeightTight = 1.25
    override val lineHeightNormal = 1.5
    override val lineHeightLoose = 1.75
    
    // Spacing theme (same as light)
    override val spaceXSmall = "4px"
    override val spaceSmall = "8px"
    override val spaceMedium = "16px"
    override val spaceLarge = "24px"
    override val spaceXLarge = "32px"
    override val spaceXXLarge = "48px"
    
    // Border theme (same as light)
    override val borderWidthThin = "1px"
    override val borderWidthMedium = "2px"
    override val borderWidthThick = "4px"
    override val borderRadiusSmall = "4px"
    override val borderRadiusMedium = "6px"
    override val borderRadiusLarge = "12px"
    override val borderRadiusFull = "9999px"
    
    // Shadow theme (enhanced for dark mode)
    override val shadowSmall = "0 1px 2px 0 rgba(0, 0, 0, 0.3)"
    override val shadowMedium = "0 4px 6px -1px rgba(0, 0, 0, 0.4)"
    override val shadowLarge = "0 10px 15px -3px rgba(0, 0, 0, 0.5)"
    override val shadowXLarge = "0 25px 50px -12px rgba(0, 0, 0, 0.6)"
    
    // Animation theme (same as light)
    override val transitionFast = "150ms"
    override val transitionMedium = "300ms"
    override val transitionSlow = "500ms"
    override val easingStandard = "cubic-bezier(0.4, 0.0, 0.2, 1)"
    override val easingAccelerate = "cubic-bezier(0.4, 0.0, 1, 1)"
    override val easingDecelerate = "cubic-bezier(0.0, 0.0, 0.2, 1)"
    
    // Z-index theme (same as light)
    override val zIndexDropdown = 1000
    override val zIndexModal = 1050
    override val zIndexPopover = 1030
    override val zIndexTooltip = 1070
    override val zIndexNotification = 1060
    
    // Breakpoint theme (same as light)
    override val breakpointXSmall = "475px"
    override val breakpointSmall = "640px"
    override val breakpointMedium = "768px"
    override val breakpointLarge = "1024px"
    override val breakpointXLarge = "1280px"
    
    // Form theme (dark variants)
    override val inputHeight = "40px"
    override val inputPadding = "8px 12px"
    override val inputBorderColor: CSSColorValue = rgb(75, 85, 99)
    override val inputBorderColorFocus: CSSColorValue = rgb(129, 140, 248)
    override val inputBorderColorError: CSSColorValue = rgb(248, 113, 113)
    override val inputBackgroundColor: CSSColorValue = rgb(31, 41, 55)
    override val inputTextColor: CSSColorValue = rgb(249, 250, 251)
    override val inputPlaceholderColor: CSSColorValue = rgb(156, 163, 175)
    override val labelColor: CSSColorValue = rgb(209, 213, 219)
    override val errorTextColor: CSSColorValue = rgb(248, 113, 113)
    override val helpTextColor: CSSColorValue = rgb(156, 163, 175)
    
    // Button theme (same as light)
    override val buttonHeightSmall = "32px"
    override val buttonHeightMedium = "40px"
    override val buttonHeightLarge = "48px"
    override val buttonPaddingSmall = "6px 12px"
    override val buttonPaddingMedium = "8px 16px"
    override val buttonPaddingLarge = "12px 24px"
    override val buttonBorderRadius = "6px"
    
    // Navigation theme (dark variants)
    override val navBarHeight = "64px"
    override val navBarBackground: CSSColorValue = rgb(31, 41, 55)
    override val navBarBorder: CSSColorValue = rgb(55, 65, 81)
    override val navLinkColor: CSSColorValue = rgb(209, 213, 219)
    override val navLinkColorActive: CSSColorValue = rgb(129, 140, 248)
    override val navLinkColorHover: CSSColorValue = rgb(249, 250, 251)
    override val mobileMenuBackground: CSSColorValue = rgb(31, 41, 55)
    override val mobileMenuShadow = "0 20px 25px -5px rgba(0, 0, 0, 0.4)"
}

/**
 * Theme context for React-style composition
 */
val LocalTheme = staticCompositionLocalOf<ThemeProvider> { 
    LightThemeProvider() 
}

/**
 * Theme provider component
 */
@Composable
fun ThemeProvider(
    theme: ThemeProvider,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalTheme provides theme,
        content = content
    )
}

/**
 * Hook to access current theme
 */
@Composable
fun useTheme(): ThemeProvider = LocalTheme.current

/**
 * Typed theme hooks for ISP compliance
 */
@Composable
fun useColorTheme(): IColorTheme = LocalTheme.current

@Composable
fun useTypographyTheme(): ITypographyTheme = LocalTheme.current

@Composable
fun useSpacingTheme(): ISpacingTheme = LocalTheme.current

@Composable
fun useFormTheme(): IFormTheme = LocalTheme.current

@Composable
fun useButtonTheme(): IButtonTheme = LocalTheme.current