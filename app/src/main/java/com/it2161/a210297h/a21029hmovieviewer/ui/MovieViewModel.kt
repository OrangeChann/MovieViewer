package com.it2161.a210297h.a21029hmovieviewer.ui

    import android.app.Application
    import android.util.Log
    import androidx.lifecycle.AndroidViewModel
    import androidx.lifecycle.viewModelScope
    import com.it2161.a210297h.a21029hmovieviewer.data.*
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.launch

    class MovieViewModel(application: Application) : AndroidViewModel(application) {

        private val movieDao = MovieDatabase.getDatabase(application).movieDao()
        private val repository = MovieRepository(MovieApiService.api, movieDao, application)

        private val _movies = MutableStateFlow<List<Movie>>(emptyList())
        val movies: StateFlow<List<Movie>> = _movies


        private val _movieDetails = MutableStateFlow<Movie?>(null)
        val movieDetails: StateFlow<Movie?> = _movieDetails

        private val _reviews = MutableStateFlow<List<Review>>(emptyList()) // ✅ Added reviews
        val reviews: StateFlow<List<Review>> = _reviews

        fun loadMovies(category: String) {
            viewModelScope.launch {
                val moviesList = repository.getMovies(category)
                _movies.value = moviesList
                Log.d("MovieViewModel", "Loaded ${moviesList.size} movies for category: $category")
            }
        }

        fun loadMovieDetails(movieId: Int) {
            viewModelScope.launch {
                val details = repository.getMovieDetails(movieId)
                _movieDetails.value = details
                Log.d("MovieViewModel", "Loaded details for movieId: $movieId")
            }
        }

        fun loadMovieReviews(movieId: Int) {
            viewModelScope.launch {
                val reviewsList = repository.getMovieReviews(movieId).reviews
                _reviews.value = reviewsList
                Log.d("MovieViewModel", "Loaded ${reviewsList.size} reviews for movieId: $movieId")
            }
        }

        private val _similarMovies = MutableStateFlow<List<Movie>>(emptyList())
        val similarMovies: StateFlow<List<Movie>> = _similarMovies

        fun loadSimilarMovies(movieId: Int) {
            viewModelScope.launch {
                val movies = repository.getSimilarMovies(movieId)
                _similarMovies.value = movies
            }
        }

        private val _searchedMovies = MutableStateFlow<List<Movie>>(emptyList())
        val searchedMovies: StateFlow<List<Movie>> = _searchedMovies

        fun searchMovies(query: String) {
            viewModelScope.launch {
                val results = repository.getSearchedMovies(query)
                _searchedMovies.value = results
            }
        }

        //New
        private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
        val favoriteMovies: StateFlow<List<Movie>> = _favoriteMovies

        fun loadFavoriteMovies() {
            viewModelScope.launch {
                _favoriteMovies.value = repository.getFavoriteMovies()
            }
        }

        fun isMovieFavorite(movieId: Int): Boolean {
            return favoriteMovies.value.any { it.id == movieId }
        }


        fun toggleFavorite(movieId: Int, isFavorite: Boolean) {
            viewModelScope.launch {
                repository.toggleFavorite(movieId, isFavorite)
                _favoriteMovies.value = repository.getFavoriteMovies() // ✅ Force UI to update immediately
            }
        }




    }

