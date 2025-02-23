package com.example.ransomsensei.data.testing

import android.content.Context
import androidx.room.Room
import com.example.ransomsensei.data.RansomSenseiDataRepository
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus
import com.example.ransomsensei.data.entity.Difficulty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/** Test implementation of [RansomSenseiDataRepository] */
class TestRansomSenseiDataRepositoryImpl(val context: Context) : RansomSenseiDataRepository {
    val db = Room.inMemoryDatabaseBuilder(
        context, RansomSenseiDatabase::class.java
    ).build()

    val cardDao = db.cardDao()
    val cardSetDao = db.cardSetDao()
    private var lastInteraction = 0L

    init {
        CoroutineScope(Dispatchers.IO).launch {
            populate()
        }
    }

    private suspend fun populate() {
        val cardSet = CardSet(
            cardSetId = 1,
            cardSetName = "Test Set",
            cardSetStatus = CardSetStatus.ENABLED
        )
        cardSetDao.insertCardSet(cardSet)

        val card = Card(
            cardId = 1,
            cardSetId = 1,
            kanjiValue = "日本語",
            kanaValue = "にほんご",
            englishValue = "Japanese",
            difficulty = Difficulty.EASY
        )
        cardDao.insertCards(card)
    }

    override suspend fun getHomePackage(): String {
        return "com.example.ransomsensei"
    }

    override suspend fun getLastInteraction(): Long {
        return lastInteraction
    }

    override suspend fun setLastInteraction(timestamp: Long) {
        lastInteraction = timestamp
    }

    override suspend fun getRandomActiveCard(): Card? {
        return cardDao.getRandomActive()
    }

    override fun getCardSetFlow(cardSetId: Int): Flow<CardSet> {
        return cardSetDao.getCardSetFlow(cardSetId)
    }

    override fun getAllCardSetsFlow(): Flow<List<CardSet>> {
        return cardSetDao.getAllFlow()
    }

    override fun getCardsInSetFlow(cardSetId: Int): Flow<List<Card>> {
        return cardDao.getCardsInSetFlow(cardSetId)
    }

    override suspend fun deleteCardSets(cardSets: List<CardSet>) {
        cardSetDao.deleteCardSets(cardSets)
    }

    override fun isDefaultHomeApp(): Boolean {
        return true;
    }
}