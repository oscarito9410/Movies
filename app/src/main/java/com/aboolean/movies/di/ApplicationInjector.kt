package com.aboolean.movies.di

import androidx.room.Room
import com.aboolean.movies.data.local.MoviesDao
import com.aboolean.movies.data.local.MoviesDataBase
import com.aboolean.movies.data.remote.MovieEndpoints
import com.aboolean.movies.data.repository.FavoriteMoviesRepository
import com.aboolean.movies.data.repository.FavoriteMoviesRepositoryImpl
import com.aboolean.movies.data.repository.PopularMoviesRepository
import com.aboolean.movies.data.repository.PopularMoviesRepositoryImpl
import com.aboolean.movies.domain.usecase.GetFavoritesMoviesUseCase
import com.aboolean.movies.domain.usecase.GetFavoritesMoviesUseCaseImpl
import com.aboolean.movies.domain.usecase.GetPopularMoviesUseCase
import com.aboolean.movies.domain.usecase.GetPopularMoviesUseCaseImpl
import com.aboolean.movies.ui.favorite.FavoritesMoviesViewModel
import com.aboolean.movies.ui.popular.PopularMoviesViewModel
import com.aboolean.movies.utils.Constants.URL_BASE
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val ApplicationModule = module {
    factory<PopularMoviesRepository> {
        PopularMoviesRepositoryImpl(get() as MovieEndpoints, get() as MoviesDao)
    }

    factory<FavoriteMoviesRepository> {
        FavoriteMoviesRepositoryImpl(get() as MoviesDao)
    }

    factory<GetPopularMoviesUseCase> {
        GetPopularMoviesUseCaseImpl(get() as PopularMoviesRepository,
                get() as FavoriteMoviesRepository)
    }
    viewModel {
        PopularMoviesViewModel(get() as GetPopularMoviesUseCase)
    }

    factory<GetFavoritesMoviesUseCase> {
        GetFavoritesMoviesUseCaseImpl(get() as FavoriteMoviesRepository)
    }

    viewModel {
        FavoritesMoviesViewModel(get() as GetFavoritesMoviesUseCase)
    }
}


val NetworkModule = module {

    single {
        Room.databaseBuilder(androidContext(), MoviesDataBase::class.java, "movies_databse").build()
    }

    single { get<MoviesDataBase>().moviesDao() }

    single {
        Retrofit.Builder()
                .baseUrl(URL_BASE)
                .client(get())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    single {
        StethoInterceptor()
    }

    factory {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return@factory loggingInterceptor
    }


    single {
        OkHttpClient.Builder()
                .addInterceptor(get<HttpLoggingInterceptor>())
                .addNetworkInterceptor(get<StethoInterceptor>())
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
    }

    single { get<Retrofit>().create(MovieEndpoints::class.java) }
}