package hu.bme.aut.android.projectmanagerapp.datasource.project

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.project.Project
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import hu.bme.aut.android.projectmanagerapp.ui.projects.InProgress
import hu.bme.aut.android.projectmanagerapp.ui.projects.ProjectsResponseError
import hu.bme.aut.android.projectmanagerapp.ui.projects.ProjectsResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.projects.ProjectsViewState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProjectNetworkDataSource {
    fun getProjects(token: String,pageNumber: Int): MutableLiveData<ProjectsViewState> {
        val call = RetrofitClient.projectApiInterface.getProjects("Bearer ${token}",pageNumber)
        val projectResultData = MutableLiveData<ProjectsViewState>()
        projectResultData.value= InProgress
        call.enqueue(object: Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                Log.d("success DEBUG : ", response.body().toString())
                if(response.body()!=null)
                    projectResultData.value = ProjectsResponseSuccess(response.body()!!)
                else
                    projectResultData.value=ProjectsResponseError(response.code().toString())

            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                Log.d("Fail DEBUG : ", t.message.toString())
                projectResultData.value = ProjectsResponseError(t.message.toString())
            }

        })

        return projectResultData
    }

}
