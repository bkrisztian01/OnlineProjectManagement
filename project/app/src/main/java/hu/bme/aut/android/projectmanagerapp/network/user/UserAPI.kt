package hu.bme.aut.android.projectmanagerapp.network.user

import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.data.task.TaskBody
import hu.bme.aut.android.projectmanagerapp.data.user.SignInBody
import hu.bme.aut.android.projectmanagerapp.data.user.UserResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAPI {
    @GET("/user")
    fun getUser(): Call<UserResult>


    @POST("/user/signup")
    fun createUser(@Body user: SignInBody): Call<ResponseBody>

}