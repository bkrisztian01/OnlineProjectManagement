package hu.bme.aut.android.projectmanagerapp.ui.milestone

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.data.milestone.MilestoneResult
import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.repository.milestone.MilestoneRepository
import hu.bme.aut.android.projectmanagerapp.repository.project.ProjectRepository

class MilestoneViewModel: ViewModel() {
    private var milestoneRepository: MilestoneRepository = MilestoneRepository()

    fun getMilestones() : LiveData<MilestoneResult>? {
        return milestoneRepository.getMilestones()
    }
}