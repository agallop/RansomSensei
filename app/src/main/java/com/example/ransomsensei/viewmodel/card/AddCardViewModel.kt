package com.example.ransomsensei.viewmodel.card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.Difficulty

class AddCardViewModel(val ransoSenseiDatabase: RansomSenseiDatabase) : ViewModel() {
    var englishValue by mutableStateOf<String>("")
        private set
    var kanaValue by mutableStateOf<String>("")
        private set
    var kanjiValue by mutableStateOf<String>("")
        private set

    var difficulty by mutableStateOf<Difficulty>(Difficulty.UNKNOWN)
        private set

    private var cardSetId by mutableStateOf<Int>(0)
        private set


    fun updateCardSetId(value: Int) {
        cardSetId = value
    }

    fun updateEnglishValue(value: String) {
        englishValue = value
    }

    fun updateKanaValue(value: String) {
        kanaValue = value
    }

    fun updateKanjiValue(value: String) {
        kanjiValue = value
    }

    fun updateDifficulty(value: Difficulty) {
        difficulty = value
    }

    fun canSave(): Boolean {
        return englishValue.isNotEmpty() && kanaValue.isNotEmpty()
                && kanaValue.isNotEmpty() && difficulty != Difficulty.UNKNOWN
    }

    suspend fun saveCard() {
        ransoSenseiDatabase.cardDao().insertCards(
            Card(
                cardSetId = cardSetId, kanaValue = kanaValue, kanjiValue = kanjiValue,
                englishValue = englishValue, difficulty = difficulty
            )
        )
    }
}