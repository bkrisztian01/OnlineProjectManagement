package hu.bme.aut.android.projectmanagerapp.ui.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.repository.project.ProjectRepository

class ProjectViewModel : ViewModel() {
    private var projectRepository: ProjectRepository = ProjectRepository()

    fun getProjects(token: String, pageNumber: Int): LiveData<ProjectsViewState>? {
        return projectRepository.getProjects(token, pageNumber)
    }
}