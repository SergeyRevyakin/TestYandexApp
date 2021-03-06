package ru.serg.testyandexapp.ui.detailed_information

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.serg.testyandexapp.data.CompanyNewsItem
import ru.serg.testyandexapp.data.CompanyOverview
import ru.serg.testyandexapp.data.GraphHistoryItem
import ru.serg.testyandexapp.data.entity.CompanyCard
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.network.websocket.TradesService
import ru.serg.testyandexapp.room.FavouriteRepository
import ru.serg.testyandexapp.ui.search.AlphaVantageRepo
import ru.serg.testyandexapp.ui.search.FinnhubRepo
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailedInformationViewModel @Inject constructor(
    private val tradesService: TradesService,
    private val finnhubRepo: FinnhubRepo,
    private val alphaVantageRepo: AlphaVantageRepo,
    private val favouriteRepository: FavouriteRepository
) : ViewModel() {
    private val _tradeData = MutableLiveData<List<GraphHistoryItem>>()
    val tradeData = _tradeData
    val tradeDataHistory = mutableListOf<GraphHistoryItem>()

    private val _dayData = MutableLiveData<Resource<List<GraphHistoryItem>>>()
    val dayData = _dayData

    private val _companyNews = MutableLiveData<Resource<List<CompanyNewsItem>>>()
    val companyNews = _companyNews

    private val _companyOverview = MutableLiveData<Resource<CompanyOverview>>()
    val companyOverview = _companyOverview

    lateinit var companyCard: CompanyCard
    val favourites = favouriteRepository.getFavourites()


    fun getLivePriceUpdate() {

        tradesService.getTradeData(companyCard.ticker)
            .onEach {
                it?.let {
                    _tradeData.postValue(it)
                    tradeDataHistory.addAll(it)
                }
            }
            .launchIn(viewModelScope)
    }


    fun getDayData(days: Int, resolution: String) {
        val calendar = Calendar.getInstance()
        val nowDate = calendar.timeInMillis / 1000
        calendar.add(Calendar.DATE, -days)
        val fromDate = calendar.timeInMillis / 1000
        viewModelScope.launch {
            finnhubRepo.getCandlesByPeriod(
                companyCard.ticker,
                resolution,
                fromDate,
                nowDate
            ).collect {
                _dayData.value = it
            }
        }
    }

    fun getCompanyOverview() {
        viewModelScope.launch {
            alphaVantageRepo.getCompanyOverview(companyCard.ticker)
                .collect {
                    _companyOverview.value = it
                }
        }
    }

    fun getCompanyNews(date:String) {
        val calendar = Calendar.getInstance()
        val nowDate = calendar.timeInMillis / 1000
        calendar.add(Calendar.DATE, -30)
        val fromDate = calendar.timeInMillis / 1000
        viewModelScope.launch {
            finnhubRepo.getCompanyNews(
                companyCard.ticker,
                date,
                date
            ).collect {
                _companyNews.value = it
            }
        }
    }

    fun closeWebSockets() {
        tradesService.closeWebSockets()
    }

    fun changeFavouriteStatus() {
        viewModelScope.launch {
            if (!companyCard.isFavourite) {
                favouriteRepository.insert(companyCard)
            } else {
                favouriteRepository.removeFromFavourites(companyCard)
            }
        }
        companyCard.isFavourite = !companyCard.isFavourite
    }

}