package org.ransomsensei.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import org.ransomsensei.data.dao.CardDao
import org.ransomsensei.data.dao.CardSetDao
import org.ransomsensei.data.entity.Card
import org.ransomsensei.data.entity.CardSet

@Database(
    entities = [CardSet::class, Card::class],
    exportSchema = true,
    version = 1
)
abstract class RansomSenseiDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    abstract fun cardSetDao(): CardSetDao

    companion object {
        private const val DATABASE_NAME = "ransomSensei"

        @OptIn(InternalCoroutinesApi::class)
        fun createInstance(context: Context): RansomSenseiDatabase {

            return Room.databaseBuilder(
                context.applicationContext,
                RansomSenseiDatabase::class.java,
                DATABASE_NAME
            )
                .addCallback(object : Callback() {

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        db.execSQL(
                            """
                                CREATE TEMP TRIGGER IF NOT EXISTS increase_card_set_count AFTER INSERT ON Card
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

                        db.execSQL(
                            """
                                CREATE TEMP TRIGGER IF NOT EXISTS decrease_card_set_count AFTER DELETE ON Card
                                    BEGIN
                                        UPDATE CardSet
                                            SET card_count = (
                                                SELECT COUNT(*)
                                                FROM Card
                                                WHERE card_set_id = OLD.card_set_id)
                                                WHERE card_set_id = OLD.card_set_id;
                                END;
                            """.trimIndent()
                        )
                    }
                })
                .build()
        }
    }
}

