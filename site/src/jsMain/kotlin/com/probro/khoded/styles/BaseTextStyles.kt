package com.probro.khoded.styles.base

import com.probro.khoded.styles.KhodedColors
import com.probro.khoded.styles.KhodedTypography
import com.probro.khoded.styles.KhodedSpacing
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.style.KobwebComposeStyleSheet.attr
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

/**
 * Foundation typography styles and text component system for the Khoded design system.
 *
 * This file establishes the base text styling architecture that provides consistent
 * typography across the entire application. It follows a mobile-first approach with
 * responsive breakpoints and implements a variant-based system for different text
 * use cases.
 *
 * The typography system includes:
 * - Base text styles with responsive behavior
 * - Semantic text variants for different contexts
 * - Consistent spacing, sizing, and color schemes
 * - Accessibility-compliant typography practices
 *
 * Architecture:
 * - BaseTextStyle: Foundation for all text components
 * - Variants: Specialized text styles for specific use cases
 * - ComponentKind interfaces: Type safety for style application
 *
 * @since 1.0.0
 * @see KhodedTypography for typography scales and constants
 * @see KhodedColors for color definitions
 * @see KhodedSpacing for spacing constants
 */

/**
 * Component kind interfaces for type-safe style application.
 * These sealed interfaces enable compile-time safety when applying styles
 * to ensure text styles are only used on appropriate components.
 */

/** Base interface for all text-based components */
sealed interface BaseTextKind : ComponentKind

/** Interface for heading components (h1-h6) */
sealed interface HeadingKind : ComponentKind

/** Interface for body/content text components */
sealed interface BodyKind : ComponentKind

/**
 * Core foundation text style that serves as the base for all text components.
 *
 * This style establishes the fundamental typography properties including:
 * - Responsive font sizing across all breakpoints
 * - Consistent font family and line height
 * - Text overflow and wrapping behavior
 * - Base spacing and alignment
 * - Accessibility-compliant defaults
 *
 * All other text variants extend this base style to ensure consistency
 * while allowing for specific customizations.
 */
val BaseTextStyle = CssStyle<BaseTextKind> {
    base {
        Modifier
            .fontSize(KhodedTypography.base)
            .fontFamily(KhodedTypography.fontFamilyDefault)
            .lineHeight(KhodedTypography.normal)
            .letterSpacing(KhodedTypography.baseLetterSpacing)
            .color(KhodedColors.TextPrimary)
            .padding(0.px)
            .margin(0.px)
            .textAlign(TextAlign.Center)
            .textOverflow(TextOverflow.Ellipsis)
            .overflow(Overflow.Hidden)
            .overflowWrap(OverflowWrap.Anywhere)
            .fillMaxWidth()
    }
    Breakpoint.ZERO {
        Modifier.fontSize(KhodedTypography.sm)
    }
    Breakpoint.SM {
        Modifier.fontSize(KhodedTypography.base)
    }
    Breakpoint.MD {
        Modifier.fontSize(KhodedTypography.lg)
    }
}

/**
 * Base heading style for all heading components (h1-h6).
 *
 * Provides foundational styling for semantic heading elements with
 * consistent width and layout properties. Extended by specific
 * heading variants for different hierarchical levels.
 */
val HeadingStyle = CssStyle<HeadingKind> {
    base {
        Modifier.fillMaxWidth()
    }
}

/**
 * Base body style for page-level content containers.
 *
 * Establishes the foundational styling for body elements including
 * background colors, text colors, and font families that provide
 * the default appearance for content areas.
 */
val BodyStyle = CssStyle<BodyKind> {
    base {
        Modifier
            .fillMaxSize()
            .backgroundColor(KhodedColors.Background)
            .color(KhodedColors.TextPrimary)
            .fontFamily(KhodedTypography.fontFamilyDefault)
    }
}

/**
 * Primary text variant for main content and important text blocks.
 *
 * This variant is designed for primary content areas, body text, and
 * important messaging. It provides excellent readability with appropriate
 * sizing, spacing, and responsive behavior across all device sizes.
 *
 * Features:
 * - Left-aligned text for improved readability
 * - Semi-bold font weight for emphasis
 * - Responsive padding and font sizing
 * - Relaxed line height for comfortable reading
 */
val MainTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(KhodedTypography.lg)
            .textAlign(TextAlign.Start)
            .fontWeight(KhodedTypography.semiBold)
            .padding(leftRight = KhodedSpacing.sm)
            .lineHeight(KhodedTypography.relaxed)
            .color(KhodedColors.TextPrimary)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(KhodedTypography.base)
            .padding(leftRight = KhodedSpacing.md)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(KhodedTypography.lg)
            .padding(leftRight = KhodedSpacing.lg)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(KhodedTypography.xl2)
            .padding(leftRight = KhodedSpacing.xl)
    }
    Breakpoint.LG {
        Modifier
            .fontSize(KhodedTypography.xl3)
            .padding(leftRight = KhodedSpacing.xl2)
    }
    Breakpoint.XL {
        Modifier
            .fontSize(KhodedTypography.xl4)
            .padding(leftRight = KhodedSpacing.xl2)
    }
}

/**
 * Section title variant for page and content section headings.
 *
 * Designed for major section headings throughout the application,
 * providing strong visual hierarchy and brand-consistent typography.
 * Uses heading font family and bold weight for maximum impact.
 *
 * Features:
 * - Center-aligned for visual balance
 * - Heading font family for brand consistency
 * - Bold font weight for strong hierarchy
 * - Tight line height for compact appearance
 * - Fully responsive sizing across all breakpoints
 */
val SectionTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .color(KhodedColors.TextPrimary)
            .textAlign(TextAlign.Center)
            .fontWeight(KhodedTypography.bold)
            .fontFamily(KhodedTypography.fontFamilyHeading)
            .lineHeight(KhodedTypography.tight)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(KhodedTypography.xl2)
    }
    Breakpoint.SM {
        Modifier.fontSize(KhodedTypography.xl3)
    }
    Breakpoint.MD {
        Modifier.fontSize(KhodedTypography.xl4)
    }
    Breakpoint.LG {
        Modifier.fontSize(KhodedTypography.xl5)
    }
    Breakpoint.XL {
        Modifier.fontSize(KhodedTypography.xl6)
    }
}
/**
 * Subtitle variant for supporting text beneath main headings.
 *
 * Provides complementary text styling for descriptions, taglines,
 * and supporting content that appears below main titles. Uses
 * inverse colors and specific positioning for visual balance.
 *
 * Features:
 * - Constrained width (80%) for optimal line length
 * - Left-aligned for natural reading flow
 * - Inverse text color for subtle contrast
 * - Generous margins for proper spacing
 * - Responsive positioning and sizing
 */
val SubTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth(80.percent)
            .textAlign(TextAlign.Start)
            .color(KhodedColors.TextInverse)
            .margin(topBottom = KhodedSpacing.xl3)
            .lineHeight(KhodedTypography.relaxed)
    }
    Breakpoint.ZERO {
        Modifier
            .fontSize(KhodedTypography.sm)
            .translateY(ty = KhodedSpacing.xl)
            .fillMaxWidth(60.percent)
    }
    Breakpoint.SM {
        Modifier
            .fontSize(KhodedTypography.base)
            .margin(topBottom = KhodedSpacing.lg)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(KhodedTypography.lg)
            .margin(topBottom = KhodedSpacing.xl)
    }
    Breakpoint.LG {
        Modifier
            .fontSize(KhodedTypography.xl)
            .translateX(KhodedSpacing.xl4)
    }
}

/**
 * Accent text variant for highlighted and decorative text elements.
 *
 * Used for special callouts, decorative text, and elements that need
 * to stand out from the main content flow. Features brand colors
 * and flexible positioning for design emphasis.
 *
 * Features:
 * - Fit-content width for flexible sizing
 * - Brand purple color for visual emphasis
 * - Semi-bold weight for subtle emphasis
 * - Responsive positioning with transforms
 */
val AccentTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .maxWidth(MaxWidth.FitContent)
            .color(KhodedColors.Purple600)
            .fontWeight(KhodedTypography.semiBold)
            .fontSize(KhodedTypography.lg)
    }
    Breakpoint.ZERO {
        Modifier.translateY(ty = KhodedSpacing.xl7)
    }
    Breakpoint.XL {
        Modifier
            .translate(tx = KhodedSpacing.xl4, ty = KhodedSpacing.xl5)
            .fontSize(KhodedTypography.xl3)
    }
}

/**
 * Highlight text variant for emphasized inline content.
 *
 * Perfect for keywords, important phrases, and inline emphasis
 * within larger text blocks. Provides strong visual distinction
 * while maintaining readability.
 *
 * Features:
 * - Bold font weight for strong emphasis
 * - Brand purple color for consistency
 * - Minimal padding for inline use
 * - Responsive font sizing
 */
val HighlightTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontWeight(KhodedTypography.bold)
            .fontSize(KhodedTypography.xl)
            .padding(leftRight = KhodedSpacing.xs)
            .color(KhodedColors.Purple600)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(KhodedTypography.base)
    }
    Breakpoint.SM {
        Modifier.fontSize(KhodedTypography.lg)
    }
}

/**
 * Button text variant optimized for interactive elements.
 *
 * Specifically designed for button labels and call-to-action text,
 * with properties optimized for readability in interactive contexts.
 * Includes text wrapping and overflow handling for various button sizes.
 *
 * Features:
 * - Balanced text wrapping for better button layouts
 * - Visible overflow for accessibility
 * - Fit-content height for proper button sizing
 * - Comprehensive responsive font scaling
 */
val ButtonTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(FontSize.Medium)
            .overflowWrap(OverflowWrap.Anywhere)
            .overflow(Overflow.Visible)
            .styleModifier {
                attr("text-wrap", "balance")
            }
            .height(Height.FitContent)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.XSmall)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.Smaller)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.Medium)
    }
    Breakpoint.LG {
        Modifier.fontSize(FontSize.Large)
    }
    Breakpoint.XL {
        Modifier.fontSize(FontSize.Larger)
    }
}

/**
 * Team biography paragraph variant for founder and team content.
 *
 * Optimized for biographical content, team descriptions, and
 * personal narrative text. Provides clean, readable formatting
 * for longer-form content about team members.
 *
 * Features:
 * - Full-width layout for content areas
 * - Center alignment for formal presentation
 * - Reset padding and margins for clean layout
 */
val TeamBioParagraphVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .padding(0.px)
            .margin(0.px)
            .textAlign(TextAlign.Center)
    }
}

/**
 * Job title variant for position headings and role descriptions.
 *
 * Used for displaying job positions, roles, and titles throughout
 * the application, particularly in team sections and job listings.
 *
 * Features:
 * - Large font size for prominence
 * - Bold font weight for hierarchy
 * - Fit-content height for clean layout
 * - Standard margins for spacing
 */
val JobTitleVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontSize(FontSize.Large)
            .fontWeight(FontWeight.Bold)
            .height(Height.FitContent)
            .margin(10.px)
    }
}

/**
 * Job description variant for role details and responsibilities.
 *
 * Complements job titles with appropriate styling for longer
 * descriptive text about positions and responsibilities.
 *
 * Features:
 * - Large font size for readability
 * - Full-width layout for content
 * - Fit-content height for proper spacing
 */
val JobDescriptionVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fillMaxWidth()
            .fontSize(FontSize.Large)
            .height(Height.FitContent)
    }
}

/**
 * Contact prompt text variant for contact page headings.
 *
 * Large, bold text specifically designed for contact page headlines
 * and call-to-action prompts. Provides strong visual hierarchy
 * and encourages user engagement.
 *
 * Features:
 * - Extra large font size (48px on desktop)
 * - Left alignment for natural reading
 * - Bold font weight for maximum impact
 * - Comprehensive responsive scaling
 */
val ContactPromptTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .fontSize(48.px)
            .textAlign(TextAlign.Start)
            .fontWeight(FontWeight.Bold)
    }
    Breakpoint.ZERO {
        Modifier.fontSize(FontSize.Larger)
    }
    Breakpoint.SM {
        Modifier.fontSize(FontSize.XLarge)
    }
    Breakpoint.MD {
        Modifier.fontSize(FontSize.XXLarge)
    }
    Breakpoint.LG {
        Modifier.fontSize(48.px)
    }
}

/**
 * Company contact information text variant.
 *
 * Designed for displaying company contact details, addresses,
 * and business information with appropriate emphasis and alignment.
 *
 * Features:
 * - Large, bold text for easy reading
 * - Right alignment for layout balance
 * - Bolder font weight for emphasis
 * - Clean spacing with reset margins
 */
val CompanyContactTextVariant = BaseTextStyle.addVariant {
    base {
        Modifier
            .padding(0.px)
            .margin(0.px)
            .fontSize(FontSize.Larger)
            .fontWeight(FontWeight.Bolder)
            .textAlign(TextAlign.End)
    }
}