package com.example.ransomsensei.viewmodel.cardset

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ransomsensei.data.RansomSenseiDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus

class EditCardSetViewModel(val database: RansomSenseiDatabase) : ViewModel() {
    var name by mutableStateOf("")
        private set
    var status by mutableStateOf(CardSetStatus.UNKNOWN)
    private lateinit var _existingCardSet: CardSet

    suspend fun loadCardSet(cardSetId: Int) {
        _existingCardSet = database.cardSetDao().getCardSet(cardSetId)
        name = _existingCardSet.cardSetName
        status = _existingCardSet.cardSetStatus
    }

    suspend fun updateCardSet() {
        database.cardSetDao().updateCardSet(
            _existingCardSet.copy(
                cardSetName = name,
                cardSetStatus = status
            )
        )
    }

    fun onNameChange(name: String) {
        this.name = name
    }

    fun onStatusChange(status: CardSetStatus) {
        this.status = status
    }

}