package hu.bme.aut.android.projectmanagerapp.network.singletask

import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult
import hu.bme.aut.android.projectmanagerapp.data.task.TaskBody
import hu.bme.aut.android.projectmanagerapp.data.task.TaskResult
import hu.bme.aut.android.projectmanagerapp.model.Task
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface SingleTaskAPI {
    @GET("/tasks/{id}")
    fun getSingleTask(@Path("id") id: Int): Call<SingleTaskResult>
    @PUT("/tasks/{id}")
    fun updateTask(@Path("id") id: Int,@Body task: TaskBody): Call<ResponseBody>


}