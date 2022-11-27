package hu.bme.aut.android.projectmanagerapp.data.milestone

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Milestone (
    val id: Int,
    val name: String,
    val description: String,
    val deadline: String,
    val status: String,
    val archived: Boolean,
    val tasks: List<Task>?
    ): Serializable