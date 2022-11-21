package hu.bme.aut.android.projectmanagerapp.ui.login

import hu.bme.aut.android.projectmanagerapp.data.login.LoginResponse

sealed class LoginViewState

object InProgress : LoginViewState()
data class LoginResponseSuccess(val data: LoginResponse) : LoginViewState()
data class LoginResponseError(val exceptionMsg: String) : LoginViewState()