package hu.bme.aut.android.projectmanagerapp.ui.login

sealed class SignInViewState

object InProgressSignIn : SignInViewState()
data class SignInResponseSuccess(val data: String) : SignInViewState()
data class SignInResponseError(val exceptionMsg: String) : SignInViewState()