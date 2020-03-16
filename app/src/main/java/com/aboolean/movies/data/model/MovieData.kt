package com.aboolean.movies.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "movies", indices = [Index(value = ["title"], unique = true)])
data class MovieData(
        @PrimaryKey
        val id: Long = 0,
        @ColumnInfo
        var title: String? = null,
        @ColumnInfo
        val overview: String? = null,
        @ColumnInfo
        @Json(name = "release_date")
        val releaseDate: String? = null,
        @ColumnInfo
        @Json(name = "vote_average")
        val voteAverage: Double? = null,
        @ColumnInfo
        @Json(name = "vote_count")
        val voteCount: Long? = null,
        @Json(name = "poster_path")
        val posterPath: String? = null,
        @ColumnInfo
        val isFavorite: Boolean? = null,
        @ColumnInfo
        var page: Int? = null
)

