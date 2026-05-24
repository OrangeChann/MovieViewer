package com.it2161.a210297h.a21029hmovieviewer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.it2161.a210297h.a21029hmovieviewer.AppBarState
import com.it2161.a210297h.a21029hmovieviewer.ui.MovieViewModel
import com.it2161.a210297h.a21029hmovieviewer.ui.UserViewModel

@Composable
fun LandingScreen(
    viewModel: MovieViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(), // ✅ Manage user state
    navController: NavController,
    onAppBarChange: (AppBarState) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Popular") }

    val sessionUserId = userViewModel.getUserSession() // ✅ Get logged-in user ID
    var user by remember { mutableStateOf(userViewModel.loginResult.value) }

    // ✅ Fetch user details when session ID is found
    LaunchedEffect(sessionUserId) {
        if (sessionUserId != null) {
            userViewModel.loadUser(sessionUserId) // ✅ Load full user details
        }
    }

    // ✅ Observe user data updates
    user = userViewModel.loginResult.value

    // ✅ Ensure AppBar shows the preferred name instead of "Landing"
    LaunchedEffect(user) {
        onAppBarChange(
            AppBarState(
                title = "Hi, ${user?.preferredName ?: "Welcome"}", // ✅ Show Preferred Name or "Welcome"
                navigationIcon = { }, // No back button for LandingScreen
                actions = {
                    var expanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Favorites") },
                            onClick = {
                                expanded = false
                                navController.navigate("favorites")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("View Profile") },
                            onClick = {
                                expanded = false
                                navController.navigate("profile")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                expanded = false
                                userViewModel.logoutUser() // ✅ Clear session
                                navController.navigate("Login") {
                                    popUpTo("Login") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            )
        )
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.loadMovies("Popular")
        viewModel.loadFavoriteMovies() // ✅ Ensure favorites reload when navigating back
    }


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // ✅ Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
                viewModel.searchMovies(newQuery)
            },
            label = { Text("Search Movies") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Category Dropdown
        if (searchQuery.isEmpty()) {
            DropdownMenu(selectedCategory) { category ->
                selectedCategory = category
                viewModel.loadMovies(category)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Movie List (Automatically Shows Popular on Login)
        val movies = if (searchQuery.isEmpty()) viewModel.movies.collectAsState().value else viewModel.searchedMovies.collectAsState().value

        if (movies.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))
        } else {
            LazyColumn {
                items(movies) { movie ->
                    MovieSearchItem(movie, navController)
                }
            }
        }
    }
}

@Composable
fun MovieSearchItem(movie: com.it2161.a210297h.a21029hmovieviewer.data.Movie, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("MovieDetail/${movie.id}") }, // ✅ Click to navigate to MovieDetail
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp) // ✅ Rounded corners
    ) {
        Column {
            // Movie Poster
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp) // 🔥 Increase height for bigger poster
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            // Movie Title & Release Date
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = movie.releaseDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun DropdownMenu(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Popular", "Top Rated", "Now Playing", "Upcoming")

    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selectedCategory)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}
