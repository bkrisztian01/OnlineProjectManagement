package hu.bme.aut.android.projectmanagerapp.ui.projects

import hu.bme.aut.android.projectmanagerapp.data.project.Project


sealed class ProjectsViewState

object InProgress : ProjectsViewState()
data class ProjectsResponseSuccess(val data: List<Project>) : ProjectsViewState()
data class ProjectsResponseError(val exceptionMsg: String) : ProjectsViewState()