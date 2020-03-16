package com.aboolean.movies.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(val id: Long,
                 val title: String,
                 val overview: String,
                 val releaseDate: String,
                 val voteAverage: Double,
                 val voteCount: Long,
                 val posterPath: String,
                 val page: Int,
                 var isFavorite: Boolean) : Parcelable
