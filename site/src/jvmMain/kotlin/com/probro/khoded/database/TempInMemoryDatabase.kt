package com.probro.khoded.database

import com.probro.khoded.FormAnswerDTO
import com.probro.khoded.IntakeFormDTO
import com.varabyte.kobweb.api.log.Logger
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * TempInMemoryDatabase - Temporary in-memory storage solution
 * 
 * This replaces the broken SqlX4kDatabase implementation that was causing
 * Gradle daemon crashes due to sqlx4k's lack of JVM support.
 * 
 * Features:
 * - Thread-safe in-memory storage
 * - No external dependencies 
 * - Same API interface as the intended database solution
 * - Suitable for development and testing
 * 
 * IMPORTANT: This is a temporary solution. Data is not persisted between
 * application restarts. For production, migrate to SQLDelight + PostgreSQL.
 * 
 * @since 2.0.0 (Hotfix for sqlx4k JVM compatibility issue)
 */
class TempInMemoryDatabase(
    private val logger: Logger
) {
    
    // Thread-safe in-memory storage
    private val clientMessages = ConcurrentHashMap<String, ClientMessageRecord>()
    private val projectRequests = ConcurrentHashMap<String, ProjectRequestRecord>()
    private val formAnswers = ConcurrentHashMap<String, MutableList<FormAnswerRecord>>()
    
    private val dbScope = CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
        logger.error("Database operation failed: ${throwable.message}")
        throwable.printStackTrace()
    })
    
    /**
     * Initialize database (no-op for in-memory storage)
     */
    suspend fun initializeTables() {
        logger.info("Initializing in-memory database tables")
        // No initialization needed for in-memory storage
        logger.info("In-memory database initialized successfully")
    }
    
    /**
     * Save a simple contact message
     */
    suspend fun saveMessage(
        fromName: String,
        organization: String,
        message: String
    ): String = withContext(Dispatchers.IO) {
        try {
            logger.info("Saving client message from $fromName")
            
            val messageId = UUID.randomUUID().toString()
            val record = ClientMessageRecord(
                id = messageId,
                fromName = fromName,
                organization = organization,
                message = message,
                createdAt = System.currentTimeMillis().toString()
            )
            
            clientMessages[messageId] = record
            
            logger.info("Client message saved successfully with ID: $messageId")
            messageId
            
        } catch (e: Exception) {
            logger.error("Failed to save client message: ${e.message}")
            throw e
        }
    }
    
    /**
     * Save a project request form
     */
    suspend fun saveProjectRequest(
        intakeFormDTO: IntakeFormDTO
    ): String = withContext(Dispatchers.IO) {
        try {
            logger.info("Saving project request for ${intakeFormDTO.organization}")
            
            val requestId = UUID.randomUUID().toString()
            val requesterName = getRequesterName(intakeFormDTO)
            
            // Store the main request
            val requestRecord = ProjectRequestRecord(
                id = requestId,
                requester = requesterName,
                organization = intakeFormDTO.organization ?: "Unknown",
                budgetAmount = "0.00", // Using string representation for serialization
                createdAt = System.currentTimeMillis().toString()
            )
            
            projectRequests[requestId] = requestRecord
            
            // Store individual answers for easier querying
            val allAnswers = collectAllAnswers(intakeFormDTO)
            val answerRecords = mutableListOf<FormAnswerRecord>()
            
            allAnswers.forEach { (sectionName, answers) ->
                answers.forEach { answer ->
                    val answerRecord = FormAnswerRecord(
                        id = UUID.randomUUID().toString(),
                        requestId = requestId,
                        questionText = answer.questionText,
                        answerValue = answer.answerValue,
                        sectionName = sectionName,
                        createdAt = System.currentTimeMillis().toString()
                    )
                    answerRecords.add(answerRecord)
                }
            }
            
            formAnswers[requestId] = answerRecords
            
            logger.info("Project request saved successfully with ID: $requestId")
            requestId
            
        } catch (e: Exception) {
            logger.error("Failed to save project request: ${e.message}")
            throw e
        }
    }
    
    /**
     * Retrieve recent client messages
     */
    suspend fun getRecentMessages(limit: Int = 10): List<ClientMessageRecord> = withContext(Dispatchers.IO) {
        try {
            clientMessages.values
                .sortedByDescending { it.createdAt }
                .take(limit)
                
        } catch (e: Exception) {
            logger.error("Failed to retrieve messages: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Retrieve project requests
     */
    suspend fun getProjectRequests(limit: Int = 10): List<ProjectRequestRecord> = withContext(Dispatchers.IO) {
        try {
            projectRequests.values
                .sortedByDescending { it.createdAt }
                .take(limit)
                
        } catch (e: Exception) {
            logger.error("Failed to retrieve project requests: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Health check method
     */
    suspend fun healthCheck(): Boolean = withContext(Dispatchers.IO) {
        try {
            // Simple health check - verify data structures are accessible
            clientMessages.size >= 0 && projectRequests.size >= 0
        } catch (e: Exception) {
            logger.error("Database health check failed: ${e.message}")
            false
        }
    }
    
    /**
     * Get storage statistics (useful for monitoring)
     */
    suspend fun getStorageStats(): Map<String, Int> = withContext(Dispatchers.IO) {
        mapOf(
            "clientMessages" to clientMessages.size,
            "projectRequests" to projectRequests.size,
            "formAnswers" to formAnswers.values.sumOf { it.size }
        )
    }
    
    /**
     * Clear all data (useful for testing)
     */
    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        clientMessages.clear()
        projectRequests.clear()
        formAnswers.clear()
        logger.info("All in-memory data cleared")
    }
    
    /**
     * Close database connections (no-op for in-memory)
     */
    suspend fun close() {
        try {
            dbScope.cancel()
            logger.info("In-memory database closed successfully")
        } catch (e: Exception) {
            logger.error("Error closing in-memory database: ${e.message}")
        }
    }
    
    // Helper methods
    
    private fun getRequesterName(intakeFormDTO: IntakeFormDTO): String {
        return intakeFormDTO.organization 
            ?: intakeFormDTO.contactFormAnswers?.firstOrNull()?.answerValue 
            ?: "Unknown"
    }
    
    private fun collectAllAnswers(intakeFormDTO: IntakeFormDTO): Map<String, List<FormAnswerDTO>> {
        return mapOf(
            "contact" to (intakeFormDTO.contactFormAnswers ?: emptyList()),
            "project_overview" to (intakeFormDTO.projectOverviewAnswers ?: emptyList()),
            "design_branding" to (intakeFormDTO.designBrandingAnswers ?: emptyList()),
            "content_imagery" to (intakeFormDTO.contentImageryAnswers ?: emptyList()),
            "timeline_budget" to (intakeFormDTO.timelineBudgetAnswers ?: emptyList()),
            "maintenance_updates" to (intakeFormDTO.maintenanceUpdatesAnswers ?: emptyList()),
            "structure_functionality" to (intakeFormDTO.structureFunctionalityAnswers ?: emptyList()),
            "additional_info" to (intakeFormDTO.additionalInfo ?: emptyList())
        ).filterValues { it.isNotEmpty() }
    }
}


