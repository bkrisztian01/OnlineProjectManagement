package hu.bme.aut.android.projectmanagerapp.repository.singleproject

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.data.singleproject.SingleProjectResult
import hu.bme.aut.android.projectmanagerapp.datasource.project.ProjectNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.datasource.singleproject.SingleProjectNetworkDataSource

class SingleProjectRepository {
    fun getSingleProject(): MutableLiveData<SingleProjectResult> {
        return SingleProjectNetworkDataSource.getSingleProject()
    }
}