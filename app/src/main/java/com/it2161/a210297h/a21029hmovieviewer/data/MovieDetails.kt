package com.it2161.a210297h.a21029hmovieviewer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_details")
data class MovieDetails(
    @PrimaryKey @SerializedName("id") val id: Int,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("adult") val isAdult: Boolean,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("title") val title: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("revenue") val revenue: Int,
    @SerializedName("genres") val genres: List<Genre> // ✅ Add Genres
)

data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
