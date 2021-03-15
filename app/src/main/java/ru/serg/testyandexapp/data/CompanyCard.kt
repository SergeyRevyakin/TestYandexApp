package ru.serg.testyandexapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class CompanyCard(
    val name: String,
    @PrimaryKey val ticker: String,
    val logoUrl: String,
    val currentPrice: Double,
    val deltaPrice: Double,
    val deltaPricePercentage: Double,
    var isFavourite: Boolean = false
):Serializable

//data class CompanyCardTest(
//    val name: String,
//    val ticker: String,
//    val logoUrl: String,
//    val currentPrice: Double,
//    val deltaPrice: Double,
//    val deltaPricePercentage: Double,
//    var isFavourite: Boolean = false
//)