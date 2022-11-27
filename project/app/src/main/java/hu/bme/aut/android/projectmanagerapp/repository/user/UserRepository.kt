package hu.bme.aut.android.projectmanagerapp.repository.user

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.login.LoginBody
import hu.bme.aut.android.projectmanagerapp.data.user.SignInBody
import hu.bme.aut.android.projectmanagerapp.data.user.UserResult
import hu.bme.aut.android.projectmanagerapp.datasource.user.UserNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginViewState
import hu.bme.aut.android.projectmanagerapp.ui.user.UserViewState

class UserRepository {
    fun getUser(token: String): MutableLiveData<UserViewState> {
        return UserNetworkDataSource.getUser(token)
    }
    fun createUser(signInBody: SignInBody): String{
        return UserNetworkDataSource.createUser(signInBody)
    }
    fun login(loginBody:LoginBody): MutableLiveData<LoginViewState>{
        return UserNetworkDataSource.login(loginBody)
    }

    fun getRefreshToken(): MutableLiveData<LoginViewState> {
        return UserNetworkDataSource.getRefreshToken()
    }


}