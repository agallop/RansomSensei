package com.example.ransomsensei.data.di

import com.example.ransomsensei.data.RansomSenseiDataRepository
import com.example.ransomsensei.data.RansomSenseiDataRepositoryImpl
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataRepositoryModule = module {
    single { RansomSenseiDatabase.getInstance(androidContext()) }
    single { RansomSenseiDataStoreManager(androidContext()) }
    single { androidContext().packageManager }
    single<RansomSenseiDataRepository> { RansomSenseiDataRepositoryImpl(get(), get(), get()) }
}