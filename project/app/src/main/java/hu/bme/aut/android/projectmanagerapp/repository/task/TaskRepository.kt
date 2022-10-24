package hu.bme.aut.android.projectmanagerapp.repository.task

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.task.TaskResult
import hu.bme.aut.android.projectmanagerapp.datasource.task.TaskNetworkDataSource

class TaskRepository {
    fun getTasks(): MutableLiveData<TaskResult> {
        return TaskNetworkDataSource.getTasks()
    }
}