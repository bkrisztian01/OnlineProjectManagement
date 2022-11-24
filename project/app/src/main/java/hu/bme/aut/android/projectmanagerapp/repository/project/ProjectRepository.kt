package hu.bme.aut.android.projectmanagerapp.repository.project

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.datasource.project.ProjectNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.ui.projects.ProjectsViewState

class ProjectRepository {
    fun getProjects(token: String,pageNumber: Int): MutableLiveData<ProjectsViewState> {
        return ProjectNetworkDataSource.getProjects(token,pageNumber)
    }
}