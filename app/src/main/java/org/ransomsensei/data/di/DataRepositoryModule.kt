package org.ransomsensei.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.RansomSenseiDataRepositoryImpl
import org.ransomsensei.data.RansomSenseiDataStoreManager
import org.ransomsensei.data.RansomSenseiDatabase

val dataRepositoryModule = module {
    single { androidContext().packageManager }
    single<RansomSenseiDataRepository> {
        RansomSenseiDataRepositoryImpl(
            _packageManager = get(),
            database = RansomSenseiDatabase.createInstance(androidContext()),
            _dataStoreManager = RansomSenseiDataStoreManager(androidContext())
        )
    }
}