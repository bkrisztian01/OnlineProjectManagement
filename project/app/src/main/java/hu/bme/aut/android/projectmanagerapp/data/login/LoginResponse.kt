package hu.bme.aut.android.projectmanagerapp.data.login

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse (val token:String)

