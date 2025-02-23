package org.ransomsensei.di

import android.app.Application
import org.ransomsensei.activity_lockscreen.di.lockScreenActivityModule
import org.ransomsensei.activity_main.di.mainActivityModule
import org.ransomsensei.activity_onboarding.di.onboardingActivityModule
import org.ransomsensei.data.di.dataRepositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinApplication)
            modules(
                dataRepositoryModule,
                mainActivityModule,
                lockScreenActivityModule,
                onboardingActivityModule
            )
        }
    }
}