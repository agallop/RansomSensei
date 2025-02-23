package com.example.ransomsensei.activity_lockscreen.di

import com.example.ransomsensei.activity_lockscreen.viewmodels.LockScreenViewModel
import com.example.ransomsensei.data.di.dataRepositoryModule
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val lockScreenActivityModule = module {
    dataRepositoryModule
    viewModel { LockScreenViewModel(get()) }
}