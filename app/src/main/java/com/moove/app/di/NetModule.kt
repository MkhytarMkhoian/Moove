package com.moove.app.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.zacsweers.moshix.adapters.AdaptedBy
import org.koin.dsl.module

val netModule = module {

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(AdaptedBy.Factory())
            .build()
    }
}
