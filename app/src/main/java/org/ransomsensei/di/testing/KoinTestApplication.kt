package org.ransomsensei.di.testing

import android.app.Application
import org.ransomsensei.activity_lockscreen.di.lockScreenActivityModule
import org.ransomsensei.data.di.testing.testDataRepositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

/** Test [Application] using koin dependency injection. */
class KoinTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinTestApplication)
            modules(
                testDataRepositoryModule,
                lockScreenActivityModule,
            )
        }
    }
}