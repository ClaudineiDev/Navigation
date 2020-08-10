package com.claudinei.navigation.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.claudinei.navigation.data.model.User
import com.claudinei.navigation.ui.registration.RegistrationViewParams

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey val id: Long = 0,
    val name: String,
    val bio: String,
    val username: String,
    val password: String
)

// Com essa "Extension" é feita a conversão de RegistrationViewParams para UserEntity
fun RegistrationViewParams.toUserEntity(): UserEntity{
    return with(this){
        UserEntity(
            name = this.name,
            bio = this.bio,
            username = this.username,
            password = this.password
        )
    }
}
// Com essa "Extension" é feita a conversão de UserEntity para User
fun UserEntity.toUser(): User {
    return User(
        id = this.id.toString(),
        name = this.name,
        bio = this.bio
    )
}


