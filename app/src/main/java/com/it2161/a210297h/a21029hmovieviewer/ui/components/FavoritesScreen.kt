package com.it2161.a210297h.a21029hmovieviewer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.it2161.a210297h.a21029hmovieviewer.AppBarState
import com.it2161.a210297h.a21029hmovieviewer.ui.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteMoviesScreen(
    navController: NavController,
    viewModel: MovieViewModel = viewModel(),
    onAppBarChange: (AppBarState) -> Unit
) {
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()

    // ✅ Ensure AppBar updates correctly
    LaunchedEffect(Unit) {
        onAppBarChange(
            AppBarState(
                title = "Favorite Movies",
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        )
    }


    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.loadFavoriteMovies()
    }


    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (favoriteMovies.isEmpty()) {
                Text("No favorite movies yet.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(favoriteMovies) { movie ->
                        MovieItem(movie, navController)
                    }
                }
            }
        }
    }
}

