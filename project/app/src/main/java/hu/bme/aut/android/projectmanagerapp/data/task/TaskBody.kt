package hu.bme.aut.android.projectmanagerapp.data.task

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone
import hu.bme.aut.android.projectmanagerapp.data.user.User
import java.io.Serializable

@JsonClass(generateAdapter = true)
class TaskBody(
    var status: String

) : Serializable