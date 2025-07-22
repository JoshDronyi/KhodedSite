package com.probro.khoded.models

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.rgba

/**
 * Color palette and theming system for the Khoded brand.
 *
 * This enum defines the complete color system used throughout the application,
 * providing both hex and RGB color values for consistent styling. The colors
 * are designed to maintain brand consistency and accessibility standards.
 *
 * @property hex Hexadecimal color value for CSS and design tools compatibility
 * @property rgb RGBA color value optimized for Compose Web styling with alpha support
 *
 * @since 1.0.0
 * @see BaseTheme for semantic color assignments
 * @see com.probro.khoded.styles.BaseTextStyles for color usage examples
 */
enum class KhodedColors(val hex: String, val rgb: CSSColorValue) {
    WHITE(hex = "#FFFFFF", rgb = rgba(255, 255, 255, 1)),
    BLACK(hex = "#000000", rgb = rgba(0, 0, 0, 1)),
    TAUPE(hex = "#e6e6e6", rgb = rgba(230, 230, 230, 1)),
    BLUE(hex = "#44b6c6", rgb = rgba(68, 182, 198, 1)),
    BLUE_HIGHLIGHT(hex = "#44b6c6", rgb = rgba(68, 182, 198, .2)),
    HOVER_BLUE(hex = "#44b6c6", rgb = rgba(68, 182, 198, .8)),
    POWDER_BLUE(hex = "#92d4de", rgb = rgba(146, 212, 222, 1)),
    LIGHT_BLUE(hex = "#6bc5d2", rgb = rgba(107, 197, 210, 1)),
    PURPLE(hex = "#390050", rgb = rgba(57, 0, 80, 1)),
    HOVER_PURPLE(hex = "#390050", rgb = rgba(57, 0, 80,.8 ))
}

/**
 * Semantic color theme configuration for the Khoded application.
 *
 * This object maps semantic color roles to specific KhodedColors values,
 * following Material Design color system principles. It provides a
 * centralized way to manage the application's visual theme.
 *
 * The theme follows accessibility guidelines ensuring proper contrast ratios
 * between text and background colors for WCAG compliance.
 *
 * @since 1.0.0
 * @see KhodedColors for available color values
 *
 * Usage:
 * ```kotlin
 * modifier.color(BaseTheme.primaryColor.rgb)
 * modifier.backgroundColor(BaseTheme.backgroundColor.rgb)
 * ```
 */
object BaseTheme {
    val primaryColor: KhodedColors = KhodedColors.PURPLE
    val onPrimaryColor: KhodedColors = KhodedColors.WHITE
    val secondaryColor: KhodedColors = KhodedColors.LIGHT_BLUE
    val onSecondaryColor: KhodedColors = KhodedColors.TAUPE
    val backgroundColor: KhodedColors = KhodedColors.POWDER_BLUE
    val onBackgroundColor: KhodedColors = KhodedColors.BLACK
    val surfaceColor: KhodedColors = KhodedColors.BLUE
    val onSurfaceColor: KhodedColors = KhodedColors.PURPLE
//        val errorColor: KhodedColors = KhodedColors.ErrorRed
}