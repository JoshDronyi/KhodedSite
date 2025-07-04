package com.probro.khoded.models

import kotlinx.browser.window

object Images {
    object HomePage {
        // Add responsive versions for better mobile performance
        const val landing_Rocket: String = "/home/rocketManSquiggle.webp" // Convert to WebP
        const val landing_Rocket_Mobile: String = "/home/rocketManSquiggle-mobile.webp"
        const val services_ChartMaker: String = "/home/laptopDudeSquiggle.webp"
        const val services_ChartMaker_Mobile: String = "/home/laptopDudeSquiggle-mobile.webp"
        const val design_Paperplane_Computer: String = "/home/laptopPlane.webp"
        const val design_Paperplane_Computer_Mobile: String = "/home/laptopPlane-mobile.webp"
        const val consultation_MessageBubble_And_Squiggle: String = "/home/textBubblesAndSquiggle.webp"
        const val consultation_CheckMessage: String = "/home/phoneTextBubble.webp"
        const val consultation_Quotes: String = "/home/quotes.svg" // Keep SVGs as-is
    }

    // Helper function to get responsive image
    fun getResponsiveImage(desktop: String, mobile: String): String {
        return if (window.innerWidth < 768) mobile else desktop
    }

    object StoryPage {
        const val megaphone: String = "/story/megaphone.png"
        const val founderJosh: String = "/story/joshFounder.png"
        const val founderEsther: String = "/story/estherFounder.png"
        const val jointFounderImage: String = "/story/foundersJoint.png"
    }

    object ContactPage {
        const val planet404: String = "/contact/planet404.png"
    }

    object Common {
        const val blackUnderline: String = "/common/blackUnderline.png"
        const val blueUnderline: String = "/common/blueUnderline.png"
        const val pinkUnderline: String = "/common/pinkUnderline.png"
    }

    object Logos {
        const val fullTransparent = "/logos/khodedlogo.svg.png"
        const val circleLogo = "/logos/KhodedCircleLogo.svg.png"
        const val minimalLogo = "/logos/minimalLogo.png"
    }

}