package hu.bme.aut.android.projectmanagerapp.data.project

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import hu.bme.aut.android.projectmanagerapp.data.user.UserRole
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val status: String,
    val startDate: String?,
    val endDate: String?,
    val estimatedTime: Int?,
    var tasks: List<Task>?,
    var milestones: List<Milestone>?,
    val userRole: String?
    ) : Serializable