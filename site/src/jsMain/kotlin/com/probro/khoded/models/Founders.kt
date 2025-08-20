package com.probro.khoded.models

/**
 * Khoded company founders - Esther and Joshua Dronyi
 */
data class Founder(
    val name: String,
    val title: String,
    val role: String,
    val bio: String = ""
)

/**
 * Company founders information
 */
object Founders {
    val ESTHER = Founder(
        name = "Esther Dronyi",
        title = "CEO & Co-Founder",
        role = "Chief Executive Officer",
        bio = "Visionary leader driving Khoded's mission to transform Connecticut businesses with Kotlin Multiplatform technology."
    )
    
    val JOSHUA = Founder(
        name = "Joshua Dronyi", 
        title = "CTO & Co-Founder",
        role = "Chief Technology Officer",
        bio = "Technical architect specializing in Kotlin Multiplatform solutions for Finance, Healthcare & Manufacturing."
    )
    
    val ALL = listOf(ESTHER, JOSHUA)
}