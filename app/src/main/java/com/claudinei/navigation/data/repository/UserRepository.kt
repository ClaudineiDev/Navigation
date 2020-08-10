package com.claudinei.navigation.data.repository

import com.claudinei.navigation.data.model.User
import com.claudinei.navigation.ui.registration.RegistrationViewParams

interface UserRepository {
    fun create(registrationViewParams: RegistrationViewParams)

    fun getUser(id: Long): User

    fun login(username: String, password: String): Long
}