package hu.bme.aut.android.projectmanagerapp.ui.singleproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.data.singleproject.SingleProjectResult
import hu.bme.aut.android.projectmanagerapp.repository.singleproject.SingleProjectRepository

class SingleProjectViewModel: ViewModel() {
    private var singleprojectRepository: SingleProjectRepository = SingleProjectRepository()

    fun getSingleProject() : LiveData<SingleProjectResult>? {
        return singleprojectRepository.getSingleProject()
    }
}