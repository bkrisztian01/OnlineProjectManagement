package hu.bme.aut.android.projectmanagerapp.network.user

import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.data.user.UserResult
import retrofit2.Call
import retrofit2.http.GET

interface UserAPI {
    @GET("/user")
    fun getUser(): Call<UserResult>

}