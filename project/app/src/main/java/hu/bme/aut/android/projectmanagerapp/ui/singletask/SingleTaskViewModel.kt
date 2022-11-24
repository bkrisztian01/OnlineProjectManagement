package hu.bme.aut.android.projectmanagerapp.ui.singletask

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.data.task.TaskBody
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import hu.bme.aut.android.projectmanagerapp.repository.singletask.SingleTaskRepository

class SingleTaskViewModel: ViewModel() {
    private var singleTaskRepository: SingleTaskRepository = SingleTaskRepository()

    fun getSingleTask(token: String, projectid: Int, id: Int) : LiveData<SingleTaskViewState>? {
        return singleTaskRepository.getSingleTask(token, projectid,id)
    }
    fun updateTask(token: String ,projectid: Int, task: Task){
        val taskbody=TaskBody(task.status)
        val id=task.id
        singleTaskRepository.updateTask(token,projectid,id, taskbody)
    }
}