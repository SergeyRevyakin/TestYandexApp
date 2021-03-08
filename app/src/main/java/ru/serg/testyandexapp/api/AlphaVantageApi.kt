package ru.serg.testyandexapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.serg.testyandexapp.data.response.CompanyGlobalQuoteResponse
import ru.serg.testyandexapp.data.response.PredictionListResponse
import ru.serg.testyandexapp.helper.EndPoints

interface AlphaVantageApi {
    @GET(EndPoints.ALPHA_VANTAGE_BASE_URL+"query?function=SYMBOL_SEARCH")
    suspend fun getPredictionsList(
        @Query("keywords") keywords: String
    ): Response<PredictionListResponse>

    @GET(EndPoints.ALPHA_VANTAGE_BASE_URL+ "query?function=GLOBAL_QUOTE")
    suspend fun getCompanyGlobalQuote(
        @Query("symbol") ticker: String
    ): Response<CompanyGlobalQuoteResponse>
}