package com.example.ransomsensei.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.ransomsensei.data.dao.CardDao
import com.example.ransomsensei.data.dao.CardSetDao
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [CardSet::class, Card::class], version = 3)
abstract class RansomSenseiDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    abstract fun cardSetDao(): CardSetDao

    companion object {
        private const val Database_NAME = "ransomSensei"

        @Volatile
        private var INSTANCE: RansomSenseiDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): RansomSenseiDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RansomSenseiDatabase::class.java,
                        Database_NAME)
                        .fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
