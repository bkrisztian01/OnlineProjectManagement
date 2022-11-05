package hu.bme.aut.android.projectmanagerapp.ui.singleproject

import hu.bme.aut.android.projectmanagerapp.data.singleproject.SingleProjectResult


sealed class SingleProjectViewState

object InProgress : SingleProjectViewState()
data class SingleProjectResponseSuccess(val data: SingleProjectResult) : SingleProjectViewState()
data class SingleProjectResponseError(val exceptionMsg: String) : SingleProjectViewState()