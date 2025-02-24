package org.ransomsensei.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.ransomsensei.data.entity.CardSet


@Dao
interface CardSetDao {
    @Query("SELECT * FROM CardSet")
    suspend fun getAll(): List<CardSet>

    @Query("SELECT * FROM CardSet")
    fun getAllFlow(): Flow<List<CardSet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardSet(cardSet: CardSet)

    @Update()
    suspend fun updateCardSet(cardSet: CardSet)

    @Delete
    suspend fun deleteCardSets(cardSets: Collection<CardSet>)

    @Query("Select * FROM CardSet WHERE card_set_id = :cardSetId")
    suspend fun getCardSet(cardSetId: Int): CardSet?

    @Query("Select * FROM CardSet WHERE card_set_id = :cardSetId")
    fun getCardSetFlow(cardSetId: Int): Flow<CardSet>
}