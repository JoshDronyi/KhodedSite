package com.probro.khoded.database

// CRITICAL FIX: This file has been disabled due to sqlx4k JVM compatibility issues
// sqlx4k library does not support JVM targets, only Kotlin Native
// This was causing Gradle daemon crashes during compilation

/*
import com.probro.khoded.FormAnswerDTO
import com.probro.khoded.IntakeFormDTO
import com.probro.khoded.KhodedConfig
import com.varabyte.kobweb.api.log.Logger
import io.github.smyrgeorge.sqlx4k.postgres.PostgreSQLDriver
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import java.util.*

/**
 * SqlX4kDatabase - Lightweight database service using sqlx4k
 * 
 * Replaces the heavy PostgreSQL + HikariCP + Exposed stack with a single,
 * pure Kotlin multiplatform dependency that provides:
 * - Non-blocking I/O operations
 * - Direct SQL with type safety
 * - Coroutine-based async operations
 * - Single dependency vs 9 previous dependencies
 * 
 * Performance benefits:
 * - ~2.2MB+ dependency reduction
 * - No JDBC overhead
 * - Native coroutine support
 * - Better connection pooling
 * 
 * @since 2.0.0 (Performance optimization)
 */
class SqlX4kDatabase(
    private val logger: Logger,
    private val isDevelopment: Boolean = true // Since you're not saving data yet
) {
    
    private val driver by lazy {
        PostgreSQLDriver(
            host = if (isDevelopment) "localhost" else "your-prod-host",
            port = 5432,
            username = if (isDevelopment) KhodedConfig.devUsername else KhodedConfig.prodUsername,
            password = if (isDevelopment) KhodedConfig.devPassword else KhodedConfig.prodPassword,
            database = "khodedBackendData",
            schema = "khoded_base_state",
            maxConnections = 10
        )
    }
    
    private val dbScope = CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
        logger.error("Database operation failed: ${throwable.message}")
        throwable.printStackTrace()
    })
    
    /**
     * Initialize database tables (replaces Exposed's SchemaUtils)
     */
    suspend fun initializeTables() {
        try {
            logger.info("Initializing database tables with sqlx4k")
            
            // Create tables using direct SQL (much more explicit than Exposed)
            driver.execute("""
                CREATE TABLE IF NOT EXISTS client_messages (
                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                    from_name VARCHAR(50) NOT NULL,
                    organization VARCHAR(150) NOT NULL,
                    message VARCHAR(500) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """.trimIndent())
            
            driver.execute("""
                CREATE TABLE IF NOT EXISTS project_requests (
                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                    requester VARCHAR(50) NOT NULL,
                    organization VARCHAR(150),
                    budget_amount DECIMAL(10,2) DEFAULT 0.00,
                    budget_currency VARCHAR(3) DEFAULT 'USD',
                    form_data JSONB NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """.trimIndent())
            
            driver.execute("""
                CREATE TABLE IF NOT EXISTS form_answers (
                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                    request_id UUID REFERENCES project_requests(id) ON DELETE CASCADE,
                    question_text VARCHAR(300) NOT NULL,
                    answer_value VARCHAR(500) NOT NULL,
                    section_name VARCHAR(100),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """.trimIndent())
            
            // Create indexes for better performance
            driver.execute("CREATE INDEX IF NOT EXISTS idx_client_messages_created_at ON client_messages(created_at)")
            driver.execute("CREATE INDEX IF NOT EXISTS idx_project_requests_created_at ON project_requests(created_at)")
            driver.execute("CREATE INDEX IF NOT EXISTS idx_form_answers_request_id ON form_answers(request_id)")
            
            logger.info("Database tables initialized successfully")
            
        } catch (e: Exception) {
            logger.error("Failed to initialize database tables: ${e.message}")
            throw e
        }
    }
    
    /**
     * Save a simple contact message (replaces the old saveMessage)
     */
    suspend fun saveMessage(
        fromName: String,
        organization: String,
        message: String
    ): String = withContext(Dispatchers.IO) {
        try {
            logger.info("Saving client message from $fromName")
            
            val messageId = UUID.randomUUID().toString()
            
            driver.execute(
                sql = """
                    INSERT INTO client_messages (id, from_name, organization, message) 
                    VALUES (?, ?, ?, ?)
                """.trimIndent(),
                parameters = listOf(messageId, fromName, organization, message)
            )
            
            logger.info("Client message saved successfully with ID: $messageId")
            messageId
            
        } catch (e: Exception) {
            logger.error("Failed to save client message: ${e.message}")
            throw e
        }
    }
    
    /**
     * Save a project request form (simplified version of the old saveProjectRequest)
     */
    suspend fun saveProjectRequest(
        intakeFormDTO: IntakeFormDTO
    ): String = withContext(Dispatchers.IO) {
        try {
            logger.info("Saving project request for ${intakeFormDTO.organization}")
            
            val requestId = UUID.randomUUID().toString()
            val requesterName = getRequesterName(intakeFormDTO)
            
            // Store the main request
            driver.execute(
                sql = """
                    INSERT INTO project_requests (id, requester, organization, form_data) 
                    VALUES (?, ?, ?, ?::jsonb)
                """.trimIndent(),
                parameters = listOf(
                    requestId,
                    requesterName,
                    intakeFormDTO.organization ?: "Unknown",
                    Json.encodeToString(IntakeFormDTO.serializer(), intakeFormDTO)
                )
            )
            
            // Store individual answers for easier querying
            val allAnswers = collectAllAnswers(intakeFormDTO)
            allAnswers.forEach { (sectionName, answers) ->
                answers.forEach { answer ->
                    driver.execute(
                        sql = """
                            INSERT INTO form_answers (request_id, question_text, answer_value, section_name) 
                            VALUES (?, ?, ?, ?)
                        """.trimIndent(),
                        parameters = listOf(requestId, answer.questionText, answer.answerValue, sectionName)
                    )
                }
            }
            
            logger.info("Project request saved successfully with ID: $requestId")
            requestId
            
        } catch (e: Exception) {
            logger.error("Failed to save project request: ${e.message}")
            throw e
        }
    }
    
    /**
     * Retrieve recent client messages (example query method)
     */
    suspend fun getRecentMessages(limit: Int = 10): List<ClientMessageRecord> = withContext(Dispatchers.IO) {
        try {
            val results = driver.query(
                sql = """
                    SELECT id, from_name, organization, message, created_at 
                    FROM client_messages 
                    ORDER BY created_at DESC 
                    LIMIT ?
                """.trimIndent(),
                parameters = listOf(limit)
            )
            
            results.map { row ->
                ClientMessageRecord(
                    id = row["id"] as String,
                    fromName = row["from_name"] as String,
                    organization = row["organization"] as String,
                    message = row["message"] as String,
                    createdAt = row["created_at"] as String
                )
            }
            
        } catch (e: Exception) {
            logger.error("Failed to retrieve messages: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Retrieve project requests (example query method)
     */
    suspend fun getProjectRequests(limit: Int = 10): List<ProjectRequestRecord> = withContext(Dispatchers.IO) {
        try {
            val results = driver.query(
                sql = """
                    SELECT id, requester, organization, budget_amount, created_at 
                    FROM project_requests 
                    ORDER BY created_at DESC 
                    LIMIT ?
                """.trimIndent(),
                parameters = listOf(limit)
            )
            
            results.map { row ->
                ProjectRequestRecord(
                    id = row["id"] as String,
                    requester = row["requester"] as String,
                    organization = row["organization"] as String?,
                    budgetAmount = row["budget_amount"] as BigDecimal,
                    createdAt = row["created_at"] as String
                )
            }
            
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
            driver.query("SELECT 1")
            true
        } catch (e: Exception) {
            logger.error("Database health check failed: ${e.message}")
            false
        }
    }
    
    /**
     * Close database connections
     */
    suspend fun close() {
        try {
            driver.close()
            dbScope.cancel()
            logger.info("Database connections closed successfully")
        } catch (e: Exception) {
            logger.error("Error closing database: ${e.message}")
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

/**
 * Data classes for query results (replaces Exposed entities)
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
    val budgetAmount: BigDecimal,
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
 * Database configuration object
 */
object DatabaseConfig {
    const val DEFAULT_MAX_CONNECTIONS = 10
    const val DEFAULT_CONNECTION_TIMEOUT = 30000L // 30 seconds
    const val DEFAULT_IDLE_TIMEOUT = 600000L // 10 minutes
}

/**
 * Extension functions for easier database operations
 */
suspend fun SqlX4kDatabase.executeInTransaction(block: suspend () -> Unit) {
    // sqlx4k handles transactions internally, but we can add retry logic here
    try {
        block()
    } catch (e: Exception) {
        // Add retry logic or rollback handling here if needed
        throw e
    }
}
*/