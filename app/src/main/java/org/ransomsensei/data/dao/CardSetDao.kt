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