package com.example.ransomsensei.data

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.ransomsensei.data.dao.CardDao
import com.example.ransomsensei.data.entity.Card

@Database(entities = [Card::class], version = 1)
abstract class RansomSenseiDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}
