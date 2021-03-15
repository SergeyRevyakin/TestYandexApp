package ru.serg.testyandexapp.network.websocket

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.serg.testyandexapp.data.StockTradeOperation
import ru.serg.testyandexapp.data.response.TradesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradesService @Inject constructor(
    private val gson: Gson,
    private val socket: FinnhubWebSocket
) {
    fun getTradeData(ticker:String): Flow<List<StockTradeOperation>> {

        return socket.connect(ticker)
            .map {
                gson.fromJson(it, TradesResponse::class.java)
            }
            .map {
                it.data
            }
            .flowOn(Dispatchers.IO)
    }
}