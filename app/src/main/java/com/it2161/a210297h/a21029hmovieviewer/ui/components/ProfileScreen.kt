package com.it2161.a210297h.a21029hmovieviewer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.it2161.a210297h.a21029hmovieviewer.AppBarState
import com.it2161.a210297h.a21029hmovieviewer.ui.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel(),
    onAppBarChange: (AppBarState) -> Unit // ✅ Pass AppBar update function
) {
    val sessionUserId = viewModel.getUserSession() // ✅ Get logged-in user ID
    var user by remember { mutableStateOf(viewModel.loginResult.value) }

    // ✅ Fetch user details when session ID is found
    LaunchedEffect(sessionUserId) {
        if (sessionUserId != null) {
            viewModel.loadUser(sessionUserId) // ✅ Load full user details
        }
    }

    // ✅ Observe user data updates
    user = viewModel.loginResult.value

    // ✅ Update App Bar Title when ProfileScreen is loaded
    LaunchedEffect(user) {
        onAppBarChange(
            AppBarState(
                title = "User Profile", // ✅ Show Preferred Name or "User Profile"
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        )
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (user != null) {
                Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "User ID: ${user!!.userId}", style = MaterialTheme.typography.headlineSmall)
                Text(text = "Preferred Name: ${user!!.preferredName}", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    viewModel.logoutUser()
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                }) {
                    Text("Logout")
                }
            } else {
                Text("No user logged in.")
            }
        }
    }
}
