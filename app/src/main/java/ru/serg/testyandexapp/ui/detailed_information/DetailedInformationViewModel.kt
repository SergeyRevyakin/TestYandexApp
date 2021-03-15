package ru.serg.testyandexapp.ui.detailed_information

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.serg.testyandexapp.data.StockTradeOperation
import ru.serg.testyandexapp.data.response.TradesResponse
import ru.serg.testyandexapp.network.websocket.TradesService
import javax.inject.Inject

@HiltViewModel
class DetailedInformationViewModel @Inject constructor(
    private val tradesService: TradesService,
    application: Application
) : ViewModel() {
    private val _tradeData = MediatorLiveData<List<StockTradeOperation>>()
    val tradeData = _tradeData

    init {
        tradesService.getTradeData("BINANCE:BTCUSDT")
            .onEach {
                _tradeData.postValue(it)
            }
            .launchIn(viewModelScope)
    }

    fun getLivePriceUpdate(token:String){

    }

}