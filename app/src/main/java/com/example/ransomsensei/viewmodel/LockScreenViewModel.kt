package com.example.ransomsensei.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class LockScreenViewModel(
    private val database: RansomSenseiDatabase,
    private val dataStoreManager: RansomSenseiDataStoreManager
)
    : ViewModel() {
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
    var frequencyCapMillis by mutableStateOf(600000L)
        private set
    var showQuestion by mutableStateOf(false)
        private set

    fun loadQuestion() {
        CoroutineScope(Dispatchers.IO).launch {
            val cards = database.cardDao().getAllActive()
            if (cards.isNotEmpty()) {
                card = cards[Random.nextInt(cards.size)]
            }
            homeActivityPackage = dataStoreManager.getHomeActivity()
            lastInteraction = dataStoreManager.getLastInteraction()
            showQuestion = card != null
               // && lastInteraction + frequencyCapMillis < System.currentTimeMillis()
            isLoading = false
        }
    }

    fun setAnswer(answer: String) {
        currentAnswer = answer
    }

    fun updateLastInteraction() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.setLastInteraction(System.currentTimeMillis())
        }
    }
}