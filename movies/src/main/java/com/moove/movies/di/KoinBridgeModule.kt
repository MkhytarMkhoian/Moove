package com.moove.movies.di

import com.moove.core.exception.ExceptionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent

@Module
@InstallIn(SingletonComponent::class)
internal object KoinBridgeModule {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideExceptionHandler(): ExceptionHandler =
        KoinJavaComponent.get(ExceptionHandler::class.java)
}
