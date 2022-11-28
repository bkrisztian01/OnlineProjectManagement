package hu.bme.aut.android.projectmanagerapp.datasource.task

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import hu.bme.aut.android.projectmanagerapp.ui.tasks.InProgress
import hu.bme.aut.android.projectmanagerapp.ui.tasks.TaskResponseError
import hu.bme.aut.android.projectmanagerapp.ui.tasks.TaskResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.tasks.TasksViewState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object TaskNetworkDataSource {
    fun getTasks(token: String,projid: Int,milestoneid: Int,pageNumber: Int
    ): MutableLiveData<TasksViewState> {
        val call = RetrofitClient.taskApiInterface.getTasks("Bearer " + token,projid,milestoneid,pageNumber)
        val taskResultData = MutableLiveData<TasksViewState>()
        taskResultData.value = InProgress
        call.enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                Log.d("DEBUG : ", response.body().toString())
                if (response.body() != null)
                    taskResultData.value = TaskResponseSuccess(response.body()!!)
                else
                    taskResultData.value = TaskResponseError(response.code().toString())
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
                taskResultData.value = TaskResponseError(t.message!!)
            }

        })

        return taskResultData
    }

    fun getTasksByProject(token: String,projid: Int,pageNumber: Int): MutableLiveData<TasksViewState> {
        val call =RetrofitClient.taskApiInterface.getTasksByProject("Bearer " + token, projid, pageNumber)
        val taskResultData = MutableLiveData<TasksViewState>()
        taskResultData.value = InProgress
        call.enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                Log.d("DEBUG : ", response.body().toString())
                if (response.body() != null)
                    taskResultData.value = TaskResponseSuccess(response.body()!!)
                else
                    taskResultData.value = TaskResponseError(response.code().toString())
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
                taskResultData.value = TaskResponseError(t.message!!)
            }

        })

        return taskResultData
    }

    fun getUpcomingTasks(token: String): MutableLiveData<TasksViewState> {
        val call = RetrofitClient.taskApiInterface.getUpcomingTasks("Bearer " + token)
        val taskResultData = MutableLiveData<TasksViewState>()
        taskResultData.value = InProgress
        call.enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                Log.d("DEBUG : ", response.body().toString())
                if (response.body() != null)
                    taskResultData.value = TaskResponseSuccess(response.body()!!)
                else
                    taskResultData.value = TaskResponseError(response.code().toString())
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
                taskResultData.value = TaskResponseError(t.message!!)
            }

        })

        return taskResultData
    }

}