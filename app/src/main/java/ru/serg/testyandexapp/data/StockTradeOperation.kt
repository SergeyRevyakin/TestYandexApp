package ru.serg.testyandexapp.data

import com.google.gson.annotations.SerializedName

data class StockTradeOperation (
    @SerializedName("p")
    val lastPrice: Double,
    @SerializedName("s")
    val symbol: String,
    @SerializedName("t")
    val time: Long,
    @SerializedName("v")
    val volume: Double
)