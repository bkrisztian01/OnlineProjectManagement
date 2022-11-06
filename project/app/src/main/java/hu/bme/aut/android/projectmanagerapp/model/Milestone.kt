package hu.bme.aut.android.projectmanagerapp.model

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

@JsonClass(generateAdapter = true)
data class Milestone (
    val id: Int,
    val name: String,
    val description: String,
    val status: String,
    val deadline: String,
    //val projectID: Int,
    val tasks: List<Task>
    ): Serializable