package hu.bme.aut.android.projectmanagerapp.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.data.login.LoginBody
import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult
import hu.bme.aut.android.projectmanagerapp.data.task.TaskBody
import hu.bme.aut.android.projectmanagerapp.data.user.SignInBody
import hu.bme.aut.android.projectmanagerapp.data.user.UserResult
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.repository.singletask.SingleTaskRepository
import hu.bme.aut.android.projectmanagerapp.repository.user.UserRepository
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginViewState

class UserViewModel: ViewModel() {
    private var userRepository: UserRepository = UserRepository()

    fun createUser(user: User){
        val taskbody= SignInBody(user.username,user.fullname,user.email,user.password)
        userRepository.createUser(taskbody)
    }

    fun login(email:String, password:String): LiveData<LoginViewState>?{
        val loginBody=LoginBody(email,password)
        return userRepository.login(loginBody)
    }
}