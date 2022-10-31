package hu.bme.aut.android.projectmanagerapp.datasource.singleproject

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.singleproject.SingleProjectResult
import hu.bme.aut.android.projectmanagerapp.data.task.TaskResult
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SingleProjectNetworkDataSource {
    fun getSingleProject(): MutableLiveData<SingleProjectResult> {
        val call = RetrofitClient.singleprojectApiInterface.getSingleProject()
        val singleprojectResultData = MutableLiveData<SingleProjectResult>()
        call.enqueue(object: Callback<SingleProjectResult> {
            override fun onResponse(call: Call<SingleProjectResult>, response: Response<SingleProjectResult>) {
                Log.d("DEBUG : ", response.body().toString())
                singleprojectResultData.value = response.body()
            }

            override fun onFailure(call: Call<SingleProjectResult>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
            }

        })

        return singleprojectResultData
    }

}