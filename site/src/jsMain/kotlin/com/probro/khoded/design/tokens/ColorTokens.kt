package com.probro.khoded.design.tokens

import org.jetbrains.compose.web.css.*

/**
 * Color Design Tokens
 * 
 * Centralized color definitions following the Khoded brand guidelines.
 * Provides semantic naming for better maintainability.
 */

object ColorTokens {
    
    // =============================================================================
    // RAW COLOR PALETTE - Internal Use Only
    // =============================================================================
    
    private object RawColors {
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
        
        // Blue Palette (Secondary Brand)
        val blue50 = rgb(239, 246, 255)
        val blue100 = rgb(219, 234, 254)
        val blue200 = rgb(191, 219, 254)
        val blue300 = rgb(147, 197, 253)
        val blue400 = rgb(96, 165, 250)
        val blue500 = rgb(59, 130, 246)     // SECONDARY BRAND COLOR
        val blue600 = rgb(37, 99, 235)
        val blue700 = rgb(29, 78, 216)
        val blue800 = rgb(30, 64, 175)
        val blue900 = rgb(30, 58, 138)
        
        // Neutral Palette
        val neutral50 = rgb(250, 250, 250)
        val neutral100 = rgb(245, 245, 245)
        val neutral200 = rgb(229, 229, 229)
        val neutral300 = rgb(212, 212, 212)
        val neutral400 = rgb(163, 163, 163)
        val neutral500 = rgb(115, 115, 115)
        val neutral600 = rgb(82, 82, 82)
        val neutral700 = rgb(64, 64, 64)
        val neutral800 = rgb(38, 38, 38)
        val neutral900 = rgb(23, 23, 23)
        
        // Accent Colors
        val green500 = rgb(34, 197, 94)
        val red500 = rgb(239, 68, 68)
        val yellow500 = rgb(234, 179, 8)
        val orange500 = rgb(249, 115, 22)
        
        // Special Colors
        val white = rgb(255, 255, 255)
        val black = rgb(0, 0, 0)
    }
    
    // =============================================================================
    // SEMANTIC COLOR SYSTEM - Use These In Components
    // =============================================================================
    
    object Brand {
        val primary = RawColors.purple500
        val primaryLight = RawColors.purple400
        val primaryDark = RawColors.purple600
        val primarySubtle = RawColors.purple100
        
        val secondary = RawColors.blue500
        val secondaryLight = RawColors.blue400
        val secondaryDark = RawColors.blue600
        val secondarySubtle = RawColors.blue100
    }
    
    object Text {
        val primary = RawColors.neutral900
        val secondary = RawColors.neutral700
        val tertiary = RawColors.neutral500
        val inverse = RawColors.white
        val disabled = RawColors.neutral400
        val link = Brand.primary
        val linkHover = Brand.primaryDark
    }
    
    object Background {
        val primary = RawColors.white
        val secondary = RawColors.neutral50
        val tertiary = RawColors.neutral100
        val inverse = RawColors.neutral900
        val disabled = RawColors.neutral200
    }
    
    object Border {
        val default = RawColors.neutral200
        val strong = RawColors.neutral300
        val subtle = RawColors.neutral100
        val focus = Brand.primary
        val error = RawColors.red500
        val success = RawColors.green500
    }
    
    object Status {
        val success = RawColors.green500
        val successBackground = rgb(240, 253, 244)
        val warning = RawColors.yellow500
        val warningBackground = rgb(254, 252, 232)
        val error = RawColors.red500
        val errorBackground = rgb(254, 242, 242)
        val info = RawColors.blue500
        val infoBackground = rgb(239, 246, 255)
    }
    
    object Interactive {
        val primary = Brand.primary
        val primaryHover = Brand.primaryDark
        val primaryActive = RawColors.purple700
        val primaryDisabled = RawColors.neutral300
        
        val secondary = RawColors.neutral100
        val secondaryHover = RawColors.neutral200
        val secondaryActive = RawColors.neutral300
        
        val ghost = Color.transparent
        val ghostHover = RawColors.neutral100
        val ghostActive = RawColors.neutral200
    }
    
    object Surface {
        val raised = RawColors.white
        val overlay = rgba(0, 0, 0, 0.5)
        val overlayLight = rgba(255, 255, 255, 0.9)
        val card = RawColors.white
        val cardHover = RawColors.neutral50
    }
    
    // =============================================================================
    // GRADIENT SYSTEM
    // =============================================================================
    
    object Gradients {
        val brandPrimary = "linear-gradient(135deg, ${RawColors.purple500} 0%, ${RawColors.blue500} 100%)"
        val brandSubtle = "linear-gradient(135deg, ${RawColors.purple100} 0%, ${RawColors.blue100} 100%)"
        val heroGradient = "linear-gradient(135deg, ${RawColors.purple600} 0%, ${RawColors.blue600} 100%)"
        val cardGradient = "linear-gradient(145deg, ${RawColors.white} 0%, ${RawColors.neutral50} 100%)"
    }
    
    // =============================================================================
    // DARK MODE SUPPORT (Future Implementation)
    // =============================================================================
    
    object Dark {
        object Text {
            val primary = RawColors.neutral100
            val secondary = RawColors.neutral300
            val tertiary = RawColors.neutral500
        }
        
        object Background {
            val primary = RawColors.neutral900
            val secondary = RawColors.neutral800
            val tertiary = RawColors.neutral700
        }
        
        object Border {
            val default = RawColors.neutral700
            val strong = RawColors.neutral600
            val subtle = RawColors.neutral800
        }
    }
}