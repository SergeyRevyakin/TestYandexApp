package ru.serg.testyandexapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.serg.testyandexapp.data.response.CompanyProfileResponse
import ru.serg.testyandexapp.data.response.FinnhubCandlesByPeriodResponse
import ru.serg.testyandexapp.data.response.FinnhubQuoteResponse
import ru.serg.testyandexapp.data.response.FinnhubSearchByNameResponse
import ru.serg.testyandexapp.helper.EndPoints

interface FinnhubApi {
    @GET(EndPoints.FINHUB_BASE_URL + "stock/profile2")
    suspend fun getCompanyProfile(
        @Query("symbol") ticker: String
    ): Response<CompanyProfileResponse>

    @GET(EndPoints.FINHUB_BASE_URL + "quote")
    suspend fun getCompanyQuote(
        @Query("symbol") ticker: String
    ): Response<FinnhubQuoteResponse>

    @GET(EndPoints.FINHUB_BASE_URL + "search")
    suspend fun getCompaniesByName(
        @Query("q") name: String
    ): Response<FinnhubSearchByNameResponse>

    @GET(EndPoints.FINHUB_BASE_URL + "stock/candle")
    suspend fun getCandlesByPeriod(
        @Query("symbol") ticker: String,
        @Query("resolution") resolution: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): Response<FinnhubCandlesByPeriodResponse>

}