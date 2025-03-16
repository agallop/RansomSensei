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
package org.ransomsensei.activity_lockscreen.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.entity.Card
import java.util.Date
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class LockScreenViewModel(
    private val _repository: RansomSenseiDataRepository,
    private val _date: Date
) : ViewModel() {
    var isLoading by mutableStateOf(true)
        private set
    var card by mutableStateOf<Card?>(null)
        private set
    var currentAnswer by mutableStateOf("")
        private set
    var homeActivityPackage by mutableStateOf("")
        private set
    var lastInteraction by mutableStateOf(0L)
        private set
    var showQuestion by mutableStateOf(false)
        private set
    var countdown = flow {
        for (i in 5 downTo 1) {
            val time = i.toDuration(DurationUnit.SECONDS)
            emit(time.toString())
            delay(1000)
        }
        allowSkip = true
    }
        private set
    var allowSkip by mutableStateOf(false)
        private set

    fun loadQuestion() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                card = _repository.getRandomActiveCard()
                homeActivityPackage = _repository.getHomePackage().first()
                lastInteraction = _repository.getLastInteraction()
            }

            showQuestion = card != null
            delay(500)
            isLoading = false
        }
    }

    fun setAnswer(answer: String) {
        currentAnswer = answer
    }

    fun updateLastInteraction() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _repository.setLastInteraction(_date.time)
            }
        }
    }
}