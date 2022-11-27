package hu.bme.aut.android.projectmanagerapp.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.repository.task.TaskRepository

class TasksViewModel: ViewModel() {
    private var taskRepository: TaskRepository = TaskRepository()

    fun getTasks(token: String, projid: Int, milestoneid: Int,pageNumber:Int) : LiveData<TasksViewState>? {
        return taskRepository.getTasks(token,projid,milestoneid,pageNumber)
    }
    fun getTasksByProject(token:String, projid: Int,pageNumber: Int): LiveData<TasksViewState>?{
        return taskRepository.getTasksByProject(token,projid,pageNumber)
    }

    fun getUpcomingTasks(token:String): LiveData<TasksViewState>?{
        return taskRepository.getUpcomingTasks(token)
    }
}