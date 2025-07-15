package com.probro.khoded.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.PageContext
import kotlinx.browser.document
import org.w3c.dom.HTMLMetaElement
import org.w3c.dom.HTMLLinkElement

data class SEOData(
    val title: String,
    val description: String,
    val keywords: String = "",
    val ogImage: String = "/logos/khodedlogo.svg.png",
    val ogType: String = "website",
    val twitterCard: String = "summary_large_image",
    val canonicalUrl: String = "",
    val structuredData: String? = null,
    val author: String = "Khoded",
    val robots: String = "index, follow, max-image-preview:large, max-snippet:-1, max-video-preview:-1",
    val language: String = "en-US",
    val themeColor: String = "#390050"
)

@Composable
fun SEOHead(seoData: SEOData, pageContext: PageContext) {
    // Set document title with proper length (50-60 chars)
    document.title = if (seoData.title.length > 60) {
        seoData.title.take(57) + "..."
    } else {
        seoData.title
    }

    // Helper function to set meta tags with proper error handling
    fun setMetaTag(name: String, content: String, property: Boolean = false) {
        if (content.isBlank()) return

        val selector = if (property) "meta[property='$name']" else "meta[name='$name']"
        val existing = document.querySelector(selector)

        if (existing != null) {
            (existing as HTMLMetaElement).content = content
        } else {
            val meta = document.createElement("meta") as HTMLMetaElement
            if (property) {
                meta.setAttribute("property", name)
            } else {
                meta.setAttribute("name", name)
            }
            meta.content = content
            document.head?.appendChild(meta)
        }
    }

    // Core SEO meta tags
    setMetaTag("description", seoData.description.take(160)) // Limit to 160 chars
    setMetaTag("keywords", seoData.keywords)
    setMetaTag("author", seoData.author)
    setMetaTag("robots", seoData.robots)
    setMetaTag("language", seoData.language)
    setMetaTag("theme-color", seoData.themeColor)

    // Viewport meta tag (critical for mobile)
    setMetaTag("viewport", "width=device-width, initial-scale=1.0, viewport-fit=cover")

    // Performance and security meta tags
    setMetaTag("dns-prefetch-control", "on")
    setMetaTag("preconnect", "https://fonts.googleapis.com")
    setMetaTag("preconnect", "https://fonts.gstatic.com")

    // Open Graph tags (Facebook, LinkedIn, etc.)
    setMetaTag("og:title", seoData.title, true)
    setMetaTag("og:description", seoData.description.take(160), true)
    setMetaTag("og:image", seoData.ogImage, true)
    setMetaTag("og:image:width", "1200", true)
    setMetaTag("og:image:height", "630", true)
    setMetaTag("og:image:alt", seoData.title, true)
    setMetaTag("og:type", seoData.ogType, true)
    setMetaTag("og:url", seoData.canonicalUrl.ifEmpty { pageContext.route.path }, true)
    setMetaTag("og:site_name", "Khoded - Professional Web Development", true)
    setMetaTag("og:locale", "en_US", true)

    // Twitter Card tags
    setMetaTag("twitter:card", seoData.twitterCard)
    setMetaTag("twitter:title", seoData.title)
    setMetaTag("twitter:description", seoData.description.take(160))
    setMetaTag("twitter:image", seoData.ogImage)
    setMetaTag("twitter:image:alt", seoData.title)
    setMetaTag("twitter:site", "@khoded")
    setMetaTag("twitter:creator", "@khoded")

    // Canonical URL
    val canonicalUrl = seoData.canonicalUrl.ifEmpty {
        "https://khoded.com${pageContext.route.path}"
    }
    val existingCanonical = document.querySelector("link[rel='canonical']")
    if (existingCanonical != null) {
        existingCanonical.setAttribute("href", canonicalUrl)
    } else {
        val link = document.createElement("link") as HTMLLinkElement
        link.rel = "canonical"
        link.href = canonicalUrl
        document.head?.appendChild(link)
    }

    // Structured Data (JSON-LD) with enhanced schema
    seoData.structuredData?.let { jsonLd ->
        val existingScript = document.querySelector("script[type='application/ld+json']")
        if (existingScript != null) {
            existingScript.textContent = jsonLd
        } else {
            val script = document.createElement("script")
            script.setAttribute("type", "application/ld+json")
            script.textContent = jsonLd
            document.head?.appendChild(script)
        }
    }

    // Preload critical resources
    val preloadLinks = listOf(
        "/logos/khodedlogo.svg.png" to "image",
        "/fonts/space-grotesk.woff2" to "font"
    )

    preloadLinks.forEach { (href, asType) ->
        val existingPreload = document.querySelector("link[rel='preload'][href='$href']")
        if (existingPreload == null) {
            val preloadLink = document.createElement("link") as HTMLLinkElement
            preloadLink.rel = "preload"
            preloadLink.href = href
            preloadLink.setAttribute("as", asType)
            if (asType == "font") {
                preloadLink.setAttribute("crossorigin", "anonymous")
            }
            document.head?.appendChild(preloadLink)
        }
    }
}

// Enhanced SEO data with better structured data
object KhodedSEO {
    val homePage = SEOData(
        title = "Khoded - Professional Web Development & Digital Solutions | Custom Websites",
        description = "Transform your digital presence with Khoded's custom web development, hosting, and branding services. Get a FREE 30-minute consultation today! Expert web developers in Connecticut.",
        keywords = "web development, custom websites, web hosting, branding, SEO, Connecticut web design, professional web development, digital solutions",
        canonicalUrl = "https://khoded.com/",
        structuredData = """
        {
            "@context": "https://schema.org",
            "@graph": [
                {
                    "@type": "Organization",
                    "@id": "https://khoded.com/#organization",
                    "name": "Khoded",
                    "url": "https://khoded.com",
                    "logo": {
                        "@type": "ImageObject",
                        "url": "https://khoded.com/logos/khodedlogo.svg.png",
                        "width": 400,
                        "height": 400
                    },
                    "description": "Professional web development and digital solutions company specializing in custom websites, hosting, and branding",
                    "contactPoint": {
                        "@type": "ContactPoint",
                        "telephone": "+1-833-454-6333",
                        "contactType": "customer service",
                        "email": "admin@khoded.com",
                        "availableLanguage": "English"
                    },
                    "address": {
                        "@type": "PostalAddress",
                        "streetAddress": "2389 Main St. STE 100",
                        "addressLocality": "Glastonbury",
                        "addressRegion": "CT",
                        "postalCode": "06033",
                        "addressCountry": "US"
                    },
                    "sameAs": [
                        "https://linkedin.com/company/khoded",
                        "https://twitter.com/khoded"
                    ],
                    "foundingDate": "2023",
                    "numberOfEmployees": "2-10",
                    "areaServed": {
                        "@type": "Country",
                        "name": "United States"
                    }
                },
                {
                    "@type": "WebSite",
                    "@id": "https://khoded.com/#website",
                    "url": "https://khoded.com",
                    "name": "Khoded",
                    "publisher": {
                        "@id": "https://khoded.com/#organization"
                    },
                    "inLanguage": "en-US"
                },
                {
                    "@type": "Service",
                    "serviceType": "Web Development",
                    "provider": {
                        "@id": "https://khoded.com/#organization"
                    },
                    "areaServed": {
                        "@type": "Country",
                        "name": "United States"
                    }
                }
            ]
        }
        """.trimIndent()
    )

    val aboutPage = SEOData(
        title = "About Khoded - Meet Our Web Development Experts | Esther & Josh Dronyi",
        description = "Learn about Khoded's mission, founders Esther and Josh Dronyi, and our commitment to creating exceptional web experiences for businesses. Professional web development team.",
        keywords = "about khoded, web development team, esther dronyi, josh dronyi, company story, professional web developers",
        canonicalUrl = "https://khoded.com/about"
    )

    val contactPage = SEOData(
        title = "Contact Khoded - Free Web Development Consultation | Call 833-454-6333",
        description = "Ready to transform your digital presence? Contact Khoded for a FREE 30-minute consultation. Located in Glastonbury, CT. Professional web development services.",
        keywords = "contact khoded, free consultation, web development consultation, glastonbury web design, connecticut web development",
        canonicalUrl = "https://khoded.com/contact"
    )
}