package org.ransomsensei.data

import android.content.Intent
import android.content.pm.PackageManager
import org.ransomsensei.data.entity.Card
import org.ransomsensei.data.entity.CardSet
import kotlinx.coroutines.flow.Flow

class RansomSenseiDataRepositoryImpl(
    val packageManager: PackageManager,
    database: RansomSenseiDatabase,
    val dataStoreManager: RansomSenseiDataStoreManager
) : RansomSenseiDataRepository {
    val cardDao = database.cardDao()
    val cardSetDao = database.cardSetDao()

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

    override suspend fun getHomePackage(): String {
        return dataStoreManager.getHomeActivity()
    }

    override suspend fun getLastInteraction(): Long {
        return dataStoreManager.getLastInteraction()
    }

    override suspend fun setLastInteraction(timestamp: Long) {
        dataStoreManager.setLastInteraction(timestamp)
    }

    override fun isDefaultHomeApp(): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return (resolveInfo?.activityInfo?.packageName ?: "").contains("ransomsensei")
    }
}