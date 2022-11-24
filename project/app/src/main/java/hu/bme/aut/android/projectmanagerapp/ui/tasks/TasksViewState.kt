package hu.bme.aut.android.projectmanagerapp.ui.tasks
import hu.bme.aut.android.projectmanagerapp.data.task.Task


sealed class TasksViewState

object InProgress : TasksViewState()
data class TaskResponseSuccess(val data: List<Task>) : TasksViewState()
data class TaskResponseError(val exceptionMsg: String) : TasksViewState()