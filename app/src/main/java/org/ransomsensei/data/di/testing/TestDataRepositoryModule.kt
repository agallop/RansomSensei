package org.ransomsensei.data.di.testing

import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.testing.TestRansomSenseiDataRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val testDataRepositoryModule = module {
    single<RansomSenseiDataRepository> { TestRansomSenseiDataRepositoryImpl(androidContext()) }
}