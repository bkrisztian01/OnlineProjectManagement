package hu.bme.aut.android.projectmanagerapp.data.user

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.data.project.Project

@JsonClass(generateAdapter = true)
data class UserRole (
    val id: Int,
    val role: String,
    val project: Project?,
    val user: User?
)