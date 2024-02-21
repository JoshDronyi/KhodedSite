package com.probro.khoded

import kotlinx.serialization.Serializable

@Serializable
data class EmailData(
    val name: String? = null,
    val email: String? = null,
    val organization: String? = null,
    val subject:String? = null,
    val message: String? = null
)


fun getCredentialsFile(){
//    val file =
}
