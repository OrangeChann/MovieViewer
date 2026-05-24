package com.it2161.a210297h.a21029hmovieviewer.data


class UserRepository(private val userDao: UserDao) {

    suspend fun loginUser(userId: String, password: String): User? {
        return userDao.login(userId, password)
    }

    suspend fun registerUser(user: User) {
        userDao.register(user)
    }

    // ✅ Add function to get user details by ID
    suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)
    }

}
