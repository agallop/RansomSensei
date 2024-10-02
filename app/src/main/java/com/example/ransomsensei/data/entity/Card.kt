package com.example.ransomsensei.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Difficulty {Easy, Medium, Hard}

@Entity(tableName = "Cards")
data class Card (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "kana_value") val kanaValue: String?,
    @ColumnInfo(name = "kanji_value") val kanjiValue: String?,
    @ColumnInfo(name = "english_value") val englishValue: String?,
    @ColumnInfo(name = "difficulty") val difficulty: Difficulty?,
)