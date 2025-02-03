package com.example.ransomsensei.koin

import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.viewmodel.LockScreenViewModel
import com.example.ransomsensei.viewmodel.MainScreenViewModel
import com.example.ransomsensei.viewmodel.cardset.AddCardSetViewModel
import com.example.ransomsensei.viewmodel.cardset.CardSetViewModel
import com.example.ransomsensei.viewmodel.cardset.EditCardSetViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RansomSenseiDatabase.getInstance(androidContext()) }
    single { RansomSenseiDataStoreManager(androidContext()) }
}

val viewModelModule = module {
    viewModel { MainScreenViewModel(get(), get()) }
    viewModel { CardSetViewModel(get()) }
    viewModel { LockScreenViewModel(get(), get()) }
    viewModel { AddCardSetViewModel(get()) }
    viewModel { EditCardSetViewModel(get()) }
}