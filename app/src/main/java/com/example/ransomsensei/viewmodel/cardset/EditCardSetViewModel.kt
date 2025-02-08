package com.example.ransomsensei.viewmodel.cardset

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ransomsensei.data.RansomSenseiDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus

class EditCardSetViewModel(val database: RansomSenseiDatabase) : ViewModel() {
    var cardSetId by mutableStateOf(0)
        private set
    var name by mutableStateOf("")
        private set
    var status by mutableStateOf(CardSetStatus.UNKNOWN)

    suspend fun loadCardSet(cardSetId: Int) {
        this.cardSetId = cardSetId
        val existingCardSet = database.cardSetDao().getCardSet(cardSetId)
         name = existingCardSet.cardSetName
        status = existingCardSet.cardSetStatus
    }

    suspend fun updateCardSet() {
        database.cardSetDao().updateCardSet(CardSet(cardSetId, name, status))
    }

    fun onNameChange(name: String) {
        this.name = name
    }

    fun onStatusChange(status: CardSetStatus) {
        this.status = status
    }

}