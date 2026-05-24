package com.it2161.a210297h.a21029hmovieviewer.data

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("results") val reviews: List<Review>
)

data class Review(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String
)
