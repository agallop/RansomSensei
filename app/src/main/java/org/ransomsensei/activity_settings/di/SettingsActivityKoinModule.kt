package org.ransomsensei.activity_settings.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.ransomsensei.activity_settings.viewmodels.SettingsViewModel

val settingsActivityModule = module {
    viewModel { SettingsViewModel(get()) }
}