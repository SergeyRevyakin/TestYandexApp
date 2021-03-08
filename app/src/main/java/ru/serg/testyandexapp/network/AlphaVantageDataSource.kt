package ru.serg.testyandexapp.network

import ru.serg.testyandexapp.api.AlphaVantageApi
import javax.inject.Inject

class AlphaVantageDataSource @Inject constructor(
    private val api:AlphaVantageApi
) {
    suspend fun getPredictionsList(keywords: String) =
        api.getPredictionsList(keywords)
}