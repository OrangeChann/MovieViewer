package com.it2161.a210297h.a21029hmovieviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables modern edge-to-edge rendering
        setContent {
            AppMain() // Call AppMain from MovieViewer.kt
        }
    }
}
