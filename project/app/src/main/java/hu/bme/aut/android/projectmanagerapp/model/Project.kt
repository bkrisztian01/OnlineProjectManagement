package hu.bme.aut.android.projectmanagerapp.model

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.util.*
@JsonClass(generateAdapter = true)
data class Project(
    val id: Int,
    val name: String,
    val description: String,
    //val client: String,
    val startDate: String,
    val endDate: String,
    val estimatedTime: Int,
    var tasks: List<Task>,
    var milestones: List<Milestone>
    ) : Serializable