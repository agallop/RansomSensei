package com.example.ransomsensei.di

import android.app.Application
import com.example.ransomsensei.activity_lockscreen.di.lockScreenActivityModule
import com.example.ransomsensei.activity_main.di.mainActivityModule
import com.example.ransomsensei.activity_onboarding.di.onboardingActivityModule
import com.example.ransomsensei.data.di.dataRepositoryModule
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