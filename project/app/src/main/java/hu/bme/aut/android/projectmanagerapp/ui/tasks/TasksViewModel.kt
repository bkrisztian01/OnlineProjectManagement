package hu.bme.aut.android.projectmanagerapp.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.data.task.TaskResult
import hu.bme.aut.android.projectmanagerapp.repository.task.TaskRepository

class TasksViewModel: ViewModel() {
    private var taskRepository: TaskRepository = TaskRepository()

    fun getTasks() : LiveData<TaskResult>? {
        return taskRepository.getTasks()
    }
}