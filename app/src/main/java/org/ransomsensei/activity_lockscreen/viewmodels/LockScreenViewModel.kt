package org.ransomsensei.activity_lockscreen.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.entity.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class LockScreenViewModel(
    private val repository: RansomSenseiDataRepository
) : ViewModel() {
    var isLoading by mutableStateOf(true)
        private set
    var card by mutableStateOf<Card?>(null)
        private set
    var currentAnswer by mutableStateOf("")
        private set
    var homeActivityPackage by mutableStateOf("")
        private set
    var lastInteraction by mutableStateOf(0L)
        private set
    var showQuestion by mutableStateOf(false)
        private set
    var countdown = flow {
        for (i in 5 downTo 1) {
            val time = i.toDuration(DurationUnit.SECONDS)
            emit(time.toString())
            delay(1000)
        }
        allowSkip = true
    }
        private set
    var allowSkip by mutableStateOf(false)
        private set

    fun loadQuestion() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                card = repository.getRandomActiveCard()
                homeActivityPackage = repository.getHomePackage()
                lastInteraction = repository.getLastInteraction()
            }

            showQuestion = card != null
            delay(500)
            isLoading = false
        }
    }

    fun setAnswer(answer: String) {
        currentAnswer = answer
    }

    fun updateLastInteraction() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.setLastInteraction(System.currentTimeMillis())
            }
        }
    }
}