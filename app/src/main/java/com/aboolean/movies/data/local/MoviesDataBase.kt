package com.aboolean.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aboolean.movies.data.model.MovieData

@Database(entities = [MovieData::class], version = 1, exportSchema = false)
abstract class MoviesDataBase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}