package hu.bme.aut.android.projectmanagerapp.datasource.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.login.LoginBody
import hu.bme.aut.android.projectmanagerapp.data.login.LoginResponse
import hu.bme.aut.android.projectmanagerapp.data.user.SignInBody
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.data.user.UserResult
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import hu.bme.aut.android.projectmanagerapp.ui.login.InProgress
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseError
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginViewState
import hu.bme.aut.android.projectmanagerapp.ui.user.UserResponseError
import hu.bme.aut.android.projectmanagerapp.ui.user.UserResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.user.UserViewState
import okhttp3.Interceptor
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserNetworkDataSource {
    fun getUser(token: String): MutableLiveData<UserViewState> {
        val call = RetrofitClient.userApiInterface.getUser("Bearer "+token)
        var userResultData = MutableLiveData<UserViewState>()
        userResultData.value=hu.bme.aut.android.projectmanagerapp.ui.user.InProgress
        call.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("DEBUG : ", response.body().toString())
                if(response.body()!=null)
                    userResultData.value = UserResponseSuccess(response.body()!!)
                else
                    userResultData.value=UserResponseError(response.code().toString())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
                userResultData.value=UserResponseError(t.message!!)
            }

        })

        return userResultData
    }



    fun createUser(user: SignInBody): String{
        val call=RetrofitClient.userApiInterface.createUser(user)
        var code=""
        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("Response DEBUG : task", response.body().toString())
                code= response.code().toString()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Error DEBUG : task", t.message.toString())
                code="Error"
            }

        })
        return code
    }

    fun login(data:LoginBody): MutableLiveData<LoginViewState>{
        val call=RetrofitClient.userApiInterface.login(data)
        val loginResultData = MutableLiveData<LoginViewState>()
        loginResultData.value= InProgress
        call.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.body()==null)
                    loginResultData.value=LoginResponseError(response.code().toString())
                else
                    loginResultData.value = LoginResponseSuccess(response.body()!!)
                Log.d("Response success", response.body().toString())


            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Error: login error", t.message.toString())
                loginResultData.value = LoginResponseError(t.message.toString())
            }

        })
        return loginResultData
    }

    fun getRefreshToken(): MutableLiveData<LoginViewState> {
        val call=RetrofitClient.userApiInterface.getRefreshToken()
        val refreshResultData = MutableLiveData<LoginViewState>()
        refreshResultData.value= hu.bme.aut.android.projectmanagerapp.ui.login.InProgress
        call.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.body()==null)
                    refreshResultData.value=LoginResponseError(response.code().toString())
                else
                    refreshResultData.value= LoginResponseSuccess(response.body()!!)
                Log.d("Response success", response.body().toString())


            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Error: login error", t.message.toString())
                refreshResultData.value = LoginResponseError(t.message.toString())
            }

        })
        return refreshResultData
    }



}
