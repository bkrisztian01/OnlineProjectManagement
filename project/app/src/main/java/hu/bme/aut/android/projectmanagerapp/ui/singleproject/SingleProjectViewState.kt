package hu.bme.aut.android.projectmanagerapp.ui.singleproject

import hu.bme.aut.android.projectmanagerapp.data.project.Project


sealed class SingleProjectViewState

object InProgress : SingleProjectViewState()
data class SingleProjectResponseSuccess(val data: Project) : SingleProjectViewState()
data class SingleProjectResponseError(val exceptionMsg: String) : SingleProjectViewState()