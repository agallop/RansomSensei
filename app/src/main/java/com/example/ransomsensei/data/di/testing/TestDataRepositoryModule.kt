package com.example.ransomsensei.data.di.testing

import com.example.ransomsensei.data.RansomSenseiDataRepository
import com.example.ransomsensei.data.testing.TestRansomSenseiDataRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val testDataRepositoryModule = module {
    single<RansomSenseiDataRepository> { TestRansomSenseiDataRepositoryImpl(androidContext()) }
}