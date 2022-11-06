package hu.bme.aut.android.projectmanagerapp.model

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
@JsonClass(generateAdapter = true)
class Task (
    val id: Int,
    val name: String,
    val description: String,
    var status: String,
   // val startDate: Date,
    val deadline: String,
    //val projectID: Int,
    //val milestoneID: Int,
    val prerequisiteTaskIDs: List<Int>?,
    val assignees: List<User>
    ) : Serializable