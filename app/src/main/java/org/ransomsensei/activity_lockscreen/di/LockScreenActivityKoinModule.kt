package org.ransomsensei.activity_lockscreen.di

import org.ransomsensei.activity_lockscreen.viewmodels.LockScreenViewModel
import org.ransomsensei.data.di.dataRepositoryModule
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val lockScreenActivityModule = module {
    dataRepositoryModule
    viewModel { LockScreenViewModel(get()) }
}