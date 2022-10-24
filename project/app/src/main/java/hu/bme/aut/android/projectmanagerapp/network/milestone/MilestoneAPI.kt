package hu.bme.aut.android.projectmanagerapp.network.milestone

import hu.bme.aut.android.projectmanagerapp.data.milestone.MilestoneResult
import retrofit2.Call
import retrofit2.http.GET

interface MilestoneAPI {
    @GET("/milestones")
    fun getMilestones(): Call<MilestoneResult>
}