package org.ransomsensei.activity_onboarding.viewmodels


import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.ransomsensei.activity_onboarding.util.Destination
import org.ransomsensei.data.RansomSenseiDataRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class OnboardingActivityViewModel(val repository: RansomSenseiDataRepository) : ViewModel() {
    val destinationGraph = mutableStateMapOf<Destination, Destination>()
    var isLoading by mutableStateOf(true)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            var nextDestination: Destination = Destination.Finish

            if (!repository.isDefaultHomeApp()) {
                destinationGraph.put(Destination.SetDefaultHomeAppScreen, nextDestination)
                nextDestination = Destination.SetDefaultHomeAppScreen
            }

            if (repository.getHomePackage().first() == "") {
                destinationGraph.put(Destination.SetHomeActivityScreen, nextDestination)
                nextDestination = Destination.SetHomeActivityScreen
            }

            destinationGraph.put(Destination.StartOnBoardingScreen, nextDestination)
            withContext(Dispatchers.Main) {
                isLoading = false
            }
        }
    }
}