package com.probro.khoded.local

import Answer
import Answers
import ClientMessage
import ClientMessages
import ProjectRequest
import ProjectRequestAnswers
import ProjectRequests
import ProjectSections
import Questions
import com.probro.khoded.FormAnswerDTO
import com.probro.khoded.IntakeFormDTO
import com.probro.khoded.KhodedConfig
import com.varabyte.kobweb.api.log.Logger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import java.util.*
import javax.money.Monetary

// Detect if running on Render or in production
val IS_PROD: Boolean = System.getenv("RENDER") != null || System.getenv("APP_ENVIRONMENT") == "production"

object KhodedDB {
    val db by lazy {
        val config = HikariConfig().apply {
            // Use DATABASE_URL environment variable from Render, fallback to build config
            val databaseUrl = System.getenv("DATABASE_URL")
            if (databaseUrl != null) {
                // Render provides DATABASE_URL in format: postgresql://user:pass@host:port/database
                // HikariCP needs jdbc:postgresql://user:pass@host:port/database
                jdbcUrl = if (databaseUrl.startsWith("postgresql://")) {
                    "jdbc:$databaseUrl"
                } else {
                    databaseUrl
                }
            } else {
                // Fallback to build config for local development
                jdbcUrl = if (IS_PROD) KhodedConfig.prodUri else KhodedConfig.devUri
                username = if (IS_PROD) KhodedConfig.prodUsername else KhodedConfig.devUsername
                password = if (IS_PROD) KhodedConfig.prodPassword else KhodedConfig.devPassword
            }
            
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 10
            
            // Render-specific optimizations
            if (System.getenv("RENDER") != null) {
                connectionTimeout = 20000 // 20 seconds
                idleTimeout = 300000 // 5 minutes
                maxLifetime = 1200000 // 20 minutes
                leakDetectionThreshold = 60000 // 1 minute
            }
        }
        val datasource = HikariDataSource(config)
        Database.connect(datasource = datasource)
    }
    private val dataScope = CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    })

    init {
        dataScope.launch {
            setUpSchemaTables()
        }
    }


    suspend fun setUpSchemaTables() = newSuspendedTransaction {
        SchemaUtils.create(ClientMessages)
        SchemaUtils.create(ProjectRequests)
        SchemaUtils.create(ProjectRequestAnswers)
        SchemaUtils.create(Questions)
        SchemaUtils.create(ProjectSections)
        SchemaUtils.create(Answers)
    }

    suspend fun saveMessage(
        from: String,
        organization: String,
        message: String
    ) = newSuspendedTransaction {
        ClientMessage.new(UUID.randomUUID()) {
            this.from = from
            this.organization = organization
            this.message = message
        }
    }

    suspend fun saveProjectRequest(intakeFormDTO: IntakeFormDTO, logger: Logger) = suspendedTransactionAsync {
        val answers = getAnswers(intakeFormDTO).filterNotNull()
        logger.info("Got answers $answers")
        logger.info("Creating new Project request")
        ProjectRequest.new {
            logger.info("getting requester info")
            this.requester = getRequester(intakeFormDTO)
            logger.info("setting answers")
            this.answers = SizedCollection(answers)
            logger.info("Setting requester budget")
            this.budget = Monetary.getDefaultAmountFactory()
                .setCurrency(Monetary.getCurrency("U.S"))
                .setNumber(0)
                .create()
            logger.info("creating request")
        }
    }

    private fun getRequester(intakeFormDTO: IntakeFormDTO) =
        intakeFormDTO.organization ?: intakeFormDTO.contactFormAnswers?.first()?.answerValue ?: "Unknown"

    private suspend fun getAnswers(intakeFormDTO: IntakeFormDTO): List<Answer?> = with(intakeFormDTO) {
        mutableListOf<Deferred<List<Answer>?>>().apply {
            add(dataScope.async { contactFormAnswers?.map { it.toEntity() } })
            add(dataScope.async { projectOverviewAnswers?.map { it.toEntity() } })
            add(dataScope.async { designBrandingAnswers?.map { it.toEntity() } })
            add(dataScope.async { contentImageryAnswers?.map { it.toEntity() } })
            add(dataScope.async { timelineBudgetAnswers?.map { it.toEntity() } })
            add(dataScope.async { maintenanceUpdatesAnswers?.map { it.toEntity() } })
            add(dataScope.async { structureFunctionalityAnswers?.map { it.toEntity() } })
            add(dataScope.async { additionalInfo?.map { it.toEntity() } })
        }.awaitAll()
            .filterNotNull()
            .flatten()
    }
}


fun FormAnswerDTO.toEntity(): Answer {
    return Answer.new {
        this.questionText = questionText
        this.value = answerValue
    }
}
