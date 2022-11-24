package hu.bme.aut.android.projectmanagerapp.repository.singletask

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.task.TaskBody
import hu.bme.aut.android.projectmanagerapp.datasource.singletask.SingleTaskNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.ui.singletask.SingleTaskViewState

class SingleTaskRepository {
    fun getSingleTask(token: String, projectid: Int, id: Int): MutableLiveData<SingleTaskViewState> {
        return SingleTaskNetworkDataSource.getSingleTask(token, projectid, id)
    }
    fun updateTask(token: String, projectid: Int, id: Int, taskstatus: TaskBody){
        SingleTaskNetworkDataSource.updateTask(token, projectid, id,taskstatus)
    }
}