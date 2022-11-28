package hu.bme.aut.android.projectmanagerapp.datasource.singletask

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.task.TaskBody
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import hu.bme.aut.android.projectmanagerapp.ui.singlemilestone.SingleMilestoneResponseError
import hu.bme.aut.android.projectmanagerapp.ui.singletask.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SingleTaskNetworkDataSource {
    fun getSingleTask(token: String, projid: Int, id: Int): MutableLiveData<SingleTaskViewState> {
        val call =
            RetrofitClient.singletaskApiInterface.getSingleTask("Bearer " + token, projid, id)
        val singletaskResultData = MutableLiveData<SingleTaskViewState>()
        singletaskResultData.value = InProgress
        call.enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.body() != null)
                    singletaskResultData.value = SingleTaskResponseSuccess(response.body()!!)
                else
                    singletaskResultData.value = SingleTaskResponseError(response.code().toString())
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
                singletaskResultData.value = SingleTaskResponseError(t.message!!)
            }

        })

        return singletaskResultData
    }

    fun updateTask(token: String,projectid: Int,id: Int,status: TaskBody
    ): MutableLiveData<UpdateViewState> {
        val call = RetrofitClient.singletaskApiInterface.updateTask("Bearer " + token,projectid, id, status
        )
        val updateResult = MutableLiveData<UpdateViewState>()
        updateResult.value = InProgressUpdate
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("Response DEBUG : task", response.body().toString())
                if (response.body() == null)
                    updateResult.value = UpdateError(response.code().toString())
                else
                    updateResult.value = UpdateSuccess(response.code().toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Error DEBUG : task", t.message.toString())
                updateResult.value = UpdateError(t.message!!)
            }

        })
        return updateResult
    }

}