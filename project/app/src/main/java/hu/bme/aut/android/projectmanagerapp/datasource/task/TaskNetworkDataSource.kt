package hu.bme.aut.android.projectmanagerapp.datasource.task

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult
import hu.bme.aut.android.projectmanagerapp.data.task.TaskResult
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object TaskNetworkDataSource {
    fun getTasks(): MutableLiveData<TaskResult> {
        val call = RetrofitClient.taskApiInterface.getTasks()
        val taskResultData = MutableLiveData<TaskResult>()
        call.enqueue(object: Callback<TaskResult> {
            override fun onResponse(call: Call<TaskResult>, response: Response<TaskResult>) {
                Log.d("DEBUG : ", response.body().toString())
                taskResultData.value = response.body()
            }

            override fun onFailure(call: Call<TaskResult>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
            }

        })

        return taskResultData
    }

}