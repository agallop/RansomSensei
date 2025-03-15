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
package org.ransomsensei.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CardSetStatus {UNKNOWN, ENABLED, DISABLED}

@Entity(tableName = "CardSet")
data class CardSet (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_set_id") val cardSetId: Int = 0,
    @ColumnInfo(name = "card_set_name") val cardSetName: String = "",
    @ColumnInfo(name = "card_set_status") val cardSetStatus: CardSetStatus = CardSetStatus.UNKNOWN,
    @ColumnInfo(name = "card_count") val cardCount: Int = 0

) {
    companion object {
        fun getDefaultInstance(): CardSet {
            return CardSet(
                cardSetId = 0,
                cardSetName = "",
                cardSetStatus = CardSetStatus.UNKNOWN,
                cardCount = 0)
        }
    }
}