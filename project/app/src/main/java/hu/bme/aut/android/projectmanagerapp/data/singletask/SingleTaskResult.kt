package hu.bme.aut.android.projectmanagerapp.data.singletask

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.model.Milestone
import hu.bme.aut.android.projectmanagerapp.model.Task

@JsonClass(generateAdapter = true)
data class SingleTaskResult(
    val response_code: Int,
    val singleTask: Task
)