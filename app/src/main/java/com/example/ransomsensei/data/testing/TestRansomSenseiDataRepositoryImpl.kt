package com.example.ransomsensei.data.testing

import com.example.ransomsensei.data.RansomSenseiDataRepository
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.Difficulty

/** Test implementation of [RansomSenseiDataRepository] */
class TestRansomSenseiDataRepositoryImpl : RansomSenseiDataRepository {
    var lastInteraction = 0L

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
        return Card(
            cardId = 1,
            cardSetId = 1,
            kanjiValue = "日本語",
            kanaValue = "にほんご",
            englishValue = "Japanese",
            difficulty = Difficulty.EASY)
    }
}