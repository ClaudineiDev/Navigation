package com.claudinei.navigation.data.repository

import com.claudinei.navigation.data.db.dao.UserDao
import com.claudinei.navigation.data.db.toUser
import com.claudinei.navigation.data.db.toUserEntity
import com.claudinei.navigation.data.model.User
import com.claudinei.navigation.ui.registration.RegistrationViewParams

class UserDbDataSource(
    private val userDao: UserDao
): UserRepository {
    override fun create(registrationViewParams: RegistrationViewParams) {
        val userEntity = registrationViewParams.toUserEntity()
        userDao.save(userEntity)
    }

    override fun getUser(id: Long): User {
        return userDao.getUser(id).toUser()
    }

    override fun login(username: String, password: String): Long {
        return userDao.login(username, password)
    }
}