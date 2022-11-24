package hu.bme.aut.android.projectmanagerapp.network.user

import hu.bme.aut.android.projectmanagerapp.data.login.LoginBody
import hu.bme.aut.android.projectmanagerapp.data.login.LoginResponse
import hu.bme.aut.android.projectmanagerapp.data.user.SignInBody
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.data.user.UserResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserAPI {
    @GET("/users/current")
    fun getUser(@Header("Authorization") token: String): Call<User>


    @POST("/user/signup")
    fun createUser(@Body user: SignInBody): Call<ResponseBody>


    @POST("/user/login")
    fun login(@Body data: LoginBody): Call<LoginResponse>

    @GET("refresh")
    fun getRefreshToken(): Call<LoginResponse>

}