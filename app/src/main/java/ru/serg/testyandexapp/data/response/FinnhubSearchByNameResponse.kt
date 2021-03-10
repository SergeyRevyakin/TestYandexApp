package ru.serg.testyandexapp.data.response


import com.google.gson.annotations.SerializedName

data class FinnhubSearchByNameResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("result")
    val result: List<Result>
) {
    data class Result(
        @SerializedName("description")
        val description: String,
        @SerializedName("displaySymbol")
        val displaySymbol: String,
        @SerializedName("symbol")
        val symbol: String,
        @SerializedName("type")
        val type: String
    )
}