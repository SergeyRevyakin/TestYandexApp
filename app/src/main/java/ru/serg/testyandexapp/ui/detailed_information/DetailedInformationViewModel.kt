package ru.serg.testyandexapp.ui.detailed_information

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.utils.Utils.init
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.serg.testyandexapp.data.GraphHistoryItem
import ru.serg.testyandexapp.data.StockTradeOperation
import ru.serg.testyandexapp.data.response.TradesResponse
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.network.FinnhubDataSource
import ru.serg.testyandexapp.network.websocket.TradesService
import ru.serg.testyandexapp.ui.search.FinnhubRepo
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailedInformationViewModel @Inject constructor(
    private val tradesService: TradesService,
    private val finnhubRepo: FinnhubRepo,
    application: Application
) : ViewModel() {
    private val _tradeData = MutableLiveData<List<GraphHistoryItem>>()
    val tradeData = _tradeData

    private val _dayData = MutableLiveData<Resource<List<GraphHistoryItem>>>()
    val dayData = _dayData

    init {

    }

    fun getLivePriceUpdate(ticker:String){

        tradesService.getTradeData(ticker)
            .onEach {
                _tradeData.postValue(it)
            }
            .launchIn(viewModelScope)
    }


    fun getDayData(ticker: String, days: Int){
        val calendar = Calendar.getInstance()
        val nowDate = calendar.timeInMillis/1000
        calendar.add(Calendar.DATE, -days)
        val fromDate = calendar.timeInMillis/1000
        viewModelScope.launch {
            finnhubRepo.getCandlesByPeriod(
                ticker,
                "W",
                fromDate,
                nowDate
            ).collect {
                _dayData.value = it
            }
        }
    }

    fun closeWebSockets(){
        tradesService.closeWebSockets()
    }

}