package com.it2161.a210297h.a21029hmovieviewer.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserSession(userId: String) {
        sharedPreferences.edit().putString("USER_ID", userId).apply()
    }

    fun getUserSession(): String? {
        return sharedPreferences.getString("USER_ID", null)
    }

    fun clearUserSession() {
        sharedPreferences.edit().remove("USER_ID").apply()
    }
}
