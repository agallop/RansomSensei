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
import org.ransomsensei.data.entity.Card
import org.ransomsensei.data.entity.Difficulty

class AddEditCardViewModel(private val _repository: RansomSenseiDataRepository) : ViewModel() {
    var englishValue by mutableStateOf<String>("")
        private set
    var kanaValue by mutableStateOf<String>("")
        private set
    var kanjiValue by mutableStateOf<String>("")
        private set
    var difficulty by mutableStateOf<Difficulty>(Difficulty.UNKNOWN)
        private set
    private var cardSetId by mutableStateOf<Int>(0)
    var isNew by mutableStateOf(true)
        private set

    private var _existingCard: Card? = null
    fun loadCard(cardId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _existingCard = _repository.getCard(cardId)
            englishValue = _existingCard?.englishValue ?: ""
            kanaValue = _existingCard?.kanaValue ?: ""
            kanjiValue = _existingCard?.kanjiValue ?: ""
            difficulty = _existingCard?.difficulty ?: Difficulty.UNKNOWN
            isNew = _existingCard == null
        }
    }

    fun updateCardSetId(value: Int) {
        cardSetId = value
    }

    fun updateEnglishValue(value: String) {
        englishValue = value
    }

    fun updateKanaValue(value: String) {
        kanaValue = value
    }

    fun updateKanjiValue(value: String) {
        kanjiValue = value
    }

    fun updateDifficulty(value: Difficulty) {
        difficulty = value
    }

    fun canSave(): Boolean {
        return englishValue.isNotEmpty() || kanaValue.isNotEmpty()
                && kanaValue.isNotEmpty() && difficulty != Difficulty.UNKNOWN
    }

    suspend fun insertCard() {
        _repository.insertCard(
            _existingCard?.copy(
                englishValue = englishValue, kanaValue = kanaValue,
                kanjiValue = kanjiValue, difficulty = difficulty
            ) ?: Card(
                cardSetId = cardSetId, kanaValue = kanaValue, kanjiValue = kanjiValue,
                englishValue = englishValue, difficulty = difficulty
            )
        )
    }

    fun clearForm() {
        englishValue = ""
        kanaValue = ""
        kanjiValue = ""
        difficulty = Difficulty.UNKNOWN

    }
}