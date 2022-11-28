package hu.bme.aut.android.projectmanagerapp.network.task

import hu.bme.aut.android.projectmanagerapp.data.task.Task
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskAPI {
    @GET("/projects/{projectid}/milestones/{milestoneid}/tasks/")
    fun getTasks(@Header("Authorization") token: String,@Path("projectid") projectid: Int,@Path("milestoneid") milestoneid: Int,@Query("pageNumber") pagenum: Int): Call<List<Task>>


    @GET("/projects/{projectid}/tasks/")
    fun getTasksByProject(@Header("Authorization") token: String, @Path("projectid") projectid: Int, @Query("pageNumber") pagenum: Int): Call<List<Task>>

    @GET("/user/tasks")
    fun getUpcomingTasks(@Header("Authorization") token: String): Call<List<Task>>
}