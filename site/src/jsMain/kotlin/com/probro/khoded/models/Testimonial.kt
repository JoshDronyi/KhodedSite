package com.probro.khoded.models

/**
 * Represents a customer testimonial with rating and attribution information.
 *
 * This data class encapsulates all the information needed to display customer
 * testimonials throughout the application, supporting social proof and credibility.
 *
 * @property id Unique identifier for the testimonial. Used for database operations
 *             and tracking purposes. Defaults to "0" for placeholder content.
 * @property review The testimonial text content. Should be authentic customer feedback
 *                 that highlights the value and quality of services provided.
 * @property rating Customer satisfaction rating on a scale of 0-5 stars.
 *                 Used for displaying star ratings and calculating averages.
 * @property from The name of the person providing the testimonial.
 *              Should be a real customer name (with permission) or anonymized.
 * @property position The job title or role of the testimonial provider.
 *                   Adds credibility and context to the testimonial.
 * @property organization The company or organization the testimonial provider represents.
 *                       Helps establish credibility and provides business context.
 *
 * @since 1.0.0
 * @see com.probro.khoded.pages.homeSections.Testimonial for UI implementation
 *
 * Example usage:
 * ```kotlin
 * val testimonial = Testimonial(
 *     id = "customer-001",
 *     review = "Exceptional web development service with attention to detail.",
 *     rating = 5,
 *     from = "John Smith",
 *     position = "CEO",
 *     organization = "Tech Innovations LLC"
 * )
 * ```
 */
data class Testimonial(
    val id: String = "0",
    val review: String = "Errr mahh gawd, dey good",
    val rating: Int = (0..5).random(),
    val from: String = "Yo mama",
    val position: String = "Final word for everything.",
    val organization: String = "PlaceForCoolThings LLC"
)
