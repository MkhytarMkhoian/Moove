package com.moove.movies.di

import com.moove.movies.data.MoviesDataRepository
import com.moove.movies.domain.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindMoviesRepository(impl: MoviesDataRepository): MoviesRepository
}
