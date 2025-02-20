package com.example.ransomsensei.koin

import com.example.ransomsensei.data.RansomSenseiDataRepositoryImpl
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.RansomSenseiDataRepository
import com.example.ransomsensei.activity_lockscreen.viewmodels.LockScreenViewModel
import com.example.ransomsensei.activity_main.viewmodels.CardSetsScreenViewModel
import com.example.ransomsensei.activity_main.viewmodels.AddEditCardSetScreenViewModel
import com.example.ransomsensei.activity_main.viewmodels.AddEditCardScreenViewModel
import com.example.ransomsensei.activity_main.viewmodels.CardSetDetailsScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RansomSenseiDatabase.getInstance(androidContext()) }
    single { RansomSenseiDataStoreManager(androidContext()) }
    single<RansomSenseiDataRepository> { RansomSenseiDataRepositoryImpl(get(), get()) }
}

val viewModelModule = module {
    viewModel { CardSetsScreenViewModel(get()) }
    viewModel { CardSetDetailsScreenViewModel(get()) }
    viewModel { LockScreenViewModel(get()) }
    viewModel { AddEditCardSetScreenViewModel(get()) }
    viewModel { AddEditCardScreenViewModel(get()) }
}