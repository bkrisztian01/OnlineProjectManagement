package hu.bme.aut.android.projectmanagerapp.network.project

import hu.bme.aut.android.projectmanagerapp.data.project.Project
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProjectAPI {
    @GET("/projects")
    fun getProjects(@Header("Authorization") token: String,@Query("pageNumber") pagenum: Int
    ): Call<List<Project>>

}