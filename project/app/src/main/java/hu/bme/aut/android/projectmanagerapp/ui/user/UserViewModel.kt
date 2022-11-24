package hu.bme.aut.android.projectmanagerapp.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.data.login.LoginBody
import hu.bme.aut.android.projectmanagerapp.data.user.SignInBody
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.repository.user.UserRepository
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginViewState

class UserViewModel: ViewModel() {
    private var userRepository: UserRepository = UserRepository()

    fun createUser(username: String,password: String, name: String, email:String){
        val taskbody= SignInBody(username,name,email,password)
        userRepository.createUser(taskbody)
    }

    fun login(email:String, password:String): LiveData<LoginViewState>?{
        val loginBody=LoginBody(email,password)
        return userRepository.login(loginBody)
    }
    fun getUser(token: String): LiveData<UserViewState>{
        return userRepository.getUser(token)
    }

    fun getRefreshToken(): LiveData<LoginViewState>{
        return userRepository.getRefreshToken()
    }
}