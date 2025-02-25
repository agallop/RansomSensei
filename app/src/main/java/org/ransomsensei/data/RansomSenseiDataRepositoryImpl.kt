package org.ransomsensei.data

import android.content.Intent
import android.content.pm.PackageManager
import kotlinx.coroutines.flow.Flow
import org.ransomsensei.data.entity.Card
import org.ransomsensei.data.entity.CardSet

class RansomSenseiDataRepositoryImpl(
    database: RansomSenseiDatabase,
    private val _packageManager: PackageManager,
    private val _dataStoreManager: RansomSenseiDataStoreManager
) : RansomSenseiDataRepository {
    private val _cardDao = database.cardDao()
    private val _cardSetDao = database.cardSetDao()

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

    override fun getHomePackage(): Flow<String> {
        return _dataStoreManager.getHomePackage()
    }

    override suspend fun getLastInteraction(): Long {
        return _dataStoreManager.getLastInteraction()
    }

    override suspend fun setLastInteraction(timestamp: Long) {
        _dataStoreManager.setLastInteraction(timestamp)
    }

    override fun isDefaultHomeApp(): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val resolveInfo = _packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return (resolveInfo?.activityInfo?.packageName ?: "").contains("ransomsensei")
    }

    override suspend fun getCardSet(cardSetId: Int): CardSet? {
        return _cardSetDao.getCardSet(cardSetId)
    }

    override suspend fun insertCardSet(cardSet: CardSet) {
        _cardSetDao.insertCardSet(cardSet)
    }

    override suspend fun getCard(cardId: Int): Card? {
        return _cardDao.getCard(cardId)
    }

    override suspend fun insertCard(card: Card) {
        _cardDao.insertCard(card)
    }

    override suspend fun updateCardSet(cardSet: CardSet) {
        _cardSetDao.updateCardSet(cardSet)
    }

    override suspend fun deleteCards(cards: List<Card>) {
        _cardDao.deleteCards(cards)
    }

    override suspend fun saveHomePackage(packageName: String) {
        _dataStoreManager.saveHomePackage(packageName)
    }
}