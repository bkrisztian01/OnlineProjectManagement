package hu.bme.aut.android.projectmanagerapp.datasource.singleproject

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.project.Project
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import hu.bme.aut.android.projectmanagerapp.ui.singleproject.InProgress
import hu.bme.aut.android.projectmanagerapp.ui.singleproject.SingleProjectResponseError
import hu.bme.aut.android.projectmanagerapp.ui.singleproject.SingleProjectResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.singleproject.SingleProjectViewState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SingleProjectNetworkDataSource {
    fun getSingleProject(token: String, id: Int): MutableLiveData<SingleProjectViewState> {
        val call = RetrofitClient.singleprojectApiInterface.getSingleProject("Bearer " + token, id)
        val singleprojectResultData = MutableLiveData<SingleProjectViewState>()
        singleprojectResultData.value = InProgress
        call.enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                Log.d("DEBUG : ", response.body().toString())
                if (response.body() != null)
                    singleprojectResultData.value = SingleProjectResponseSuccess(response.body()!!)
                else
                    singleprojectResultData.value =
                        SingleProjectResponseError(response.code().toString())
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
                singleprojectResultData.value = SingleProjectResponseError(t.message!!)
            }

        })

        return singleprojectResultData
    }

}