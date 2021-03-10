package ru.serg.testyandexapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryItem(
    @PrimaryKey val request:String//,
//    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
}