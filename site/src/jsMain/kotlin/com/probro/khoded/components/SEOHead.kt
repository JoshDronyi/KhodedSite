package com.probro.khoded.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.PageContext
import kotlinx.browser.document
import org.w3c.dom.HTMLMetaElement

data class SEOData(
    val title: String,
    val description: String,
    val keywords: String = "",
    val ogImage: String = "/logos/khodedlogo.svg.png",
    val ogType: String = "website",
    val twitterCard: String = "summary_large_image",
    val canonicalUrl: String = "",
    val structuredData: String? = null
)

@Composable
fun SEOHead(seoData: SEOData, pageContext: PageContext) {
    // Set document title
    document.title = seoData.title

    // Helper function to set meta tags
    fun setMetaTag(name: String, content: String, property: Boolean = false) {
        val existing = if (property) {
            document.querySelector("meta[property='$name']")
        } else {
            document.querySelector("meta[name='$name']")
        }

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

    // Basic SEO meta tags
    setMetaTag("description", seoData.description)
    setMetaTag("keywords", seoData.keywords)
    setMetaTag("robots", "index, follow")
    setMetaTag("viewport", "width=device-width, initial-scale=1.0")

    // Open Graph tags
    setMetaTag("og:title", seoData.title, true)
    setMetaTag("og:description", seoData.description, true)
    setMetaTag("og:image", seoData.ogImage, true)
    setMetaTag("og:type", seoData.ogType, true)
    setMetaTag("og:url", seoData.canonicalUrl.ifEmpty { pageContext.route.path }, true)
    setMetaTag("og:site_name", "Khoded - Professional Web Development", true)

    // Twitter Card tags
    setMetaTag("twitter:card", seoData.twitterCard)
    setMetaTag("twitter:title", seoData.title)
    setMetaTag("twitter:description", seoData.description)
    setMetaTag("twitter:image", seoData.ogImage)

    // Canonical URL
    if (seoData.canonicalUrl.isNotEmpty()) {
        val existing = document.querySelector("link[rel='canonical']")
        if (existing != null) {
            existing.setAttribute("href", seoData.canonicalUrl)
        } else {
            val link = document.createElement("link")
            link.setAttribute("rel", "canonical")
            link.setAttribute("href", seoData.canonicalUrl)
            document.head?.appendChild(link)
        }
    }

    // Structured Data (JSON-LD)
    seoData.structuredData?.let { jsonLd ->
        val existing = document.querySelector("script[type='application/ld+json']")
        if (existing != null) {
            existing.textContent = jsonLd
        } else {
            val script = document.createElement("script")
            script.setAttribute("type", "application/ld+json")
            script.textContent = jsonLd
            document.head?.appendChild(script)
        }
    }
}

// Predefined SEO data for your pages
object KhodedSEO {
    val homePage = SEOData(
        title = "Khoded - Professional Web Development & Digital Solutions",
        description = "Transform your digital presence with Khoded's custom web development, hosting, and branding services. Get a FREE 30-minute consultation today!",
        keywords = "web development, custom websites, web hosting, branding, SEO, Connecticut web design",
        canonicalUrl = "https://khoded.com/",
        structuredData = """
        {
            "@context": "https://schema.org",
            "@type": "Organization",
            "name": "Khoded",
            "description": "Professional web development and digital solutions company",
            "url": "https://khoded.com",
            "logo": "https://khoded.com/logos/khodedlogo.svg.png",
            "contactPoint": {
                "@type": "ContactPoint",
                "telephone": "833-454-6333",
                "contactType": "customer service",
                "email": "admin@khoded.com"
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
            ]
        }
        """.trimIndent()
    )

    val aboutPage = SEOData(
        title = "About Khoded - Meet Our Web Development Experts | Khoded",
        description = "Learn about Khoded's mission, founders Esther and Josh Dronyi, and our commitment to creating exceptional web experiences for businesses.",
        keywords = "about khoded, web development team, esther dronyi, josh dronyi, company story",
        canonicalUrl = "https://khoded.com/about"
    )

    val contactPage = SEOData(
        title = "Contact Khoded - Free Web Development Consultation | Khoded",
        description = "Ready to transform your digital presence? Contact Khoded for a FREE 30-minute consultation. Located in Glastonbury, CT. Call 833-454-6333.",
        keywords = "contact khoded, free consultation, web development consultation, glastonbury web design",
        canonicalUrl = "https://khoded.com/contact"
    )
}