package ru.serg.testyandexapp.ui.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.serg.testyandexapp.data.response.CompanyGlobalQuoteResponse
import ru.serg.testyandexapp.data.response.PredictionListResponse
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.network.AlphaVantageDataSource
import ru.serg.testyandexapp.network.BaseDataSource
import javax.inject.Inject

class AlphaVantageRepo @Inject constructor(
    private val dataSource: AlphaVantageDataSource
) : BaseDataSource() {

    suspend fun getPredictions(keywords: CharSequence): Flow<Resource<PredictionListResponse>> { //TODO reformat response
        return flow {
            emit(safeApiCall {
                dataSource.getPredictionsList(keywords.toString())
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCompanyGlobalQuote(ticker: String): Flow<Resource<CompanyGlobalQuoteResponse>> {
        return flow {
            emit(safeApiCall {
                dataSource.getCompanyGlobalQuote(ticker)
            })
        }.flowOn(Dispatchers.IO)
    }

}