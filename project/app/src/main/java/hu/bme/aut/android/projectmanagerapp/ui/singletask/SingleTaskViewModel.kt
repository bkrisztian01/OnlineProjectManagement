package hu.bme.aut.android.projectmanagerapp.ui.singletask

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult
import hu.bme.aut.android.projectmanagerapp.repository.singletask.SingleTaskRepository

class SingleTaskViewModel: ViewModel() {
    private var singleTaskRepository: SingleTaskRepository = SingleTaskRepository()

    fun getSingleTask() : LiveData<SingleTaskResult>? {
        return singleTaskRepository.getSingleTask()
    }
}