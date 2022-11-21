package hu.bme.aut.android.projectmanagerapp.data.task

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.model.User
import java.io.Serializable

@JsonClass(generateAdapter = true)
class TaskBody (
    val name: String,
    val description: String,
    var status: String,
    val deadline: String,
    val prerequisiteTaskIDs: List<Int>?,
    val assignees: List<User>
): Serializable