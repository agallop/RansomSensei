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
package org.ransomsensei.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.ransomsensei.data.entity.Card

@Dao
interface CardDao {

    @Query("""
            SELECT * FROM Card 
            INNER JOIN CardSet Using (card_set_id)
            WHERE card_set_status = 'ENABLED'
            ORDER BY RANDOM()
            limit 1
            """)
    suspend fun getRandomActive(): Card?

    @Query("SELECT * FROM Card WHERE card_set_id = :cardSetId")
    suspend fun getCardsInSet(cardSetId: Int): List<Card>

    @Query("SELECT * FROM Card WHERE card_set_id = :cardSetId")
    fun getCardsInSetFlow(cardSetId: Int): Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)

    @Query("Select * FROM Card WHERE card_id = :cardId")
    suspend fun getCard(cardId: Int): Card?

    @Update()
    suspend fun updateCard(card: Card)

    @Delete()
    suspend fun deleteCard(card: Card)

    @Delete
    suspend fun deleteCards(cards: Collection<Card>)
}