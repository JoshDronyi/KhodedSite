package com.probro.khoded.database

import com.varabyte.kobweb.api.log.Logger
import kotlinx.serialization.Serializable

/**
 * Shared data models for database operations
 * Compatible with both PostgreSQLService and TempInMemoryDatabase
 */

@Serializable
data class ClientMessageRecord(
    val id: String,
    val fromName: String,  
    val organization: String,
    val message: String,
    val createdAt: String
)

@Serializable
data class ProjectRequestRecord(
    val id: String,
    val requester: String,
    val organization: String?,
    val budgetAmount: String,
    val createdAt: String
)

@Serializable
data class FormAnswerRecord(
    val id: String,
    val requestId: String,
    val questionText: String,
    val answerValue: String,
    val sectionName: String?,
    val createdAt: String
)

/**
 * Database interface for abstraction between PostgreSQL and temporary implementations
 */
interface DatabaseService {
    suspend fun initializeTables()
    suspend fun saveMessage(fromName: String, organization: String, message: String): String
    suspend fun saveProjectRequest(intakeFormDTO: com.probro.khoded.IntakeFormDTO): String
    suspend fun getRecentMessages(limit: Int = 10, offset: Int = 0): List<ClientMessageRecord>
    suspend fun getProjectRequests(limit: Int = 10, offset: Int = 0, status: String? = null): List<ProjectRequestRecord>
    suspend fun healthCheck(): Boolean
    suspend fun close()
}

/**
 * Global singleton for database management
 * Supports both PostgreSQL and temporary in-memory implementations
 */
object DatabaseManager {
    private var postgresInstance: PostgreSQLService? = null
    private var tempInstance: TempInMemoryDatabase? = null
    
    fun initializePostgreSQL(logger: Logger, isDevelopment: Boolean = true): PostgreSQLService {
        if (postgresInstance == null) {
            postgresInstance = PostgreSQLService(logger, isDevelopment)
        }
        return postgresInstance!!
    }
    
    fun initializeTemporary(logger: Logger): TempInMemoryDatabase {
        if (tempInstance == null) {
            tempInstance = TempInMemoryDatabase(logger)
        }
        return tempInstance!!
    }
    
    fun getPostgreSQLInstance(): PostgreSQLService? = postgresInstance
    fun getTemporaryInstance(): TempInMemoryDatabase? = tempInstance
}