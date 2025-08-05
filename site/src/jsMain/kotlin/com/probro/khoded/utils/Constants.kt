package com.probro.khoded.utils

/**
 * Application-wide constants for layout, dimensions, and configuration values.
 *
 * This object centralizes magic numbers and configuration constants used
 * throughout the application, promoting maintainability and consistency.
 * Following Clean Code principles, these constants replace hardcoded values
 * with meaningful, named constants.
 *
 * @since 1.0.0
 * @see IDs for element identifier constants
 */
object Constants {
    
    /**
     * Standard section width for desktop layouts in pixels.
     * 
     * Used as the maximum width for main content sections to ensure
     * optimal reading experience and visual hierarchy on large screens.
     */
    const val SECTION_WIDTH = 1920
    
    /**
     * Standard section height for hero/landing sections in pixels.
     * 
     * Provides consistent vertical spacing for above-the-fold content
     * and ensures proper viewport utilization across different screen sizes.
     */
    const val SECTION_HEIGHT = 700
    
    /**
     * Maximum number of testimonials to display simultaneously.
     * 
     * Limits the testimonial carousel to prevent information overload
     * and maintain optimal user experience in testimonial sections.
     */
    const val LENGTH_OF_TELLS = 5
    
    /**
     * Marketing text for free consultation offerings.
     * 
     * Standardized call-to-action text used across consultation
     * forms and marketing materials to maintain consistent messaging.
     */
    const val FREE_TEXT = "Free 30 Min"
}

/**
 * HTML element identifiers used throughout the application.
 *
 * Centralizes element IDs to prevent conflicts and enable consistent
 * DOM manipulation and testing strategies. All IDs should be unique
 * and descriptive following HTML standards.
 *
 * @since 1.0.0
 * @see Constants for other application constants
 */
object IDs {
    
    /**
     * Unique identifier for popup/modal elements.
     * 
     * Used for popup windows, modal dialogs, and overlay components
     * to ensure proper focus management and accessibility compliance.
     */
    const val PopUpID = "PopUp"
}