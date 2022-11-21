package hu.bme.aut.android.projectmanagerapp.data.login

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginBody (
    val email: String,
    val password: String
)