package com.example.restaurantmanagementapp.R_mDataBase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.restaurantmanagementapp.R_mDataBase.entity.UserEntity

@Dao
interface UserDao: BaseDao<UserEntity> {
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username:String): UserEntity? // can return a value or null

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun authenticateUser(username: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE role = :role")
    suspend fun getUsersByRole(role: String): List<UserEntity>

    @Delete
    suspend fun deleteAll(users: List<UserEntity>)

}