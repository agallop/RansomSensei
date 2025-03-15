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
package org.ransomsensei.activity_onboarding.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ransomsensei.activity_onboarding.util.Destination
import org.ransomsensei.data.RansomSenseiDataRepository

class OnboardingActivityViewModel(private val _repository: RansomSenseiDataRepository) : ViewModel() {
    val destinationGraph = mutableStateMapOf<Destination, Destination>()
    var isLoading by mutableStateOf(true)
    var startDestination by mutableStateOf<Destination>(Destination.Finish)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val wasInOnboarding = _repository.getIsInOnboarding()
            _repository.setIsInOnboarding(true)
            var nextDestination: Destination = Destination.OnboardingCompleteScreen

            if (!_repository.isDefaultHomeApp()) {
                destinationGraph.put(Destination.SetDefaultHomeAppScreen, nextDestination)
                nextDestination = Destination.SetDefaultHomeAppScreen
            }

            if (_repository.getHomePackage().first() == "") {
                destinationGraph.put(Destination.SetHomeActivityScreen, nextDestination)
                nextDestination = Destination.SetHomeActivityScreen
            }

            if (!wasInOnboarding) {
                destinationGraph.put(Destination.StartOnBoardingScreen, nextDestination)
                startDestination = Destination.StartOnBoardingScreen
            } else {
                startDestination = nextDestination
            }
            withContext(Dispatchers.Main) {
                isLoading = false
            }
        }
    }

    fun finishOnboarding() {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.setIsInOnboarding(false)
        }
    }
}