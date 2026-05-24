package com.it2161.a210297h.a21029hmovieviewer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val movies: List<Movie>
)

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("vote_count") val voteCount: Int?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("revenue") val revenue: Int?,
    val category: String,
    val isFavorite: Boolean = false

)




