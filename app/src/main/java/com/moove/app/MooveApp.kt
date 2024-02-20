package com.moove.app

import android.app.Application
import com.moove.app.di.coroutineModule
import com.moove.app.di.exceptionsModule
import com.moove.app.di.mainModule
import com.moove.app.di.netModule
import com.moove.app.di.ticketsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class MooveApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        startKoin {
            androidContext(this@MooveApp)
//            androidLogger(if (BuildConfig.DEBUG) Level.INFO else Level.NONE)
            modules(
                mainModule,
                coroutineModule,
                exceptionsModule,
                ticketsModule,
                netModule,
            )
        }
    }
}
