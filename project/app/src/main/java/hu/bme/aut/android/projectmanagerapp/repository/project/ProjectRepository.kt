package hu.bme.aut.android.projectmanagerapp.repository.project

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.datasource.project.ProjectNetworkDataSource

class ProjectRepository {
    fun getProjects(): MutableLiveData<ProjectResult> {
        return ProjectNetworkDataSource.getProjects()
    }
}