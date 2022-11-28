package hu.bme.aut.android.projectmanagerapp.ui.milestone

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.repository.milestone.MilestoneRepository

class MilestoneViewModel : ViewModel() {
    private var milestoneRepository: MilestoneRepository = MilestoneRepository()

    fun getMilestones(token: String,projectid: Int,pageNumber: Int): LiveData<MilestonesViewState>? {
        return milestoneRepository.getMilestones(token, projectid, pageNumber)
    }
}