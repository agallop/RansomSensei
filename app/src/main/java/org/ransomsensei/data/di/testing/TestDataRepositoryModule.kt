package org.ransomsensei.data.di.testing

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.testing.TestRansomSenseiDataRepositoryImpl

val testDataRepositoryModule = module {
    single<RansomSenseiDataRepository> { TestRansomSenseiDataRepositoryImpl(androidContext()) }
}