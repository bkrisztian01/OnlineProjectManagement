package hu.bme.aut.android.projectmanagerapp.data.milestone

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.model.Milestone

@JsonClass(generateAdapter = true)
data class MilestoneResult(
    val response_code: Int,
    val milestone: Milestone
    )

