package com.probro.khoded.models

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.css.rgba

/**
 * Resource constants and styling defaults for the application.
 *
 * This object serves as a centralized location for application-wide
 * resource constants and default styling values. It provides a
 * fallback system for when specific theme values are not available.
 *
 * @since 1.0.0
 * @see com.probro.khoded.models.FONTS for primary font declarations
 * @see com.probro.khoded.models.KhodedColors for color system
 */
object Res {
    
    /**
     * Default text styling configuration.
     *
     * Contains fallback font families and text styling properties
     * used when theme-specific styles are not available or as
     * system defaults for basic text rendering.
     */
    object TextStyle {
        /**
         * Default fallback font family.
         *
         * Arial is used as a system fallback font to ensure text
         * remains readable even when custom fonts fail to load.
         * In production, this should be extended to include a
         * complete font stack with web-safe alternatives.
         *
         * Example of an improved font stack:
         * ```
         * val FONT_FAMILY = "Space Grotesk, Arial, Helvetica, sans-serif"
         * ```
         */
        val FONT_FAMILY = "Arial"
    }
}




