package com.probro.khoded.local.datatables

import BASE_VARCHAR_LENGTH
import DECIMAL_PRECISION
import DECIMAL_SCALE
import Project
import Projects
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.money.compositeMoney
import org.jetbrains.exposed.sql.money.currency
import java.util.*

object Customers : IntIdTable("Customers") {
    val budget = compositeMoney(
        amountColumn = decimal("Amount", DECIMAL_PRECISION, DECIMAL_SCALE),
        currencyColumn = currency("Currency")
    )
    val fieldOfWork = varchar("FieldOfWork", BASE_VARCHAR_LENGTH)
}

class Customer(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Customer>(Customers)

    var budget by Customers.budget
    var fieldOfWork by Customers.fieldOfWork
}

object Employees : IntIdTable("Employees") {
    val employeeNumber = varchar("EmpNumber", BASE_VARCHAR_LENGTH).uniqueIndex()
    val role = varchar("Role", BASE_VARCHAR_LENGTH)
}

class Employee(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Employee>(Employees)

    var employeeNumber by Employees.employeeNumber
    var role by Employees.role
}

//User Table
object KhodedUsers : UUIDTable("Users") {
    val name = varchar("Name", BASE_VARCHAR_LENGTH)
    val email = varchar("Email", BASE_VARCHAR_LENGTH)
    val phone = varchar("Phone", 10)
    val password = varchar("Password", BASE_VARCHAR_LENGTH)
    val createdAt = timestamp("CreatedAt")
    val customerDetails = optReference("CustomerDetails", Customers)
    val employeeDetails = optReference("EmployeeDetails", Employees)
}


class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(KhodedUsers)

    var name by KhodedUsers.name
    var email by KhodedUsers.email
    var phone by KhodedUsers.phone
    var createdAt by KhodedUsers.createdAt
    var password by KhodedUsers.password

    val customerDetails by Customer optionalReferrersOn KhodedUsers.customerDetails
    val employeeDetails by Employee optionalReferrersOn KhodedUsers.employeeDetails
    val projects by Project backReferencedOn Projects.customer
}
