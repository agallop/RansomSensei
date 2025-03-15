/*
 *  Copyright (c) 2025 Anthony Gallop <agallopdev@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

