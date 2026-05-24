package com.it2161.a210297h.a21029hmovieviewer.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log // ✅ Import Log for debugging

class MovieRepository(private val api: MovieApi, private val movieDao: MovieDao, private val context: Context) {
    private val apiKey = "833689732be431bc1a360a88b056fe63"

    suspend fun getMovies(category: String): List<Movie> {
        return if (isNetworkAvailable()) {
            val movies = when (category) {
                "Popular" -> api.getPopularMovies(apiKey).movies
                "Top Rated" -> api.getTopRatedMovies(apiKey).movies
                "Now Playing" -> api.getNowPlayingMovies(apiKey).movies
                "Upcoming" -> api.getUpcomingMovies(apiKey).movies
                else -> emptyList()
            }.map { it.copy(category = category, posterPath = "https://image.tmdb.org/t/p/w500${it.posterPath}") } // ✅ Ensure full URL

            movieDao.clearMoviesByCategory(category)
            movieDao.insertMovies(movies)
            movies
        } else {
            movieDao.getMoviesByCategory(category)
        }
    }


    suspend fun getMovieDetails(movieId: Int): Movie {
        return if (isNetworkAvailable()) {
            val details = api.getMovieDetails(movieId, apiKey)

            val movie = Movie(
                id = details.id,
                title = details.title,
                overview = details.overview,
                posterPath = "https://image.tmdb.org/t/p/w500${details.posterPath}", // ✅ Ensure full URL is stored
                runtime = details.runtime,
                voteCount = details.voteCount,
                voteAverage = details.voteAverage,
                revenue = details.revenue,
                releaseDate = details.releaseDate,
                category = "Detail"
            )

            movieDao.insertMovies(listOf(movie)) // ✅ Save full URL in Room
            movie
        } else {
            movieDao.getMovie(movieId) ?: throw Exception("Movie details unavailable offline")
        }
    }

    suspend fun getMovieReviews(movieId: Int): ReviewResponse {
        return if (isNetworkAvailable()) {
            api.getMovieReviews(movieId, apiKey) // ✅ Fetch from API
        } else {
            ReviewResponse(emptyList()) // ✅ Return empty list if offline
        }
    }

    suspend fun getSearchedMovies(query: String): List<Movie> {
        return if (isNetworkAvailable()) {
            api.searchMovie(apiKey, query).movies // ✅ Fetch from API
        } else {
            emptyList() // ✅ No offline support for search
        }
    }

    suspend fun getSimilarMovies(movieId: Int): List<Movie> {
        return if (isNetworkAvailable()) {
            api.getSimilarMovies(movieId, apiKey).movies
        } else {
            emptyList() // No offline support for similar movies
        }
    }

    // New
    // ✅ Load favorite movies
    suspend fun getFavoriteMovies(): List<Movie> {
        return movieDao.getFavoriteMovies()
    }

    // ✅ Toggle favorite movie status
    suspend fun toggleFavorite(movieId: Int, isFavorite: Boolean) {
        movieDao.toggleFavorite(movieId, isFavorite)
    }



    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}
