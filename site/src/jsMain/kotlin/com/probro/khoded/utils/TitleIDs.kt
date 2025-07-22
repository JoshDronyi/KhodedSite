package com.probro.khoded.utils

/**
 * Section title identifiers for anchor-based navigation and accessibility.
 *
 * This object provides centralized management of section title IDs used
 * throughout the application for smooth scrolling navigation, accessibility
 * landmarks, and SEO optimization. All IDs follow the CSS selector format
 * with hash prefixes for direct use in navigation functions.
 *
 * These IDs serve dual purposes:
 * 1. Anchor navigation for single-page application routing
 * 2. Accessibility landmarks for screen readers and assistive technologies
 *
 * @since 1.0.0
 * @see com.probro.khoded.models.Routes for page-level routing
 * @see com.probro.khoded.utils.Navigation for navigation implementation
 */
object TitleIDs {
    
    // Home page section identifiers
    
    /**
     * Consultation section title anchor ID.
     * 
     * Used for navigating to and identifying the consultation/contact
     * section within the home page layout.
     */
    const val consultationTitleID = "#consultationTitle"
    
    /**
     * Design section title anchor ID.
     * 
     * Used for navigating to and identifying the design services
     * showcase section within the home page.
     */
    const val designTitleID = "#designTitle"
    
    /**
     * Services section title anchor ID.
     * 
     * Used for navigating to and identifying the main services
     * overview section within the home page.
     */
    const val servicesTitleID = "#servicesTitle"
    
    /**
     * Landing/hero section title anchor ID.
     * 
     * Used for navigating back to the top hero section and
     * providing a landmark for the main page introduction.
     */
    const val landingTitleID = "#landingTitle"

    // Story/About page section identifiers
    
    /**
     * Company story section title anchor ID.
     * 
     * Used for navigating to and identifying the company history
     * and background section within the about page.
     */
    const val storyTitle = "#storyTitle"
    
    /**
     * Founders section title anchor ID.
     * 
     * Used for navigating to and identifying the founders/team
     * introduction section within the about page.
     */
    const val founderTitle = "#founderTitle"
    
    /**
     * Career opportunities section title anchor ID.
     * 
     * Used for navigating to and identifying the careers/job
     * opportunities section within the about page.
     */
    const val opportunitiesTitle = "#opportunitiesTitle"
}
