package org.ransomsensei.data.testing

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.RansomSenseiDatabase
import org.ransomsensei.data.entity.Card
import org.ransomsensei.data.entity.CardSet
import org.ransomsensei.data.entity.CardSetStatus
import org.ransomsensei.data.entity.Difficulty

/** Test implementation of [RansomSenseiDataRepository] */
class TestRansomSenseiDataRepositoryImpl(context: Context) : RansomSenseiDataRepository {
    private val _db = Room.inMemoryDatabaseBuilder(
        context, RansomSenseiDatabase::class.java
    ).build()
    private val _cardDao = _db.cardDao()
    private val _cardSetDao = _db.cardSetDao()
    private var _lastInteraction = 0L
    private var _homePackage = "org.ransomsensei"

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val cardSet = CardSet(
                cardSetId = 1,
                cardSetName = "Test Set",
                cardSetStatus = CardSetStatus.ENABLED
            )
            _cardSetDao.insertCardSet(cardSet)

            val card = Card(
                cardId = 1,
                cardSetId = 1,
                kanjiValue = "日本語",
                kanaValue = "にほんご",
                englishValue = "Japanese",
                difficulty = Difficulty.EASY
            )
            _cardDao.insertCard(card)
        }
    }

    override fun getHomePackage(): Flow<String> {
        return flow {
            emit(_homePackage)
        }
    }

    override suspend fun getLastInteraction(): Long {
        return _lastInteraction
    }

    override suspend fun setLastInteraction(timestamp: Long) {
        _lastInteraction = timestamp
    }

    override suspend fun getRandomActiveCard(): Card? {
        return _cardDao.getRandomActive()
    }

    override fun getCardSetFlow(cardSetId: Int): Flow<CardSet> {
        return _cardSetDao.getCardSetFlow(cardSetId)
    }

    override fun getAllCardSetsFlow(): Flow<List<CardSet>> {
        return _cardSetDao.getAllFlow()
    }

    override fun getCardsInSetFlow(cardSetId: Int): Flow<List<Card>> {
        return _cardDao.getCardsInSetFlow(cardSetId)
    }

    override suspend fun deleteCardSets(cardSets: List<CardSet>) {
        _cardSetDao.deleteCardSets(cardSets)
    }

    override fun isDefaultHomeApp(): Boolean {
        return true
    }

    override suspend fun getCardSet(cardSetId: Int): CardSet? {
        return _cardSetDao.getCardSet(cardSetId)
    }

    override suspend fun insertCardSet(cardSet: CardSet) {
        return _cardSetDao.insertCardSet(cardSet)
    }

    override suspend fun updateCardSet(cardSet: CardSet) {
        return _cardSetDao.updateCardSet(cardSet)
    }

    override suspend fun getCard(cardId: Int): Card? {
        return _cardDao.getCard(cardId)
    }

    override suspend fun insertCard(card: Card) {
        return _cardDao.insertCard(card)
    }

    override suspend fun deleteCards(cards: List<Card>) {
        return _cardDao.deleteCards(cards)
    }

    override suspend fun saveHomePackage(packageName: String) {
        _homePackage = packageName
    }
}