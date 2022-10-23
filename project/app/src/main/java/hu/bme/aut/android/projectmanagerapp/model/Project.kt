package hu.bme.aut.android.projectmanagerapp.model

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.util.*
@JsonClass(generateAdapter = true)
data class Project(
    val id: Int,
    val name: String,
    val desc: String,
    val client: String,
    val startDate: Date,
    val endDate: Date,
    val length: Int
    ) : Serializable