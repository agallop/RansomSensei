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
        viewModelScope.launch(Dispatchers.IO) {
            needToLaunchOnboardingActivity = _repository.getHomePackage().first().isEmpty() ||
                    !_repository.isDefaultHomeApp()
        }
    }
}