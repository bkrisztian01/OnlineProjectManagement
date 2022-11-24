package hu.bme.aut.android.projectmanagerapp.data.user

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val username: String,
    val fullname: String,
    val email: String
    ) :Serializable{
}