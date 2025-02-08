package com.example.ransomsensei.data.entity

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