package hu.bme.aut.android.projectmanagerapp.ui.singletask

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult
import hu.bme.aut.android.projectmanagerapp.data.task.TaskBody
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.repository.singletask.SingleTaskRepository

class SingleTaskViewModel: ViewModel() {
    private var singleTaskRepository: SingleTaskRepository = SingleTaskRepository()

    fun getSingleTask(id: Int) : LiveData<SingleTaskResult>? {
        return singleTaskRepository.getSingleTask(id)
    }
    fun updateTask(task: Task){
        val taskbody=TaskBody(task.name,task.description,task.status,task.deadline,task.prerequisiteTaskIDs,task.assignees)
        val id=task.id
        singleTaskRepository.updateTask(id, taskbody)
    }
}