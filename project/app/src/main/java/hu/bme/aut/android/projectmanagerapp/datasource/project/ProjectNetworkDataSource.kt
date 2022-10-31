package hu.bme.aut.android.projectmanagerapp.datasource.project

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProjectNetworkDataSource {
    fun getProjects(): MutableLiveData<ProjectResult> {
        val call = RetrofitClient.projectApiInterface.getProjects()
        val projectResultData = MutableLiveData<ProjectResult>()
        call.enqueue(object: Callback<ProjectResult> {
            override fun onResponse(call: Call<ProjectResult>, response: Response<ProjectResult>) {
                Log.d("DEBUG : ", response.body().toString())
                projectResultData.value = response.body()
            }

            override fun onFailure(call: Call<ProjectResult>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
            }

        })

        return projectResultData
    }

}
