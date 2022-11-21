package hu.bme.aut.android.projectmanagerapp.datasource.project

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import hu.bme.aut.android.projectmanagerapp.ui.projects.InProgress
import hu.bme.aut.android.projectmanagerapp.ui.projects.ProjectsResponseError
import hu.bme.aut.android.projectmanagerapp.ui.projects.ProjectsResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.projects.ProjectsViewState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProjectNetworkDataSource {
    fun getProjects(/*token: String*/): MutableLiveData<ProjectsViewState> {
        val call = RetrofitClient.projectApiInterface.getProjects(/*token*/)
        val projectResultData = MutableLiveData<ProjectsViewState>()
        projectResultData.value= InProgress
        call.enqueue(object: Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                Log.d("success DEBUG : ", response.body().toString())
                projectResultData.value = ProjectsResponseSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                Log.d("Fail DEBUG : ", t.message.toString())
                projectResultData.value = ProjectsResponseError(t.message.toString())
            }

        })

        return projectResultData
    }

}
