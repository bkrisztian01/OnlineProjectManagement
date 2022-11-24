package hu.bme.aut.android.projectmanagerapp.data.project

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val startDate: String?,
    val endDate: String?,
    val estimatedTime: Int?,
    /*var tasks: List<Task>,
    var milestones: List<Milestone>*/
    ) : Serializable