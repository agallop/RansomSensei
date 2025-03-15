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

    suspend fun insertOrUpdateCardSet() {
        when(_existingCardSet) {
            null -> _repository.insertCardSet(
                CardSet(cardSetName = name, cardSetStatus = status)
            )
            else -> _repository.updateCardSet(
                _existingCardSet!!.copy(
                    cardSetName = name,
                    cardSetStatus = status
                ))
        }
    }

    fun onNameChange(name: String) {
        this.name = name
    }

    fun onStatusChange(status: CardSetStatus) {
        this.status = status
    }
}