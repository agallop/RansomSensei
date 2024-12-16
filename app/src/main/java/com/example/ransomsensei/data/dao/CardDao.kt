package com.example.ransomsensei.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ransomsensei.data.entity.Card

@Dao
interface CardDao {
    @Query("SELECT * FROM Cards")
    suspend fun getAll(): List<Card>

    @Query("SELECT * FROM Cards WHERE difficulty = 'EASY'")
    suspend fun loadAllEasy(): List<Card>

    @Query("SELECT * FROM Cards WHERE difficulty = 'MEDIUM'")
    suspend fun loadAllMedium(): List<Card>

    @Query("SELECT * FROM Cards WHERE difficulty = 'HARD'")
    suspend fun loadAllHard(): List<Card>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(vararg cards: Card)

    @Update()
    suspend fun updateCard(card: Card)

    @Delete()
    suspend fun deleteCard(card: Card)
}