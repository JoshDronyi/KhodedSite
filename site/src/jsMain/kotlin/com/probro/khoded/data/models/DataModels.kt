package com.probro.khoded.data.models

import kotlinx.serialization.*

/**
 * Type-Safe Data Models - Simplified Stub Implementation
 * 
 * This is a simplified stub to resolve compilation issues.
 * Full implementation would require kotlinx-datetime and proper serialization.
 */

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val createdAt: String = js("new Date().toISOString()").toString()
)

@Serializable
data class Project(
    val id: String,
    val title: String,
    val description: String,
    val status: ProjectStatus = ProjectStatus.ACTIVE,
    val createdAt: String = js("new Date().toISOString()").toString()
)

@Serializable
enum class ProjectStatus {
    ACTIVE, COMPLETED, PAUSED, CANCELLED
}

@Serializable
data class Contact(
    val id: String,
    val name: String,
    val email: String,
    val message: String,
    val createdAt: String = js("new Date().toISOString()").toString()
)

@Serializable
data class Service(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val active: Boolean = true
)

@Serializable
data class NewsletterSubscription(
    val id: String,
    val email: String,
    val subscribedAt: String = js("new Date().toISOString()").toString(),
    val active: Boolean = true
)