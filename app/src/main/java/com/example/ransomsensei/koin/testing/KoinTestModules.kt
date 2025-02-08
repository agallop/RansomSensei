package com.example.ransomsensei.koin.testing

import com.example.ransomsensei.data.RansomSenseiDataRepository
import com.example.ransomsensei.data.testing.TestRansomSenseiDataRepositoryImpl
import org.koin.dsl.module

val testModule  = module {
    single<RansomSenseiDataRepository> { TestRansomSenseiDataRepositoryImpl() }
}