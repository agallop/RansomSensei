package com.example.ransomsensei.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

import com.example.ransomsensei.data.dao.CardDao
import com.example.ransomsensei.data.dao.CardSetDao
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [CardSet::class, Card::class], version = 6)
abstract class RansomSenseiDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    abstract fun cardSetDao(): CardSetDao

    companion object {
        private const val DATABASE_NAME = "ransomSensei"

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
                        DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                println("HERE\nHERE\nHERE")
                                db.execSQL(
                                    """
                                        CREATE TRIGGER IF NOT EXISTS update_card_set_count AFTER INSERT ON Card
                                        BEGIN
                                            UPDATE CardSet
                                            SET card_count = (
                                                SELECT COUNT(*)
                                                FROM Card
                                                WHERE card_set_id = NEW.card_set_id)
                                                WHERE card_set_id = NEW.card_set_id;
                                        END;
                                    """.trimIndent()
                                )
                            }

                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                                println("HERE2\nHERE2\nHERE2")

                                db.execSQL(
                                    """
                                        CREATE TRIGGER IF NOT EXISTS update_card_set_count AFTER INSERT ON Card
                                        BEGIN
                                            UPDATE CardSet
                                            SET card_count = (
                                                SELECT COUNT(*)
                                                FROM Card
                                                WHERE card_set_id = NEW.card_set_id)
                                                WHERE card_set_id = NEW.card_set_id;
                                        END;
                                    """.trimIndent())
                            }
                        })
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
