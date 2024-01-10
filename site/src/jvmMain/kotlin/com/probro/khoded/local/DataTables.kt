import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
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

object Questions : IntIdTable() {
    var isAnswerList = bool("is_Answer_List")
    var questionText = varchar("Question_Text", 300)
}

class Question(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Question>(Questions)

    var isAnswerList by Questions.isAnswerList
    var questionText by Questions.questionText
}

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

object ProjectSections : IntIdTable("Section") {
    var title = varchar("Section_Title", 300)
    var description = varchar("Section_Description", 300)
}

class ProjectSection(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProjectSection>(ProjectSections)

    var title by ProjectSections.title
    var description by ProjectSections.description
}

object ProjectRequestAnswers : Table("ProjectRequestAnswer") {
    val requestID = reference("RequestID", ProjectRequests.id)
    val sectionID = reference("SectionID", ProjectSections.id)
    val answerID = reference("AnswerID", Answers.id)
    override val primaryKey = PrimaryKey(ProjectRequests.id, ProjectSections.id, Answers.id)
}


object Answers : UUIDTable("Answer") {
    var value = varchar("value", 500)
    var questionText = reference("QuestionText", Questions.questionText)
}

class Answer(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Answer>(Answers)

    var value by Answers.value
    var questionText by Answers.questionText
}

