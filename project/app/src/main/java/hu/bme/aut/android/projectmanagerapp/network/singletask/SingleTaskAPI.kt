package hu.bme.aut.android.projectmanagerapp.network.singletask

import hu.bme.aut.android.projectmanagerapp.data.task.TaskBody
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface SingleTaskAPI {
    @GET("/projects/{projectid}/tasks/{id}")
    fun getSingleTask(@Header("Authorization") token: String,@Path("projectid") projectid: Int,@Path("id") id: Int): Call<Task>


    @PUT("/projects/{projectid}/tasks/{id}")
    fun updateTask(@Header("Authorization") token: String,@Path("projectid") projectid: Int,@Path("id") id: Int,@Body status: TaskBody): Call<ResponseBody>


}