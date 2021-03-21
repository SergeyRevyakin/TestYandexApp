package ru.serg.testyandexapp.data.response


import com.google.gson.annotations.SerializedName

data class CompanyNewsResponse(
    @SerializedName("category")
    val category: String,
    @SerializedName("datetime")
    val datetime: Long,
    @SerializedName("headline")
    val headline: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val imageUrl: String,
    @SerializedName("related")
    val ticker: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("url")
    val url: String
)