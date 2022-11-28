package hu.bme.aut.android.projectmanagerapp.repository.singleproject

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.datasource.singleproject.SingleProjectNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.ui.singleproject.SingleProjectViewState

class SingleProjectRepository {
    fun getSingleProject(token: String, id: Int): MutableLiveData<SingleProjectViewState> {
        return SingleProjectNetworkDataSource.getSingleProject(token, id)
    }
}