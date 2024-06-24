import com.probro.khoded.local.datatables.Consultation
import com.probro.khoded.local.datatables.Consultations
import com.probro.khoded.local.datatables.KhodedUsers
import com.probro.khoded.local.datatables.User
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

object Projects : UUIDTable("Projects") {
    val name = varchar("Name", BASE_VARCHAR_LENGTH)
    val description = varchar("Description", MAX_VARCHAR_LENGTH)
    val consultations = optReference("Consultations", Consultations)
    val customer = reference("Customers", KhodedUsers)
    val createdAt = timestamp("CreatedAt")
}

class Project(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Project>(Projects)

    var name by Projects.name
    var description by Projects.description
    var createdAt by Projects.createdAt
    val consultations by Consultation optionalReferencedOn Projects.consultations
    var customer by User referencedOn Projects.customer
}

