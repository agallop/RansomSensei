package com.example.ransomsensei.di

import com.example.ransomsensei.data.RansomSenseiDataRepositoryImpl
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.RansomSenseiDataRepository
import com.example.ransomsensei.activity_lockscreen.viewmodels.LockScreenViewModel
import com.example.ransomsensei.activity_main.viewmodels.CardSetsViewModel
import com.example.ransomsensei.activity_main.viewmodels.AddEditCardSetViewModel
import com.example.ransomsensei.activity_main.viewmodels.AddEditCardViewModel
import com.example.ransomsensei.activity_main.viewmodels.CardSetDetailsViewModel
import com.example.ransomsensei.activity_main.viewmodels.MainActivityViewModel
import com.example.ransomsensei.activity_onboarding.di.onboardingActivityKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RansomSenseiDatabase.getInstance(androidContext()) }
    single { RansomSenseiDataStoreManager(androidContext()) }
    single { androidContext().packageManager }
    single<RansomSenseiDataRepository> { RansomSenseiDataRepositoryImpl(get(), get(), get()) }
    onboardingActivityKoinModules
}

val viewModelModule = module {
    viewModel { CardSetsViewModel(get()) }
    viewModel { CardSetDetailsViewModel(get()) }
    viewModel { LockScreenViewModel(get()) }
    viewModel { AddEditCardSetViewModel(get()) }
    viewModel { AddEditCardViewModel(get()) }
    viewModel { MainActivityViewModel(get()) }
}