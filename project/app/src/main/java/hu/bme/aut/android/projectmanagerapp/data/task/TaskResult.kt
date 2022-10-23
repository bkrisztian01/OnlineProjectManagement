package hu.bme.aut.android.projectmanagerapp.data.task

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.model.Milestone
import hu.bme.aut.android.projectmanagerapp.model.Task

@JsonClass(generateAdapter = true)
data class TaskResult(
    val response_code: Int,
    val tasks: List<Task>
)