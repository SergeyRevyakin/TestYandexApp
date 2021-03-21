package ru.serg.testyandexapp.data

data class CompanyNewsItem(
    val dateTime: Long,
    val imageUrl: String,
    val headLine: String,
    val body: String,
    val source: String
)