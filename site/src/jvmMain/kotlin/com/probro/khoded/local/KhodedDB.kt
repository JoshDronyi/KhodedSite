package com.probro.khoded.local

import Answer
import ClientMessage
import ClientMessages
import ProjectRequest
import ProjectRequestAnswers
import ProjectRequests
import com.probro.khoded.FormAnswerDTO
import com.probro.khoded.IntakeFormDTO
import com.probro.khoded.KhodedConfig
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

const val IS_PROD: Boolean = false

object KhodedDB {
    val db by lazy {
        val config = HikariConfig().apply {
            jdbcUrl = if (IS_PROD) KhodedConfig.prodUri else KhodedConfig.devUri
            driverClassName = "org.postgresql.driver"
            username = if (IS_PROD) KhodedConfig.prodUsername else KhodedConfig.devUsername
            password = if (IS_PROD) KhodedConfig.prodPassword else KhodedConfig.devPassword
            maximumPoolSize = 10
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

    private suspend fun setUpSchemaTables() = newSuspendedTransaction {
        SchemaUtils.create(ClientMessages)
        SchemaUtils.create(ProjectRequests)
        SchemaUtils.create(ProjectRequestAnswers)
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

    suspend fun saveProjectRequest(intakeFormDTO: IntakeFormDTO) = suspendedTransactionAsync {
        val answers = getAnswers(intakeFormDTO).filterNotNull()
        ProjectRequest.new {
            this.requester = intakeFormDTO.organization ?: "Unknown"
            this.answers = SizedCollection(answers)
            this.budget = Monetary.getDefaultAmountFactory()
                .setCurrency(Monetary.getCurrency("U.S"))
                .setNumber(0)
                .create()
        }
    }

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


fun FormAnswerDTO.toEntity(isAnswerList: Boolean = false): Answer {
    return Answer.new {
        this.questionText = questionText
        this.value = answerValue
        this.isAnswerList = isAnswerList
    }
}
