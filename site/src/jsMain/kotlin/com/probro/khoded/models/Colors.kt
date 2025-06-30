package com.probro.khoded.models

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.rgba


enum class KhodedColors(val hex: String, val rgb: CSSColorValue) {
    WHITE(hex = "#FFFFFF", rgb = rgba(255, 255, 255, 1)),
    BLACK(hex = "#000000", rgb = rgba(0, 0, 0, 1)),
    TAUPE(hex = "#e6e6e6", rgb = rgba(230, 230, 230, 1)),
    BLUE(hex = "#44b6c6", rgb = rgba(68, 182, 198, 1)),
    HOVER_BLUE(hex = "#44b6c6", rgb = rgba(68, 182, 198, .8)),
    POWDER_BLUE(hex = "#92d4de", rgb = rgba(146, 212, 222, 1)),
    LIGHT_BLUE(hex = "#6bc5d2", rgb = rgba(107, 197, 210, 1)),
    PURPLE(hex = "#390050", rgb = rgba(57, 0, 80, 1)),
    HOVER_PURPLE(hex = "#390050", rgb = rgba(57, 0, 80,.8 ))
}

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