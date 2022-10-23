package hu.bme.aut.android.projectmanagerapp.network.task

import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.data.task.TaskResult
import retrofit2.Call
import retrofit2.http.GET

interface TaskAPI {
    @GET("/tasks")
    fun getTasks(): Call<TaskResult>

}