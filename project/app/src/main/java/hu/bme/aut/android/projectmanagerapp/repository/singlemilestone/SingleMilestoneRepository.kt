package hu.bme.aut.android.projectmanagerapp.repository.singlemilestone

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.datasource.milestone.MilestoneNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.ui.singlemilestone.SingleMilestoneViewState

class SingleMilestoneRepository {
    fun getMilestone(token: String,projectid: Int,milestoneid: Int): MutableLiveData<SingleMilestoneViewState> {
        return MilestoneNetworkDataSource.getMilestone(token,projectid,milestoneid)
    }
}