package com.example.ransomsensei.activity_onboarding.di

import com.example.ransomsensei.activity_onboarding.viewmodels.OnboardingActivityViewModel
import com.example.ransomsensei.activity_onboarding.viewmodels.SetDefaultHomeAppViewModel
import com.example.ransomsensei.activity_onboarding.viewmodels.SetHomeActivityViewModel
import com.example.ransomsensei.activity_onboarding.viewmodels.StartOnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingActivityModule = module {
    viewModel { SetHomeActivityViewModel(get(), get()) }
    viewModel { SetDefaultHomeAppViewModel() }
    viewModel { OnboardingActivityViewModel(get()) }
    viewModel { StartOnboardingViewModel() }
}