package com.example.ransomsensei.activity_main.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.Difficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditCardScreenViewModel(val ransoSenseiDatabase: RansomSenseiDatabase) : ViewModel() {
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

    private var _existingCard: Card? = null
    fun loadCard(cardId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _existingCard = ransoSenseiDatabase.cardDao().getCard(cardId)
            englishValue = _existingCard?.englishValue ?: ""
            kanaValue = _existingCard?.kanaValue ?: ""
            kanjiValue = _existingCard?.kanjiValue ?: ""
            difficulty = _existingCard?.difficulty ?: Difficulty.UNKNOWN
        }
    }

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

    suspend fun insertCard() {
        ransoSenseiDatabase.cardDao().insertCards(
            _existingCard?.copy(
                englishValue = englishValue, kanaValue = kanaValue,
                kanjiValue = kanjiValue, difficulty = difficulty
            ) ?: Card(
                cardSetId = cardSetId, kanaValue = kanaValue, kanjiValue = kanjiValue,
                englishValue = englishValue, difficulty = difficulty
            )
        )
    }
}