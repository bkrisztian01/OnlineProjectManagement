package hu.bme.aut.android.projectmanagerapp.model

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Milestone (
    val id: Int,
    val name: String,
    val projectID: Int,
    val tasks: ArrayList<Task>): Serializable