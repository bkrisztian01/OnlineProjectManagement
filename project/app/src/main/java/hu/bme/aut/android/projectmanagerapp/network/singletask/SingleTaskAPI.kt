package hu.bme.aut.android.projectmanagerapp.network.singletask

import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult
import retrofit2.Call
import retrofit2.http.GET

interface SingleTaskAPI {
    @GET("/task")
    fun getSingleTask(): Call<SingleTaskResult>

}