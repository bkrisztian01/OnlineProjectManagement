package hu.bme.aut.android.projectmanagerapp.model

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
@JsonClass(generateAdapter = true)
class Task (
    val id: Int,
    val name: String,
    val desc: String,
    var status: String,
    val startDate: Date,
    val endDate: Date,
    val projectID: Int,
    val milestoneID: Int,
    val prerequisiteTasks: ArrayList<Task>?,
    val assignees: ArrayList<User>
    ) : Serializable