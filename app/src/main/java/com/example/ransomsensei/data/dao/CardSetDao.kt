package com.example.ransomsensei.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet

@Dao
interface CardSetDao {
    @Query("SELECT * FROM CardSet")
    suspend fun getAll(): List<CardSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardSet(cardSet: CardSet)

    @Query("Select Count(*) " +
            "FROM CardSet " +
            "INNER JOIN Card " +
            "Using (card_set_id) " +
            "WHERE card_set_id = :cardSetId")
    suspend fun getCardCount(cardSetId: Int)

    @Delete
    suspend fun deleteCardSets(cardSets: Collection<CardSet>)

    @Query("Select * FROM CardSet WHERE card_set_id = :cardSetId")
    suspend fun getCardSet(cardSetId: Int): CardSet

}