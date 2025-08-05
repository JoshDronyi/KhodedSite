package com.probro.khoded.models

import kotlinx.browser.window

/**
 * Centralized image resource management system for the Khoded application.
 *
 * This object provides organized access to all image assets used throughout
 * the application, supporting responsive design patterns and performance
 * optimization through format-specific resource management.
 *
 * The structure follows a page-based organization with common resources
 * grouped separately. All paths are relative to the public resources directory.
 *
 * @since 1.0.0
 * @see com.probro.khoded.components.composables.OptimizedImage for usage
 */
object Images {
    /**
     * Image resources specifically for the home/landing page.
     *
     * Includes hero images, service illustrations, and other homepage-specific
     * visual content. Provides both desktop and mobile-optimized versions
     * for responsive design and performance optimization.
     */
    object HomePage {
        // Add responsive versions for better mobile performance
        const val landing_Rocket: String = "/home/newRocketMan.webp" // Convert to WebP
        const val landing_Rocket_Mobile: String = "/home/rocketManSquiggle-mobile.webp"
        const val services_ChartMaker: String = "/home/laptopDudeSquiggle.webp"
        const val services_ChartMaker_Mobile: String = "/home/laptopDudeSquiggle-mobile.webp"
        const val design_Paperplane_Computer: String = "/home/laptopPlane.webp"
        const val design_Paperplane_Computer_Mobile: String = "/home/laptopPlane-mobile.webp"
        const val consultation_MessageBubble_And_Squiggle: String = "/home/textBubblesAndSquiggle.webp"
        const val consultation_CheckMessage: String = "/home/phoneTextBubble.webp"
        const val consultation_Quotes: String = "/home/quotes.svg" // Keep SVGs as-is
    }

    /**
     * Determines the appropriate image resource based on screen size.
     *
     * This helper function implements responsive image loading by selecting
     * between desktop and mobile-optimized image variants based on viewport width.
     * The breakpoint at 768px aligns with common mobile/tablet boundaries.
     *
     * @param desktop Path to the desktop-optimized image resource
     * @param mobile Path to the mobile-optimized image resource
     * @return The appropriate image path based on current viewport width
     *
     * Example usage:
     * ```kotlin
     * val imageUrl = Images.getResponsiveImage(
     *     desktop = Images.HomePage.landing_Rocket,
     *     mobile = Images.HomePage.landing_Rocket_Mobile
     * )
     * ```
     */
    fun getResponsiveImage(desktop: String, mobile: String): String {
        return if (window.innerWidth < 768) mobile else desktop
    }

    /**
     * Image resources for the company story/about page.
     *
     * Contains founder photos, company imagery, and other assets
     * related to the company's background and team information.
     */
    object StoryPage {
        const val megaphone: String = "/story/megaphone.png"
        const val founderJosh: String = "/story/joshFounder.png"
        const val founderEsther: String = "/story/estherFounder.png"
        const val jointFounderImage: String = "/story/foundersJoint.png"
    }

    /**
     * Image resources for the contact page.
     *
     * Contains contact-related imagery and illustrations used
     * to enhance the user experience on contact forms and pages.
     */
    object ContactPage {
        const val planet404: String = "/contact/planet404.png"
    }

    /**
     * Shared image resources used across multiple pages.
     *
     * Contains decorative elements, underlines, and other graphical
     * components that appear throughout the application.
     */
    object Common {
        const val blackUnderline: String = "/common/blackUnderline.png"
        const val blueUnderline: String = "/common/blueUnderline.png"
        const val pinkUnderline: String = "/common/pinkUnderline.png"
    }

    /**
     * Brand logo variations and company branding assets.
     *
     * Contains different logo formats and variations for use
     * in headers, footers, and branding contexts throughout the site.
     */
    object Logos {
        const val fullTransparent = "/logos/khodedlogo.svg.png"
        const val circleLogo = "/logos/KhodedCircleLogo.svg.png"
        const val minimalLogo = "/logos/minimalLogo.png"
    }

}