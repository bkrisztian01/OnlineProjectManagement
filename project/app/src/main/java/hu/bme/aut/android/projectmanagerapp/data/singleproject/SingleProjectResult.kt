package hu.bme.aut.android.projectmanagerapp.data.singleproject

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.model.Milestone
import hu.bme.aut.android.projectmanagerapp.model.Project

@JsonClass(generateAdapter = true)
data class SingleProjectResult(
    val response_code: Int,
    val singleProject: Project
)