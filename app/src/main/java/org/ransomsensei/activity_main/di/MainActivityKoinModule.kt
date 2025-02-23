package org.ransomsensei.activity_main.di

import org.ransomsensei.activity_main.viewmodels.AddEditCardSetViewModel
import org.ransomsensei.activity_main.viewmodels.AddEditCardViewModel
import org.ransomsensei.activity_main.viewmodels.CardSetDetailsViewModel
import org.ransomsensei.activity_main.viewmodels.CardSetsViewModel
import org.ransomsensei.activity_main.viewmodels.MainActivityViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainActivityModule = module {
    viewModel { CardSetsViewModel(get()) }
    viewModel { CardSetDetailsViewModel(get()) }
    viewModel { AddEditCardSetViewModel(get()) }
    viewModel { AddEditCardViewModel(get()) }
    viewModel { MainActivityViewModel(get()) }
}