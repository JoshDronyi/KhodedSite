package com.probro.khoded.models

import com.probro.khoded.utils.Navigator

sealed class Routes {
    object Home {
        const val SLUG = "/"
        const val LANDING_ROUTE: String = "#homeLanding"
        const val OUR_SERVICES: String = "#ourServices"
        const val DESIGN: String = "#design"
        const val CONSULTATION: String = "#consultation"
    }

    object Story {
        const val SLUG = "/story"
        const val FOUNDERS: String = "#founders"
        const val OUR_STORY: String = "#ourStory"
        const val JOIN_OUR_TEAM: String = "#joinOurTeam"
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
    }

    object Misc {
        const val TERMS_AND_CONDTIONS = "#termsAndConditions"
    }
}