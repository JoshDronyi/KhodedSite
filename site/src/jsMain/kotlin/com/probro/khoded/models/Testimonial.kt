package com.probro.khoded.models

data class Testimonial(
    val id: String = "0",
    val review: String = "Errr mahh gawd, dey good",
    val rating: Int = (0..5).random(),
    val from: String = "Yo mama",
    val position: String = "Final word for everything.",
    val organization: String = "PlaceForCoolThings LLC"
)
