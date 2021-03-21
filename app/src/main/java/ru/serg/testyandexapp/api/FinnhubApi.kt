package ru.serg.testyandexapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.serg.testyandexapp.data.response.*
import ru.serg.testyandexapp.helper.EndPoints

interface FinnhubApi {
    @GET("stock/profile2")
    suspend fun getCompanyProfile(
        @Query("symbol") ticker: String
    ): Response<CompanyProfileResponse>

    @GET("quote")
    suspend fun getCompanyQuote(
        @Query("symbol") ticker: String
    ): Response<FinnhubQuoteResponse>

    @GET("search")
    suspend fun getCompaniesByName(
        @Query("q") name: String
    ): Response<FinnhubSearchByNameResponse>

    @GET("stock/candle")
    suspend fun getCandlesByPeriod(
        @Query("symbol") ticker: String,
        @Query("resolution") resolution: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): Response<FinnhubCandlesByPeriodResponse>

    @GET("company-news")
    suspend fun getCompanyNews(
        @Query("symbol") ticker: String,
        @Query("from") from: String,
        @Query("to") to: String
    ):Response<List<CompanyNewsResponse>>

}