package hu.bme.aut.android.projectmanagerapp.data.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SignInBody(
    val username: String,
    val fullname: String,
    val email: String,
    val password: String
)