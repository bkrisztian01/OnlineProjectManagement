package hu.bme.aut.android.projectmanagerapp.network.milestone

import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone
import retrofit2.Call
import retrofit2.http.*

interface MilestoneAPI {
    @GET("/projects/{projid}/milestones")
    fun getMilestones(@Header("Authorization") token: String,@Path("projid") projid: Int ,@Query("pageNumber")pageNumber: Int): Call<List<Milestone>>


    @GET("/projects/{projid}/milestones/{milestoneid}")
    fun getMilestone(@Header("Authorization") token: String, @Path("projid") projid: Int, @Path("milestoneid") milestoneid: Int): Call<Milestone>
}