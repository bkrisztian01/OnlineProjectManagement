package hu.bme.aut.android.projectmanagerapp.ui.singletask

import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult


sealed class SingleTaskViewState

object InProgress : SingleTaskViewState()
data class SingleTaskResponseSuccess(val data: SingleTaskResult) : SingleTaskViewState()
data class SingleTaskResponseError(val exceptionMsg: String) : SingleTaskViewState()