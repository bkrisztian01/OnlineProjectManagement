package hu.bme.aut.android.projectmanagerapp.data.user

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.model.Milestone
import hu.bme.aut.android.projectmanagerapp.model.User

@JsonClass(generateAdapter = true)
data class UserResult(
    val response_code: Int,
    val user: User
)
