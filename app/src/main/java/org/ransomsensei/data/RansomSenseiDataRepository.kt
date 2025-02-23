package org.ransomsensei.data

import org.ransomsensei.data.entity.Card
import org.ransomsensei.data.entity.CardSet
import kotlinx.coroutines.flow.Flow

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
}