package ru.serg.testyandexapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompanyBrief(
    val name:String,
    val ticker:String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)