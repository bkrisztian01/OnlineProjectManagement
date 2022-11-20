package hu.bme.aut.android.projectmanagerapp.datasource.singletask

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult
import hu.bme.aut.android.projectmanagerapp.data.task.TaskBody
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SingleTaskNetworkDataSource {
    fun getSingleTask(id:Int): MutableLiveData<SingleTaskResult> {
        val call = RetrofitClient.singletaskApiInterface.getSingleTask(id)
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

    fun updateTask(id: Int, task: TaskBody){
        val call=RetrofitClient.singletaskApiInterface.updateTask(id,task)
        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("Response DEBUG : task", response.body().toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Error DEBUG : task", t.message.toString())
            }

        })
    }

}