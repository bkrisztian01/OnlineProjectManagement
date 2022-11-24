package hu.bme.aut.android.projectmanagerapp.repository.milestone

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.datasource.milestone.MilestoneNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.ui.milestone.MilestonesViewState

class MilestoneRepository {
    fun getMilestones(token: String, projectid: Int,pageNumber:Int): MutableLiveData<MilestonesViewState> {
        return MilestoneNetworkDataSource.getMilestones(token, projectid,pageNumber)
    }
}