package hu.bme.aut.android.projectmanagerapp.data.login

import com.squareup.moshi.JsonClass
import hu.bme.aut.android.projectmanagerapp.data.user.User

@JsonClass(generateAdapter = true)
data class LoginResponse (
    var accessToken: String,
)

