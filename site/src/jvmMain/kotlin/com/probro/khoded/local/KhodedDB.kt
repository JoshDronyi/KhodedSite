package com.probro.khoded.local

import Project
import Projects
import com.google.api.client.util.DateTime
import com.probro.khoded.KhodedConfig
import com.probro.khoded.local.datatables.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

const val IS_PROD: Boolean = false
const val POSTGRES_DRIVER ="org.postgresql.Driver"

object KhodedDB {
    val db by lazy {
        val config = HikariConfig().apply {
            jdbcUrl = if (IS_PROD) KhodedConfig.prodUri else KhodedConfig.devUri
            driverClassName = POSTGRES_DRIVER
            username = if (IS_PROD) KhodedConfig.prodUsername else KhodedConfig.devUsername
            password = if (IS_PROD) KhodedConfig.prodPassword else KhodedConfig.devPassword
            maximumPoolSize = 10
        }
        val datasource = HikariDataSource(config)
        Database.connect(datasource = datasource)
    }

    private val dataScope = CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    })

    init {
        dataScope.launch {
            setUpSchemaTables()
        }
    }

    private suspend fun setUpSchemaTables() = newSuspendedTransaction {
        SchemaUtils.create(Customers)
        SchemaUtils.create(Employees)
        SchemaUtils.create(KhodedUsers)
        SchemaUtils.create(Projects)
        SchemaUtils.create(Consultations)
    }

    suspend fun createNewUser(
        name: String, email: String, phone: String, password: String
    ) = newSuspendedTransaction {
        User.new {
            this.name = name
            this.email = email
            this.phone = phone
            this.password = password
            this.createdAt = Clock.System.now()
        }
    }

    suspend fun updateUser(user: User) = newSuspendedTransaction {
        User.findByIdAndUpdate(user.id.value) { userToUpdate ->
            userToUpdate.apply {
                name = user.name
                email = user.email
                phone = user.phone
                password = user.password
            }
        }
    }

    suspend fun deleteUser(user: User) = newSuspendedTransaction {
        User.removeFromCache(user)
    }

    suspend fun createProjectForUser(
        user: User,
        name: String,
        description: String
    ) = newSuspendedTransaction {
        Project.new {
            this.name = name
            this.description = description
            this.customer = user
            this.createdAt = Clock.System.now()
        }
    }

    suspend fun updateProject(project: Project) = newSuspendedTransaction {
        Project.findByIdAndUpdate(project.id.value) { oldProj ->
            oldProj.name = project.name
            oldProj.description = project.description
        }
    }

    suspend fun deleteProject(project: Project) = newSuspendedTransaction {
        Project.removeFromCache(project)
    }

    suspend fun createNewConsultation(
        message: String,
        suggestedTimes: List<DateTime>,
        meetingMedium: String
    ) = newSuspendedTransaction {
        Consultation.new {
            this.message = message
            this.meetingMedium = meetingMedium
            this.suggestedTimes = suggestedTimes.joinToString()
            this.processed = false
        }
    }

    suspend fun updateConsultation(consultation: Consultation) = newSuspendedTransaction {
        Consultation.findByIdAndUpdate(consultation.id.value) { oldJawn ->
            with(oldJawn) {
                message = consultation.message
                meetingTime = consultation.meetingTime
                suggestedTimes = consultation.suggestedTimes
                processed = consultation.processed
                meetingMedium = consultation.meetingMedium
            }
        }
    }

    suspend fun removeConsultation(consultation: Consultation) = newSuspendedTransaction {
        Consultation.removeFromCache(consultation)
    }
}