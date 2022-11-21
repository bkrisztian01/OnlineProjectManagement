package hu.bme.aut.android.projectmanagerapp.repository.user

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.projectmanagerapp.data.login.LoginBody
import hu.bme.aut.android.projectmanagerapp.data.project.ProjectResult
import hu.bme.aut.android.projectmanagerapp.data.task.TaskBody
import hu.bme.aut.android.projectmanagerapp.data.user.SignInBody
import hu.bme.aut.android.projectmanagerapp.data.user.UserResult
import hu.bme.aut.android.projectmanagerapp.datasource.project.ProjectNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.datasource.user.UserNetworkDataSource
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginViewState
import hu.bme.aut.android.projectmanagerapp.ui.projects.ProjectsViewState

class UserRepository {
    fun getUser(): MutableLiveData<UserResult> {
        return UserNetworkDataSource.getUser()
    }
    fun createUser(signInBody: SignInBody){
        UserNetworkDataSource.createUser(signInBody)
    }
    fun login(loginBody:LoginBody): MutableLiveData<LoginViewState>{
        return UserNetworkDataSource.login(loginBody)
    }


}