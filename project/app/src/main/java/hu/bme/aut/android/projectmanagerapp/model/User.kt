package hu.bme.aut.android.projectmanagerapp.model

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val username: String,
    val password: String,
    val name: String
    ) :Serializable{
}