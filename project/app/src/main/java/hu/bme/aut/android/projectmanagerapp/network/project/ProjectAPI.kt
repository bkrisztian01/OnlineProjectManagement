package hu.bme.aut.android.projectmanagerapp.network.project

import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.model.Project
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProjectAPI {
    @GET("/projects")//kell egy {token}
    fun getProjects(/*@Path("token") token: String */): Call<List<Project>>

}