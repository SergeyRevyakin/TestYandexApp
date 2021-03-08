package ru.serg.testyandexapp.data

data class CompanyCard(
    val name:String,
    val ticker:String,
    val logoUrl:String,
    val currentPrice:String,
    val deltaPrice:String,
    val deltaPricePercentage:String,
    var isFavourite:Boolean = false
)