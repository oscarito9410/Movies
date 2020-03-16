package com.aboolean.movies.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aboolean.movies.data.model.MovieData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movie: MovieData): Completable

    @Query("SELECT * FROM movies WHERE page =(:page)")
    fun getPopularLocalMovies(page: Int): Single<List<MovieData>>

    @Query("SELECT*FROM movies WHERE isFavorite = 1")
    fun getFavoritesMovies(): LiveData<List<MovieData>>

    @Query("UPDATE movies SET isFavorite = (:isFavorite) WHERE id = (:id)")
    fun updateFavorite(id: Long, isFavorite: Boolean): Completable
}