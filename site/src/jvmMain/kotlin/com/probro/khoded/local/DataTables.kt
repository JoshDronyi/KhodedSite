import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.money.compositeMoney
import org.jetbrains.exposed.sql.money.currency
import java.util.*

object ClientMessages : UUIDTable("ClientMessages") {
    val from = varchar("from", 50)
    val organization = varchar("organization", 150)
    val message = varchar("message", 500)
}

class ClientMessage(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ClientMessage>(ClientMessages)

    var from by ClientMessages.from
    var organization by ClientMessages.organization
    var message by ClientMessages.message
}

const val DECIMAL_PRECISION = 2
const val DECIMAL_SCALE = 2

object ProjectRequests : UUIDTable("Project Requests") {
    val requester = varchar("Requester_Name", 50)
    val budget = compositeMoney(
        amountColumn = decimal("amount", DECIMAL_PRECISION, DECIMAL_SCALE),
        currencyColumn = currency("dollars")
    )
}

class ProjectRequest(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProjectRequest>(ProjectRequests)

    var requester by ProjectRequests.requester
    var budget by ProjectRequests.budget
    var answers by Answer via ProjectRequestAnswers
}

object Sections : UUIDTable("Section") {
    var name = varchar("name", 50)
    var description = varchar("description", 200)
}

object ProjectRequestAnswers : Table("ProjectRequestAnswer") {
    val answer = reference("Answer", Answers)
    val section = varchar("section", 50)
    val projectRequest = reference("ProjectRequest", ProjectRequests)
    override val primaryKey = PrimaryKey(Answers.id, ProjectRequests.id, Sections.id)
}

object Answers : UUIDTable("Answer") {
    var isAnswerList = bool("isAnswerList")
    var questionText = varchar("questionText", 500)
    var value = varchar("value", 500)
}

class Answer(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Answer>(Answers)

    var isAnswerList by Answers.isAnswerList
    var questionText by Answers.questionText
    var value by Answers.value
}

