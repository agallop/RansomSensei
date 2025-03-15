/*
 *  Copyright (c) 2025 Anthony Gallop <agallopdev@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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