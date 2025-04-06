/*
 *  Copyright (c) 2025 Anthony Gallop <agallopdev@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ransomsensei.activity_main.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.entity.CardSet

class CardSetsViewModel(
    private val _repository: RansomSenseiDataRepository
) : ViewModel() {
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
    var menuExpanded by mutableStateOf(false)
        private set

    fun loadCardSets() {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getAllCardSetsFlow().collect { cardSets ->
                selectedCardSets = setOf()
                _cardSets.update { cardSets }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _repository.getHomePackage().collect {
                needToSetHomeActivity = it.isEmpty()
            }
            isLoading = false
        }
    }

    fun toggleCardSetSelection(cardSet: CardSet) {
        selectedCardSets = selectedCardSets.toMutableSet().apply {
            if (contains(cardSet)) remove(cardSet) else add(cardSet)
        }
    }

    fun toggleMenuExpanded() {
        menuExpanded = !menuExpanded
    }

    fun showDeleteConfirmation() {
        showDeleteConfirmation = true
    }

    fun hideDeleteConfirmation() {
        showDeleteConfirmation = false
    }

    fun deleteSelectedCardSets() {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.deleteCardSets(selectedCardSets.toList())
            withContext(Dispatchers.Main) {
                selectedCardSets = setOf()
                showDeleteConfirmation = false
            }
        }
    }
}