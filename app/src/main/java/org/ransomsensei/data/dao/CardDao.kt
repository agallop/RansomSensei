package org.ransomsensei.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.ransomsensei.data.entity.Card

@Dao
interface CardDao {

    @Query("""
            SELECT * FROM Card 
            INNER JOIN CardSet Using (card_set_id)
            WHERE card_set_status = 'ENABLED'
            ORDER BY RANDOM()
            limit 1
            """)
    suspend fun getRandomActive(): Card?

    @Query("SELECT * FROM Card WHERE card_set_id = :cardSetId")
    suspend fun getCardsInSet(cardSetId: Int): List<Card>

    @Query("SELECT * FROM Card WHERE card_set_id = :cardSetId")
    fun getCardsInSetFlow(cardSetId: Int): Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)

    @Query("Select * FROM Card WHERE card_id = :cardId")
    suspend fun getCard(cardId: Int): Card?

    @Update()
    suspend fun updateCard(card: Card)

    @Delete()
    suspend fun deleteCard(card: Card)

    @Delete
    suspend fun deleteCards(cards: Collection<Card>)
}