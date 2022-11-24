package hu.bme.aut.android.projectmanagerapp.network.singleproject

import hu.bme.aut.android.projectmanagerapp.data.project.Project
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SingleProjectAPI {
    @GET("/projects/{id}")
    fun getSingleProject(@Header("Authorization") token: String, @Path("id") id:Int): Call<Project>

}