import com.probro.khoded.local.datatables.Consultation
import com.probro.khoded.local.datatables.Consultations
import com.probro.khoded.local.datatables.KhodedUsers
import com.probro.khoded.local.datatables.User
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

const val DECIMAL_PRECISION = 10
const val DECIMAL_SCALE = 2

const val BASE_VARCHAR_LENGTH = 100
const val MAX_VARCHAR_LENGTH = BASE_VARCHAR_LENGTH * 5


object Projects : UUIDTable("Projects") {
    val name = varchar("Name", BASE_VARCHAR_LENGTH)
    val description = varchar("Description", MAX_VARCHAR_LENGTH)
    val consultations = reference("Consultations", Consultations)
    val customer = reference("Customers", KhodedUsers)
}

class Project(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Project>(Projects)

    var name by Projects.name
    var description by Projects.description
    var consultations by Consultation referencedOn Projects.consultations
    var customer by User referencedOn Projects.customer
}

