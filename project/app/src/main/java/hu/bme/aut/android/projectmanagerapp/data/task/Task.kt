package hu.bme.aut.android.projectmanagerapp.data.task

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone
import hu.bme.aut.android.projectmanagerapp.data.user.User
import java.io.Serializable

@JsonClass(generateAdapter = true)
class Task (
    val id: Int,
    val name: String,
    val description: String,
    var status: String,
    val deadline: String?,
    var archived: Boolean,
    val milestone: Milestone?,
    val assignees: List<User>?,
    val prerequisiteTaskIDs: List<Int>?,

    ) : Serializable