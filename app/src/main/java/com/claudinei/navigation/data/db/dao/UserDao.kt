package com.claudinei.navigation.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.claudinei.navigation.data.db.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: UserEntity)

    @Query("SELECT * FROM USER WHERE ID = :id")
    fun getUser(id: Long): UserEntity

    @Query("SELECT ID FROM USER WHERE USERNAME = :username and password")
    fun login(username: String, password: String):Long
}