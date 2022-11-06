package hu.bme.aut.android.projectmanagerapp.repository.project

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.datasource.project.ProjectNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.ui.projects.ProjectsViewState

class ProjectRepository {
    fun getProjects(): MutableLiveData<ProjectsViewState> {

        return ProjectNetworkDataSource.getProjects()
    }
}