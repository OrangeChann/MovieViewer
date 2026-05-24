package com.it2161.a210297h.a21029hmovieviewer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.it2161.a210297h.a21029hmovieviewer.ui.UserViewModel
import com.it2161.a210297h.a21029hmovieviewer.AppScreen

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: UserViewModel = viewModel()
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf("") }

    // Form validation errors
    var userIdError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val loginResult by viewModel.loginResult

    // Check if user is already logged in
    LaunchedEffect(Unit) {
        val sessionUserId = viewModel.getUserSession()
        if (sessionUserId != null) {
            navController.navigate(AppScreen.LandingScreen.name)
        }
    }

    LaunchedEffect(loginResult) {
        if (loginResult != null) {
            viewModel.saveUserSession(loginResult!!.userId)
            viewModel.loadUser(loginResult!!.userId)
            navController.navigate(AppScreen.LandingScreen.name)
        } else if (userId.isNotEmpty() && password.isNotEmpty()) {
            loginError = "Invalid UserID or Password"
        }
    }

    Column(
        modifier = modifier.fillMaxSize().wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userId,
            onValueChange = {
                userId = it
                userIdError = if (it.length < 4) "UserID must be at least 4 characters" else null
            },
            label = { Text("User ID") },
            isError = userIdError != null,
            modifier = Modifier.padding(16.dp)
        )
        if (userIdError != null) {
            Text(userIdError!!, color = Color.Red, modifier = Modifier.padding(start = 16.dp))
        }

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = if (it.isEmpty()) "Password is required" else null
            },
            label = { Text("Password") },
            isError = passwordError != null,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (showPassword) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.padding(16.dp)
        )
        if (passwordError != null) {
            Text(passwordError!!, color = Color.Red, modifier = Modifier.padding(start = 16.dp))
        }

        if (loginError.isNotEmpty()) {
            Text(text = loginError, color = Color.Red, modifier = Modifier.padding(16.dp))
        }

        Button(
            onClick = {
                // Perform validation before attempting login
                userIdError = if (userId.length < 4) "UserID must be at least 4 characters" else null
                passwordError = if (password.isEmpty()) "Password is required" else null

                // Only proceed if there are no errors
                if (userIdError == null && passwordError == null) {
                    viewModel.loginUser(userId, password)
                }
            },
            enabled = userIdError == null && passwordError == null,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Login")
        }

        TextButton(
            onClick = { navController.navigate(AppScreen.Register.name) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Register")
        }
    }
}
