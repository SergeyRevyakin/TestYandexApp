package ru.serg.testyandexapp.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import ru.serg.testyandexapp.data.CompanyBrief
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.room.AppDatabase
import ru.serg.testyandexapp.room.FavouriteRepository
import ru.serg.testyandexapp.room.HistoryItem
import ru.serg.testyandexapp.room.HistoryRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val alphaVantageRepo: AlphaVantageRepo,
    private val finnhubRepo: FinnhubRepo,
    application: Application
) : ViewModel() {
    private val _predictionsData = MutableLiveData<Resource<List<CompanyBrief>>>()
    val predictionsData = _predictionsData

    private val _companyInfo = MutableLiveData<Resource<CompanyCard>>()
    val companyInfo = _companyInfo

    private val _companyInfoList = MutableLiveData<MutableList<Resource<CompanyCard>>>()
    val companyInfoList = _companyInfoList

    private val historyRepository: HistoryRepository
    val history: LiveData<List<HistoryItem>>
    private val favouriteRepository: FavouriteRepository
    var favourites: LiveData<List<CompanyCard>>
//    lateinit var favouritesList: List<CompanyCard>

    init {
        _companyInfoList.value = mutableListOf()

        val database = AppDatabase.getAppDatabase(application)!!

        historyRepository = HistoryRepository(database.historyDao())
        history = historyRepository.getHistory

        favouriteRepository = FavouriteRepository(database.favouriteDao())
        favourites = favouriteRepository.getFavourites()
//        viewModelScope.launch {
//            favouritesList = favouriteRepository.getFavouritesList()
//        }

    }


    fun getPredictionsAlpha(keywords: String) {
        saveInHistory(keywords)
        viewModelScope.launch {
            alphaVantageRepo.getPredictions(keywords).collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        val companyBriefList = mutableListOf<CompanyBrief>()
                        resource.data?.bestMatches?.forEach { result ->
                            companyBriefList.add(CompanyBrief(result.name, result.symbol))
                        }
                        _predictionsData.value = Resource.success(companyBriefList)
                    }
                    Resource.Status.LOADING -> {
                        _predictionsData.value = Resource.loading()
                    }
                    Resource.Status.ERROR -> {
                        _predictionsData.value = Resource.error("Error")
                    }
                }
            }
        }
    }

    fun getPredictions(keywords: String) {
        viewModelScope.launch {
            finnhubRepo.getCompaniesByName(keywords).collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        val companyBriefList = mutableListOf<CompanyBrief>()
                        resource.data?.result?.forEach { result ->
                            companyBriefList.add(CompanyBrief(result.description, result.symbol))
                        }
                        _predictionsData.value = Resource.success(companyBriefList)
                    }
                    Resource.Status.LOADING -> {
                        _predictionsData.value = Resource.loading()
                    }
                    Resource.Status.ERROR -> {
                        _predictionsData.value = Resource.error("Error")
                    }
                }

//                getFullSearchResults()
            }
        }
    }

    //    fun getCompanyBaseInfo(ticker: String) {
//        viewModelScope.launch {
//            _companyInfo.value = Resource.loading(null)
//            finnhubRepo.getCompanyProfile(ticker)
//                .zip(alphaVantageRepo.getCompanyGlobalQuote(ticker)) { profile, qoute ->
//                    if (profile.status == Resource.Status.SUCCESS &&
//                        qoute.status == Resource.Status.SUCCESS
//                    ) {
//                        val companyProfile = profile.data!!
//                        val globalQuote = qoute.data!!.globalQuote
//                        _companyInfo.value = Resource.success(
//                            CompanyCard(
//                                companyProfile.name,
//                                companyProfile.ticker,
//                                companyProfile.logo,
//                                globalQuote.price.toDouble(),
//                                globalQuote.change.toDouble(),
//                                globalQuote.changePercent.removeSuffix("%").toDouble(),
//                                false
//                            )
//                        )
////                _companyInfo.value = card
//                    }
//                }.collect()
//        }
//    }
    fun getCompanyBaseInfo(ticker: String) {
        viewModelScope.launch {
            _companyInfo.value = Resource.loading(null)
            finnhubRepo.getCompanyProfile(ticker)
                .zip(finnhubRepo.getCompanyQuote(ticker)) { profile, qoute ->
                    if (profile.status == Resource.Status.SUCCESS &&
                        qoute.status == Resource.Status.SUCCESS
                    ) {
                        val companyProfile = profile.data!!
                        val globalQuote = qoute.data!!
                        if (!companyProfile.name.isNullOrBlank() &&
                            !globalQuote.currentPrice.isNaN()
                        ) {
                            val priceDelta =
                                globalQuote.currentPrice.minus(globalQuote.prevClosePrice)
                            val companyCard = Resource.success(
                                CompanyCard(
                                    companyProfile.name,
                                    companyProfile.ticker,
                                    companyProfile.logo,
                                    globalQuote.currentPrice,
                                    priceDelta,
                                    priceDelta.div(globalQuote.prevClosePrice).times(100),
                                    favouriteRepository.exist(companyProfile.ticker)
                                )
                            )

                            _companyInfo.value = companyCard
//                _companyInfo.value = card
                        }
                    }
                }.collect()
        }
    }

    fun saveInHistory(request: String) {
        viewModelScope.launch {
            historyRepository.insert(HistoryItem(request))
        }
    }

    fun saveOrRemoveFavourite(companyCard: CompanyCard) {
        viewModelScope.launch {
            if (companyCard.isFavourite) {
                favouriteRepository.insert(companyCard)
            } else {
                favouriteRepository.removeFromFavourites(companyCard)
            }
        }
    }
}

