package ru.serg.testyandexapp.data.response


import com.google.gson.annotations.SerializedName

data class FinnhubQuoteResponse(
    @SerializedName("c")
    val currentPrice: Double,
    @SerializedName("h")
    val highPriceOfDay: Double,
    @SerializedName("l")
    val lowPriceOfDay: Double,
    @SerializedName("o")
    val openPrice: Double,
    @SerializedName("pc")
    val prevClosePrice: Double,
    @SerializedName("t")
    val t: Int
)