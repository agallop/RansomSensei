package org.ransomsensei.data.di

import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.RansomSenseiDataRepositoryImpl
import org.ransomsensei.data.RansomSenseiDataStoreManager
import org.ransomsensei.data.RansomSenseiDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataRepositoryModule = module {
    single { RansomSenseiDatabase.getInstance(androidContext()) }
    single { RansomSenseiDataStoreManager(androidContext()) }
    single { androidContext().packageManager }
    single<RansomSenseiDataRepository> { RansomSenseiDataRepositoryImpl(get(), get(), get()) }
}