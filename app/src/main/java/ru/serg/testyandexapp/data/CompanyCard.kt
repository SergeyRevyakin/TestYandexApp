package ru.serg.testyandexapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompanyCard(
    val name: String,
    val ticker: String,
    val logoUrl: String,
    val currentPrice: Double,
    val deltaPrice: Double,
    val deltaPricePercentage: Double,
    var isFavourite: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

//data class CompanyCardTest(
//    val name: String,
//    val ticker: String,
//    val logoUrl: String,
//    val currentPrice: Double,
//    val deltaPrice: Double,
//    val deltaPricePercentage: Double,
//    var isFavourite: Boolean = false
//)