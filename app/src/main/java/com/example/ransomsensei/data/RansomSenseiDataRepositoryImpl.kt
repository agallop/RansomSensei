package com.example.ransomsensei.data

import com.example.ransomsensei.data.dao.CardDao
import com.example.ransomsensei.data.dao.CardSetDao
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet

class RansomSenseiDataRepositoryImpl(
    val database: RansomSenseiDatabase,
    val dataStoreManager: RansomSenseiDataStoreManager
) : RansomSenseiDataRepository {
    val cardDao = database.cardDao()
    val cardSetDao = database.cardSetDao()

    override suspend fun getAllActiveCards() : List<Card> {
        return cardDao.getAllActive()
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


}