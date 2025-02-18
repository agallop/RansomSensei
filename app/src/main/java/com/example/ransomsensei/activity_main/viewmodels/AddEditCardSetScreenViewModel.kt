package com.example.ransomsensei.activity_main.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditCardSetScreenViewModel
    (val database: RansomSenseiDatabase) : ViewModel() {
        var name by mutableStateOf("")
        private set
        var status by mutableStateOf(CardSetStatus.UNKNOWN)
        private var _existingCardSet: CardSet? = null

    fun loadCardSet(cardSetId: Int) {
            viewModelScope.launch(Dispatchers.IO) {
                _existingCardSet = database.cardSetDao().getCardSet(cardSetId)
                name = _existingCardSet?.cardSetName ?: ""
                status = _existingCardSet?.cardSetStatus ?: CardSetStatus.UNKNOWN
            }
        }

        suspend fun insertCardSet() {
            database.cardSetDao().insertCardSet(
                _existingCardSet?.copy(
                    cardSetName = name,
                    cardSetStatus = status
                ) ?: CardSet(cardSetName = name, cardSetStatus = status)
            )
        }

        fun onNameChange(name: String) {
            this.name = name
        }

        fun onStatusChange(status: CardSetStatus) {
            this.status = status
        }
}