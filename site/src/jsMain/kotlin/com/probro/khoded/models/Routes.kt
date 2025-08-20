package com.probro.khoded.models

/**
 * Defines the application's routing structure using a sealed class hierarchy.
 *
 * This sealed class provides a type-safe way to define and manage all application routes.
 * Each nested object represents a major section of the website with its associated
 * slugs and anchor-based navigation paths.
 *
 * The route structure follows a hierarchical pattern:
 * - SLUG: The main page path
 * - Additional constants: Anchor-based navigation within pages
 *
 * @since 1.0.0
 * @see com.probro.khoded.utils.Navigation for navigation implementation
 */
sealed class Routes {
    /**
     * Routes for the home/landing page.
     *
     * Contains the main entry point and various sections within the home page
     * accessible via anchor navigation.
     */
    object Home {
        const val SLUG = "/"
        const val LANDING_ROUTE: String = "#landing"
        const val OUR_SERVICES: String = "#ourServices"
        const val DESIGN: String = "#design"
        const val CONSULTATION: String = "index.html#consultation"
    }

    /**
     * Routes for the company story/about page.
     *
     * Includes navigation to different sections of the company story,
     * founders information, and team joining opportunities.
     */
    object Story {
        const val SLUG = "/story"
        const val FOUNDERS: String = "#founders"
        const val OUR_STORY: String = "#ourStory"
        const val JOIN_OUR_TEAM: String = "#joinOurTeam"
    }

    /**
     * Routes for the services page.
     *
     * Provides navigation to service offerings, detailed breakdowns,
     * frequently asked questions, and getting started information.
     */
    object Services {
        const val SLUG = "/services"
        const val LANDING: String = "#servicesLanding"
        const val SERVICE_BREAKDOWN: String = "#servicesBreakdown"
        const val FAQ: String = "#faq"
        const val GET_STARTED: String = "#getStarted"
    }

    /**
     * Routes for the contact page.
     *
     * Contains paths to contact forms and landing sections for
     * customer inquiries and communications.
     */
    object Contact {
        const val SLUG = "/contact"
        const val LANDING: String = "#contactLanding"
    }

    /**
     * Miscellaneous routes for utility pages.
     *
     * Contains links to legal documents, terms of service,
     * and other auxiliary content.
     */
    object Misc {
        const val TERMS_AND_CONDTIONS = "#termsAndConditions"
    }
}