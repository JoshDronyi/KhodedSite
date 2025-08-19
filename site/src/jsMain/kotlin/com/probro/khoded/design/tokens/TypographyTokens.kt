package com.probro.khoded.design.tokens

import org.jetbrains.compose.web.css.*

/**
 * Typography Design Tokens
 * 
 * Provides consistent typography scale, font weights, and line heights
 * following modern design system principles.
 */

object TypographyTokens {
    
    // =============================================================================
    // FONT FAMILIES
    // =============================================================================
    
    object FontFamily {
        val primary = "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
        val secondary = "Inter, sans-serif"
        val mono = "'JetBrains Mono', 'Fira Code', Consolas, 'Courier New', monospace"
        val display = "Inter, sans-serif"
    }
    
    // =============================================================================
    // FONT WEIGHTS
    // =============================================================================
    
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
    
    // =============================================================================
    // FONT SIZES
    // =============================================================================
    
    object FontSize {
        // Display sizes for hero sections
        val display2xl = 72.px   // 72px
        val displayXl = 60.px   // 60px
        val displayLg = 48.px      // 48px
        val displayMd = 36.px   // 36px
        val displaySm = 30.px  // 30px
        
        // Heading sizes
        val headingXl = 30.px     // 30px
        val headingLarge = 24.px  // 24px
        val headingMedium = 20.px // 20px
        val headingSmall = 18.px  // 18px
        val headingXs = 16.px     // 16px
        
        // Body text sizes
        val bodyLarge = 18.px     // 18px
        val bodyMedium = 16.px    // 16px  
        val bodySmall = 14.px     // 14px
        val bodyXs = 12.px        // 12px
        
        // Label and caption sizes
        val labelLarge = 14.px    // 14px
        val labelMedium = 12.px   // 12px
        val labelSmall = 11.px    // 11px
        val caption = 12.px       // 12px
        val overline = 10.px      // 10px
    }
    
    // =============================================================================
    // LINE HEIGHTS
    // =============================================================================
    
    object LineHeight {
        val tight = 1.25
        val snug = 1.375
        val normal = 1.5
        val relaxed = 1.625
        val loose = 2.0
        
        // Semantic line heights
        val heading = tight
        val body = normal
        val caption = snug
    }
    
    // =============================================================================
    // LETTER SPACING
    // =============================================================================
    
    object LetterSpacing {
        val tighter = (-0.05).em
        val tight = (-0.025).em
        val normal = 0.em
        val wide = 0.025.em
        val wider = 0.05.em
        val widest = 0.1.em
    }
    
    // =============================================================================
    // SEMANTIC TYPOGRAPHY STYLES
    // =============================================================================
    
    object Heading {
        // H1 - Page titles, hero headings
        object H1 {
            val fontSize = FontSize.headingXl
            val fontWeight = FontWeight.bold
            val lineHeight = LineHeight.heading
            val letterSpacing = LetterSpacing.tight
        }
        
        // H2 - Section headings
        object H2 {
            val fontSize = FontSize.headingLarge
            val fontWeight = FontWeight.semiBold
            val lineHeight = LineHeight.heading
            val letterSpacing = LetterSpacing.tight
        }
        
        // H3 - Subsection headings
        object H3 {
            val fontSize = FontSize.headingMedium
            val fontWeight = FontWeight.semiBold
            val lineHeight = LineHeight.heading
            val letterSpacing = LetterSpacing.normal
        }
        
        // H4 - Component headings
        object H4 {
            val fontSize = FontSize.headingSmall
            val fontWeight = FontWeight.medium
            val lineHeight = LineHeight.heading
            val letterSpacing = LetterSpacing.normal
        }
    }
    
    object Body {
        // Large body text for important content
        object Large {
            val fontSize = FontSize.bodyLarge
            val fontWeight = FontWeight.normal
            val lineHeight = LineHeight.body
            val letterSpacing = LetterSpacing.normal
        }
        
        // Standard body text
        object Medium {
            val fontSize = FontSize.bodyMedium
            val fontWeight = FontWeight.normal
            val lineHeight = LineHeight.body
            val letterSpacing = LetterSpacing.normal
        }
        
        // Small body text
        object Small {
            val fontSize = FontSize.bodySmall
            val fontWeight = FontWeight.normal
            val lineHeight = LineHeight.body
            val letterSpacing = LetterSpacing.normal
        }
    }
    
    object Label {
        // Large labels for form fields
        object Large {
            val fontSize = FontSize.labelLarge
            val fontWeight = FontWeight.medium
            val lineHeight = LineHeight.snug
            val letterSpacing = LetterSpacing.normal
        }
        
        // Medium labels
        object Medium {
            val fontSize = FontSize.labelMedium
            val fontWeight = FontWeight.medium
            val lineHeight = LineHeight.snug
            val letterSpacing = LetterSpacing.normal
        }
        
        // Small labels and captions
        object Small {
            val fontSize = FontSize.labelSmall
            val fontWeight = FontWeight.normal
            val lineHeight = LineHeight.caption
            val letterSpacing = LetterSpacing.wide
        }
    }
    
    // =============================================================================
    // CONVENIENCE ALIASES
    // =============================================================================
    
    // Backwards compatibility with existing KhodedDesignSystem
    val fontWeightLight = FontWeight.light
    val fontWeightNormal = FontWeight.normal
    val fontWeightMedium = FontWeight.medium
    val fontWeightSemiBold = FontWeight.semiBold
    val fontWeightBold = FontWeight.bold
    
    val headingLarge = FontSize.headingLarge
    val headingMedium = FontSize.headingMedium
    val headingSmall = FontSize.headingSmall
    
    val bodyLarge = FontSize.bodyLarge
    val bodyMedium = FontSize.bodyMedium
    val bodySmall = FontSize.bodySmall
    
    val labelLarge = FontSize.labelLarge
    val labelMedium = FontSize.labelMedium
    val labelSmall = FontSize.labelSmall
}