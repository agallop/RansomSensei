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
package org.ransomsensei.activity_main.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.ransomsensei.data.RansomSenseiDataRepository

class MainActivityViewModel(private val _repository: RansomSenseiDataRepository) : ViewModel() {
    var needToLaunchOnboardingActivity by mutableStateOf(false)
        private set

    init {
        checkOnboardingStatus()
    }

    fun onActivityResume() {
        needToLaunchOnboardingActivity = false
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            needToLaunchOnboardingActivity = _repository.getHomePackage().first().isEmpty() ||
                    !_repository.isDefaultHomeApp() || _repository.getIsInOnboarding()
        }
    }
}