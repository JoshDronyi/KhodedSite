package com.probro.khoded.models

sealed class Routes {
    object Home {
        const val SLUG = "/"
        const val LANDING_ROUTE: String = "#homeLanding"
        const val WEB_DESIGN: String = "#webDesign"
        const val HOSTING: String = "#hosting"
        const val BRANDING: String = "#branding"
        const val CONSULTATION: String = "#consultation"
        const val TESTIMONIALS: String = "#testimonials"
        const val GET_STARTED: String = "#getStarted"
    }

    object About {
        const val SLUG = "/about"
        const val LANDING: String = "#aboutLanding"
        const val TEAM: String = "#team"
        const val STORY: String = "#story"
        const val SEPARATOR: String = "#separator"
        const val OPPORTUNITIES: String = "#opportunities"
    }

    object Services {
        const val SLUG = "/services"
        const val LANDING: String = "#servicesLanding"
        const val SERVICE_BREAKDOWN: String = "#servicesBreakdown"
        const val FAQ: String = "#faq"
        const val GET_STARTED: String = "#getStarted"
    }

    object Contact {
        const val SLUG = "/contact"
        const val LANDING: String = "#contactLanding"
        const val INTAKE_FORM: String = "#intakeForm"
        const val CONTACT_US: String = "#contactUs"
    }

    object Misc {
        const val NAME = "Misc"
        const val COMING_SOON: String = "comingSoon"
    }
}