package com.example.ransomsensei.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ransomsensei.data.entity.Card

@Dao
interface CardDao {
    @Query("SELECT * FROM Cards")
    fun getAll(): List<Card>

    @Query("SELECT * FROM Cards WHERE difficulty = 'EASY'")
    fun loadAllEasy(): List<Card>

    @Query("SELECT * FROM Cards WHERE difficulty = 'MEDIUM'")
    fun loadAllMedium(): List<Card>

    @Query("SELECT * FROM Cards WHERE difficulty = 'HARD'")
    fun loadAllHard(): List<Card>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCards(vararg cards: Card)

    @Update()
    fun updateCard(card: Card)
}