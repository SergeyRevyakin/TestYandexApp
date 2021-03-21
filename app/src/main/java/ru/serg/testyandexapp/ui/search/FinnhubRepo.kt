package ru.serg.testyandexapp.ui.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.serg.testyandexapp.data.CompanyNewsItem
import ru.serg.testyandexapp.data.GraphHistoryItem
import ru.serg.testyandexapp.data.response.CompanyProfileResponse
import ru.serg.testyandexapp.data.response.FinnhubQuoteResponse
import ru.serg.testyandexapp.data.response.FinnhubSearchByNameResponse
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.network.BaseDataSource
import ru.serg.testyandexapp.network.FinnhubDataSource
import javax.inject.Inject

class FinnhubRepo @Inject constructor(
    private val finnhubDataSource: FinnhubDataSource
) : BaseDataSource() {

    suspend fun getCompanyProfile(ticker: String): Flow<Resource<CompanyProfileResponse>> {
        return flow {
            emit(safeApiCall {
                finnhubDataSource.getCompanyProfile(ticker)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCompanyQuote(ticker: String): Flow<Resource<FinnhubQuoteResponse>> {
        return flow {
            emit(safeApiCall {
                finnhubDataSource.getCompanyQuote(ticker)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCompaniesByName(name: String): Flow<Resource<FinnhubSearchByNameResponse>> {
        return flow {
            emit(safeApiCall {
                finnhubDataSource.getCompaniesByName(name)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCandlesByPeriod(
        ticker: String,
        resolution: String,
        from: Long,
        to: Long
    ): Flow<Resource<List<GraphHistoryItem>>> {
        val list = mutableListOf<GraphHistoryItem>()
        return flow {
            emit(safeApiCall {
                finnhubDataSource.getCandlesByPeriod(ticker, resolution, from, to)
            })
        }.map { resource ->

            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    for (i in resource.data!!.closePrice.indices) {
                        list.add(
                            GraphHistoryItem(
                                resource.data.closePrice[i].toFloat(),
                                resource.data.time[i].toFloat()
                            )
                        )
                    }
                    Resource.success(list)
                }
                Resource.Status.ERROR -> {
                    Resource.error("Error")
                }
                Resource.Status.LOADING -> {
                    Resource.loading()
                }

            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCompanyNews(
        ticker: String,
        from: String,
        to: String
    ): Flow<Resource<List<CompanyNewsItem>>> {
        val result = mutableListOf<CompanyNewsItem>()
        return flow {
            emit(safeApiCall {
                finnhubDataSource.getCompanyNews(ticker, from, to)
            })
        }.map { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    resource.data?.forEach {
                        result.add(
                            CompanyNewsItem(
                                it.datetime,
                                it.imageUrl,
                                it.headline,
                                it.summary,
                                it.source
                            )
                        )
                    }
                    Resource.success(result)
                }
                Resource.Status.ERROR -> {
                    Resource.error("Error")
                }
                Resource.Status.LOADING -> {
                    Resource.loading()
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}