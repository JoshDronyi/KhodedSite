package com.probro.khoded.components.seo

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLMetaElement

/**
 * Comprehensive SEO Head Component
 * 
 * Implements state-of-the-art SEO practices including:
 * - Open Graph and Twitter Card meta tags
 * - JSON-LD structured data
 * - Performance optimization hints
 * - Security headers via meta tags
 * - Progressive Web App manifest
 */

data class SEOConfig(
    val title: String,
    val description: String,
    val keywords: List<String> = emptyList(),
    val canonicalUrl: String? = null,
    val ogImage: String? = null,
    val ogImageAlt: String? = null,
    val ogType: String = "website",
    val twitterCard: String = "summary_large_image",
    val author: String = "Khoded",
    val robots: String = "index, follow",
    val structuredData: Map<String, Any>? = null,
    val alternateLanguages: Map<String, String> = emptyMap(),
    val lastModified: String? = null,
    val publishedTime: String? = null
)

@Composable
fun KhodedSEOHead(config: SEOConfig) {
    val currentUrl = remember { window.location.href }
    val siteName = "Khoded - Professional Web Development Services"
    
    LaunchedEffect(config) {
        // Set document title
        document.title = config.title
        
        // Clear existing meta tags that we'll be replacing
        clearExistingMetaTags()
        
        // Create all meta tags
        createMetaTags(config, currentUrl, siteName)
        
        // Add structured data
        config.structuredData?.let { data ->
            addStructuredData(data)
        }
        
        // Add default structured data if none provided
        if (config.structuredData == null) {
            addDefaultStructuredData(config, currentUrl)
        }
    }
}

private fun clearExistingMetaTags() {
    val metaTags = listOf(
        "description", "keywords", "author", "robots", "canonical",
        "og:title", "og:description", "og:image", "og:image:alt", "og:type", "og:url", "og:site_name",
        "twitter:card", "twitter:title", "twitter:description", "twitter:image", "twitter:image:alt"
    )
    
    metaTags.forEach { name ->
        document.querySelector("meta[name='$name']")?.remove()
        document.querySelector("meta[property='$name']")?.remove()
    }
    
    document.querySelector("link[rel='canonical']")?.remove()
}

private fun createMetaTags(config: SEOConfig, currentUrl: String, siteName: String) {
    // Basic meta tags
    createMetaTag("description", config.description)
    if (config.keywords.isNotEmpty()) {
        createMetaTag("keywords", config.keywords.joinToString(", "))
    }
    createMetaTag("author", config.author)
    createMetaTag("robots", config.robots)
    createMetaTag("viewport", "width=device-width, initial-scale=1.0, viewport-fit=cover")
    createMetaTag("theme-color", "#6B21A8") // Khoded Purple
    createMetaTag("msapplication-TileColor", "#6B21A8")
    
    // Performance and rendering hints
    createMetaTag("format-detection", "telephone=no")
    createMetaTag("mobile-web-app-capable", "yes")
    createMetaTag("apple-mobile-web-app-capable", "yes")
    createMetaTag("apple-mobile-web-app-status-bar-style", "default")
    createMetaTag("apple-mobile-web-app-title", "Khoded")
    
    // Security headers
    createMetaTag("referrer", "strict-origin-when-cross-origin")
    createMetaTag("X-Content-Type-Options", "nosniff")
    createMetaTag("X-Frame-Options", "DENY")
    createMetaTag("X-XSS-Protection", "1; mode=block")
    
    // Open Graph tags
    createMetaProperty("og:title", config.title)
    createMetaProperty("og:description", config.description)
    createMetaProperty("og:type", config.ogType)
    createMetaProperty("og:url", config.canonicalUrl ?: currentUrl)
    createMetaProperty("og:site_name", siteName)
    createMetaProperty("og:locale", "en_US")
    
    config.ogImage?.let { image ->
        createMetaProperty("og:image", image)
        createMetaProperty("og:image:width", "1200")
        createMetaProperty("og:image:height", "630")
        createMetaProperty("og:image:type", "image/jpeg")
        config.ogImageAlt?.let { alt ->
            createMetaProperty("og:image:alt", alt)
        }
    }
    
    // Twitter Card tags
    createMetaProperty("twitter:card", config.twitterCard)
    createMetaProperty("twitter:title", config.title)
    createMetaProperty("twitter:description", config.description)
    createMetaProperty("twitter:site", "@khoded")
    createMetaProperty("twitter:creator", "@khoded")
    
    config.ogImage?.let { image ->
        createMetaProperty("twitter:image", image)
        config.ogImageAlt?.let { alt ->
            createMetaProperty("twitter:image:alt", alt)
        }
    }
    
    // Article specific meta tags
    config.publishedTime?.let { time ->
        createMetaProperty("article:published_time", time)
    }
    
    config.lastModified?.let { time ->
        createMetaProperty("article:modified_time", time)
    }
    
    // Canonical URL
    config.canonicalUrl?.let { url ->
        createLinkTag("canonical", url)
    }
    
    // Alternate language versions
    config.alternateLanguages.forEach { (lang, url) ->
        createLinkTag("alternate", url, mapOf("hreflang" to lang))
    }
    
    // Preconnect to external domains for performance
    createLinkTag("preconnect", "https://fonts.googleapis.com")
    createLinkTag("preconnect", "https://fonts.gstatic.com", mapOf("crossorigin" to ""))
    createLinkTag("dns-prefetch", "https://www.google-analytics.com")
}

private fun createMetaTag(name: String, content: String) {
    val meta = document.createElement("meta") as HTMLMetaElement
    meta.name = name
    meta.content = content
    document.head?.appendChild(meta)
}

private fun createMetaProperty(property: String, content: String) {
    val meta = document.createElement("meta") as HTMLMetaElement
    meta.setAttribute("property", property)
    meta.content = content
    document.head?.appendChild(meta)
}

private fun createLinkTag(rel: String, href: String, attributes: Map<String, String> = emptyMap()) {
    val link = document.createElement("link")
    link.setAttribute("rel", rel)
    link.setAttribute("href", href)
    attributes.forEach { (key, value) ->
        link.setAttribute(key, value)
    }
    document.head?.appendChild(link)
}

private fun addStructuredData(data: Map<String, Any>) {
    val script = document.createElement("script")
    script.setAttribute("type", "application/ld+json")
    script.textContent = kotlinx.serialization.json.Json.encodeToString(
        kotlinx.serialization.json.JsonElement.serializer(), 
        kotlinx.serialization.json.JsonObject(data.mapValues { 
            kotlinx.serialization.json.JsonPrimitive(it.value.toString()) 
        })
    )
    document.head?.appendChild(script)
}

private fun addDefaultStructuredData(config: SEOConfig, currentUrl: String) {
    val organizationData = mapOf(
        "@context" to "https://schema.org",
        "@type" to "Organization",
        "name" to "Khoded",
        "description" to "Professional web development, hosting, and branding services",
        "url" to "https://khoded.com",
        "logo" to "https://khoded.com/images/logo.png",
        "email" to "admin@khoded.com",
        "telephone" to "+1-833-454-6333",
        "address" to mapOf(
            "@type" to "PostalAddress",
            "addressCountry" to "US"
        ),
        "sameAs" to listOf(
            "https://linkedin.com/company/khoded",
            "https://twitter.com/khoded"
        ),
        "foundingDate" to "2020",
        "contactPoint" to mapOf(
            "@type" to "ContactPoint",
            "telephone" to "+1-833-454-6333",
            "contactType" to "customer service",
            "email" to "admin@khoded.com"
        )
    )
    
    val websiteData = mapOf(
        "@context" to "https://schema.org",
        "@type" to "WebSite",
        "name" to "Khoded",
        "url" to "https://khoded.com",
        "description" to config.description,
        "publisher" to mapOf(
            "@type" to "Organization",
            "name" to "Khoded"
        ),
        "potentialAction" to mapOf(
            "@type" to "SearchAction",
            "target" to mapOf(
                "@type" to "EntryPoint",
                "urlTemplate" to "https://khoded.com/search?q={search_term_string}"
            ),
            "query-input" to "required name=search_term_string"
        )
    )
    
    val serviceData = mapOf(
        "@context" to "https://schema.org",
        "@type" to "ProfessionalService",
        "name" to "Khoded Web Development Services",
        "description" to "Professional web development, hosting, and branding services that help businesses grow online",
        "provider" to mapOf(
            "@type" to "Organization",
            "name" to "Khoded"
        ),
        "areaServed" to "US",
        "hasOfferCatalog" to mapOf(
            "@type" to "OfferCatalog",
            "name" to "Web Development Services",
            "itemListElement" to listOf(
                mapOf(
                    "@type" to "Offer",
                    "itemOffered" to mapOf(
                        "@type" to "Service",
                        "name" to "Custom Web Development",
                        "description" to "Custom web application development using modern technologies"
                    )
                ),
                mapOf(
                    "@type" to "Offer", 
                    "itemOffered" to mapOf(
                        "@type" to "Service",
                        "name" to "Web Hosting",
                        "description" to "Secure and reliable web hosting solutions"
                    )
                ),
                mapOf(
                    "@type" to "Offer",
                    "itemOffered" to mapOf(
                        "@type" to "Service", 
                        "name" to "Branding & SEO",
                        "description" to "Brand development and search engine optimization services"
                    )
                )
            )
        )
    )
    
    // Add all structured data
    addStructuredData(organizationData)
    addStructuredData(websiteData)
    addStructuredData(serviceData)
}

// Predefined SEO configurations for different pages
object SEOConfigs {
    fun homePage() = SEOConfig(
        title = "Khoded - Professional Web Development Services",
        description = "Transform your business with professional web development, secure hosting, and strategic branding services. Get a free consultation today.",
        keywords = listOf(
            "web development", "custom websites", "web hosting", "branding", "SEO", 
            "responsive design", "e-commerce", "web applications", "digital marketing"
        ),
        ogImage = "https://khoded.com/images/og-home.jpg",
        ogImageAlt = "Khoded professional web development services",
        ogType = "website"
    )
    
    fun servicesPage() = SEOConfig(
        title = "Web Development Services - Khoded",
        description = "Comprehensive web development services including custom applications, responsive design, e-commerce solutions, and ongoing maintenance.",
        keywords = listOf(
            "web development services", "custom web applications", "responsive web design",
            "e-commerce development", "website maintenance", "web consulting"
        ),
        ogImage = "https://khoded.com/images/og-services.jpg",
        ogImageAlt = "Professional web development services by Khoded",
        ogType = "website"
    )
    
    fun aboutPage() = SEOConfig(
        title = "About Khoded - Expert Web Development Team",
        description = "Learn about Khoded's experienced web development team, our mission to deliver exceptional digital solutions, and our commitment to client success.",
        keywords = listOf(
            "about khoded", "web development team", "company story", "expert developers",
            "digital solutions", "client success"
        ),
        ogImage = "https://khoded.com/images/og-about.jpg",
        ogImageAlt = "Khoded web development team",
        ogType = "website"
    )
    
    fun contactPage() = SEOConfig(
        title = "Contact Khoded - Get Your Free Consultation",
        description = "Ready to start your web development project? Contact Khoded today for a free consultation and discover how we can help your business grow online.",
        keywords = listOf(
            "contact khoded", "web development consultation", "get quote",
            "start project", "business consultation"
        ),
        ogImage = "https://khoded.com/images/og-contact.jpg",
        ogImageAlt = "Contact Khoded for web development services",
        ogType = "website"
    )
}