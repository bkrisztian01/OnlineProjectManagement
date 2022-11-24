package hu.bme.aut.android.projectmanagerapp.ui.singlemilestone

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.repository.singlemilestone.SingleMilestoneRepository

class SingleMilestoneViewModel: ViewModel() {
    private var milestoneRepository: SingleMilestoneRepository= SingleMilestoneRepository()
    fun getMilestone(token: String, projectid: Int, milestoneid: Int): LiveData<SingleMilestoneViewState>{
        return milestoneRepository.getMilestone(token,projectid,milestoneid)
    }
}