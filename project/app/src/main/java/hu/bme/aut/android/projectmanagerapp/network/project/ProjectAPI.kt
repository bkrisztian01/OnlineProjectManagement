package hu.bme.aut.android.projectmanagerapp.network.project

import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import retrofit2.Call
import retrofit2.http.GET

interface ProjectAPI {
    @GET("/projects")
    fun getProjects(): Call<ProjectResult>

}