package com.probro.khoded.constants

/**
 * Application-wide constants for the Khoded project.
 * Contains magic numbers and hardcoded values extracted for maintainability.
 */
object KhodedConstants {
    
    /** Rate limiting and timeout constants */
    object RateLimiting {
        const val RATE_LIMIT_WINDOW_MS = 15 * 60 * 1000L // 15 minutes
        const val MAX_REQUESTS_PER_WINDOW = 5
        const val REQUEST_TIMEOUT_MS = 30 * 1000L // 30 seconds
        const val MAX_RETRY_ATTEMPTS = 3
        const val MAX_DELAY_MS = 32_000L // 32 seconds
    }
    
    /** Email and messaging constants */
    object Email {
        const val MAX_MESSAGE_LENGTH = 1000
        const val MAX_SUBJECT_LENGTH = 200
        const val EMAIL_VALIDATION_TIMEOUT_MS = 5_000L
    }
    
    /** Performance and monitoring constants */
    object Performance {
        const val PERFORMANCE_SAMPLE_RATE = 0.1 // 10% sampling
        const val METRICS_BATCH_SIZE = 100
        const val METRICS_FLUSH_INTERVAL_MS = 30_000L // 30 seconds
    }
    
    /** UI and interaction constants */
    object UI {
        const val ANIMATION_DURATION_MS = 300L
        const val DEBOUNCE_DELAY_MS = 500L
        const val SCROLL_THROTTLE_MS = 16L // ~60fps
    }
    
    /** Security constants */
    object Security {
        const val TOKEN_EXPIRY_BUFFER_MS = 5 * 60 * 1000L // 5 minutes
        const val MAX_LOGIN_ATTEMPTS = 5
        const val LOCKOUT_DURATION_MS = 30 * 60 * 1000L // 30 minutes
    }
}