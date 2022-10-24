package hu.bme.aut.android.projectmanagerapp.repository.milestone

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.milestone.MilestoneResult
import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.datasource.milestone.MilestoneNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.datasource.project.ProjectNetworkDataSource

class MilestoneRepository {
    fun getMilestones(): MutableLiveData<MilestoneResult> {
        return MilestoneNetworkDataSource.getMilestones()
    }
}