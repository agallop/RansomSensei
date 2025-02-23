package com.example.ransomsensei.di.testing

import android.app.Application
import com.example.ransomsensei.di.appModule
import com.example.ransomsensei.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

/** Test [Application] using koin dependency injection. */
class KoinTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinTestApplication)
            modules(appModule, testModule, viewModelModule)
        }
    }
}