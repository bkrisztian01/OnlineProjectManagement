package hu.bme.aut.android.projectmanagerapp.network.singleproject

import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.data.singleproject.SingleProjectResult
import retrofit2.Call
import retrofit2.http.GET

interface SingleProjectAPI {
    @GET("/project")
    fun getSingleProject(): Call<SingleProjectResult>

}