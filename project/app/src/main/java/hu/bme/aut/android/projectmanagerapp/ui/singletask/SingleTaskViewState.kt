package hu.bme.aut.android.projectmanagerapp.ui.singletask
import hu.bme.aut.android.projectmanagerapp.data.task.Task


sealed class SingleTaskViewState

object InProgress : SingleTaskViewState()
data class SingleTaskResponseSuccess(val data: Task) : SingleTaskViewState()
data class SingleTaskResponseError(val exceptionMsg: String) : SingleTaskViewState()