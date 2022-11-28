package hu.bme.aut.android.projectmanagerapp.ui.singletask


sealed class UpdateViewState

object InProgressUpdate : UpdateViewState()
data class UpdateSuccess(val data: String) : UpdateViewState()
data class UpdateError(val exceptionMsg: String) : UpdateViewState()