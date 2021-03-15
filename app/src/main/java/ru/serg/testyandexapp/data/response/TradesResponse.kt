package ru.serg.testyandexapp.data.response


import com.google.gson.annotations.SerializedName
import ru.serg.testyandexapp.data.StockTradeOperation

data class TradesResponse(
    @SerializedName("data")
    val data: List<StockTradeOperation>,
    @SerializedName("type")
    val type: String
) {
    data class TradeData(
        @SerializedName("p")
        val lastPrice: Double,
        @SerializedName("s")
        val symbol: String,
        @SerializedName("t")
        val time: Long,
        @SerializedName("v")
        val volume: Double
    )
}