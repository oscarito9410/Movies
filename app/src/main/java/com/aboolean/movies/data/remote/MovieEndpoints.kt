package com.aboolean.movies.data.remote

import com.aboolean.movies.data.model.PageMovie
import com.aboolean.movies.utils.Constants.API_KEY
import com.aboolean.movies.utils.Constants.DEFAULT_LANGUAGE
import com.aboolean.movies.utils.Constants.DEFAULT_REGION
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieEndpoints {
    @GET("popular")
    fun getPopularMovies(
            @Query("page") page: Int,
            @Query("api_key") apiKey: String = API_KEY,
            @Query("language") language: String = DEFAULT_LANGUAGE,
            @Query("region") region: String = DEFAULT_REGION
    ): Single<PageMovie>
}