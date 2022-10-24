package hu.bme.aut.android.projectmanagerapp.datasource.singletask

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SingleTaskNetworkDataSource {
    fun getSingleTask(): MutableLiveData<SingleTaskResult> {
        val call = RetrofitClient.singletaskApiInterface.getSingleTask()
        val singletaskResultData = MutableLiveData<SingleTaskResult>()
        call.enqueue(object: Callback<SingleTaskResult> {
            override fun onResponse(call: Call<SingleTaskResult>, response: Response<SingleTaskResult>) {
                Log.d("DEBUG : ", response.body().toString())
                singletaskResultData.value = response.body()
            }

            override fun onFailure(call: Call<SingleTaskResult>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
            }

        })

        return singletaskResultData
    }

}