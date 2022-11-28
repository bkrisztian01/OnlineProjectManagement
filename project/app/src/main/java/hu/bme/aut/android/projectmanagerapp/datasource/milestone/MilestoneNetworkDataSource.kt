package hu.bme.aut.android.projectmanagerapp.datasource.milestone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone
import hu.bme.aut.android.projectmanagerapp.network.RetrofitClient
import hu.bme.aut.android.projectmanagerapp.ui.milestone.MilestoneResponseError
import hu.bme.aut.android.projectmanagerapp.ui.milestone.MilestoneResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.milestone.MilestonesViewState
import hu.bme.aut.android.projectmanagerapp.ui.singlemilestone.InProgress
import hu.bme.aut.android.projectmanagerapp.ui.singlemilestone.SingleMilestoneResponseError
import hu.bme.aut.android.projectmanagerapp.ui.singlemilestone.SingleMilestoneResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.singlemilestone.SingleMilestoneViewState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MilestoneNetworkDataSource {
    fun getMilestones(token: String,projectid: Int,pageNumber: Int): MutableLiveData<MilestonesViewState> {
        val call = RetrofitClient.milestoneApiInterface.getMilestones("Bearer " + token,projectid, pageNumber)
        val milestoneResultData = MutableLiveData<MilestonesViewState>()
        call.enqueue(object : Callback<List<Milestone>> {
            override fun onResponse(call: Call<List<Milestone>>,response: Response<List<Milestone>>) {
                Log.d("DEBUG : ", response.body().toString())
                if (response.body() != null)
                    milestoneResultData.value = MilestoneResponseSuccess(response.body()!!)
                else
                    milestoneResultData.value = MilestoneResponseError(response.code().toString())
            }

            override fun onFailure(call: Call<List<Milestone>>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
                milestoneResultData.value = MilestoneResponseError(t.message.toString())
            }

        })

        return milestoneResultData
    }


    fun getMilestone(token: String,projectid: Int,milestoneid: Int): MutableLiveData<SingleMilestoneViewState> {
        val call = RetrofitClient.milestoneApiInterface.getMilestone("Bearer " + token,projectid,milestoneid)
        val milestoneResultData = MutableLiveData<SingleMilestoneViewState>()
        milestoneResultData.value = InProgress
        call.enqueue(object : Callback<Milestone> {
            override fun onResponse(call: Call<Milestone>, response: Response<Milestone>) {
                Log.d("DEBUG : ", response.body().toString())
                if (response.body() != null)
                    milestoneResultData.value = SingleMilestoneResponseSuccess(response.body()!!)
                else
                    milestoneResultData.value =
                        SingleMilestoneResponseError(response.code().toString())
            }

            override fun onFailure(call: Call<Milestone>, t: Throwable) {
                Log.d("DEBUG : ", t.message.toString())
                milestoneResultData.value = SingleMilestoneResponseError(t.message.toString())
            }

        })

        return milestoneResultData
    }
}