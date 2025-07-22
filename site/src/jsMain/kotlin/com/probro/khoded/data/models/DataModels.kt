package com.probro.khoded.data.models

import kotlinx.serialization.*
import kotlinx.datetime.*

/**
 * Type-Safe Data Models
 * 
 * Comprehensive data model definitions with:
 * - Kotlinx Serialization for JSON handling
 * - Validation rules and constraints
 * - Immutable data structures
 * - Type-safe API interfaces
 * - Error handling models
 */

// =============================================================================
// CORE BUSINESS MODELS
// =============================================================================

@Serializable
data class ContactFormData(
    val fullName: String,
    val email: String,
    val phoneNumber: String? = null,
    val company: String? = null,
    val projectType: ProjectType,
    val budget: BudgetRange? = null,
    val timeline: Timeline? = null,
    val message: String,
    val hearAboutUs: String? = null,
    val allowMarketing: Boolean = false,
    val submittedAt: Instant = Clock.System.now()
) {
    fun validate(): ValidationResult<ContactFormData> {
        val errors = mutableListOf<ValidationError>()
        
        // Name validation
        if (fullName.isBlank()) {
            errors.add(ValidationError("fullName", "Full name is required"))
        } else if (fullName.length < 2) {
            errors.add(ValidationError("fullName", "Full name must be at least 2 characters"))
        } else if (fullName.length > 100) {
            errors.add(ValidationError("fullName", "Full name must be less than 100 characters"))
        }
        
        // Email validation
        if (email.isBlank()) {
            errors.add(ValidationError("email", "Email is required"))
        } else if (!isValidEmail(email)) {
            errors.add(ValidationError("email", "Please enter a valid email address"))
        }
        
        // Phone validation (if provided)
        phoneNumber?.let { phone ->
            if (phone.isNotBlank() && !isValidPhoneNumber(phone)) {
                errors.add(ValidationError("phoneNumber", "Please enter a valid phone number"))
            }
        }
        
        // Message validation
        if (message.isBlank()) {
            errors.add(ValidationError("message", "Project description is required"))
        } else if (message.length < 10) {
            errors.add(ValidationError("message", "Please provide more details about your project (at least 10 characters)"))
        } else if (message.length > 2000) {
            errors.add(ValidationError("message", "Project description must be less than 2000 characters"))
        }
        
        // Company name validation (if provided)
        company?.let { comp ->
            if (comp.length > 100) {
                errors.add(ValidationError("company", "Company name must be less than 100 characters"))
            }
        }
        
        return if (errors.isEmpty()) {
            ValidationResult.Success(this)
        } else {
            ValidationResult.Error(errors)
        }
    }
}

@Serializable
enum class ProjectType(val displayName: String, val description: String) {
    @SerialName("web_development")
    WEB_DEVELOPMENT("Web Development", "Custom websites and web applications"),
    
    @SerialName("ecommerce")
    ECOMMERCE("E-Commerce", "Online stores and marketplace platforms"),
    
    @SerialName("mobile_app")
    MOBILE_APP("Mobile App", "iOS and Android mobile applications"),
    
    @SerialName("redesign")
    REDESIGN("Website Redesign", "Modernizing existing websites"),
    
    @SerialName("maintenance")
    MAINTENANCE("Maintenance & Support", "Ongoing website maintenance and updates"),
    
    @SerialName("consultation")
    CONSULTATION("Consultation", "Technical advice and project planning"),
    
    @SerialName("other")
    OTHER("Other", "Custom project requirements")
}

@Serializable
enum class BudgetRange(val displayName: String, val minAmount: Int?, val maxAmount: Int?) {
    @SerialName("under_5k")
    UNDER_5K("Under $5,000", null, 5000),
    
    @SerialName("5k_to_15k")
    RANGE_5K_15K("$5,000 - $15,000", 5000, 15000),
    
    @SerialName("15k_to_50k")
    RANGE_15K_50K("$15,000 - $50,000", 15000, 50000),
    
    @SerialName("50k_to_100k")
    RANGE_50K_100K("$50,000 - $100,000", 50000, 100000),
    
    @SerialName("over_100k")
    OVER_100K("Over $100,000", 100000, null),
    
    @SerialName("not_sure")
    NOT_SURE("Not Sure Yet", null, null)
}

@Serializable
enum class Timeline(val displayName: String, val weeks: Int?) {
    @SerialName("asap")
    ASAP("As Soon As Possible", 2),
    
    @SerialName("1_month")
    ONE_MONTH("Within 1 Month", 4),
    
    @SerialName("3_months")
    THREE_MONTHS("Within 3 Months", 12),
    
    @SerialName("6_months")
    SIX_MONTHS("Within 6 Months", 24),
    
    @SerialName("flexible")
    FLEXIBLE("Timeline is Flexible", null),
    
    @SerialName("planning")
    PLANNING("Still Planning", null)
}

// =============================================================================
// NEWSLETTER SUBSCRIPTION MODEL
// =============================================================================

@Serializable
data class NewsletterSubscription(
    val email: String,
    val firstName: String? = null,
    val interests: List<NewsletterInterest> = emptyList(),
    val source: String? = null,
    val subscribedAt: Instant = Clock.System.now(),
    val isActive: Boolean = true,
    val doubleOptIn: Boolean = false
) {
    fun validate(): ValidationResult<NewsletterSubscription> {
        val errors = mutableListOf<ValidationError>()
        
        if (email.isBlank()) {
            errors.add(ValidationError("email", "Email is required"))
        } else if (!isValidEmail(email)) {
            errors.add(ValidationError("email", "Please enter a valid email address"))
        }
        
        firstName?.let { name ->
            if (name.length > 50) {
                errors.add(ValidationError("firstName", "First name must be less than 50 characters"))
            }
        }
        
        return if (errors.isEmpty()) {
            ValidationResult.Success(this)
        } else {
            ValidationResult.Error(errors)
        }
    }
}

@Serializable
enum class NewsletterInterest(val displayName: String) {
    @SerialName("web_development")
    WEB_DEVELOPMENT("Web Development Tips"),
    
    @SerialName("design_trends")
    DESIGN_TRENDS("Design Trends"),
    
    @SerialName("business_growth")
    BUSINESS_GROWTH("Business Growth"),
    
    @SerialName("technology")
    TECHNOLOGY("Technology Updates"),
    
    @SerialName("case_studies")
    CASE_STUDIES("Case Studies"),
    
    @SerialName("industry_news")
    INDUSTRY_NEWS("Industry News")
}

// =============================================================================
// PORTFOLIO AND CASE STUDY MODELS
// =============================================================================

@Serializable
data class CaseStudy(
    val id: String,
    val title: String,
    val slug: String,
    val client: Client,
    val projectType: ProjectType,
    val description: String,
    val challenge: String,
    val solution: String,
    val results: List<ProjectResult>,
    val technologies: List<Technology>,
    val timeline: ProjectTimeline,
    val images: List<ProjectImage>,
    val testimonial: ClientTestimonial?,
    val isPublished: Boolean = true,
    val isFeatured: Boolean = false,
    val publishedAt: Instant?,
    val updatedAt: Instant = Clock.System.now()
)

@Serializable
data class Client(
    val name: String,
    val industry: Industry,
    val website: String? = null,
    val logo: String? = null,
    val size: CompanySize
)

@Serializable
enum class Industry(val displayName: String) {
    @SerialName("technology")
    TECHNOLOGY("Technology"),
    
    @SerialName("healthcare")
    HEALTHCARE("Healthcare"),
    
    @SerialName("finance")
    FINANCE("Finance"),
    
    @SerialName("ecommerce")
    ECOMMERCE("E-Commerce"),
    
    @SerialName("education")
    EDUCATION("Education"),
    
    @SerialName("nonprofit")
    NONPROFIT("Non-Profit"),
    
    @SerialName("manufacturing")
    MANUFACTURING("Manufacturing"),
    
    @SerialName("real_estate")
    REAL_ESTATE("Real Estate"),
    
    @SerialName("hospitality")
    HOSPITALITY("Hospitality"),
    
    @SerialName("other")
    OTHER("Other")
}

@Serializable
enum class CompanySize(val displayName: String) {
    @SerialName("startup")
    STARTUP("Startup (1-10 employees)"),
    
    @SerialName("small")
    SMALL("Small Business (11-50 employees)"),
    
    @SerialName("medium")
    MEDIUM("Medium Business (51-200 employees)"),
    
    @SerialName("large")
    LARGE("Large Business (201-1000 employees)"),
    
    @SerialName("enterprise")
    ENTERPRISE("Enterprise (1000+ employees)")
}

@Serializable
data class ProjectResult(
    val metric: String,
    val value: String,
    val improvement: String? = null,
    val description: String? = null
)

@Serializable
data class Technology(
    val name: String,
    val category: TechCategory,
    val icon: String? = null,
    val color: String? = null
)

@Serializable
enum class TechCategory(val displayName: String) {
    @SerialName("frontend")
    FRONTEND("Frontend"),
    
    @SerialName("backend")
    BACKEND("Backend"),
    
    @SerialName("database")
    DATABASE("Database"),
    
    @SerialName("cloud")
    CLOUD("Cloud & DevOps"),
    
    @SerialName("mobile")
    MOBILE("Mobile"),
    
    @SerialName("design")
    DESIGN("Design & UX"),
    
    @SerialName("analytics")
    ANALYTICS("Analytics"),
    
    @SerialName("other")
    OTHER("Other")
}

@Serializable
data class ProjectTimeline(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val phases: List<ProjectPhase>
)

@Serializable
data class ProjectPhase(
    val name: String,
    val duration: String,
    val deliverables: List<String>
)

@Serializable
data class ProjectImage(
    val url: String,
    val thumbnailUrl: String? = null,
    val alt: String,
    val caption: String? = null,
    val type: ImageType,
    val order: Int = 0
)

@Serializable
enum class ImageType(val displayName: String) {
    @SerialName("hero")
    HERO("Hero Image"),
    
    @SerialName("desktop")
    DESKTOP("Desktop Screenshot"),
    
    @SerialName("mobile")
    MOBILE("Mobile Screenshot"),
    
    @SerialName("mockup")
    MOCKUP("Design Mockup"),
    
    @SerialName("before")
    BEFORE("Before Image"),
    
    @SerialName("after")
    AFTER("After Image"),
    
    @SerialName("process")
    PROCESS("Process Image")
}

@Serializable
data class ClientTestimonial(
    val quote: String,
    val author: TestimonialAuthor,
    val rating: Int, // 1-5 stars
    val isVerified: Boolean = false
) {
    fun validate(): ValidationResult<ClientTestimonial> {
        val errors = mutableListOf<ValidationError>()
        
        if (quote.isBlank()) {
            errors.add(ValidationError("quote", "Testimonial quote is required"))
        } else if (quote.length > 1000) {
            errors.add(ValidationError("quote", "Testimonial must be less than 1000 characters"))
        }
        
        if (rating !in 1..5) {
            errors.add(ValidationError("rating", "Rating must be between 1 and 5"))
        }
        
        val authorValidation = author.validate()
        if (authorValidation is ValidationResult.Error) {
            errors.addAll(authorValidation.errors.map { 
                ValidationError("author.${it.field}", it.message) 
            })
        }
        
        return if (errors.isEmpty()) {
            ValidationResult.Success(this)
        } else {
            ValidationResult.Error(errors)
        }
    }
}

@Serializable
data class TestimonialAuthor(
    val name: String,
    val title: String,
    val company: String,
    val avatar: String? = null
) {
    fun validate(): ValidationResult<TestimonialAuthor> {
        val errors = mutableListOf<ValidationError>()
        
        if (name.isBlank()) {
            errors.add(ValidationError("name", "Author name is required"))
        } else if (name.length > 100) {
            errors.add(ValidationError("name", "Author name must be less than 100 characters"))
        }
        
        if (title.isBlank()) {
            errors.add(ValidationError("title", "Author title is required"))
        } else if (title.length > 100) {
            errors.add(ValidationError("title", "Author title must be less than 100 characters"))
        }
        
        if (company.isBlank()) {
            errors.add(ValidationError("company", "Company name is required"))
        } else if (company.length > 100) {
            errors.add(ValidationError("company", "Company name must be less than 100 characters"))
        }
        
        return if (errors.isEmpty()) {
            ValidationResult.Success(this)
        } else {
            ValidationResult.Error(errors)
        }
    }
}

// =============================================================================
// VALIDATION FRAMEWORK
// =============================================================================

sealed class ValidationResult<out T> {
    data class Success<T>(val data: T) : ValidationResult<T>()
    data class Error(val errors: List<ValidationError>) : ValidationResult<Nothing>()
    
    fun isValid(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    
    inline fun onSuccess(action: (T) -> Unit): ValidationResult<T> {
        if (this is Success) action(data)
        return this
    }
    
    inline fun onError(action: (List<ValidationError>) -> Unit): ValidationResult<T> {
        if (this is Error) action(errors)
        return this
    }
    
    fun getOrNull(): T? = if (this is Success) data else null
    fun getErrorsOrNull(): List<ValidationError>? = if (this is Error) errors else null
}

@Serializable
data class ValidationError(
    val field: String,
    val message: String,
    val code: String? = null
)

// Validation utility functions
private fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
    return email.matches(emailRegex) && email.length <= 254
}

private fun isValidPhoneNumber(phone: String): Boolean {
    // Remove all non-digit characters for validation
    val digitsOnly = phone.replace(Regex("[^\\d]"), "")
    // Should be 10-15 digits (international format)
    return digitsOnly.length in 10..15
}

// =============================================================================
// API RESPONSE MODELS
// =============================================================================

@Serializable
sealed class ApiResponse<out T> {
    @Serializable
    @SerialName("success")
    data class Success<T>(
        val data: T,
        val message: String? = null,
        val timestamp: Instant = Clock.System.now()
    ) : ApiResponse<T>()
    
    @Serializable
    @SerialName("error")
    data class Error(
        val message: String,
        val code: String? = null,
        val details: Map<String, String>? = null,
        val timestamp: Instant = Clock.System.now()
    ) : ApiResponse<Nothing>()
}

@Serializable
data class PaginatedResponse<T>(
    val data: List<T>,
    val pagination: PaginationInfo
)

@Serializable
data class PaginationInfo(
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int,
    val hasNext: Boolean,
    val hasPrev: Boolean
)

// =============================================================================
// FORM STATE MANAGEMENT
// =============================================================================

data class FormFieldState<T>(
    val value: T,
    val error: String? = null,
    val touched: Boolean = false,
    val validating: Boolean = false
) {
    val isValid: Boolean get() = error == null
    val hasError: Boolean get() = error != null && touched
}

data class FormState<T>(
    val data: T,
    val fields: Map<String, FormFieldState<*>>,
    val isSubmitting: Boolean = false,
    val submitError: String? = null,
    val submitSuccess: Boolean = false
) {
    val isValid: Boolean get() = fields.values.all { it.isValid }
    val hasErrors: Boolean get() = fields.values.any { it.hasError }
    val canSubmit: Boolean get() = isValid && !isSubmitting
}