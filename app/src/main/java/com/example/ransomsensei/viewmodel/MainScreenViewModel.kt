package com.example.ransomsensei.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.CardSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(private val database: RansomSenseiDatabase,
private val dataStoreManager: RansomSenseiDataStoreManager) : ViewModel() {
    private val _cardSets = MutableStateFlow(emptyList<CardSet>())
    val cardSets = _cardSets.asStateFlow()
    var isLoading by mutableStateOf(true)
        private set
    var needToSetHomeActivity by mutableStateOf(false)
        private set
    var selectedCardSets by mutableStateOf<Set<CardSet>>(setOf())
        private set
    var showDeleteConfirmation by mutableStateOf(false)
        private set

    fun loadCardSets() {
        println("loadCardSets")
        viewModelScope.launch {
            database.cardSetDao().getAllFlow().collect { cardSets ->
                selectedCardSets = setOf()
                _cardSets.update { cardSets }
            }
        }

        viewModelScope.launch {
            needToSetHomeActivity = dataStoreManager.getHomeActivity().isEmpty()
            isLoading = false;
        }
    }

    fun toggleCardSetSelection(cardSet: CardSet) {
        selectedCardSets = selectedCardSets.toMutableSet().apply {
            if (contains(cardSet)) remove(cardSet) else add(cardSet)
        }
    }

    fun isCardSetSelected(cardSet: CardSet): Boolean {
        return selectedCardSets.contains(cardSet)
    }

    fun showDeleteConfirmation() {
        showDeleteConfirmation = true
    }

    fun hideDeleteConfirmation() {
        showDeleteConfirmation = false
    }

    fun deleteSelectedCardSets() {
        CoroutineScope(Dispatchers.IO).launch {
            database.cardSetDao().deleteCardSets(selectedCardSets.toList())
            selectedCardSets = setOf()
            showDeleteConfirmation = false
        }
    }

    fun showHomeActivityWelcome() {
        needToSetHomeActivity = false;
    }
}