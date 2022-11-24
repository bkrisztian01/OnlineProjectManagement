package hu.bme.aut.android.projectmanagerapp.ui.singleproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.repository.singleproject.SingleProjectRepository

class SingleProjectViewModel: ViewModel() {
    private var singleprojectRepository: SingleProjectRepository = SingleProjectRepository()

    fun getSingleProject(token: String,  id: Int) : LiveData<SingleProjectViewState>? {
        return singleprojectRepository.getSingleProject(token,id)
    }
}