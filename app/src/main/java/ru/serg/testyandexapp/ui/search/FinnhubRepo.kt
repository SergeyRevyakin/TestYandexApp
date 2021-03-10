package ru.serg.testyandexapp.ui.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.serg.testyandexapp.data.response.CompanyProfileResponse
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.network.BaseDataSource
import ru.serg.testyandexapp.network.FinnhubDataSource
import javax.inject.Inject

class FinnhubRepo @Inject constructor(
    private val finnhubDataSource: FinnhubDataSource
) : BaseDataSource() {

    suspend fun getCompanyProfile(ticker:String): Flow<Resource<CompanyProfileResponse>>{
        return flow {
            emit(safeApiCall {
                finnhubDataSource.getCompanyProfile(ticker)
            })
        }.flowOn(Dispatchers.IO)

    }
}