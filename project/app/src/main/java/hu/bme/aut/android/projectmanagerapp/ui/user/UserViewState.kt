package hu.bme.aut.android.projectmanagerapp.ui.user

import hu.bme.aut.android.projectmanagerapp.data.user.UserResult


sealed class UserViewState

object InProgress : UserViewState()
data class UserResponseSuccess(val data: UserResult) : UserViewState()
data class UserResponseError(val exceptionMsg: String) : UserViewState()