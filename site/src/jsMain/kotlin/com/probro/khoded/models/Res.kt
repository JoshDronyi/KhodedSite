package com.probro.khoded.models

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.css.rgba

object Res {
    object TextStyle {
        val FONT_FAMILY = "Arial"
    }

    enum class BrandColors(val hex: String, val rgb: CSSColorValue) {
        KhodedBlue(hex = "#4788f9", rgb = rgba(71, 136, 249, a = 1)),
        KhodedSilver(hex = "#f8f8f8", rgb = rgba(248, 248, 248, a = 1)),
        Saphire(hex = "#075cf2", rgb = rgba(7, 92, 242, a = 1)),
        ErrorRed(hex = "#FF5349", rgb = rgba(255, 83, 73, a = 1)),
        LightGoldenYellow(hex = "Yellow", rgb = Color.lightgoldenrodyellow),
        DarkGoldenYellow(hex = "Yellow", rgb = Color.darkgoldenrod),
        KhodedLightGray(hex = "Gray", rgb = Color.lightgray),
        KhodedBlack(hex = "#FFF", rgb = Color.black),
        KhodedWhite(hex = "#000", rgb = Color.white),
        KhodedPink(hex = "#a82bfe", rgb = rgb(168, 43, 254))
    }


    object Themes {
        val baseTheme = BaseTheme()

        data class BaseTheme(
            val primaryColor: BrandColors = BrandColors.Saphire,
            val onPrimaryColor: BrandColors = BrandColors.KhodedSilver,
            val secondaryColor: BrandColors = BrandColors.LightGoldenYellow,
            val onSecondaryColor: BrandColors = BrandColors.DarkGoldenYellow,
            val backgroundColor: BrandColors = BrandColors.KhodedBlue,
            val onBackgroundColor: BrandColors = BrandColors.KhodedLightGray,
            val surfaceColor: BrandColors = BrandColors.KhodedWhite,
            val onSurfaceColor: BrandColors = BrandColors.KhodedBlack,
            val errorColor: BrandColors = BrandColors.ErrorRed
        )
    }
}




