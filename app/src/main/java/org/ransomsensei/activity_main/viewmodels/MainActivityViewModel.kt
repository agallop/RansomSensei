package org.ransomsensei.activity_main.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.ransomsensei.data.RansomSenseiDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivityViewModel(val repository: RansomSenseiDataRepository) : ViewModel() {
    var needToLaunchOnboardingActivity by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            needToLaunchOnboardingActivity = repository.getHomePackage().first().isEmpty() ||
                    !repository.isDefaultHomeApp()
        }
    }
}