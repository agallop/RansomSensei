package org.ransomsensei.activity_lockscreen.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.ransomsensei.activity_lockscreen.viewmodels.HomeActivityViewModel
import org.ransomsensei.activity_lockscreen.viewmodels.LockScreenViewModel
import org.ransomsensei.data.di.dataRepositoryModule

val lockScreenActivityModule = module {
    dataRepositoryModule
    viewModel { HomeActivityViewModel(get()) }
    viewModel { LockScreenViewModel(get()) }
}