package hu.bme.aut.android.projectmanagerapp.repository.singletask

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult
import hu.bme.aut.android.projectmanagerapp.datasource.project.ProjectNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.datasource.singleproject.SingleProjectNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.datasource.singletask.SingleTaskNetworkDataSource

class SingleTaskRepository {
    fun getSingleTask(): MutableLiveData<SingleTaskResult> {
        return SingleTaskNetworkDataSource.getSingleTask()
    }
}