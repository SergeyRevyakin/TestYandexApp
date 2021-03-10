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

    init {
        _companyInfoList.value = mutableListOf()
        val historyDao = AppDatabase.getAppDatabase(application)!!.historyDao()
        historyRepository = HistoryRepository(historyDao)
        history = historyRepository.getHistory
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
                        if (companyProfile.name != null &&
                            globalQuote.currentPrice != null
                        ) {

                            val priceDelta =
                                globalQuote.currentPrice.minus(globalQuote.prevClosePrice)
                            _companyInfo.value = Resource.success(
                                CompanyCard(
                                    companyProfile.name,
                                    companyProfile.ticker,
                                    companyProfile.logo,
                                    globalQuote.currentPrice,
                                    priceDelta,
                                    priceDelta.div(globalQuote.prevClosePrice).times(100),
                                    false
                                )
                            )
//                _companyInfo.value = card
                        }
                    }
                }.collect()
        }
    }

//    fun getFullSearchResults() {
//        _predictionsData.value?.data?.bestMatches.let {
//            viewModelScope.launch {
//                it?.forEach {
//                    finnhubRepo.getCompanyProfile(it.symbol)
//                        .zip(alphaVantageRepo.getCompanyGlobalQuote(it.symbol)) { profile, qoute ->
//                            val companyProfile = profile.data!!
//                            val globalQuote = qoute.data!!.globalQuote
//                            //_companyInfo.value
//                            val card = Resource.success(
//                                CompanyCard(
//                                    companyProfile.name,
//                                    companyProfile.ticker,
//                                    companyProfile.logo,
//                                    globalQuote.price.toDouble(),
//                                    globalQuote.change.toDouble(),
//                                    globalQuote.changePercent.removeSuffix("%").toDouble()
//                                )
//                            )
//                            _companyInfoList.value?.plus(card)
//                            _companyInfoList.value?.forEach {
//                                it.data
//                            }
//
//                        }.collect()
//                }
//            }
//        }
//
////        _companyInfoList.value?.get(0)
//    }

    fun saveInHistory(request: String) {
        viewModelScope.launch {
            historyRepository.insert(HistoryItem(request))
        }
    }
}

