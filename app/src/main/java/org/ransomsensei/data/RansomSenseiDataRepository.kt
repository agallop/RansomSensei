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
package org.ransomsensei.data

import kotlinx.coroutines.flow.Flow
import org.ransomsensei.data.entity.Card
import org.ransomsensei.data.entity.CardSet

interface RansomSenseiDataRepository {
    fun getHomePackage() : Flow<String>
    suspend fun getLastInteraction() : Long
    suspend fun setLastInteraction(timestamp: Long)
    suspend fun getRandomActiveCard() : Card?
    fun getCardSetFlow(cardSetId: Int) : Flow<CardSet>
    fun getAllCardSetsFlow() : Flow<List<CardSet>>
    fun getCardsInSetFlow(cardSetId: Int) : Flow<List<Card>>
    suspend fun deleteCardSets(cardSets: List<CardSet>)
    fun isDefaultHomeApp() : Boolean
    suspend fun getCardSet(cardSetId: Int) : CardSet?
    suspend fun insertCardSet(cardSet: CardSet)
    suspend fun updateCardSet(cardSet: CardSet)
    suspend fun getCard(cardId: Int) : Card?
    suspend fun insertCard(card: Card)
    suspend fun deleteCards(cards: List<Card>)
    suspend fun saveHomePackage(packageName: String)
    suspend fun setIsInOnboarding(onboarding: Boolean)
    suspend fun getIsInOnboarding(): Boolean
}