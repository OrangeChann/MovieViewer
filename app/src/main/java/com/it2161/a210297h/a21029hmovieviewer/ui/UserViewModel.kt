package com.it2161.a210297h.a21029hmovieviewer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.it2161.a210297h.a21029hmovieviewer.data.User
import com.it2161.a210297h.a21029hmovieviewer.data.UserDatabase
import com.it2161.a210297h.a21029hmovieviewer.data.UserPreferences
import com.it2161.a210297h.a21029hmovieviewer.data.UserRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private val userPreferences: UserPreferences

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        userPreferences = UserPreferences(application)
    }

    private val _loginResult = mutableStateOf<User?>(null)
    val loginResult: State<User?> = _loginResult

    fun loginUser(userId: String, password: String) {
        viewModelScope.launch {
            val user = repository.loginUser(userId, password)
            _loginResult.value = user

            // Save session if login is successful
            if (user != null) {
                userPreferences.saveUserSession(user.userId)
            }
        }
    }


    fun registerUser(user: User) {
        viewModelScope.launch {
            repository.registerUser(user)
        }
    }

    fun loadUser(userId: String) {
        viewModelScope.launch {
            val user = repository.getUserById(userId) // ✅ Fetch user details from DB
            _loginResult.value = user // ✅ Store user in ViewModel state
        }
    }

    fun saveUserSession(userId: String) {
        userPreferences.saveUserSession(userId)
    }


    fun getUserSession(): String? {
        return userPreferences.getUserSession()
    }

    fun logoutUser() {
        userPreferences.clearUserSession()
        _loginResult.value = null
    }
}
