package com.example.ransomsensei.data

import com.example.ransomsensei.data.entity.Card

interface RansomSenseiDataRepository {
    suspend fun getAllActiveCards() : List<Card>
    suspend fun getHomePackage() : String
    suspend fun getLastInteraction() : Long
    suspend fun setLastInteraction(timestamp: Long)
}