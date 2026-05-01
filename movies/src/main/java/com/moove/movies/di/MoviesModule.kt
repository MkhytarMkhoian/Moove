package com.moove.movies.di

import com.moove.core.okhttp.ConnectionErrorInterceptor
import com.moove.movies.BuildConfig
import com.moove.movies.data.MoviesDataRepository
import com.moove.movies.data.net.AuthorizationInterceptor
import com.moove.movies.data.net.MoviesRemoteDataSource
import com.moove.movies.data.net.api.TmdbApi
import com.moove.movies.domain.MoviesRepository
import com.moove.movies.domain.use_cases.GetMovieDetailsUseCase
import com.moove.movies.domain.use_cases.GetPopularMoviesUseCase
import com.moove.movies.presentation.details.MovieDetailsNavigator
import com.moove.movies.presentation.details.MovieDetailsViewModel
import com.moove.movies.presentation.list.MovieListNavigator
import com.moove.movies.presentation.list.MovieListViewModel
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

internal const val TMDB_OKHTTP = "tmdb_okhttp"
internal const val TMDB_RETROFIT = "tmdb_retrofit"

val moviesModule = module {

    // region domain
    factory { GetPopularMoviesUseCase(get()) }
    factory { GetMovieDetailsUseCase(get()) }
    // endregion

    // region data
    single { MoviesRemoteDataSource(get()) }
    single<MoviesRepository> {
        MoviesDataRepository(
            remoteDataSource = get(),
            backgroundDispatcher = Dispatchers.IO,
        )
    }
    // endregion

    // region net (TMDB)
    single(named(TMDB_OKHTTP)) {
        val logger = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        OkHttpClient.Builder()
            .addInterceptor(ConnectionErrorInterceptor())
            .addInterceptor(AuthorizationInterceptor(BuildConfig.TMDB_API_KEY))
            .addInterceptor(logger)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }
    single(named(TMDB_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .client(get(named(TMDB_OKHTTP)))
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
    single { get<Retrofit>(named(TMDB_RETROFIT)).create<TmdbApi>() }
    // endregion

    // region navigators
    factory { MovieListNavigator(get(), get()) }
    factory { MovieDetailsNavigator(get()) }
    // endregion

    // region viewmodels
    viewModel {
        MovieListViewModel(
            exceptionHandler = get(),
            getPopularMoviesUseCase = get(),
        )
    }
    viewModel { (movieId: Long) ->
        MovieDetailsViewModel(
            exceptionHandler = get(),
            movieId = movieId,
            getMovieDetailsUseCase = get(),
        )
    }
    // endregion
}

private const val TIMEOUT_SECONDS = 30L
