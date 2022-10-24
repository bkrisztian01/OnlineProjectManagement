package hu.bme.aut.android.projectmanagerapp.datasource.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.task.TaskResult
import hu.bme.aut.android.projectmanagerapp.data.user.UserResult
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserNetworkDataSource {
    fun getUser(): MutableLiveData<UserResult> {
        val call = RetrofitClient.userApiInterface.getUser()
        val userResultData = MutableLiveData<UserResult>()
        call.enqueue(object: Callback<UserResult> {
            override fun onResponse(call: Call<UserResult>, response: Response<UserResult>) {
                Log.d("DEBUG : ", response.body().toString())
                userResultData.value = response.body()
            }

            override fun onFailure(call: Call<UserResult>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
            }

        })

        return userResultData
    }

}