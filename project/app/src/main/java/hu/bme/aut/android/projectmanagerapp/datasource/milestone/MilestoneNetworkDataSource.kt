package hu.bme.aut.android.projectmanagerapp.datasource.milestone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.milestone.MilestoneResult
import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MilestoneNetworkDataSource {
    fun getMilestones(): MutableLiveData<MilestoneResult> {
        val call = RetrofitClient.milestoneApiInterface.getMilestones()
        val milestoneResultData = MutableLiveData<MilestoneResult>()
        call.enqueue(object: Callback<MilestoneResult> {
            override fun onResponse(call: Call<MilestoneResult>, response: Response<MilestoneResult>) {
                Log.d("DEBUG : ", response.body().toString())
                milestoneResultData.value = response.body()
            }

            override fun onFailure(call: Call<MilestoneResult>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
            }

        })

        return milestoneResultData
    }

}