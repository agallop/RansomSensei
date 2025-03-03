package org.ransomsensei.activity_lockscreen.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ransomsensei.data.RansomSenseiDataRepository

class HomeActivityViewModel(private val repository: RansomSenseiDataRepository) : ViewModel() {
    var isLoading by mutableStateOf(true)
    var isInOnboarding by mutableStateOf(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            isInOnboarding = repository.getIsInOnboarding()
            withContext(Dispatchers.Main) {
                isLoading = false
            }
        }
    }

}