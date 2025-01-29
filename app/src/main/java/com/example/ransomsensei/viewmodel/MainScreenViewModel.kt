package com.example.ransomsensei.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.CardSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainScreenViewModel(private val database: RansomSenseiDatabase,
private val dataStoreManager: RansomSenseiDataStoreManager) : ViewModel() {
    var cardSets by mutableStateOf<List<CardSet>>(listOf())
        private set
    var isLoading by mutableStateOf(true)
        private set
    var needToSetHomeActivity by mutableStateOf(false)
        private set
    var selectedCardSets by mutableStateOf<Set<CardSet>>(setOf())
        private set
    var showDeleteConfirmation by mutableStateOf(false)
        private set

    fun loadCardSets() {
        CoroutineScope(Dispatchers.IO).launch {
            cardSets = database.cardSetDao().getAll();
            needToSetHomeActivity = dataStoreManager.getHomeActivity().isEmpty()
            isLoading = false;
        }
    }

    fun toggleCardSetSelection(cardSet: CardSet) {
        selectedCardSets = selectedCardSets.toMutableSet().apply {
            if (contains(cardSet)) remove(cardSet) else add(cardSet)
        }
    }

    fun reloadCardSets() {
        CoroutineScope(Dispatchers.IO).launch {
            cardSets = database.cardSetDao().getAll()
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
}