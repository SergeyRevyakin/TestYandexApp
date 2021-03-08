package ru.serg.testyandexapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.serg.testyandexapp.data.response.CompanyProfileResponse
import ru.serg.testyandexapp.helper.EndPoints

interface FinnhubApi {
    @GET(EndPoints.FINHUB_BASE_URL + "stock/profile2?")
    suspend fun getCompanyProfile(
        @Query("symbol") symbol:String
    ): Response<CompanyProfileResponse>
}