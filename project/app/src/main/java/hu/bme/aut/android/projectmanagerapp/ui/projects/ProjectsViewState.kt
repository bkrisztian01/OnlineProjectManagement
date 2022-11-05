package hu.bme.aut.android.projectmanagerapp.ui.projects

import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult


sealed class ProjectsViewState

object InProgress : ProjectsViewState()
data class ProjectsResponseSuccess(val data: ProjectResult) : ProjectsViewState()
data class ProjectsResponseError(val exceptionMsg: String) : ProjectsViewState()