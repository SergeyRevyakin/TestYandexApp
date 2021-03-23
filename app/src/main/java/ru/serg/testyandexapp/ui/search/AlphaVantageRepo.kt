package ru.serg.testyandexapp.ui.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.serg.testyandexapp.data.CompanyOverview
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

    suspend fun getCompanyOverview(ticker: String): Flow<Resource<CompanyOverview>>{
        return flow {
            emit (safeApiCall { dataSource.getCompanyOverview(ticker) })
        }.map { resource->
            when(resource.status) {
                Resource.Status.SUCCESS -> {
                    resource.data?.name?.let {
                        Resource.success(
                            CompanyOverview(
                                resource.data.name,
                                resource.data.description,
                                resource.data.country,
                                resource.data.fullTimeEmployees
                            )
                        )
                    } ?: Resource.error(resource.message ?: "Error")
                }
                Resource.Status.ERROR -> {
                    Resource.error(resource.message?: "Error")
                }

                Resource.Status.LOADING -> {
                    Resource.loading()
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}