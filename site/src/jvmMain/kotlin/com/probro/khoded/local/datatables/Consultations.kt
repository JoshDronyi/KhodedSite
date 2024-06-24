package com.probro.khoded.local.datatables

import BASE_VARCHAR_LENGTH
import MAX_VARCHAR_LENGTH
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.*

//Consultation Table
object Consultations : UUIDTable("Consultations") {
    val message = varchar("message", MAX_VARCHAR_LENGTH)
    val meetingTime = datetime("MeetingTime")
    val fallbackTime = datetime("FallbackTime")
    val processed = bool("Processed")
    val meetingMedium = varchar("MeetingMedium", BASE_VARCHAR_LENGTH)
}

class Consultation(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Consultation>(Consultations)

    var message by Consultations.message
    var meetingTime by Consultations.meetingTime
    val fallbackTime by Consultations.fallbackTime
    val processed by Consultations.processed
    val meetingMedium by Consultations.meetingMedium
}