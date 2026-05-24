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
import com.it2161.a210297h.a21029hmovieviewer.data.User
import com.it2161.a210297h.a21029hmovieviewer.ui.UserViewModel
import com.it2161.a210297h.a21029hmovieviewer.AppScreen

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: UserViewModel = viewModel()
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var preferredName by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var registerSuccess by remember { mutableStateOf(false) }

    // Form validation errors
    var userIdError by remember { mutableStateOf<String?>(null) }
    var preferredNameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            navController.navigate(AppScreen.Login.name)
        }
    }

    Column(
        modifier = modifier.fillMaxSize().wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)

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
            value = preferredName,
            onValueChange = {
                preferredName = it
                preferredNameError = if (it.length < 2) "Preferred Name must be at least 2 characters" else null
            },
            label = { Text("Preferred Name") },
            isError = preferredNameError != null,
            modifier = Modifier.padding(16.dp)
        )
        if (preferredNameError != null) {
            Text(preferredNameError!!, color = Color.Red, modifier = Modifier.padding(start = 16.dp))
        }

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = when {
                    it.length < 6 -> "Password must be at least 6 characters"
                    !it.any { char -> char.isDigit() } -> "Password must contain at least 1 number"
                    else -> null
                }
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

        Button(
            onClick = {
                userIdError =
                    if (userId.isEmpty()) "User ID is required" else if (userId.length < 4) "UserID must be at least 4 characters" else null
                preferredNameError =
                    if (preferredName.isEmpty()) "Preferred Name is required" else if (preferredName.length < 2) "Preferred Name must be at least 2 characters" else null
                passwordError = when {
                    password.isEmpty() -> "Password is required"
                    password.length < 6 -> "Password must be at least 6 characters"
                    !password.any { char -> char.isDigit() } -> "Password must contain at least 1 number"
                    else -> null
                }
                // Only proceed if there are NO errors
                if (userIdError == null && preferredNameError == null && passwordError == null) {
                    viewModel.registerUser(User(userId, password, preferredName))
                    registerSuccess = true
                }
            },
            enabled = userIdError == null && preferredNameError == null && passwordError == null,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Register")
        }

        TextButton(
            onClick = { navController.navigate(AppScreen.Login.name) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Already have an account? Login")
        }
    }
}
