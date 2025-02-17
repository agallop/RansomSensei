package com.example.ransomsensei.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ransomsensei.data.entity.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM Card")
    suspend fun getAll(): List<Card>

    @Query("SELECT * FROM Card " +
            "INNER JOIN CardSet Using (card_set_id)" +
            "WHERE card_set_status = 'ENABLED'")
    suspend fun getAllActive(): List<Card>

    @Query("SELECT * FROM Card WHERE difficulty = 'EASY'")
    suspend fun loadAllEasy(): List<Card>

    @Query("SELECT * FROM Card WHERE difficulty = 'MEDIUM'")
    suspend fun loadAllMedium(): List<Card>

    @Query("SELECT * FROM Card WHERE difficulty = 'HARD'")
    suspend fun loadAllHard(): List<Card>

    @Query("SELECT * FROM Card WHERE card_set_id = :cardSetId")
    suspend fun getCardsInSet(cardSetId: Int): List<Card>

    @Query("SELECT * FROM Card WHERE card_set_id = :cardSetId")
    fun getCardsInSetFlow(cardSetId: Int): Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(vararg cards: Card)

    @Query("Select * FROM Card WHERE card_id = :cardId")
    suspend fun getCard(cardId: Int): Card

    @Update()
    suspend fun updateCard(card: Card)

    @Delete()
    suspend fun deleteCard(card: Card)

    @Delete
    suspend fun deleteCards(cards: Collection<Card>)
}