package hu.bme.aut.android.projectmanagerapp.data.project

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.model.Milestone
import hu.bme.aut.android.projectmanagerapp.model.Project

@JsonClass(generateAdapter = true)
data class ProjectResult(
    //val response_code: Int,
    val project: List<Project>
    )
