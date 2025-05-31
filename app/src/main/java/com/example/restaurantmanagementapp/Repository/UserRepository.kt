package com.example.restaurantmanagementapp.Repository

import com.example.restaurantmanagementapp.R_mDataBase.dao.UserDao
import com.example.restaurantmanagementapp.R_mDataBase.entity.UserEntity


class UserRepository(private val userDao: UserDao) {
    suspend fun authenticate(username: String, password: String): UserEntity? {
        return userDao.authenticateUser(username,password)
    }

    suspend fun getUserByUsername(username: String): UserEntity? {
        return userDao.getUserByUsername(username)
    }

    suspend fun insertUser(user: UserEntity): Long {
        return userDao.insert(user)
    }
    suspend fun updateUser(user: UserEntity) {
        userDao.update(user)
    }

    suspend fun deleteUsers(users: List<UserEntity>) {
        userDao.deleteAll(users)
    }

    suspend fun getUsersByRole(role: String): List<UserEntity> =
        userDao.getUsersByRole(role)

    suspend fun registerUser(username: String, password: String, role: String): Int? {
        val user = UserEntity(username = username, password = password, role = role)
        return userDao.insert(user).toInt() // Assuming insert returns rowId
    }

}