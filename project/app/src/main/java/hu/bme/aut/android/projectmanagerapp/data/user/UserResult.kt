package hu.bme.aut.android.projectmanagerapp.data.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResult(
    val response_code: Int,
    val user: User
)
