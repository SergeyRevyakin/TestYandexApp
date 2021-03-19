package ru.serg.testyandexapp.network

import ru.serg.testyandexapp.api.AlphaVantageApi
import javax.inject.Inject

class AlphaVantageDataSource @Inject constructor(
    private val api: AlphaVantageApi
) {
    suspend fun getPredictionsList(keywords: String) =
        api.getPredictionsList(keywords)

    suspend fun getCompanyGlobalQuote(ticker: String) =
        api.getCompanyGlobalQuote(ticker)

    suspend fun getCompanyOverview(ticker: String) =
        api.getCompanyOverview(ticker)
}