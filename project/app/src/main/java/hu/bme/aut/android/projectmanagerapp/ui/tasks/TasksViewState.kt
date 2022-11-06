package hu.bme.aut.android.projectmanagerapp.ui.tasks

import hu.bme.aut.android.projectmanagerapp.data.task.TaskResult


sealed class TasksViewState

object InProgress : TasksViewState()
data class TaskResponseSuccess(val data: TaskResult) : TasksViewState()
data class TaskResponseError(val exceptionMsg: String) : TasksViewState()