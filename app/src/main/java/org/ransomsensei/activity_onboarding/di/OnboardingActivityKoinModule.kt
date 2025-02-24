package org.ransomsensei.activity_onboarding.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.ransomsensei.activity_onboarding.viewmodels.OnboardingActivityViewModel
import org.ransomsensei.activity_onboarding.viewmodels.SetDefaultHomeAppViewModel
import org.ransomsensei.activity_onboarding.viewmodels.SetHomeActivityViewModel
import org.ransomsensei.activity_onboarding.viewmodels.StartOnboardingViewModel

val onboardingActivityModule = module {
    viewModel { SetHomeActivityViewModel(get(), get()) }
    viewModel { SetDefaultHomeAppViewModel() }
    viewModel { OnboardingActivityViewModel(get()) }
    viewModel { StartOnboardingViewModel() }
}