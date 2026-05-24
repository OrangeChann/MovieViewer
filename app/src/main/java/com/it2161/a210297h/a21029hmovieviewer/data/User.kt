package com.it2161.a210297h.a21029hmovieviewer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String,
    val password: String,
    val preferredName: String
)
