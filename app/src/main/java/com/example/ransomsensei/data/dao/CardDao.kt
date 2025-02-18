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
    @Query("""
            WITH random_cardset AS (
            Select * FROM CardSet
            ORDER BY RANDOM()
            limit 1)
            SELECT * FROM Card
            INNER JOIN random_cardset Using (card_set_id)
            ORDER BY RANDOM()
            limit 1
        """
        )
    suspend fun getRandomActive(): Card?

    @Query("SELECT * FROM Card WHERE card_set_id = :cardSetId")
    suspend fun getCardsInSet(cardSetId: Int): List<Card>

    @Query("SELECT * FROM Card WHERE card_set_id = :cardSetId")
    fun getCardsInSetFlow(cardSetId: Int): Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(vararg cards: Card)

    @Query("Select * FROM Card WHERE card_id = :cardId")
    suspend fun getCard(cardId: Int): Card?

    @Update()
    suspend fun updateCard(card: Card)

    @Delete()
    suspend fun deleteCard(card: Card)

    @Delete
    suspend fun deleteCards(cards: Collection<Card>)
}