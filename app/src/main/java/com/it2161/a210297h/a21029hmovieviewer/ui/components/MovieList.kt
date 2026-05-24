package com.it2161.a210297h.a21029hmovieviewer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.it2161.a210297h.a21029hmovieviewer.data.Movie

@Composable
fun MovieList(movies: List<Movie>, navController: NavController) {
    LazyColumn {
        items(movies) { movie ->
            MovieItem(movie, navController)
        }
    }
}

@Composable
fun MovieItem(movie: Movie, navController: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("MovieDetail/${movie.id}") {
                    navController.navigate("MovieDetail/${movie.id}")
                    launchSingleTop = true // Prevents multiple instances
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp) // ✅ Keeps poster large
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = movie.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = "Release: ${movie.releaseDate}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
