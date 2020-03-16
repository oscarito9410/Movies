package com.aboolean.movies.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PageMovie(
        @Json(name = "page")
        val page: Int? = null,
        @Json(name = "total_pages")
        val totalPage: Int? = null,
        @Json(name = "results")
        val results: List<MovieData>? = null
)