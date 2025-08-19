package com.probro.khoded.design.tokens

import org.jetbrains.compose.web.css.*

/**
 * Spacing Design Tokens
 * 
 * Provides consistent spacing values following the 8px grid system.
 * All spacing values are multiples of 0.25rem (4px) for pixel-perfect designs.
 */

object SpacingTokens {
    
    // =============================================================================
    // BASE SPACING SCALE (8px Grid System)
    // =============================================================================
    
    object Space {
        val px = 1.px
        val none = 0.px
        val xs = 4.px      // 4px
        val sm = 8.px       // 8px
        val md = 12.px      // 12px
        val lg = 16.px         // 16px
        val xl = 20.px      // 20px
        val xl2 = 24.px      // 24px
        val xl3 = 28.px     // 28px
        val xl4 = 32.px        // 32px
        val xl5 = 40.px      // 40px
        val xl6 = 48.px        // 48px
        val xl7 = 56.px      // 56px
        val xl8 = 64.px        // 64px
        val xl9 = 80.px        // 80px
        val xl10 = 96.px       // 96px
        val xl11 = 128.px       // 128px
        val xl12 = 160.px      // 160px
        val xl13 = 192.px      // 192px
        val xl14 = 256.px      // 256px
        val xl15 = 320.px      // 320px
        val xl16 = 384.px      // 384px
    }
    
    // =============================================================================
    // SEMANTIC SPACING
    // =============================================================================
    
    object ComponentSpacing {
        val buttonPadding = Space.md
        val inputPadding = Space.sm
        val cardPadding = Space.lg
        val sectionPadding = Space.xl4
        val containerPadding = Space.xl2
    }
    
    object LayoutSpacing {
        val gutter = Space.lg
        val marginSmall = Space.sm
        val marginMedium = Space.lg
        val marginLarge = Space.xl2
        val marginXLarge = Space.xl4
    }
    
    // Touch targets and interactive elements
    object InteractiveSpacing {
        val touchTarget = 44.px
        val touchTargetMin = 40.px
        val focusRingWidth = 2.px
        val dividerThickness = 1.px
    }
    
    // =============================================================================
    // RESPONSIVE SPACING
    // =============================================================================
    
    object ResponsiveSpacing {
        val mobileContainer = Space.lg
        val tabletContainer = Space.xl2
        val desktopContainer = Space.xl4
        
        val mobileSectionGap = Space.xl2
        val tabletSectionGap = Space.xl4
        val desktopSectionGap = Space.xl6
    }
    
    // =============================================================================
    // GRID SYSTEM
    // =============================================================================
    
    object GridSpacing {
        val columnGap = Space.lg
        val rowGap = Space.lg
        val gridPadding = Space.xl2
    }
    
    // =============================================================================
    // CONVENIENCE ALIASES
    // =============================================================================
    
    // Backwards compatibility with existing code
    val space1 = Space.xs
    val space2 = Space.sm  
    val space3 = Space.md
    val space4 = Space.lg
    val space5 = Space.xl
    val spacing = Space // Alias for the main spacing object
    val touchTarget = InteractiveSpacing.touchTarget
    val touchTargetMin = InteractiveSpacing.touchTargetMin
}