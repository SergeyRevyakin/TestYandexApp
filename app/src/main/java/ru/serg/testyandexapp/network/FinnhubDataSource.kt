package ru.serg.testyandexapp.network

import retrofit2.http.Query
import ru.serg.testyandexapp.api.FinnhubApi
import javax.inject.Inject

class FinnhubDataSource @Inject constructor(
    private val api: FinnhubApi
) {
    suspend fun getCompanyProfile(ticker:String) =
        api.getCompanyProfile(ticker)
}