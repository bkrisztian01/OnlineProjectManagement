package hu.bme.aut.android.projectmanagerapp.network.project

import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.model.Project
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProjectAPI {
    @GET("/projects")
    fun getProjects(/*@Query()*/): Call<List<Project>>

}