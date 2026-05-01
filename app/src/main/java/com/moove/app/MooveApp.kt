package com.moove.app

import android.app.Application
import com.moove.app.di.coroutineModule
import com.moove.app.di.deepLinkModule
import com.moove.app.di.exceptionsModule
import com.moove.app.di.mainModule
import com.moove.app.di.netModule
import com.moove.movies.di.moviesModule
import com.moove.tickets.di.ticketsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class MooveApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        startKoin {
            androidContext(this@MooveApp)
            modules(
                mainModule,
                coroutineModule,
                exceptionsModule,
                ticketsModule,
                moviesModule,
                netModule,
                deepLinkModule,
            )
        }
    }
}
