package ru.serg.testyandexapp.data.response


import com.google.gson.annotations.SerializedName

data class FinnhubCandlesByPeriodResponse(
    @SerializedName("c")
    val closePrice: List<Double>,
    @SerializedName("h")
    val highPrice: List<Double>,
    @SerializedName("l")
    val lowPrice: List<Double>,
    @SerializedName("o")
    val openPrice: List<Double>,
    @SerializedName("s")
    val status: String,
    @SerializedName("t")
    val time: List<Long>,
    @SerializedName("v")
    val volume: List<Long>
)