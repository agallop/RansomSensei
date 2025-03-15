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
import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.entity.Card
import org.ransomsensei.data.entity.CardSet

class CardSetDetailsViewModel(
    private val _repository: RansomSenseiDataRepository,
) : ViewModel() {
    var cardSetId by mutableStateOf(0)
        private set
    private val _cardSet = MutableStateFlow(CardSet.getDefaultInstance())
    val cardSet = _cardSet.asStateFlow()
    private val _cards = MutableStateFlow(emptyList<Card>())
    val cards = _cards.asStateFlow()
    var selectedCards by mutableStateOf<Set<Card>>(setOf())
        private set
    var showDeleteConfirmation by mutableStateOf(false)
        private set

    fun loadCards(cardSetId: Int) {
        this.cardSetId = cardSetId
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getCardSetFlow(cardSetId).collect { cardSet ->
                _cardSet.update { cardSet }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _repository.getCardsInSetFlow(cardSetId).collect { cards ->
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
            _repository.deleteCards(selectedCards.toList())
            selectedCards = setOf()
            showDeleteConfirmation = false
        }
    }
}