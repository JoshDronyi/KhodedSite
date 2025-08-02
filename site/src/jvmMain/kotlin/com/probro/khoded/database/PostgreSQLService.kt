package com.probro.khoded.database

import com.probro.khoded.FormAnswerDTO
import com.probro.khoded.IntakeFormDTO
import com.probro.khoded.KhodedConfig
import com.varabyte.kobweb.api.log.Logger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import java.util.*

/**
 * PostgreSQLService - Production-ready PostgreSQL database service using JDBI + HikariCP
 * 
 * This replaces the temporary in-memory database with a proper PostgreSQL implementation
 * optimized for Docker deployment and production use.
 * 
 * Features:
 * - JDBI for lightweight, type-safe SQL operations
 * - HikariCP for high-performance connection pooling
 * - Docker-compatible configuration
 * - Comprehensive error handling and logging
 * - Thread-safe operations
 * 
 * Performance Benefits:
 * - Lightweight dependency footprint (~2MB vs 7MB+ for Exposed)
 * - High developer satisfaction (JDBI is widely appreciated)
 * - Excellent connection pooling with HikariCP
 * - Type-safe SQL operations
 * 
 * @since 2.1.0 (Production PostgreSQL implementation)
 */
class PostgreSQLService(
    private val logger: Logger,
    private val isDevelopment: Boolean = true
) {
    
    private val dataSource by lazy {
        val config = HikariConfig().apply {
            // Docker-compatible configuration
            jdbcUrl = if (isDevelopment) {
                System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/khoded_db"
            } else {
                System.getenv("DATABASE_URL") ?: KhodedConfig.prodUri
            }
            
            driverClassName = "org.postgresql.Driver"
            
            username = if (isDevelopment) {
                System.getenv("DATABASE_USER") ?: "khoded_user"
            } else {
                System.getenv("DATABASE_USER") ?: KhodedConfig.prodUsername
            }
            
            password = if (isDevelopment) {
                System.getenv("DATABASE_PASSWORD") ?: "khoded_password"
            } else {
                System.getenv("DATABASE_PASSWORD") ?: KhodedConfig.prodPassword
            }
            
            // Optimized connection pool settings
            maximumPoolSize = 10
            minimumIdle = 3
            idleTimeout = 300000L // 5 minutes
            connectionTimeout = 20000L // 20 seconds
            leakDetectionThreshold = 60000L // 1 minute
            
            // Performance optimizations
            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
            addDataSourceProperty("useServerPrepStmts", "true")
            addDataSourceProperty("useLocalSessionState", "true")
            addDataSourceProperty("rewriteBatchedStatements", "true")
            addDataSourceProperty("cacheResultSetMetadata", "true")
            addDataSourceProperty("cacheServerConfiguration", "true")
            addDataSourceProperty("elideSetAutoCommits", "true")
            addDataSourceProperty("maintainTimeStats", "false")
        }
        
        HikariDataSource(config)
    }
    
    private val jdbi by lazy {
        Jdbi.create(dataSource)
            .installPlugin(PostgresPlugin())
            .installPlugin(KotlinPlugin())
    }
    
    private val dbScope = CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
        logger.error("Database operation failed: ${throwable.message}")
        throwable.printStackTrace()
    })
    
    /**
     * Initialize database tables with proper schema
     */
    suspend fun initializeTables() {
        try {
            logger.info("Initializing PostgreSQL database tables with JDBI")
            
            jdbi.useHandle<Exception> { handle ->
                // Create tables using PostgreSQL-optimized SQL
                handle.execute("""
                    CREATE TABLE IF NOT EXISTS client_messages (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        from_name VARCHAR(100) NOT NULL,
                        organization VARCHAR(200) NOT NULL,
                        message TEXT NOT NULL,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                    )
                """.trimIndent())
                
                handle.execute("""
                    CREATE TABLE IF NOT EXISTS project_requests (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        requester VARCHAR(100) NOT NULL,
                        organization VARCHAR(200),
                        budget_amount DECIMAL(12,2) DEFAULT 0.00,
                        budget_currency VARCHAR(3) DEFAULT 'USD',
                        form_data JSONB NOT NULL,
                        status VARCHAR(50) DEFAULT 'pending',
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                    )
                """.trimIndent())
                
                handle.execute("""
                    CREATE TABLE IF NOT EXISTS form_answers (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        request_id UUID NOT NULL REFERENCES project_requests(id) ON DELETE CASCADE,
                        question_text VARCHAR(500) NOT NULL,
                        answer_value VARCHAR(1000) NOT NULL,
                        section_name VARCHAR(100),
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        
                        -- Create index for efficient queries
                        CONSTRAINT fk_form_answers_request_id FOREIGN KEY (request_id) REFERENCES project_requests(id)
                    )
                """.trimIndent())
                
                // Create performance indexes
                handle.execute("CREATE INDEX IF NOT EXISTS idx_client_messages_created_at ON client_messages(created_at DESC)")
                handle.execute("CREATE INDEX IF NOT EXISTS idx_project_requests_created_at ON project_requests(created_at DESC)")
                handle.execute("CREATE INDEX IF NOT EXISTS idx_project_requests_status ON project_requests(status)")
                handle.execute("CREATE INDEX IF NOT EXISTS idx_form_answers_request_id ON form_answers(request_id)")
                handle.execute("CREATE INDEX IF NOT EXISTS idx_form_answers_section ON form_answers(section_name)")
                
                // Create updated_at trigger function
                handle.execute("""
                    CREATE OR REPLACE FUNCTION update_updated_at_column()
                    RETURNS TRIGGER AS $$
                    BEGIN
                        NEW.updated_at = CURRENT_TIMESTAMP;
                        RETURN NEW;
                    END;
                    $$ language 'plpgsql'
                """.trimIndent())
                
                // Apply triggers
                handle.execute("""
                    DROP TRIGGER IF EXISTS update_project_requests_updated_at ON project_requests;
                    CREATE TRIGGER update_project_requests_updated_at 
                        BEFORE UPDATE ON project_requests 
                        FOR EACH ROW EXECUTE FUNCTION update_updated_at_column()
                """.trimIndent())
            }
            
            logger.info("PostgreSQL database tables initialized successfully")
            
        } catch (e: Exception) {
            logger.error("Failed to initialize PostgreSQL tables: ${e.message}")
            throw e
        }
    }
    
    /**
     * Save a client message with proper error handling
     */
    suspend fun saveMessage(
        fromName: String,
        organization: String,
        message: String
    ): String = withContext(Dispatchers.IO) {
        try {
            logger.info("Saving client message from $fromName to PostgreSQL")
            
            val messageId = jdbi.withHandle<String, Exception> { handle ->
                handle.createQuery("""
                    INSERT INTO client_messages (from_name, organization, message) 
                    VALUES (:fromName, :organization, :message)
                    RETURNING id::text
                """.trimIndent())
                    .bind("fromName", fromName)
                    .bind("organization", organization)
                    .bind("message", message)
                    .mapTo(String::class.java)
                    .one()
            }
            
            logger.info("Client message saved successfully with ID: $messageId")
            messageId
            
        } catch (e: Exception) {
            logger.error("Failed to save client message: ${e.message}")
            throw e
        }
    }
    
    /**
     * Save a project request form with transaction support
     */
    suspend fun saveProjectRequest(
        intakeFormDTO: IntakeFormDTO
    ): String = withContext(Dispatchers.IO) {
        try {
            logger.info("Saving project request for ${intakeFormDTO.organization} to PostgreSQL")
            
            val requestId = jdbi.inTransaction<String, Exception> { handle ->
                val requesterName = getRequesterName(intakeFormDTO)
                val formDataJson = Json.encodeToString(IntakeFormDTO.serializer(), intakeFormDTO)
                
                // Insert main project request
                val requestId = handle.createQuery("""
                    INSERT INTO project_requests (requester, organization, form_data) 
                    VALUES (:requester, :organization, :formData::jsonb)
                    RETURNING id::text
                """.trimIndent())
                    .bind("requester", requesterName)
                    .bind("organization", intakeFormDTO.organization ?: "Unknown")
                    .bind("formData", formDataJson)
                    .mapTo(String::class.java)
                    .one()
                
                // Insert individual form answers for efficient querying
                val allAnswers = collectAllAnswers(intakeFormDTO)
                allAnswers.forEach { (sectionName, answers) ->
                    answers.forEach { answer ->
                        handle.createUpdate("""
                            INSERT INTO form_answers (request_id, question_text, answer_value, section_name) 
                            VALUES (:requestId::uuid, :questionText, :answerValue, :sectionName)
                        """.trimIndent())
                            .bind("requestId", requestId)
                            .bind("questionText", answer.questionText)
                            .bind("answerValue", answer.answerValue)
                            .bind("sectionName", sectionName)
                            .execute()
                    }
                }
                
                requestId
            }
            
            logger.info("Project request saved successfully with ID: $requestId")
            requestId
            
        } catch (e: Exception) {
            logger.error("Failed to save project request: ${e.message}")
            throw e
        }
    }
    
    /**
     * Retrieve recent client messages with pagination
     */
    suspend fun getRecentMessages(limit: Int = 10, offset: Int = 0): List<ClientMessageRecord> = withContext(Dispatchers.IO) {
        try {
            jdbi.withHandle<List<ClientMessageRecord>, Exception> { handle ->
                handle.createQuery("""
                    SELECT id::text, from_name as fromName, organization, message, created_at::text as createdAt 
                    FROM client_messages 
                    ORDER BY created_at DESC 
                    LIMIT :limit OFFSET :offset
                """.trimIndent())
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ClientMessageRecord::class.java)
                    .list()
            }
        } catch (e: Exception) {
            logger.error("Failed to retrieve messages: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Retrieve project requests with status filtering
     */
    suspend fun getProjectRequests(
        limit: Int = 10, 
        offset: Int = 0,
        status: String? = null
    ): List<ProjectRequestRecord> = withContext(Dispatchers.IO) {
        try {
            val sql = if (status != null) {
                """
                SELECT id::text, requester, organization, budget_amount::text as budgetAmount, created_at::text as createdAt 
                FROM project_requests 
                WHERE status = :status
                ORDER BY created_at DESC 
                LIMIT :limit OFFSET :offset
                """.trimIndent()
            } else {
                """
                SELECT id::text, requester, organization, budget_amount::text as budgetAmount, created_at::text as createdAt 
                FROM project_requests 
                ORDER BY created_at DESC 
                LIMIT :limit OFFSET :offset
                """.trimIndent()
            }
            
            jdbi.withHandle<List<ProjectRequestRecord>, Exception> { handle ->
                val query = handle.createQuery(sql)
                    .bind("limit", limit)
                    .bind("offset", offset)
                
                if (status != null) {
                    query.bind("status", status)
                }
                
                query.mapTo(ProjectRequestRecord::class.java).list()
            }
        } catch (e: Exception) {
            logger.error("Failed to retrieve project requests: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Health check with connection validation
     */
    suspend fun healthCheck(): Boolean = withContext(Dispatchers.IO) {
        try {
            jdbi.withHandle<Boolean, Exception> { handle ->
                handle.createQuery("SELECT 1").mapTo(Int::class.java).one() == 1
            }
        } catch (e: Exception) {
            logger.error("PostgreSQL health check failed: ${e.message}")
            false
        }
    }
    
    /**
     * Get database statistics for monitoring
     */
    suspend fun getDatabaseStats(): Map<String, Long> = withContext(Dispatchers.IO) {
        try {
            jdbi.withHandle<Map<String, Long>, Exception> { handle ->
                val stats = mutableMapOf<String, Long>()
                
                // Get table counts
                stats["client_messages"] = handle.createQuery("SELECT COUNT(*) FROM client_messages")
                    .mapTo(Long::class.java).one()
                    
                stats["project_requests"] = handle.createQuery("SELECT COUNT(*) FROM project_requests")
                    .mapTo(Long::class.java).one()
                    
                stats["form_answers"] = handle.createQuery("SELECT COUNT(*) FROM form_answers")
                    .mapTo(Long::class.java).one()
                
                // Get connection pool stats
                stats["active_connections"] = dataSource.hikariPoolMXBean?.activeConnections?.toLong() ?: 0L
                stats["idle_connections"] = dataSource.hikariPoolMXBean?.idleConnections?.toLong() ?: 0L
                stats["total_connections"] = dataSource.hikariPoolMXBean?.totalConnections?.toLong() ?: 0L
                
                stats
            }
        } catch (e: Exception) {
            logger.error("Failed to get database stats: ${e.message}")
            emptyMap()
        }
    }
    
    /**
     * Close database connections properly
     */
    suspend fun close() {
        try {
            dataSource.close()
            dbScope.cancel()
            logger.info("PostgreSQL connections closed successfully")
        } catch (e: Exception) {
            logger.error("Error closing PostgreSQL database: ${e.message}")
        }
    }
    
    // Helper methods
    
    private fun getRequesterName(intakeFormDTO: IntakeFormDTO): String {
        return intakeFormDTO.organization 
            ?: intakeFormDTO.contactFormAnswers?.firstOrNull()?.answerValue 
            ?: "Unknown Requester"
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


