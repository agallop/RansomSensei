package com.example.ransomsensei.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CardSetStatus {UNKNOWN, ENABLED, DISABLED}

@Entity(tableName = "CardSet")
data class CardSet (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_set_id") val cardSetId: Int,
    @ColumnInfo(name = "card_set_name") val cardSetName: String?,
    @ColumnInfo(name = "card_set_status") val cardSetStatus: CardSetStatus
)