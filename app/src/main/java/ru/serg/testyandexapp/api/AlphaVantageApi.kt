package ru.serg.testyandexapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.serg.testyandexapp.data.response.CompanyGlobalQuoteResponse
import ru.serg.testyandexapp.data.response.CompanyOverviewResponse
import ru.serg.testyandexapp.data.response.PredictionListResponse

interface AlphaVantageApi {
    @GET("query?function=SYMBOL_SEARCH")
    suspend fun getPredictionsList(
        @Query("keywords") keywords: String
    ): Response<PredictionListResponse>

    @GET("query?function=GLOBAL_QUOTE")
    suspend fun getCompanyGlobalQuote(
        @Query("symbol") ticker: String
    ): Response<CompanyGlobalQuoteResponse>

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyOverview(
        @Query("symbol") ticker: String
    ): Response<CompanyOverviewResponse>
}