package com.probro.khoded.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Google Cloud Service Account Configuration
 * 
 * This class loads service account credentials from Docker secrets in production
 * or from environment variables in development. It properly handles the full
 * Google Cloud Service Account JSON key file format.
 */

@Serializable
data class ServiceAccountCredentials(
    val type: String,
    val project_id: String,
    val private_key_id: String,
    val private_key: String,
    val client_email: String,
    val client_id: String,
    val auth_uri: String,
    val token_uri: String,
    val auth_provider_x509_cert_url: String,
    val client_x509_cert_url: String,
    val universe_domain: String? = "googleapis.com"
)

object ServiceAccountConfig {
    
    private var _credentials: ServiceAccountCredentials? = null
    
    /**
     * Get service account credentials, loading them if not already cached
     */
    val credentials: ServiceAccountCredentials
        get() = _credentials ?: loadCredentials().also { _credentials = it }
    
    /**
     * Load service account credentials from Docker secrets or environment
     */
    private fun loadCredentials(): ServiceAccountCredentials {
        return try {
            // Try to load from Docker secrets (production)
            loadFromDockerSecrets()
        } catch (e: Exception) {
            try {
                // Fall back to environment variables (development)
                loadFromEnvironmentVariables()
            } catch (e2: Exception) {
                // Fall back to build configuration (if available)
                loadFromBuildConfig()
            }
        }
    }
    
    /**
     * Load from Docker secrets (production deployment)
     */
    private fun loadFromDockerSecrets(): ServiceAccountCredentials {
        val serviceAccountFile = File("/run/secrets/gmail_service_account_key")
        if (!serviceAccountFile.exists()) {
            throw IllegalStateException("Service account key file not found at /run/secrets/gmail_service_account_key")
        }
        
        val serviceAccountJson = serviceAccountFile.readText().trim()
        return Json.decodeFromString<ServiceAccountCredentials>(serviceAccountJson)
    }
    
    /**
     * Load from environment variables (development)
     */
    private fun loadFromEnvironmentVariables(): ServiceAccountCredentials {
        val serviceAccountJson = System.getenv("GOOGLE_SERVICE_ACCOUNT_JSON")
            ?: throw IllegalStateException("GOOGLE_SERVICE_ACCOUNT_JSON environment variable not set")
        
        return Json.decodeFromString<ServiceAccountCredentials>(serviceAccountJson)
    }
    
    /**
     * Load from build configuration (fallback)
     */
    private fun loadFromBuildConfig(): ServiceAccountCredentials {
        // This would use the existing KhodedConfig values if they're set
        // For now, throw an error to indicate proper configuration is needed
        throw IllegalStateException(
            "No service account credentials found. Please provide either:\n" +
            "1. Docker secret at /run/secrets/gmail_service_account_key (production)\n" +
            "2. GOOGLE_SERVICE_ACCOUNT_JSON environment variable (development)\n" +
            "3. Configure credentials in build.gradle.kts"
        )
    }
    
    /**
     * Validate that credentials are properly configured
     */
    fun validateCredentials(): List<String> {
        val issues = mutableListOf<String>()
        
        try {
            val creds = credentials
            
            if (creds.type != "service_account") {
                issues.add("Invalid credential type: ${creds.type}. Expected 'service_account'")
            }
            
            if (creds.project_id.isBlank()) {
                issues.add("project_id is empty")
            }
            
            if (creds.private_key.isBlank()) {
                issues.add("private_key is empty")
            }
            
            if (creds.client_email.isBlank()) {
                issues.add("client_email is empty")
            }
            
            if (!creds.client_email.endsWith(".gserviceaccount.com")) {
                issues.add("client_email should end with .gserviceaccount.com")
            }
            
            // Validate private key format
            if (!creds.private_key.contains("BEGIN PRIVATE KEY")) {
                issues.add("private_key should contain '-----BEGIN PRIVATE KEY-----'")
            }
            
        } catch (e: Exception) {
            issues.add("Failed to load credentials: ${e.message}")
        }
        
        return issues
    }
    
    /**
     * Get a summary of the loaded configuration (without sensitive data)
     */
    fun getConfigSummary(): String {
        return try {
            val creds = credentials
            buildString {
                appendLine("Google Cloud Service Account Configuration:")
                appendLine("  Type: ${creds.type}")
                appendLine("  Project ID: ${creds.project_id}")
                appendLine("  Client Email: ${creds.client_email}")
                appendLine("  Private Key ID: ${creds.private_key_id}")
                appendLine("  Auth URI: ${creds.auth_uri}")
                appendLine("  Token URI: ${creds.token_uri}")
                appendLine("  Universe Domain: ${creds.universe_domain}")
                appendLine("  Private Key: [LOADED - ${creds.private_key.length} characters]")
            }
        } catch (e: Exception) {
            "Configuration Error: ${e.message}"
        }
    }
}

/**
 * Extension to integrate with existing KhodedConfig for backward compatibility
 */
object GmailConfig {
    val clientEmail: String get() = ServiceAccountConfig.credentials.client_email
    val privateKey: String get() = ServiceAccountConfig.credentials.private_key
    val projectId: String get() = ServiceAccountConfig.credentials.project_id
}