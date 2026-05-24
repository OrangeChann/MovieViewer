package com.it2161.a210297h.a21029hmovieviewer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.it2161.a210297h.a21029hmovieviewer.AppBarState
import com.it2161.a210297h.a21029hmovieviewer.AppScreen
import com.it2161.a210297h.a21029hmovieviewer.R
import com.it2161.a210297h.a21029hmovieviewer.data.Movie
import com.it2161.a210297h.a21029hmovieviewer.ui.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    navController: NavController,
    viewModel: MovieViewModel = viewModel(),
    onAppBarChange: (AppBarState) -> Unit
) {
    val movieDetails by viewModel.movieDetails.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val similarMovies by viewModel.similarMovies.collectAsState()

    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
        viewModel.loadMovieReviews(movieId)
        viewModel.loadSimilarMovies(movieId)
        viewModel.loadFavoriteMovies() // ✅ Load only once
    }

    SideEffect {
        isFavorite = viewModel.isMovieFavorite(movieId) // ✅ Ensure favorite status is retained
    }


    LaunchedEffect(movieDetails, isFavorite) {
        onAppBarChange(
            AppBarState(
                title = movieDetails?.title ?: "Movie Details",
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(AppScreen.LandingScreen.name) {
                            popUpTo(AppScreen.LandingScreen.name) { inclusive = true } // ✅ Ensures fresh reload
                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }

                },
                actions = {
                    IconButton(onClick = {
                        isFavorite = !isFavorite
                        viewModel.toggleFavorite(movieId, isFavorite)
                        viewModel.loadFavoriteMovies() // ✅ Ensure persistence
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            movieDetails?.let { movie ->
                Image(
                    painter = rememberAsyncImagePainter(
                        model = if (movie.posterPath.isNullOrEmpty())
                            R.drawable.stockloginimag
                        else "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                    ),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )

                Text(text = "Release Date: ${movie.releaseDate}", fontWeight = FontWeight.Bold)
                Text(text = "Run Time: ${movie.runtime}", fontWeight = FontWeight.Bold)
                Text(text = "Vote Count: ${movie.voteCount}", fontWeight = FontWeight.Bold)
                Text(text = "Vote Average: ${movie.voteAverage}", fontWeight = FontWeight.Bold)
                Text(text = "Revenue: ${movie.revenue}", fontWeight = FontWeight.Bold)

                Text(text = "Overview", fontWeight = FontWeight.Bold)
                Text(text = movie.overview)

                Spacer(modifier = Modifier.height(16.dp))

                // ✅ **"Go to Favorites" Button**
                Button(
                    onClick = { navController.navigate("favorites") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text("Go to Favorites")
                }

                // ✅ **Similar Movies**
                if (similarMovies.isNotEmpty()) {
                    Text(
                        text = "Similar Movies",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    LazyRow {
                        items(similarMovies) { movie ->
                            SimilarMovieItem(movie, navController)
                        }
                    }
                } else {
                    Text(
                        text = "No similar movies available.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ✅ **Reviews**
                if (reviews.isNotEmpty()) {
                    Text(
                        text = "Reviews",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    reviews.forEach { review ->
                        ReviewItem(review)
                    }
                } else {
                    Text(
                        text = "No reviews available.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } ?: CircularProgressIndicator()
        }
    }
}




@Composable
fun ReviewItem(review: com.it2161.a210297h.a21029hmovieviewer.data.Review) {
    var expanded by remember { mutableStateOf(false) }
    val maxChars = 150 // ✅ Limit before showing Read More

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Author: ${review.author}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (review.content.length > maxChars) {
                if (expanded) {
                    Text(text = review.content, fontSize = 14.sp, color = Color.DarkGray)
                    TextButton(onClick = { expanded = false }) {
                        Text("Read Less", color = Color.Blue)
                    }
                } else {
                    Text(text = review.content.take(maxChars) + "...", fontSize = 14.sp, color = Color.DarkGray)
                    TextButton(onClick = { expanded = true }) {
                        Text("Read More", color = Color.Blue)
                    }
                }
            } else {
                Text(text = review.content, fontSize = 14.sp, color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun SimilarMovieItem(movie: Movie, navController: NavController) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .clickable { navController.navigate("MovieDetail/${movie.id}") },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                ),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
