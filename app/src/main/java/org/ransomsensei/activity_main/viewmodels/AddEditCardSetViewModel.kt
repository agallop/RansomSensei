package org.ransomsensei.activity_main.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.entity.CardSet
import org.ransomsensei.data.entity.CardSetStatus

class AddEditCardSetViewModel(private val _repository: RansomSenseiDataRepository) : ViewModel() {
    var name by mutableStateOf("")
        private set
    var status by mutableStateOf(CardSetStatus.UNKNOWN)
    var isNew by mutableStateOf(true)
        private set
    private var _existingCardSet: CardSet? = null


    fun loadCardSet(cardSetId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _existingCardSet = _repository.getCardSet(cardSetId)
            if (_existingCardSet != null) {
                name = _existingCardSet?.cardSetName ?: ""
                status = _existingCardSet?.cardSetStatus ?: CardSetStatus.UNKNOWN
                isNew = false
            }
        }
    }

    suspend fun insertCardSet() {
        _repository.insertCardSet(
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