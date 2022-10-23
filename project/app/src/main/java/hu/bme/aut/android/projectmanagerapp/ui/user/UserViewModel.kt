package hu.bme.aut.android.projectmanagerapp.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.projectmanagerapp.data.singletask.SingleTaskResult
import hu.bme.aut.android.projectmanagerapp.data.user.UserResult
import hu.bme.aut.android.projectmanagerapp.repository.singletask.SingleTaskRepository
import hu.bme.aut.android.projectmanagerapp.repository.user.UserRepository

class UserViewModel: ViewModel() {
    private var userRepository: UserRepository = UserRepository()

    fun getMoneyRates() : LiveData<UserResult>? {
        return userRepository.getUser()
    }
}