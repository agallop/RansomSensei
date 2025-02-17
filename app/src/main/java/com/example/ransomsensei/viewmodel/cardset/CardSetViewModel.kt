package com.example.ransomsensei.viewmodel.cardset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardSetViewModel(
    private val database: RansomSenseiDatabase
) : ViewModel() {
    val cardDao = database.cardDao()
    val cardSetDao = database.cardSetDao()

    var cardSetId by mutableStateOf(0)
        private set
    private val _cardSet = MutableStateFlow(CardSet.getDefaultInstance())
    val cardSet = _cardSet.asStateFlow()
    private val _cards = MutableStateFlow(emptyList<Card>())
    val cards = _cards.asStateFlow()
    var selectedCards by mutableStateOf<Set<Card>>(setOf())
        private set
    var hasChanges by mutableStateOf(false)
        private set
    var showDeleteConfirmation by mutableStateOf(false)
        private set

    fun loadCards(cardSetId: Int) {
        this.cardSetId = cardSetId
        viewModelScope.launch(Dispatchers.IO) {
            cardSetDao.getCardSetFlow(cardSetId).collect { cardSet ->
                println(cardSet)
                _cardSet.update { cardSet }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            cardDao.getCardsInSetFlow(cardSetId).collect { cards ->
                println(cards)
                _cards.update { cards }
            }
        }
    }

    fun toggleCardSelection(card: Card) {
        selectedCards = selectedCards.toMutableSet().apply {
            if (contains(card)) remove(card) else add(card)
        }
    }

    fun showDeleteConfirmation() {
        showDeleteConfirmation = true
    }

    fun hideDeleteConfirmation() {
        showDeleteConfirmation = false
    }

    fun deleteSelectedCards() {
        viewModelScope.launch(Dispatchers.IO) {
            database.cardDao().deleteCards(selectedCards.toList())
            selectedCards = setOf()
            showDeleteConfirmation = false
        }
    }
}