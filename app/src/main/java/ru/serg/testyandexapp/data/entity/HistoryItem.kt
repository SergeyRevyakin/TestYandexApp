package ru.serg.testyandexapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryItem(
    @PrimaryKey val request: String
)