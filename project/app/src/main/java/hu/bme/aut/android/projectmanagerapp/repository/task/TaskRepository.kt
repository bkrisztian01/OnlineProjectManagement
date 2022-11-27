package hu.bme.aut.android.projectmanagerapp.repository.task

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.datasource.task.TaskNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.ui.tasks.TasksViewState

class TaskRepository {
    fun getTasks(token: String,projid: Int, milestoneid: Int,pageNumber: Int): MutableLiveData<TasksViewState> {
        return TaskNetworkDataSource.getTasks(token, projid,milestoneid,pageNumber)
    }
    fun getTasksByProject(token: String, projid: Int, pageNumber: Int):MutableLiveData<TasksViewState> {
        return TaskNetworkDataSource.getTasksByProject(token, projid,pageNumber)
    }

    fun getUpcomingTasks(token: String): MutableLiveData<TasksViewState>{
        return TaskNetworkDataSource.getUpcomingTasks(token)
    }
}