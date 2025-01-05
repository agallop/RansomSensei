package com.example.ransomsensei.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class Difficulty {UNKNOWN, EASY, MEDIUM, HARD}

@Entity(tableName = "Card",
    foreignKeys = [ForeignKey(
        entity = CardSet::class,
        parentColumns = arrayOf("card_set_id"),
        childColumns = arrayOf("card_set_id"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )])
data class Card (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_id") val cardId: Int,
    @ColumnInfo(name = "card_set_id") val cardSetId: Int,
    @ColumnInfo(name = "kana_value") val kanaValue: String?,
    @ColumnInfo(name = "kanji_value") val kanjiValue: String?,
    @ColumnInfo(name = "english_value") val englishValue: String?,
    @ColumnInfo(name = "difficulty") val difficulty: Difficulty?,
)