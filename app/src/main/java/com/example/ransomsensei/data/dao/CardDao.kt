package com.example.ransomsensei.data.dao

import androidx.core.view.WindowInsetsCompat.Type.InsetsType
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
    fun loadAllHard(): List<Card>

    @Query("SELECT * FROM Cards WHERE difficulty = 'MEDIUM'")
    fun loadAllEasy(): List<Card>

    @Query("SELECT * FROM Cards WHERE difficulty = 'HARD'")
    fun loadAllMedium(): List<Card>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCards(vararg cards: Card)

    @Update()
    fun updateCard(card: Card)
}