package com.example.ransomsensei.viewmodel.cardset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardSetViewModel(
    private val database: RansomSenseiDatabase) : ViewModel() {
        var cardSetId by mutableStateOf(0)
            private set
        var cardSet by mutableStateOf<CardSet>(CardSet.getDefaultInstance())
            private set
        var cards by mutableStateOf<List<Card>>(listOf())
            private set
    var selectedCards by mutableStateOf<Set<Card>>(setOf())
        private set
        var hasChanges by mutableStateOf(false)
            private set
    var showDeleteConfirmation by mutableStateOf(false)
        private set

        fun loadCards(cardSetId: Int) {
            this.cardSetId = cardSetId
            CoroutineScope(Dispatchers.IO).launch {
                cardSet = database.cardSetDao().getCardSet(cardSetId)
                cards = database.cardDao().getCardsInSet(cardSetId)
            }
        }

    fun reloadCards() {
        CoroutineScope(Dispatchers.IO).launch {
            cards = database.cardDao().getCardsInSet(cardSetId)
        }
    }

    fun toggleCardSelection(card: Card) {
        selectedCards = selectedCards.toMutableSet().apply {
            if (contains(card)) remove(card) else add(card)
        }
    }

        fun cardSetChanged() {
            hasChanges = true
        }

        fun showDeleteConfirmation() {
            showDeleteConfirmation = false
        }

        fun hideDeleteConfirmation() {
            showDeleteConfirmation = false
        }
    fun deleteSelectedCards() {
        CoroutineScope(Dispatchers.IO).launch {
            database.cardDao().deleteCards(selectedCards.toList())
            selectedCards = setOf()
            showDeleteConfirmation = false
        }
    }
    }